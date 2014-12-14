package ru.spbftu.igorbotian.phdapp.svm.analytics;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.svm.PairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleClassificationReport;

import java.util.Objects;

/**
 * Реализация фабрики средств кросс-валидации попарного классификатора
 *
 * @see IntervalPairwiseClassifierCrossValidators
 */
@Singleton
class IntervalPairwiseClassifierCrossValidatorsImpl implements IntervalPairwiseClassifierCrossValidators {

    /**
     * Средство генерации обучающей выборки для классификатора, используемое по-умолчанию
     */
    private SampleGenerator sampleGenerator;

    /**
     * Реализация по умолчанию попарного классификатора, над которым проводится кросс-валидация
     */
    private PairwiseClassifier classifier;

    @Inject
    public IntervalPairwiseClassifierCrossValidatorsImpl(SampleGenerator sampleGenerator,
                                                         PairwiseClassifier classifier) {
        this.sampleGenerator = Objects.requireNonNull(sampleGenerator);
        this.classifier = Objects.requireNonNull(classifier);

        // TODO init validators
    }

    @Override
    public IntervalPairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator() {
        return null; // TODO
    }

    @Override
    public IntervalPairwiseClassifierCrossValidator<MultiClassificationReport> averagePrecisionValidator() {
        return null; // TODO
    }

    @Override
    public IntervalPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnSampleSizeAnalyzer() {
        return null; // TODO
    }

    @Override
    public IntervalPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnTrainingSetSizeAnalyzer() {
        return null; // TODO
    }

    @Override
    public IntervalPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnClassifierParametersAnalyzer() {
        return null; // TODO
    }

    @Override
    public IntervalPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer() {
        return null; // TODO
    }
}
