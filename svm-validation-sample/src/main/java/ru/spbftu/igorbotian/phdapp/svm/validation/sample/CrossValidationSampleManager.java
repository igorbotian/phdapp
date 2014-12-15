package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

/**
 * Средство формирования выборки для кросс-валидации классификатора
 */
public interface CrossValidationSampleManager {

    /**
     * Задание средства генерации выборки для кросс-валидации классификатора
     *
     * @param sampleGenerator средство генерации выборки
     * @throws java.lang.NullPointerException если параметр не задан
     */
    void setSampleGenerator(CrossValidationSampleGenerator sampleGenerator);

    /**
     * Получение доступа к средству генерации выборки для кросс-валидации классификатора
     * @return средство генерации выборки для кросс-валидации классификатора
     */
    CrossValidationSampleGenerator sampleGenerator();
}
