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
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.Data
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class DataImpl implements Data {

    /**
     * Классы классификации
     */
    private final Set<? extends DataClass> classes;

    /**
     * Множество объектов, предназначенных для классификации или выполнения какого-либо другого действия над ними
     */
    private final Set<? extends DataObject> objects;

    public DataImpl(Set<? extends DataClass> classes, Set<? extends DataObject> objects)
            throws DataException {

        Objects.requireNonNull(classes);
        Objects.requireNonNull(objects);

        if (classes.size() < 2) {
            throw new DataException("Number of classes cannot be less than 2");
        }

        if (objects.isEmpty()) {
            throw new DataException("At least one object should be presented");
        }

        this.classes = Collections.unmodifiableSet(classes);
        this.objects = Collections.unmodifiableSet(objects);

        if (objectsHaveDifferentParams(objects)) {
            throw new DataException("Objects should not have different set of parameters");
        }
    }

    /**
     * Проверка на то, отличается ли хотя бы один объект множеством определяемых его параметров от других или нет
     *
     * @param objects множество объектов
     * @return <code>true</code>, если есть хотя бы один объект, отличающийся от других множеством своих параметров;
     * <code>false</code>, если все объекты характеризуются одинаковым набором параметров
     */
    protected boolean objectsHaveDifferentParams(Set<? extends DataObject> objects) {
        Objects.requireNonNull(objects);

        if (objects.isEmpty()) {
            return false;
        }

        Iterator<? extends DataObject> it = objects.iterator();
        Set<String> primerParamNames =
                it.next().parameters().stream().map(DataObjectParameter::name).collect(Collectors.toSet());

        while (it.hasNext()) {
            Set<String> paramNames =
                    it.next().parameters().stream().map(DataObjectParameter::name).collect(Collectors.toSet());

            if (primerParamNames.size() != paramNames.size() || !primerParamNames.containsAll(paramNames)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<? extends DataClass> classes() {
        return classes;
    }

    @Override
    public Set<? extends DataObject> objects() {
        return objects;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objects, classes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Data)) {
            return false;
        }

        DataImpl other = (DataImpl) obj;
        return classes.size() == other.classes.size()
                && classes.containsAll(other.classes)
                && objects.size() == other.objects.size()
                && objects.containsAll(other.objects);
    }

    @Override
    public String toString() {
        return String.join(";", classes.toString(), objects.toString());
    }
}
