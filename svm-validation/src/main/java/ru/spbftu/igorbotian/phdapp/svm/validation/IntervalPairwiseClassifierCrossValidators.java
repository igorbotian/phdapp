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
     * Средство кросс-валидации, ориентированное на среднее значение точности серии попарных классификаций
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> averagePrecisionValidator();

    /**
     * Средство анализа зависимости точности классификации от размера обучающей выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnSampleSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от размера обучающей выборки
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnTrainingSetSizeAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от параметров классификации
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnClassifierParametersAnalyzer();

    /**
     * Средство анализа завимимости точности классификации от процентного соотношения количества точных/интервальных
     * экспертных оценок
     *
     * @return средство кросс-валидации для данного действия
     */
    PairwiseClassifierCrossValidator<MultiClassificationReport> precisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer();
}
