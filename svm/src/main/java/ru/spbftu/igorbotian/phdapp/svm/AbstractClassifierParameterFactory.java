package ru.spbftu.igorbotian.phdapp.svm;

import java.util.Comparator;

/**
 * Абстрактная реализация фабрики параметров классификатора
 */
public abstract class AbstractClassifierParameterFactory {

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
    protected <T> ClassifierParameter<T> newParameter(String name, Class<T> valueClass, T value,
                                                      T minValue, T maxValue, Comparator<T> comparator) {
        return new DefaultClassifierParameterImpl<>(name, valueClass, value, minValue, maxValue, comparator);
    }
}
