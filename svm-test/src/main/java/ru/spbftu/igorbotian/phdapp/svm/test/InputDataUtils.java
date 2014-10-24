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

package ru.spbftu.igorbotian.phdapp.svm.test;

import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.impl.InputDataFactory;

import java.util.*;
import java.util.function.Function;

/**
 * Класс, который предоставляет проводить различные операции над наборами исходных данных
 *
 * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedData
 * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedData
 */
public final class InputDataUtils {

    /**
     * Формирует новый набор исходных данных, тестирующая и обучающая выборка которого формируется случайным образом
     * из заданного набора классифицированных данных.
     *
     * @param data             набор классифицированных данных
     * @param trainingSetRatio процентное соотношение из исходного количества объектов,
     *                         которое будет составлять формируемая обучающая выборка (0.0;1.0)
     * @return новый набор исходных данных
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если
     *                                                          в случае проблемы формирования нового набора исходных данных
     * @throws java.lang.NullPointerException                   если набор классифицированных данных не задан
     * @throws java.lang.IllegalArgumentException               если процентное соотношение объектов обучающей выборки
     *                                                          не в диапазоне (0.0;1.0)
     */
    public static InputData shuffle(ClassifiedData data, float trainingSetRatio) throws DataException {
        if (trainingSetRatio <= 0.0f || trainingSetRatio >= 1.0f) {
            throw new IllegalArgumentException("Training set ratio should be in range of (0.0.;1.0.): "
                    + trainingSetRatio);
        }

        List<? extends ClassifiedObject> objects = new ArrayList<>(Objects.requireNonNull(data).objects());
        Collections.shuffle(objects);

        int sizeOfTrainingSet = (int) Math.ceil(trainingSetRatio * objects.size());
        Set<? extends ClassifiedObject> trainingSet = new HashSet<>(objects.subList(0, sizeOfTrainingSet));
        Set<? extends UnclassifiedObject> testingSet = new HashSet<>(objects.subList(sizeOfTrainingSet, objects.size()));

        return InputDataFactory.newData(data.classes(), trainingSet, testingSet);
    }

    /**
     * Размывание объектов набоора исходных данных по заданному алгоритму
     *
     * @param data         набор исходных данных
     * @param power        во сколько раз увеличится размер результирующего набора исходных данных
     * @param blurFunction алгоритм, по которому будет размыт каждый объект исходной обучающей выборки
     * @return новый набор исходных данных, отличающийся от исходного количеством объектов
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException в случае проблем формирования нового набора исходных данных
     * @throws java.lang.NullPointerException                   если набор исходных данных или алгоритм размытия не заданы
     * @throws java.lang.IllegalArgumentException               если степень размытия имеет неположительное значение
     */
    public static UnclassifiedData blur(UnclassifiedData data, int power, Function<UnclassifiedObject,
            UnclassifiedObject> blurFunction) throws DataException {
        Objects.requireNonNull(data);
        Objects.requireNonNull(blurFunction);

        if (power <= 0) {
            throw new IllegalArgumentException("Power should have a positive value");
        }

        Set<UnclassifiedObject> blurredTrainingSet = new HashSet<>();

        for (UnclassifiedObject obj : data.objects()) {
            blurredTrainingSet.addAll(blur(obj, power, blurFunction));
        }

        return DataFactory.newUnclassifiedData(data.classes(), blurredTrainingSet);
    }

    private static Set<UnclassifiedObject> blur(UnclassifiedObject obj, int power,
                                                    Function<UnclassifiedObject, UnclassifiedObject> blurFunction) {
        assert (obj != null);
        assert (power > 0);
        assert (blurFunction != null);

        Set<UnclassifiedObject> result = new HashSet<>();

        for (int i = 0; i < power; i++) {
            result.add(blurFunction.apply(obj));
        }

        return result;
    }
}
