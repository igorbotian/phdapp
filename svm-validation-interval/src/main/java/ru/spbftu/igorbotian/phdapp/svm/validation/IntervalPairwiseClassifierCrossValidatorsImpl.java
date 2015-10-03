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
    private final AccuracyValidator accuracyValidator;

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     */
    private final AverageAccuracyValidator averageAccuracyValidator;

    /**
     * Средство анализа зависимости точности классификации от размера обучающей выборки
     */
    private final AccuracyDependenceOnSampleSizeAnalyzer accuracyDependenceOnSampleSizeAnalyzer;

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     */
    private final PreciseIntervalJudgementsRatioAnalyzer preciseIntervalJudgementsRatioAnalyzer;

    /**
     * Средство анализа завимимости точности классификации от параметров классификации
     */
    private final AccuracyDependenceOnClassifierParametersAnalyzer accuracyDependenceOnClassifierParametersAnalyzer;

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

        this.accuracyValidator = new AccuracyValidator(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, mathDataFactory, dataFactory, appConfig);
        this.averageAccuracyValidator = new AverageAccuracyValidator(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, accuracyValidator, appConfig);
        this.accuracyDependenceOnSampleSizeAnalyzer = new AccuracyDependenceOnSampleSizeAnalyzer(sampleManager,
                classifierParameterFactory, crossValidatorParameterFactory, reportFactory, accuracyValidator, appConfig);
        this.preciseIntervalJudgementsRatioAnalyzer
                = new PreciseIntervalJudgementsRatioAnalyzer(sampleManager,
                classifierParameterFactory, crossValidatorParameterFactory, reportFactory, accuracyValidator, appConfig);
        this.accuracyDependenceOnClassifierParametersAnalyzer = new AccuracyDependenceOnClassifierParametersAnalyzer(
                sampleManager, classifierParameterFactory, crossValidatorParameterFactory, reportFactory,
                accuracyValidator, appConfig);
        this.trainingSetSizeRatioAnalyzer = new TrainingSetSizeRatioAnalyzer(sampleManager, classifierParameterFactory,
                crossValidatorParameterFactory, reportFactory, accuracyValidator, appConfig);
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<SingleClassificationReport> accuracyValidator() {
        return accuracyValidator;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<SingleClassificationReport> asyncAccuracyValidator() {
        return accuracyValidator;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> averageAccuracyValidator() {
        return averageAccuracyValidator;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAverageAccuracyValidator() {
        return averageAccuracyValidator;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnSampleSizeAnalyzer() {
        return accuracyDependenceOnSampleSizeAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnSampleSizeAnalyzer() {
        return accuracyDependenceOnSampleSizeAnalyzer;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnTrainingSetSizeAnalyzer() {
        return trainingSetSizeRatioAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnTrainingSetSizeAnalyzer() {
        return trainingSetSizeRatioAnalyzer;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnClassifierParametersAnalyzer() {
        return accuracyDependenceOnClassifierParametersAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnClassifierParametersAnalyzer() {
        return accuracyDependenceOnClassifierParametersAnalyzer;
    }

    @Override
    public RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnPreciseIntervalJudgementsRatioAnalyzer() {
        return preciseIntervalJudgementsRatioAnalyzer;
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnPreciseIntervalJudgementsRatioAnalyzer() {
        return preciseIntervalJudgementsRatioAnalyzer;
    }
}
