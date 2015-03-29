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
 * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
 * экспертных оценок
 */
class PreciseIntervalJudgementsRatioAnalyzer
        extends AbstractRankingPairwiseClassifierCrossValidator<MultiClassificationReport> {

    private static final Logger LOGGER = Logger.getLogger(PreciseIntervalJudgementsRatioAnalyzer.class);

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final PrecisionValidator precisionValidator;

    public PreciseIntervalJudgementsRatioAnalyzer(CrossValidationSampleManager sampleManager,
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

        CrossValidatorParameter<Integer> ratio = specificValidatorParams.preciseIntervalJudgmentsCountRatio();
        int lowerBound = ratio.lowerBound().value();
        int upperBound = ratio.upperBound().value();
        int stepSize = ratio.stepSize().value();
        List<SingleClassificationReport> iterations = new ArrayList<>((upperBound - lowerBound) / stepSize);

        for (int i = lowerBound; i <= upperBound; i += stepSize) {
            LOGGER.debug("Ratio = " + i);
            CrossValidatorParameter<Integer> ratioParam = specificValidatorParams.preciseIntervalJudgmentsCountRatio(i);

            try {
                iterations.add(precisionValidator.validate(
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

            fireCrossValidationContinued((int) (100 * (((float) i - (float) lowerBound)
                    / ((float) upperBound - (float) lowerBound))));

            if (processInterrupted()) {
                fireCrossValidationInterrupted();
                return reportFactory.newMultiClassificationReport(iterations);
            }
        }

        return reportFactory.newMultiClassificationReport(iterations);
    }
}
