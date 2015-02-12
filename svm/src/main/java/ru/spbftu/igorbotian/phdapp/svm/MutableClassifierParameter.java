package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Параметр конфигурации классификатора, значение которого может быть изменено
 */
public interface MutableClassifierParameter<T> extends ClassifierParameter<T> {

    /**
     * Задание нового значения параметра конфигурации классификатора
     *
     * @param newValue новое значение заданного типа
     * @throws IllegalArgumentException если новое значение некорректное
     */
    void setValue(T newValue);
}
