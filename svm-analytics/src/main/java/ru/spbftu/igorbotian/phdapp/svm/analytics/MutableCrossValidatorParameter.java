package ru.spbftu.igorbotian.phdapp.svm.analytics;

import ru.spbftu.igorbotian.phdapp.svm.MutableClassifierParameter;

/**
 * Параметр средства кросс-валидации классификатора, значение которого может быть изменено
 */
public interface MutableCrossValidatorParameter<T> extends CrossValidatorParameter<T> {

    /**
     * Значение точного значения параметра
     *
     * @return значение в виде параметра классификатора
     */
    MutableClassifierParameter<T> value();

    /**
     * Нижняя граница интервального значения параметра
     *
     * @return значение в виде параметра классификатора
     */
    MutableClassifierParameter<T> lowerBound();

    /**
     * Верхняя граница интервального значения параметра
     *
     * @return значение в виде параметра классификатора
     */
    MutableClassifierParameter<T> upperBound();

    /**
     * Величина изменения значения параметра при последовательном изменении от нижней границы до верхней
     *
     * @return значение в виде параметра классификатора
     */
    MutableClassifierParameter<T> stepSize();
}
