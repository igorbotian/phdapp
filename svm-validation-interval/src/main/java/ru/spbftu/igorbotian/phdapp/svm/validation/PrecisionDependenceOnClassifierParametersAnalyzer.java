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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Средство анализа завимимости точности классификации от параметров классификации
 */
class PrecisionDependenceOnClassifierParametersAnalyzer
        extends AbstractRankingPairwiseClassifierCrossValidator<MultiClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(PrecisionDependenceOnClassifierParametersAnalyzer.class);

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final PrecisionValidator precisionValidator;

    /**
     * Фабрика параметров классификатора
     */
    private final IntervalClassifierParameterFactory classifierParameterFactory;

    public PrecisionDependenceOnClassifierParametersAnalyzer(CrossValidationSampleManager sampleManager,
                                                             IntervalClassifierParameterFactory classifierParameterFactory,
                                                             CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                             ReportFactory reportFactory,
                                                             PrecisionValidator precisionValidator,
                                                             ApplicationConfiguration appConfig) {
        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory, appConfig);
        this.precisionValidator = Objects.requireNonNull(precisionValidator);
        this.classifierParameterFactory = Objects.requireNonNull(classifierParameterFactory);
    }

    @Override
    protected MultiClassificationReport validate(RankingPairwiseClassifier classifier,
                                                 Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                 CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        CrossValidatorParameter<Double> penaltyParam = specificValidatorParams.penaltyParameter();
        double ppLowerBound = penaltyParam.lowerBound().value();
        double ppUpperBound = penaltyParam.upperBound().value();
        double ppStepSize = penaltyParam.stepSize().value();

        CrossValidatorParameter<Double> gaussianKernelParam = specificValidatorParams.gaussianKernelParameter();
        double gkpLowerBound = gaussianKernelParam.lowerBound().value();
        double gkpUpperBound = gaussianKernelParam.upperBound().value();
        double gkpStepSize = gaussianKernelParam.stepSize().value();

        int numberOfIterations = (int) (((ppUpperBound - ppLowerBound) / ppStepSize)
                * ((gkpUpperBound - gkpLowerBound) / gkpStepSize));
        int iterationsCompleted = 0;
        List<SingleClassificationReport> iterations = new ArrayList<>(numberOfIterations);

        for (double ccp = ppLowerBound; ccp <= ppUpperBound; ccp += ppStepSize) {
            LOGGER.debug("Constant cost parameter = " + ccp);
            ClassifierParameter<Double> ccpParam = classifierParameterFactory.penaltyParameter(ccp);

            for (double gkp = gkpLowerBound; gkp <= gkpUpperBound; gkp += gkpStepSize) {
                LOGGER.debug("Gaussian kernel parameter: " + gkp);
                ClassifierParameter<Double> gkpParam = classifierParameterFactory.gaussianKernelParameter(gkp);

                try {
                    iterations.add(precisionValidator.validate(
                                    classifier,
                                    override(specificClassifierParams,
                                            Stream.of(ccpParam, gkpParam).collect(Collectors.toSet())),
                                    specificValidatorParams)
                    );
                } catch (CrossValidationSampleException | CrossValidationException e) {
                    if (stopCrossValidationOnError()) {
                        throw e;
                    } else {
                        LOGGER.error(e);
                    }
                }

                iterationsCompleted++;
                fireCrossValidationContinued((int) (100 * ((float) iterationsCompleted / (float) numberOfIterations)));

                if (processInterrupted()) {
                    fireCrossValidationInterrupted();
                    return reportFactory.newMultiClassificationReport(iterations);
                }

            }
        }

        return reportFactory.newMultiClassificationReport(iterations);
    }
}