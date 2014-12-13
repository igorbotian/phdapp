package ru.spbftu.igorbotian.phdapp.svm.analytics;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;

/**
 * Параметр средства кросс-валидации классификатора
 */
public interface CrossValidatorParameter<T> {

    /**
     * Идентификатор параметра
     *
     * @return строковый идентификатор параметра
     */
    String name();

    /**
     * Тип значений параметра
     *
     * @return класс значений параметра
     */
    Class<T> valueClass();

    /**
     * Значение точного значения параметра
     *
     * @return значение в виде параметра классификатора
     */
    ClassifierParameter<T> value();

    /**
     * Нижняя граница интервального значения параметра
     *
     * @return значение в виде параметра классификатора
     */
    ClassifierParameter<T> lowerBound();

    /**
     * Верхняя граница интервального значения параметра
     *
     * @return значение в виде параметра классификатора
     */
    ClassifierParameter<T> upperBound();

    /**
     * Величина изменения значения параметра в процессе кросс-валидации
     *
     * @return значение в виде параметра классификатора
     */
    ClassifierParameter<T> stepSize();
}
