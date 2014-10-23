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

import java.util.*;

/**
 * Класс, который предоставляет проводить различные операции над наборами исходных данных, предназначенных
 * для обучения классификатора, в целях проверки корректности его работы
 *
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
 */
public final class TrainingDataUtils {

    /**
     * Формирует новый набор исходных данных, предназначенный для обучения классификатора, на основе заданного.
     * При этом тестирующая и обучающая выборка формируется случайным образом из тестирующей выборки заданного набора.
     *
     * @param data             исходный набор исходных данных для обучения классификатора
     * @param trainingSetRatio процентное соотношение из исходного количества объектов,
     *                         которое будет составлять формируемая обучающая выборка (0.0;1.0)
     * @return новый набор исходных данных
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если
     *                                                          в случае проблемы формирования нового набора исходных данных
     * @throws java.lang.NullPointerException                   если набор исходных данных не задан
     * @throws java.lang.IllegalArgumentException               если процентное соотношение объектов обучающей выборки
     *                                                          не в диапазоне (0.0;1.0)
     */
    public static TrainingData shuffle(TrainingData data, float trainingSetRatio) throws DataException {
        if (trainingSetRatio <= 0.0f || trainingSetRatio >= 1.0f) {
            throw new IllegalArgumentException("Training set ratio should be in range of (0.0.;1.0.): "
                    + trainingSetRatio);
        }

        List<? extends TrainingDataObject> objects = new ArrayList<>(Objects.requireNonNull(data).trainingSet());
        Collections.shuffle(objects);

        int sizeOfTrainingSet = (int) Math.ceil(trainingSetRatio * objects.size());
        Set<? extends TrainingDataObject> trainingSet = new HashSet<>(objects.subList(0, sizeOfTrainingSet));
        Set<? extends DataObject> testingSet = new HashSet<>(objects.subList(sizeOfTrainingSet, objects.size()));

        return DataFactory.newTrainingData(data.classes(), testingSet, trainingSet);
    }
}
