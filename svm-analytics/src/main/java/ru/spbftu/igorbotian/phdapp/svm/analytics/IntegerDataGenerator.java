/**
 * Copyright (c) 2014 Igor Botian
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.svm.analytics;

import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.*;
import java.util.function.Function;

/**
 * Генератор наборов исходных данных с целочисленными параметрами со значениями в заданных диапазонах
 *
 * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedData
 * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedData
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
 */
public final class IntegerDataGenerator {

    /**
     * Для генерации случайных целых чисел
     */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private IntegerDataGenerator() {
        //
    }

    /**
     * Генерация набора исходных данных со значениями целочисленных параметров в заданных диапазонах
     *
     * @param size            необходимое количество объектов, которое необходимо сгенерировать
     * @param classes         набор классов, которые будут использованы в дальнейшей классификации генерируемого набора исходных данных
     * @param paramValueRange ассоциативный массив "название параметра" - "диапазон его возможных значений"
     * @return набор исходных данных, предназначенный для дальнейшей классификации
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException в случае проблемы построения набора исходных данных
     * @throws java.lang.NullPointerException                   если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException               если количество генерируемых объектов имеет отрицательное значение;
     *                                                          если ни одного параметра не задано
     */
    public static UnclassifiedData generateData(int size, Set<? extends DataClass> classes,
                                                Map<String, Range<Integer>> paramValueRange) throws DataException {
        Objects.requireNonNull(classes);
        Objects.requireNonNull(paramValueRange);

        if (size < 0) {
            throw new IllegalArgumentException("Number of objects to generate cannot have a negative value");
        }

        if (paramValueRange.isEmpty()) {
            throw new IllegalArgumentException("Map of parameter value ranges cannot be empty");
        }

        Set<UnclassifiedObject> objects = new HashSet<>();

        for (int i = 0; i < size; i++) {
            objects.add(nextClassifiedObject(paramValueRange));
        }

        return DataFactory.newUnclassifiedData(classes, objects);
    }

    /**
     * Генерация набора исходных данных со значениями целочисленных параметров в заданных диапазонах, для объектов
     * которого известен реальный класс
     *
     * @param size            необходимое количество объектов, которое необходимо сгенерировать
     * @param paramValueRange ассоциативный массив "название параметра" - "диапазон его возможных значений"
     * @param classIdentifier механизм определения реального класса для каждого генерируемого объекта
     * @return набор исходных данных, для объектов которого известны реальные классы
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException в случае проблемы построения набора исходных данных
     * @throws java.lang.NullPointerException                   если хотя бы один из параметров не задан
     * @throws java.lang.IllegalArgumentException               если количество генерируемых объектов имеет отрицательное значение;
     *                                                          если ни одного параметра не задано
     */
    public static TrainingData generateTrainingData(int size, Map<String, Range<Integer>> paramValueRange,
                                                   Function<Set<Parameter<?>>, DataClass> classIdentifier)
            throws DataException {

        Objects.requireNonNull(paramValueRange);

        if (size < 0) {
            throw new IllegalArgumentException("Number of objects to generate cannot have a negative value");
        }

        if (paramValueRange.isEmpty()) {
            throw new IllegalArgumentException("Map of parameter value ranges cannot be empty");
        }

        Set<TrainingObject> objects = new HashSet<>();
        Set<DataClass> classes = new HashSet<>();

        for (int i = 0; i < size; i++) {
            TrainingObject obj = nextTrainingObject(paramValueRange, classIdentifier);
            objects.add(obj);
            classes.add(obj.realClass());
        }

        return DataFactory.newTrainingData(classes, objects);
    }

    private static UnclassifiedObject nextClassifiedObject(Map<String, Range<Integer>> paramValueRange) {
        Set<Parameter<?>> params = generateParams(paramValueRange);
        return DataFactory.newUnclassifiedObject(generateObjectId(params), params);
    }

    private static TrainingObject nextTrainingObject(Map<String, Range<Integer>> paramValueRange,
                                                     Function<Set<Parameter<?>>, DataClass> classIdentifier) {
        Set<Parameter<?>> params = generateParams(paramValueRange);
        DataClass realClass = classIdentifier.apply(params);
        return DataFactory.newTrainingObject(generateObjectId(params), params, realClass);
    }

    private static Set<Parameter<?>> generateParams(Map<String, Range<Integer>> paramValueRange) {
        assert (paramValueRange != null);
        assert (!paramValueRange.isEmpty());

        Set<Parameter<?>> params = new HashSet<>();

        for (String paramName : paramValueRange.keySet()) {
            Range<Integer> range = paramValueRange.get(paramName);
            int value = random(range.lowerBound(), range.upperBound());
            params.add(DataFactory.newParameter(paramName, value, BasicDataTypes.INTEGER));
        }

        return params;
    }

    private static String generateObjectId(Set<Parameter<?>> params) {
        StringBuilder objectId = new StringBuilder();
        Iterator<Parameter<?>> it = params.iterator();

        objectId.append("[");

        while (it.hasNext()) {
            objectId.append(it.next().toString());

            if (it.hasNext()) {
                objectId.append("; ");
            }
        }

        objectId.append("]");

        return objectId.toString();
    }

    // нижняя и верхняя границы включительно
    private static int random(int min, int max) {
        assert (max >= min);
        return min + RANDOM.nextInt(max - min + 1);
    }
}
