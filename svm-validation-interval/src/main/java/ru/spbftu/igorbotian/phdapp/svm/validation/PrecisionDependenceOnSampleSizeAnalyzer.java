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

import java.util.*;

/**
 * Средство анализа зависимости точности классификации от размера обучающей выборки
 */
class PrecisionDependenceOnSampleSizeAnalyzer extends AbstractRankingPairwiseClassifierCrossValidator<MultiClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(PrecisionDependenceOnSampleSizeAnalyzer.class);

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final PrecisionValidator precisionValidator;

    public PrecisionDependenceOnSampleSizeAnalyzer(CrossValidationSampleManager sampleManager,
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

        CrossValidatorParameter<Integer> sampleSize = specificValidatorParams.sampleSize();
        int lowerBound = sampleSize.lowerBound().value();
        int upperBound = sampleSize.upperBound().value();
        int stepSize = sampleSize.stepSize().value();
        int numberOfIterations = (int) Math.ceil((upperBound - lowerBound) / stepSize) + 1 /* incl. upperBound */;
        int iterationsCompleted = 0;
        List<SingleClassificationReport> iterations = new ArrayList<>(numberOfIterations);

        for (int i = lowerBound; i <= upperBound; i += stepSize) {
            LOGGER.debug("Sample size: " + i);
            CrossValidatorParameter<Integer> sampleSizeParam = specificValidatorParams.sampleSize(i);

            try {
                iterations.add(precisionValidator.validate(
                        classifier,
                        specificClassifierParams,
                        override(specificValidatorParams, Collections.singleton(sampleSizeParam))
                ));
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

        return reportFactory.newMultiClassificationReport(iterations);
    }
}
