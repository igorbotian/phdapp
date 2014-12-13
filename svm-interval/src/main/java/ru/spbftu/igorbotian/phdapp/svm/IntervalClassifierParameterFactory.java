package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Набор параметров конфигурации классификатора
 */
public interface IntervalClassifierParameterFactory {

    /**
     * Создание значения параметра постоянной стоимости
     *
     * @return параметр классификатора со значением вещественного типа
     */
    MutableClassifierParameter<Double> newConstantCostParameter();

    /**
     * Создание значения параметра Гауссова ядра
     *
     * @return параметр классификатора со значением вещественного типа
     */
    MutableClassifierParameter<Double> newGaussianKernelParameter();
}
