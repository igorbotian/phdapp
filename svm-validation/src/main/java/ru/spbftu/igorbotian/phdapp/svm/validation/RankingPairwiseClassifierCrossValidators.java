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
    RankingPairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator();

    /**
     * Средство кросс-валидации, ориентированное на значение точности единичной попарной классификации
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<SingleClassificationReport> asyncPrecisionValidator();

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> averagePrecisionValidator();

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAveragePrecisionValidator();

    /**
     * Средство анализа зависимости точности классификации от размера выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа зависимости точности классификации от размера выборки и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     *
     * @return средство кросс-валидации для данного действия
     */
    RankingPairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncRankingPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer();
}
