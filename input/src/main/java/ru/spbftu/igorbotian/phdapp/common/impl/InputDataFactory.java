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

package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.Set;

/**
 * Фабрика объектов наборов исходных данных
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PointwiseInputData
 * @see ru.spbftu.igorbotian.phdapp.common.PairwiseInputData
 */
public final class InputDataFactory {

    private InputDataFactory() {
        //
    }

    /**
     * Создание объекта типа <code>PointwiseInputData</code>
     *
     * @param classes     набор классов, которые будут участвовать в классификации объектов (не меньше двух)
     * @param trainingSet обучающая выборка (классифицированные объекты)
     * @param objects     множество объектов, подлежащих классификации
     * @return объект типа <code>PointwiseInputData</code>
     * @throws DataException если набор классов содержит меньше, чем минимально необходимое, количество элементов;
     *                       если множество объектов является пустым;
     *                       если множество объектов имеет хотя бы один объект,
     *                       отличающийся от других множеством определяюмых его параметров
     * @see ru.spbftu.igorbotian.phdapp.common.PointwiseInputData
     */
    public static PointwiseInputData newPointwiseData(Set<? extends DataClass> classes,
                                                      Set<? extends PointwiseTrainingObject> trainingSet,
                                                      Set<? extends UnclassifiedObject> objects) throws DataException {
        return new PointwiseInputDataImpl(classes, trainingSet, objects);
    }

    /**
     * Создание объекта типа <code>PairwiseInputData</code>
     *
     * @param classes     набор классов, которые будут участвовать в классификации объектов (не меньше двух)
     * @param trainingSet обучающая выборка (множество пар предпочтений)
     * @param objects     множество объектов, подлежащих классификации
     * @return объект типа <code>PairwiseInputData</code>
     * @throws DataException если набор классов содержит меньше, чем минимально необходимое, количество элементов
     * @see ru.spbftu.igorbotian.phdapp.common.PairwiseInputData
     */
    public static PairwiseInputData newPairwiseData(Set<? extends DataClass> classes,
                                                      Set<? extends PairwiseTrainingObject> trainingSet,
                                                      Set<? extends UnclassifiedObject> objects) throws DataException {
        return new PairwiseInputDataImpl(classes, trainingSet, objects);
    }
}