package ru.spbftu.igorbotian.phdapp.svm.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.svm.IntervalClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.ReportFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;

/**
 * Реализация фабрики средств кросс-валидации попарного классификатора
 *
 * @see IntervalPairwiseClassifierCrossValidators
 */
@Singleton
class IntervalPairwiseClassifierCrossValidatorsImpl implements IntervalPairwiseClassifierCrossValidators {

    /**
     * Средство кросс-валидации, ориентированное на значение точности единичной попарной классификации
     */
    private final PairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator;

    @Inject
    public IntervalPairwiseClassifierCrossValidatorsImpl(CrossValidationSampleManager sampleManager,
                                                         IntervalClassifierParameterFactory classifierParameterFactory,
                                                         CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                         ReportFactory reportFactory,
                                                         MathDataFactory mathDataFactory,
                                                         DataFactory dataFactory) {

        this.precisionValidator = new PrecisionValidator(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, mathDataFactory, dataFactory);
    }

    @Override
    public PairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator() {
        return precisionValidator;
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
