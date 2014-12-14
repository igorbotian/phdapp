package ru.spbftu.igorbotian.phdapp.svm.analytics;

import ru.spbftu.igorbotian.phdapp.svm.analytics.report.Report;

/**
 * Средство кросс-валидации попарного классификатора, который поддерживает интервальные экспертные оценки
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.PairwiseClassifierCrossValidator
 */
public interface IntervalPairwiseClassifierCrossValidator<R extends Report>
        extends PairwiseClassifierCrossValidator<R> {

    /**
     * Задание средства генерации выборки для кросс-валидации
     *
     * @param sampleGenerator средство генерации выборки для кросс-валидации
     * @throws java.lang.NullPointerException если параметр не задан
     */
    void setSampleGenerator(SampleGenerator sampleGenerator);

    /**
     * Получение доступа к средству генерации выборки для кросс-валидации
     *
     * @return средство генерации выборки для попарного классификатора
     */
    SampleGenerator sampleGenerator();
}
