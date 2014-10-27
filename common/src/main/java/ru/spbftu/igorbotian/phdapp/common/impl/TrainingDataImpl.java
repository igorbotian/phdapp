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

import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.TrainingData;
import ru.spbftu.igorbotian.phdapp.common.TrainingObject;

import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
 * @see ru.spbftu.igorbotian.phdapp.common.impl.DataFactory
 */
class TrainingDataImpl extends UnclassifiedDataImpl implements TrainingData {

    TrainingDataImpl(Set<? extends DataClass> classes, Set<? extends TrainingObject> objects) throws DataException {
        super(classes, objects);
        checkNoForeignClasses(classes, objects);
    }

    private void checkNoForeignClasses(Set<? extends DataClass> classes,
                                       Set<? extends TrainingObject> trainingSet) {
        for (TrainingObject obj : trainingSet) {
            if (!classes.contains(obj.realClass())) {
                throw new IllegalArgumentException("A training object has undefined class: " + obj.realClass().name());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked") // check is made by means of the constructor
    public Set<? extends TrainingObject> objects() {
        return (Set<? extends TrainingObject>) super.objects();
    }
}