package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

/**
 * Фабрика средств кросс-валидации попарного классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see RankingPairwiseClassifierCrossValidator
 */
public interface RankingPairwiseClassifierCrossValidators {

    /**
     * Средство кросс-валидации, ориентированное на значение точности единичной попарной классификации
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<SingleClassificationReport> accuracyValidator();

    /**
     * Средство кросс-валидации, ориентированное на значение точности единичной попарной классификации
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<SingleClassificationReport> asyncAccuracyValidator();

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> averageAccuracyValidator();

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAverageAccuracyValidator();

    /**
     * Средство анализа зависимости точности классификации от размера выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа зависимости точности классификации от размера выборки и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> accuracyDependenceOnPreciseIntervalJudgementsRatioAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAccuracyDependenceOnPreciseIntervalJudgementsRatioAnalyzer();
}
