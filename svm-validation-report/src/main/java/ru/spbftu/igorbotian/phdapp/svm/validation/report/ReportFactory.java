package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import java.util.List;

/**
 * Фабрика различных отчётов по работе классификатора
 */
public interface ReportFactory {

    /**
     * Создание отчёта, содержащего различные метрики по работе классификатора с заданными параметрами
     *
     * @param sampleSize                      количество уникальных элементов в выборке (обучащая + тестирующая) (положительное целое число)
     * @param constantCostParameter      постоянный параметр стоиомсти (положительное вещественное число)
     * @param gaussianKernelParameter    параметр Гауссова ядра (вещественное число)
     * @param judgedSampleItemsRatio          количество элементов выборки, входящих в обучающую выборки
     *                                        (в процентном соотношении) (значение в пределах [0.0;1.0])
     * @param preciseIntervalSampleItemsRatio количество интервальных предпочтений среди общего количества предпочтений в обучающей выборке
     *                                        (в процентном соотношении) (значение в пределах [0.0;1.0])
     * @param accuracy                        метрика <code>Accuracy</code> (значение в пределах [0.0;1.0])
     * @param precision                       метрика <code>Precision</code> (значение в пределах [0.0;1.0])
     * @param recall                          метрика <code>Recall</code> (значение в пределах [0.0;1.0])
     * @return отчёт заданного типа
     * @throws java.lang.IllegalArgumentException если значение хотя бы одного параметра выходит за пределы допустимых значений
     */
    SingleClassificationReport newSingleClassificationReport(final int sampleSize,
                                                             final float constantCostParameter,
                                                             final float gaussianKernelParameter,
                                                             final float judgedSampleItemsRatio,
                                                             final float preciseIntervalSampleItemsRatio,
                                                             final float accuracy,
                                                             final float precision,
                                                             final float recall);

    /**
     * Создание сводного отчёта по работе классификатора на основе ряда одиночных классификаций
     * @param classifications список из отчётов по работе одиночных классификаций
     * @return отчёт заданного типа
     * @throws java.lang.NullPointerException если список отчётов не задан
     * @throws java.lang.IllegalArgumentException если список отчётов пустой
     */
    MultiClassificationReport newMultiClassificationReport(List<SingleClassificationReport> classifications);
}
