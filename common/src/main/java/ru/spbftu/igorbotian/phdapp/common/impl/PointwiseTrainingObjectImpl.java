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
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingObject;

import java.util.Objects;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingObject
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
public class PointwiseTrainingObjectImpl extends UnclassifiedObjectImpl implements PointwiseTrainingObject {

    /**
     * Реальный класс классификации, которому соответствует объект
     */
    private final DataClass realClass;

    public PointwiseTrainingObjectImpl(String id, Set<Parameter<?>> params, DataClass realClass) {

        super(id, params);

        Objects.requireNonNull(realClass);
        this.realClass = realClass;
    }

    @Override
    public DataClass realClass() {
        return realClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), realClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof PointwiseTrainingObjectImpl)) {
            return false;
        }

        PointwiseTrainingObjectImpl other = (PointwiseTrainingObjectImpl) obj;
        return super.equals(other) && realClass.equals(other.realClass);
    }

    @Override
    public String toString() {
        return String.join(":", super.toString(), realClass.toString());
    }
}
