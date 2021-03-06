package ru.spbftu.igorbotian.phdapp.svm;

import java.util.Comparator;
import java.util.Set;

/**
 * Набор параметров конфигурации классификатора
 */
public interface IntervalClassifierParameterFactory {

    /**
     * Идентификатор штрафного параметра
     */
    public static final String PENALTY_PARAM_ID = "penaltyParameter";

    /**
     * Значение по умолчанию штрафного параметра
     */
    public static final double PENALTY_PARAM_DEFAULT_VALUE = 1.0;

    /**
     * Минимально допустимое значение штрафного параметра
     */
    public static final double PENALTY_PARAM_MIN_VALUE = (double) Integer.MIN_VALUE;

    /**
     * Максимально допустимое значение штрафного параметра
     */
    public static final double PENALTY_PARAM_MAX_VALUE = (double) Integer.MAX_VALUE;

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
     * Создание штрафного параметра со значением по умолчанию (<code>PENALTY_PARAM_DEFAULT_VALUE</code>)
     *
     * @return параметр классификатора со значением вещественного типа
     */
    ClassifierParameter<Double> penaltyParameter();

    /**
     * Создание штрафного параметра с заданным значением
     *
     * @param value значение параметра (допустимые значения:
     *              от <code>PENALTY_PARAM_MIN_VALUE</code> до <code>PENALTY_PARAM_MAX_VALUE</code>
     * @return параметр классификатора со значением вещественного типа
     * @throws IllegalArgumentException если значение меньше минимально допустимого или больше максимально допустимого
     */
    ClassifierParameter<Double> penaltyParameter(double value);

    //-------------------------------------------------------------------------

    /**
     * Создание значения параметра Гауссова ядра со значением по умолчанию (<code>GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE</code>)
     *
     * @return параметр классификатора со значением вещественного типа
     */
    ClassifierParameter<Double> gaussianKernelParameter();

    /**
     * Создание параметра Гауссова ядра с заданным значением
     *
     * @param value значение параметра (допустимые значения:
     *              от <code>GAUSSIAN_KERNEL_PARAM_MIN_VALUE</code> до <code>GAUSSIAN_KERNEL_PARAM_MAX_VALUE</code>
     * @return параметр классификатора со значением вещественного типа
     * @throws IllegalArgumentException если значение меньше минимально допустимого или больше максимально допустимого
     */
    ClassifierParameter<Double> gaussianKernelParameter(double value);

    //-------------------------------------------------------------------------

    /**
     * Получение множества всех параметров классификатора со значениями по умолчанию
     * @return непустое множество параметров классификатора
     */
    Set<ClassifierParameter<?>> defaultValues();

    /**
     * Создание параметра классификатора с заданными характеристиками
     *
     * @param name       идентификатора параметра
     * @param valueClass тип значения параметра
     * @param value      значение параметра по умолчанию
     * @param minValue   минимально допустимое значение параметра
     * @param maxValue   максимально допустимое значение параметра
     * @param comparator компаратор значений данного параметра
     * @param <T>        типа значения параметра
     * @return параметр с заданными характеристиками
     * @throws NullPointerException     если хотя бы один из параметров не задан
     * @throws IllegalArgumentException если идентификатор параметра имеет пустое значение;
     *                                  если минимальное допустимое значение имеет большее значение,
     *                                  чем максимально допустимое значение параметра
     */
    <T> ClassifierParameter<T> newParameter(String name, T value, T minValue, T maxValue, Class<T> valueClass,
                                            Comparator<T> comparator);
}
