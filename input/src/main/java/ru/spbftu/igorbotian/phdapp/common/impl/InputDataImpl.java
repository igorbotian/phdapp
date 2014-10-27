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

import java.util.Objects;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.InputData
 * @see ru.spbftu.igorbotian.phdapp.common.impl.InputDataFactory
 */
class InputDataImpl implements InputData {

    private final UnclassifiedData testingSet;
    private final TrainingData trainingSet;

    public InputDataImpl(Set<? extends DataClass> classes, Set<? extends TrainingObject> trainingSet,
                         Set<? extends UnclassifiedObject> testingSet) throws DataException {
        this.trainingSet = DataFactory.newTrainingData(classes, Objects.requireNonNull(trainingSet));
        this.testingSet = DataFactory.newUnclassifiedData(classes, Objects.requireNonNull(testingSet));
    }

    @Override
    public Set<? extends UnclassifiedObject> testingSet() {
        return testingSet.objects();
    }

    @Override
    public Set<? extends DataClass> classes() {
        return testingSet.classes();
    }

    public Set<? extends TrainingObject> trainingSet() {
        return trainingSet.objects();
    }

    @Override
    public int hashCode() {
        return Objects.hash(testingSet, trainingSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof InputDataImpl)) {
            return false;
        }

        InputDataImpl other = (InputDataImpl) obj;
        return testingSet.equals(other.testingSet)
                && trainingSet.equals(other.trainingSet);
    }

    @Override
    public String toString() {
        return String.join(";", testingSet.toString(), trainingSet.toString());
    }
}
