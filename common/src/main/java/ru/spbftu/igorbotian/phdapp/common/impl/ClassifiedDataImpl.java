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

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.ClassifiedObject;
import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.DataException;

import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedData
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class ClassifiedDataImpl extends UnclassifiedDataImpl implements ClassifiedData {

    ClassifiedDataImpl(Set<? extends DataClass> classes, Set<? extends ClassifiedObject> objects) throws DataException {
        super(classes, objects);
        checkNoForeignClasses(classes, objects);
    }

    private void checkNoForeignClasses(Set<? extends DataClass> classes,
                                       Set<? extends ClassifiedObject> trainingSet) {
        for (ClassifiedObject obj : trainingSet) {
            if (!classes.contains(obj.dataClass())) {
                throw new IllegalArgumentException("A classified object has undefined class: " + obj.dataClass().name());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<? extends ClassifiedObject> objects() {
        return (Set<? extends ClassifiedObject>) super.objects();
    }
}
