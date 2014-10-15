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

package ru.spbftu.igorbotian.phdapp.common;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class TrainingDataImpl extends DataImpl implements TrainingData {

    /**
     * Обучающая выборка (множество объектов; информация о реальном классе классификации известа для каждого из них)
     */
    private final Set<? extends TrainingDataObject> trainingSet;

    public TrainingDataImpl(Set<? extends DataClass> classes, Set<? extends DataObject> testingSet,
                            Set<? extends TrainingDataObject> trainingSet) throws DataException {
        super(classes, testingSet);

        Objects.requireNonNull(trainingSet);

        this.trainingSet = Collections.unmodifiableSet(trainingSet);

        // проверка на наличие объекта в обучающей выборке, который соответствует классу, отличному от тех,
        // которые будут использованы при классификации
        for (TrainingDataObject obj : trainingSet) {
            if (!classes().contains(obj.realClass())) {
                throw new DataException("Object has a real class not taken into account on testing: " + obj);
            }
        }

        if (objectsHaveDifferentParams(trainingSet)) {
            throw new DataException("Elements of training set should not have different set of parameters");
        }
    }

    @Override
    public Set<? extends DataObject> testingSet() {
        return objects();
    }

    @Override
    public Set<? extends TrainingDataObject> trainingSet() {
        return trainingSet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), trainingSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof TrainingData)) {
            return false;
        }

        TrainingDataImpl other = (TrainingDataImpl) obj;
        return super.equals(obj)
                && trainingSet.size() == other.trainingSet.size()
                && trainingSet.containsAll(other.trainingSet);
    }

    @Override
    public String toString() {
        return String.join(";", super.toString(), trainingSet.toString());
    }
}
