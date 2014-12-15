package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.Pair;

/**
 * Средство формирования выборки для кросс-валидации классификатора
 */
public interface CrossValidationSampleManager {

    /**
     * Получение доступа к средству генерации выборки для кросс-валидации классификатора
     *
     * @return средство генерации выборки для кросс-валидации классификатора
     */
    CrossValidationSampleGenerator sampleGenerator();

    /**
     * Генерация выборки для кросс-валидации классификатора
     *
     * @param sampleSize размер выборки (> 1; <code>Integer.MAX_VALUE</code>);
     *                   если размер имеет нечётное значение, то оно увеличивается на единицу;
     *                   необходимость чётного значения обуславливается равным количеством
     *                   двух сгенерированных частей выборки
     * @return набор верно классифицированных данных, подлежащих кросс-валидации
     * @throws CrossValidationSampleException в случае ошибки формирования выборки
     * @throws IllegalArgumentException       если размер выборки не имеет положительного значения
     */
    ClassifiedData generateSample(int sampleSize) throws CrossValidationSampleException;

    /**
     * Разбиение выборки, сгенерированной для кросс-валидации классификатора, на две части заданного размера
     *
     * @param sample выборка для кросс-валидации классификатора
     * @param ratio  процентное соотношение частей выборки (50 - выборку будут иметь одинаковый размер;
     *               0 - первая выборка пустая; 100 - вторая выборка пустая)
     * @return пара частей исходной выборки
     * @throws CrossValidationSampleException в случае ошибки разбиения выборки
     * @throws NullPointerException           если выборка не задана
     * @throws IllegalArgumentException       если
     */
    Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample, int ratio)
            throws CrossValidationSampleException;
}
