package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
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
        extends AbstractPairwiseClassifierCrossValidator<MultiClassificationReport> {

    /**
     * Средство кросс-валидации, направленное на точность работы попарного классификатора
     */
    private final PrecisionValidator precisionValidator;

    public PreciseIntervalJudgementsRatioAnalyzer(CrossValidationSampleManager sampleManager,
                                                     IntervalClassifierParameterFactory classifierParameterFactory,
                                                     CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                     ReportFactory reportFactory,
                                                     PrecisionValidator precisionValidator) {
        super(sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory);
        this.precisionValidator = Objects.requireNonNull(precisionValidator);
    }

    @Override
    protected MultiClassificationReport validate(PairwiseClassifier classifier,
                                                 Set<? extends ClassifierParameter<?>> specificClassifierParams,
                                                 CrossValidatorParameterFactory specificValidatorParams)
            throws CrossValidationSampleException, CrossValidationException {

        CrossValidatorParameter<Integer> ratio = specificValidatorParams.preciseIntervalJudgmentsCountRatio();
        int lowerBound = ratio.lowerBound().value();
        int upperBound = ratio.upperBound().value();
        int stepSize = ratio.stepSize().value();
        List<SingleClassificationReport> iterations = new ArrayList<>((upperBound - lowerBound) / stepSize);

        for(int i = lowerBound; i <= upperBound; i += stepSize) {
            CrossValidatorParameter<Integer> ratioParam = specificValidatorParams.preciseIntervalJudgmentsCountRatio(i);

            iterations.add(precisionValidator.validate(
                    classifier,
                    specificClassifierParams,
                    override(specificValidatorParams, Collections.singleton(ratioParam))
            ));
        }

        return reportFactory.newMultiClassificationReport(iterations);
    }
}
