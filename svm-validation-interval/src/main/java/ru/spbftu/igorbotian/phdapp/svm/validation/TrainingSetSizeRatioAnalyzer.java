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
 * Средство анализа завимимости точности классификации от размера обучающей выборки
 */
class TrainingSetSizeRatioAnalyzer extends AbstractRankingPairwiseClassifierCrossValidator<MultiClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(TrainingSetSizeRatioAnalyzer.class);

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final AccuracyValidator accuracyValidator;

    public TrainingSetSizeRatioAnalyzer(CrossValidationSampleManager sampleManager,
                                        IntervalClassifierParameterFactory classifierParameterFactory,
                                        CrossValidatorParameterFactory crossValidatorParameterFactory,
                                        ReportFactory reportFactory,
                                        AccuracyValidator accuracyValidator,
                                        ApplicationConfiguration appConfig) {

        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory, appConfig);
        this.accuracyValidator = Objects.requireNonNull(accuracyValidator);
    }

    @Override
    protected MultiClassificationReport validate(RankingPairwiseClassifier classifier,
                                                 Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                 CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        CrossValidatorParameter<Integer> ratio = specificValidatorParams.trainingTestingSetsSizeRatio();
        int lowerBound = ratio.lowerBound().value();
        int upperBound = ratio.upperBound().value();
        int stepSize = ratio.stepSize().value();
        int numberOfIterations = (int) Math.ceil((upperBound - lowerBound) / stepSize) + 1 /* incl. upperBound */;
        int iterationsCompleted = 0;
        List<SingleClassificationReport> iterations = new ArrayList<>(iterationsCompleted);

        for (int i = lowerBound; i <= upperBound; i += stepSize) {
            LOGGER.debug("Ratio = " + i);
            CrossValidatorParameter<Integer> ratioParam = specificValidatorParams.trainingTestingSetsSizeRatio(i);

            try {
                iterations.add(accuracyValidator.validate(
                        classifier,
                        specificClassifierParams,
                        override(specificValidatorParams, Collections.singleton(ratioParam))
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
