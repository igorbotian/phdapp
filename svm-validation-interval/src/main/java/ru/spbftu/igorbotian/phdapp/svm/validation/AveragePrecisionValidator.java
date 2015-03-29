package ru.spbftu.igorbotian.phdapp.svm.validation;

import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleException;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
 */
class AveragePrecisionValidator extends AbstractRankingPairwiseClassifierCrossValidator<MultiClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(AveragePrecisionValidator.class);

    /**
     * Средство кросс-валидации точности единичной классифации
     */
    private final PrecisionValidator precisionValidator;

    public AveragePrecisionValidator(CrossValidationSampleManager sampleManager,
                                     IntervalClassifierParameterFactory classifierParameterFactory,
                                     CrossValidatorParameterFactory crossValidatorParameterFactory,
                                     ReportFactory reportFactory,
                                     PrecisionValidator precisionValidator,
                                     ApplicationConfiguration appConfig) {
        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory, appConfig);
        this.precisionValidator = Objects.requireNonNull(precisionValidator);
    }

    @Override
    protected MultiClassificationReport validate(RankingPairwiseClassifier classifier,
                                                 Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                 CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        int samplesToGenerateCount = specificValidatorParams.samplesToGenerateCount().value().value();
        List<SingleClassificationReport> iterations = new ArrayList<>(samplesToGenerateCount);

        for (int i = 0; i < samplesToGenerateCount; i++) {
            try {
                iterations.add(precisionValidator.validate(classifier, specificValidatorParams.defaultValues()));
            } catch (CrossValidationException e) {
                if (stopCrossValidationOnError()) {
                    throw e;
                } else {
                    LOGGER.error(e);
                }
            }

            fireCrossValidationContinued((int) (100 * ((float) i / (float) samplesToGenerateCount)));

            if (processInterrupted()) {
                fireCrossValidationInterrupted();
                return reportFactory.newMultiClassificationReport(iterations);
            }
        }

        return reportFactory.newMultiClassificationReport(iterations);
    }
}
