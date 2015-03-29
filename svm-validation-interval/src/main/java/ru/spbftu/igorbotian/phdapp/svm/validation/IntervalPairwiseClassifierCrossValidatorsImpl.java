package ru.spbftu.igorbotian.phdapp.svm.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
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
    private final PrecisionValidator precisionValidator;

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     */
    private final AveragePrecisionValidator averagePrecisionValidator;

    /**
     * Средство анализа зависимости точности классификации от размера обучающей выборки
     */
    private final PrecisionDependenceOnSampleSizeAnalyzer precisionDependenceOnSampleSizeAnalyzer;

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     */
    private final PreciseIntervalJudgementsRatioAnalyzer preciseIntervalJudgementsRatioAnalyzer;

    /**
     * Средство анализа завимимости точности классификации от параметров классификации
     */
    private final PrecisionDependenceOnClassifierParametersAnalyzer precisionDependenceOnClassifierParametersAnalyzer;

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки
     */
    private final TrainingSetSizeRatioAnalyzer trainingSetSizeRatioAnalyzer;

    @Inject
    public IntervalPairwiseClassifierCrossValidatorsImpl(CrossValidationSampleManager sampleManager,
                                                         IntervalClassifierParameterFactory classifierParameterFactory,
                                                         CrossValidatorParameterFactory crossValidatorParameterFactory,
                                                         ReportFactory reportFactory,
                                                         MathDataFactory mathDataFactory,
                                                         DataFactory dataFactory,
                                                         ApplicationConfiguration appConfig) {

        this.precisionValidator = new PrecisionValidator(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, mathDataFactory, dataFactory, appConfig);
        this.averagePrecisionValidator = new AveragePrecisionValidator(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, precisionValidator, appConfig);
        this.precisionDependenceOnSampleSizeAnalyzer = new PrecisionDependenceOnSampleSizeAnalyzer(sampleManager,
                classifierParameterFactory, crossValidatorParameterFactory, reportFactory, precisionValidator, appConfig);
        this.preciseIntervalJudgementsRatioAnalyzer
                = new PreciseIntervalJudgementsRatioAnalyzer(sampleManager,
                classifierParameterFactory, crossValidatorParameterFactory, reportFactory, precisionValidator, appConfig);
        this.precisionDependenceOnClassifierParametersAnalyzer = new PrecisionDependenceOnClassifierParametersAnalyzer(
                sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory,
                precisionValidator, appConfig);
        this.trainingSetSizeRatioAnalyzer = new TrainingSetSizeRatioAnalyzer(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, precisionValidator, appConfig);
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator() {
        return precisionValidator;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<SingleClassificationReport> asyncPrecisionValidator() {
        return precisionValidator;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> averagePrecisionValidator() {
        return averagePrecisionValidator;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAveragePrecisionValidator() {
        return averagePrecisionValidator;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnSampleSizeAnalyzer() {
        return precisionDependenceOnSampleSizeAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnSampleSizeAnalyzer() {
        return precisionDependenceOnSampleSizeAnalyzer;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnTrainingSetSizeAnalyzer() {
        return trainingSetSizeRatioAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnTrainingSetSizeAnalyzer() {
        return trainingSetSizeRatioAnalyzer;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnClassifierParametersAnalyzer() {
        return precisionDependenceOnClassifierParametersAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnClassifierParametersAnalyzer() {
        return precisionDependenceOnClassifierParametersAnalyzer;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer() {
        return preciseIntervalJudgementsRatioAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer() {
        return preciseIntervalJudgementsRatioAnalyzer;
    }
}
