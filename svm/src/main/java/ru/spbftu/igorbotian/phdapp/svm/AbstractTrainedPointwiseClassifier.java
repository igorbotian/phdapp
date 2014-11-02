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

package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingObject;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Базовая реализация обучаемого классификатора
 *
 * @see TrainedPointwiseClassifier
 */
public abstract class AbstractTrainedPointwiseClassifier implements TrainedPointwiseClassifier {

    private final Set<PointwiseTrainingObject> trainingSet = new HashSet<>();

    @Override
    public void train(PointwiseTrainingObject obj) {
        trainingSet.add(Objects.requireNonNull(obj));
    }

    @Override
    public void train(Set<? extends PointwiseTrainingObject> objects) {
        trainingSet.addAll(Objects.requireNonNull(objects));
    }

    @Override
    public ClassifiedData classify(Set<? extends UnclassifiedObject> data, Set<? extends Parameter> params)
            throws ClassificationException {

        return classify(Collections.unmodifiableSet(trainingSet), Objects.requireNonNull(data),
                Objects.requireNonNull(params));
    }

    /**
     * Классификация заданного набора исходных данных
     *
     * @param trainingSet обучающая выборка
     * @param data        набор исходных объектов, подлежащих классификации
     * @param params      множество параметров классификации
     * @return классифицированных набор исходных объектов
     * @throws ru.spbftu.igorbotian.phdapp.svm.ClassificationException в случае ошбики, возникшей в процессе классификации
     * @throws java.lang.NullPointerException                          если не задан хотя бы один из параметров
     */
    protected abstract ClassifiedData classify(Set<? extends PointwiseTrainingObject> trainingSet,
                                               Set<? extends UnclassifiedObject> data,
                                               Set<? extends Parameter> params)
            throws ClassificationException;
}
