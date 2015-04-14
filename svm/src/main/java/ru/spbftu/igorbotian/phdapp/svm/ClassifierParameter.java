package ru.spbftu.igorbotian.phdapp.svm;

import java.util.Comparator;

/**
 * Параметр классификатора
 */
public interface ClassifierParameter<T> {

    /**
     * Название параметра
     * @return строковый идентификатор
     */
    String name();

    /**
     * Значение параметра
     * @return значение заданного типа
     */
    T value();

    /**
     * Получение минимально допустимого значения параметра
     * @return значение заданного типа
     */
    T minValue();

    /**
     * Получение максимально допустимого значения параметра
     * @return значение заданного типа
     */
    T maxValue();

    /**
     * Тип значения параметра
     * @return класс, соответствующий типу параметра
     */
    Class<T> valueClass();

    /**
     * Компаратор значений данного параметра
     * @return компаратор
     */
    Comparator<T> comparator();
}
