package ru.spbftu.igorbotian.phdapp.svm;

import java.util.Set;

/**
 * Набор параметров конфигурации классификатора
 */
public interface IntervalClassifierParameterFactory {

    /**
     * Идентификатор параметра постоянной стоимости
     */
    public static final String CONSTANT_COST_PARAM_ID = "constantCostParameter";

    /**
     * Значение по умолчанию параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_DEFAULT_VALUE = 100.0;

    /**
     * Минимально допустимое значение параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_MIN_VALUE = (double) Integer.MIN_VALUE;

    /**
     * Максимально допустимое значение параметра постоянной стоимости
     */
    public static final double CONSTANT_COST_PARAM_MAX_VALUE = (double) Integer.MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Идентификатор параметра Гауссова ядра
     */
    public static final String GAUSSIAN_KERNEL_PARAM_ID = "gaussianKernelParameter";

    /**
     * Значение по умолчанию параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE = 0.1;

    /**
     * Минимально допустимое значение параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_MIN_VALUE = (double) Integer.MIN_VALUE;

    /**
     * Максимально допустимое значение параметра Гауссова ядра
     */
    public static final double GAUSSIAN_KERNEL_PARAM_MAX_VALUE = (double) Integer.MAX_VALUE;

    //-------------------------------------------------------------------------

    /**
     * Создание параметра постоянной стоимости со значением по умолчанию (<code>CONSTANT_COST_PARAM_DEFAULT_VALUE</code>)
     *
     * @return параметр классификатора со значением вещественного типа
     */
    ClassifierParameter<Double> newConstantCostParameter();

    /**
     * Создание параметра постоянной стоимости с заданным значением
     *
     * @param value значение параметра (допустимые значения:
     *              от <code>CONSTANT_COST_PARAM_MIN_VALUE</code> до <code>CONSTANT_COST_PARAM_MAX_VALUE</code>
     * @return параметр классификатора со значением вещественного типа
     * @throws IllegalArgumentException если значение меньше минимально допустимого или больше максимально допустимого
     */
    ClassifierParameter<Double> newConstantCostParameter(double value);

    //-------------------------------------------------------------------------

    /**
     * Создание значения параметра Гауссова ядра со значением по умолчанию (<code>GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE</code>)
     *
     * @return параметр классификатора со значением вещественного типа
     */
    ClassifierParameter<Double> newGaussianKernelParameter();

    /**
     * Создание параметра Гауссова ядра с заданным значением
     *
     * @param value значение параметра (допустимые значения:
     *              от <code>GAUSSIAN_KERNEL_PARAM_MIN_VALUE</code> до <code>GAUSSIAN_KERNEL_PARAM_MAX_VALUE</code>
     * @return параметр классификатора со значением вещественного типа
     * @throws IllegalArgumentException если значение меньше минимально допустимого или больше максимально допустимого
     */
    ClassifierParameter<Double> newGaussianKernelParameter(double value);

    //-------------------------------------------------------------------------

    /**
     * Получение множества всех параметров классификатора со значениями по умолчанию
     * @return непустое множество параметров классификатора
     */
    Set<ClassifierParameter<?>> defaultValues();
}
