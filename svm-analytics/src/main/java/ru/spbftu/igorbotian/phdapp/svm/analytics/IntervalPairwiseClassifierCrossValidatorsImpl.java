package ru.spbftu.igorbotian.phdapp.svm.analytics;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

import java.util.Objects;

/**
 * Реализация фабрики средств кросс-валидации попарного классификатора
 *
 * @see IntervalPairwiseClassifierCrossValidators
 */
@Singleton
class IntervalPairwiseClassifierCrossValidatorsImpl implements IntervalPairwiseClassifierCrossValidators {

    /**
     * Реализация по умолчанию попарного классификатора, над которым проводится кросс-валидация
     */
    private PairwiseClassifier classifier;

    @Inject
    public IntervalPairwiseClassifierCrossValidatorsImpl(PairwiseClassifier classifier) {
        this.classifier = Objects.requireNonNull(classifier);

        // TODO init validators
    }

    @Override
    public PairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator() {
        return null; // TODO
    }

    @Override
    public PairwiseClassifierCrossValidator<MultiClassificationReport> averagePrecisionValidator() {
        return null; // TODO
    }

    @Override
    public PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnSampleSizeAnalyzer() {
        return null; // TODO
    }

    @Override
    public PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnTrainingSetSizeAnalyzer() {
        return null; // TODO
    }

    @Override
    public PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnClassifierParametersAnalyzer() {
        return null; // TODO
    }

    @Override
    public PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer() {
        return null; // TODO
    }
}
