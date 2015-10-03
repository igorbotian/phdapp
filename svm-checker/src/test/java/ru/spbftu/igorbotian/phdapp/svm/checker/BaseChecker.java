package ru.spbftu.igorbotian.phdapp.svm.checker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.locale.java.JavaI18NLocalizationModule;
import ru.spbftu.igorbotian.phdapp.log.Log4j;
import ru.spbftu.igorbotian.phdapp.output.csv.CSVOutputDataManagementModule;
import ru.spbftu.igorbotian.phdapp.output.csv.ReportCSVWriter;
import ru.spbftu.igorbotian.phdapp.output.csv.ReportCSVWriterFactory;
import ru.spbftu.igorbotian.phdapp.svm.IntervalPairwiseClassifierModule;
import ru.spbftu.igorbotian.phdapp.svm.IntervalRankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SvmValidationReportManagementModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.SvmValidationIntervalSampleManagementModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.SvmValidationPreciseSampleManagementModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.SvmValidationSampleManagementModule;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Базовые средства механизма автоматизированной кросс-валидации ранжирующего попарного классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class BaseChecker {

    protected static final int MAX_JUDGEMENT_GROUP_SIZE = 3;
    protected static final int SAMPLE_SIZE = 500;
    protected static final int SAMPLES_TO_GENERATE = 100;
    protected static final int PRECISE_INTERVAL_JUDGEMENT_COUNT_RATIO = 50;
    protected static final int TRAINING_TESTING_SETS_SIZE_RATIO = 75;

    /* Значения параметров ниже имеют оптимальные значения, найденные эмпиририческим способом */

    protected static final double GAUSSIAN_KERNEL_PARAMETER = 8;
    protected static final double PENALTY_PARAMETER = 13.5;

    /**
     * Параметр, задающий поведение механизма кросс-валидации в случае ошибки
     */
    private static final String STOP_CROSS_VALIDATION_ON_ERROR_PARAM = "stopCrossValidationOnError";

    /**
     * Название системного свойства, значение которого указывает на директорию для хранения конфигурационных файлов
     */
    private static final String CONFIG_FOLDER_SYSTEM_PROPERTY = "phdapp.conf.folder";

    /**
     * Директория для хранения конфигурационных файлов
     */
    private static final Path CONFIG_FOLDER = getConfigFolder();

    private ReportCSVWriterFactory reportFactory;
    protected RankingPairwiseClassifier classifier;
    protected CrossValidatorParameterFactory parameters;
    protected IntervalPairwiseClassifierCrossValidators intervalValidators;
    protected IntervalPairwiseClassifierCrossValidators preciseValidators;

    private Set<CrossValidatorParameter<?>> defaultParameters;

    @Before
    public void setUp() {
        Log4j.init(CONFIG_FOLDER);

        Injector parentInjector = Guice.createInjector(
                new DataModule(),
                new ApplicationConfigurationModule(Paths.get("..")),
                new JavaI18NLocalizationModule(),
                new CSVOutputDataManagementModule(),
                new CrossValidationParametrizationModule(),
                new SvmValidationReportManagementModule(),
                new SvmValidationSampleManagementModule()
        );

        Injector intervalInjector = parentInjector.createChildInjector(
                new IntervalPairwiseClassifierModule(),
                //new HausdorffIntervalRankingPairwiseClassifierModule(),
                //new ClusterCentroidIntervalRankingPairwiseClassifierModule(),
                new SvmValidationIntervalSampleManagementModule(),
                new SvmIntervalClassifierValidationModule()
        );

        reportFactory = intervalInjector.getInstance(ReportCSVWriterFactory.class);
        classifier = intervalInjector.getInstance(IntervalRankingPairwiseClassifier.class);
        parameters = intervalInjector.getInstance(CrossValidatorParameterFactory.class);
        intervalValidators = intervalInjector.getInstance(IntervalPairwiseClassifierCrossValidators.class);
        intervalInjector.getInstance(ApplicationConfiguration.class).setBoolean(
                STOP_CROSS_VALIDATION_ON_ERROR_PARAM, true);

        Injector preciseInjector = parentInjector.createChildInjector(
                new IntervalPairwiseClassifierModule(),
                //new HausdorffIntervalRankingPairwiseClassifierModule(),
                //new ClusterCentroidIntervalRankingPairwiseClassifierModule(),
                new SvmValidationPreciseSampleManagementModule(),
                new SvmIntervalClassifierValidationModule()
        );
        preciseValidators = preciseInjector.getInstance(IntervalPairwiseClassifierCrossValidators.class);

        defaultParameters = Stream.of(
                maxJudgementGroupSize(MAX_JUDGEMENT_GROUP_SIZE),
                sampleSize(SAMPLE_SIZE),
                samplesToGenerate(SAMPLES_TO_GENERATE),
                preciseIntervalJudgmentsCountRatio(PRECISE_INTERVAL_JUDGEMENT_COUNT_RATIO),
                trainingTestingSetsSizeRatio(TRAINING_TESTING_SETS_SIZE_RATIO),
                gaussianKernelParameter(GAUSSIAN_KERNEL_PARAMETER),
                penaltyParameter(PENALTY_PARAMETER)
        ).collect(Collectors.toSet());
    }

    private static Path getConfigFolder() {
        String customConfFolderName = System.getProperty(CONFIG_FOLDER_SYSTEM_PROPERTY);

        if (customConfFolderName != null && !customConfFolderName.isEmpty()) {
            Path customConfigFolder = Paths.get(customConfFolderName);

            if (Files.exists(customConfigFolder)) {
                return customConfigFolder;
            }
        }

        return Paths.get("..");
    }

    @SuppressWarnings("unchecked")
    protected <R extends Report> void check(String reportFileName,
                                            RankingPairwiseClassifierCrossValidator<R> crossValidator,
                                            CrossValidatorParameter<?>... crossValidatorParameters)
            throws CrossValidationException, IOException {

        Objects.requireNonNull(crossValidator);
        Objects.requireNonNull(crossValidatorParameters);
        Objects.requireNonNull(reportFileName);

        long start = System.currentTimeMillis();
        R report = crossValidator.validate(classifier, applySpecificParameters(crossValidatorParameters));
        long end = System.currentTimeMillis();
        System.out.println(String.format("Duration = %.5f s.", ((end - start) / 1000.0)));

        ReportCSVWriter<R> csvWriter = reportFactory.get((Class<R>) report.getClass());
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(reportFileName));
            csvWriter.writeTo(report, writer, true);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    protected Set<CrossValidatorParameter<?>> applySpecificParameters(CrossValidatorParameter<?>... params) {
        assert params != null;

        Set<CrossValidatorParameter<?>> result = new HashSet<>();
        Arrays.stream(params).forEach(result::add);

        for (CrossValidatorParameter<?> defaultParam : defaultParameters) {
            if (!containsParam(result, defaultParam)) {
                result.add(defaultParam);
            }
        }

        assert result.size() == defaultParameters.size();
        return Collections.unmodifiableSet(result);
    }

    private boolean containsParam(Set<CrossValidatorParameter<?>> set, CrossValidatorParameter<?> param) {
        assert set != null;
        assert param != null;

        for (CrossValidatorParameter<?> entry : set) {
            if (entry.name().equals(param.name())) {
                return true;
            }
        }

        return false;
    }

    protected CrossValidatorParameter<?> maxJudgementGroupSize(int value) {
        return parameters.maxJudgementGroupSize(value);
    }

    protected CrossValidatorParameter<?> sampleSize(int size) {
        return parameters.sampleSize(size, 4, size, 5, 1, 50);
    }

    protected CrossValidatorParameter<?> samplesToGenerate(int count) {
        return parameters.samplesToGenerateCount(count, 1, count, 1, 1, 1);
    }

    protected CrossValidatorParameter<?> preciseIntervalJudgmentsCountRatio(int ratio) {
        return parameters.preciseIntervalJudgmentsCountRatio(ratio);
    }

    protected CrossValidatorParameter<?> trainingTestingSetsSizeRatio(int ratio) {
        return parameters.trainingTestingSetsSizeRatio(ratio);
    }

    protected CrossValidatorParameter<?> gaussianKernelParameter(double value) {
        return parameters.gaussianKernelParameter(value);
    }

    protected CrossValidatorParameter<?> penaltyParameter(double value) {
        return parameters.penaltyParameter(value);
    }
}
