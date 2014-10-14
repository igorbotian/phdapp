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
import java.util.Set;

/**
 * Набор для для классификации или выполнения какого-либо другого действия.
 * Состоит из набора классов классификации и множества объектов, которые необходимо классифицировать.
 * Класс является потокобезопасным, а его объекты - неизменяемыми.
 *
 * @see DataClass, DataObject, TrainingData, TrainingDataBuilder
 */
public class Data {

    /**
     * Классы классификации
     */
    private final Set<? extends DataClass> classes;

    /**
     * Множество объектов, предназначенных для классификации или выполнения какого-либо другого действия над ними
     */
    private final Set<? extends DataObject> objects;

    /**
     * Конструктор класса
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество объектов
     * @throws DataException                                    если набор классов содержит меньше, чем минимально необходимое количество элементов;
     *                                                          если множество объектов является пустым
     * @throws java.lang.NullPointerException                   если множество классов или множество объектов равно <code>null</code>
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если множество объектов имеет хотя бы один объект,
     *                                                          отличающихся от других множеством определяюмых его параметров
     */
    public Data(Set<? extends DataClass> classes, Set<? extends DataObject> objects)
            throws DataException {

        if (classes == null) {
            throw new NullPointerException("Classes cannot be null");
        }

        if (classes.size() < 2) {
            throw new DataException("Number of classes cannot be less than 2");
        }

        if (objects == null) {
            throw new NullPointerException("Objects cannot be null");
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
        Iterator<? extends DataObject> it = objects.iterator();
        Set<DataObjectParameter> primerParams = it.next().parameters();

        while (it.hasNext()) {
            Set<DataObjectParameter> params = it.next().parameters();

            if (primerParams.size() != params.size() || !primerParams.containsAll(params)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Получение набора классов классификации
     *
     * @return непустое неизменяемое множество классов классификации
     */
    public Set<? extends DataClass> classes() {
        return classes;
    }

    /**
     * Получение множества объектов, предназначенных для классификации
     *
     * @return непустое неизменяемое множество объектов
     */
    public Set<? extends DataObject> objects() {
        return objects;
    }

    @Override
    public int hashCode() {
        int hashCode = 13;

        hashCode += classes.hashCode() + 29;
        hashCode += objects.hashCode() + 37;

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Data)) {
            return false;
        }

        Data other = (Data) obj;
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
