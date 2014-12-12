package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Параметр классификатора
 */
public interface ClassifierParameter<T> {

    /**
     * Название параметра
     * @return строковый идентификатор
     */
    public String name();

    /**
     * Значение параметра
     * @return значение заданного типа
     */
    public T value();

    /**
     * Получение минимально допустимого значения параметра
     * @return значение заданного типа
     */
    public T minValue();

    /**
     * Получение максимально допустимого значения параметра
     * @return значение заданного типа
     */
    public T maxValue();

    /**
     * Тип значения параметра
     * @return класс, соответствующий типу параметра
     */
    public Class<T> valueClass();
}
