package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.ClassifiedObject;
import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;

import java.util.Set;
import java.util.function.BiFunction;

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
     * @param ratio  процентное соотношение частей выборки (50 - выборки будут иметь одинаковый размер;
     *               0 - первая выборка пустая; 100 - вторая выборка пустая)
     * @return пара частей исходной выборки
     * @throws CrossValidationSampleException в случае ошибки разбиения выборки
     * @throws NullPointerException           если выборка не задана
     * @throws IllegalArgumentException       если процентное соотношение выходит за пределы допустимых значений
     */
    Pair<ClassifiedData, ClassifiedData> divideSampleIntoTwoGroups(ClassifiedData sample, int ratio)
            throws CrossValidationSampleException;

    /**
     * Генерация обучающей выборки для кросс-валидации классификатора на базе классифицированных данных
     *
     * @param source                набор классифицированных данных
     * @param ratio                 процентное соотношение точных и интервальных экспертных оценок в обучающей выбоорке
     *                              (50 - выборка состоит из одинакового количества оценок двух видов;
     *                              0 - выборка состоит только из точных оценок;
     *                              100 - выборка состоит только из интервальных оценок)
     * @param maxJudgementGroupSize максимально допустимое количество объектов в одной из двух частей интервальной
     *                              экспертной оценки (положительное целое число, большее двух)
     * @param expertFunction        функция, определящая экспертную оценку для заданных наборов объектов;
     *                              возвращает 1, если первая группа объектов предпочтительнее второй;
     *                              возвращает -1, если вторая группа предпочтительнее первой
     * @return обучающая выборка
     * @throws CrossValidationSampleException в случае генерации обучающей выборки
     * @throws NullPointerException           если набор данных не задан
     * @throws IllegalArgumentException       если процентное соотношение выходит за пределы допустимых значений
     */
    PairwiseTrainingSet generateTrainingSet(ClassifiedData source, int ratio, int maxJudgementGroupSize,
                                            BiFunction<Set<? extends ClassifiedObject>,
                                                    Set<? extends ClassifiedObject>,
                                                    Integer> expertFunction)
            throws CrossValidationSampleException;
}
