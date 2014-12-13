package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Набор параметров конфигурации классификатора
 */
public interface IntervalClassifierParameters {

    /**
     * Получение значения параметра постоянной стоимости
     *
     * @return параметр классификатора со значением вещественного типа
     */
    MutableClassifierParameter<Double> constantCostParameter();

    /**
     * Получение значения параметра Гауссова ядра
     *
     * @return параметр классификатора со значением вещественного типа
     */
    MutableClassifierParameter<Double> gaussianKernelParameter();
}
