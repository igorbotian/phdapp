package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.svm.validation.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

/**
 * Фабрика средств кросс-валидации попарного классификатора
 *
 * @see PairwiseClassifierCrossValidator
 */
public interface IntervalPairwiseClassifierCrossValidators {

    /**
     * Средство кросс-валидации, ориентированное на значение точности единичной попарной классификации
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<SingleClassificationReport> precisionValidator();

    /**
     * Средство кросс-валидации, ориентированное на значение точности единичной попарной классификации
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncPairwiseClassifierCrossValidator<SingleClassificationReport> asyncPrecisionValidator();

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> averagePrecisionValidator();

    /**
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncPairwiseClassifierCrossValidator<MultiClassificationReport> asyncAveragePrecisionValidator();

    /**
     * Средство анализа зависимости точности классификации от размера выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа зависимости точности классификации от размера выборки и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     * и работающее в асинхронном режиме
     *
     * @return средство кросс-валидации для данного действия
     */
    AsyncPairwiseClassifierCrossValidator<MultiClassificationReport> asyncPrecisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer();
}
