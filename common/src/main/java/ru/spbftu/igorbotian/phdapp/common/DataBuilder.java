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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация шаблона проектирования <i>Builder</i>, направленная на поэтапное формирование набора исходных данных
 * для классификации или выполнения какого-либо другого действия над ними.
 *
 * @see DataClass, DataObject
 */
public class DataBuilder {

    /**
     * Классы классификации
     */
    private Set<DataClass> classes;

    /**
     * Множество объектов для проведения классификации над ними или выполнения какого-либо другого действия
     */
    private Set<DataObject> objects;

    /**
     * Задание нового класса конфигурации
     *
     * @param clazz класс конфигурации
     * @throws java.lang.NullPointerException если класс не задан
     */
    public void defineClass(DataClass clazz) {
        Objects.requireNonNull(clazz);

        if(classes == null) {
            classes = new LinkedHashSet<>();
        }

        classes.add(clazz);
    }

    /**
     * Добавление нового элемента в множество исходных объектов
     *
     * @param data новый элемент множества исходных объектов
     * @throws java.lang.NullPointerException если объект не задан
     */
    public void addObject(DataObject data) {
        Objects.requireNonNull(data);

        if(objects == null) {
            objects = new LinkedHashSet<>();
        }

        objects.add(data);
    }

    /**
     * Указывает, возможно ли в данный момент формирование набора исходных данных или нет
     *
     * @return <code>true</code>, если формирование возможно; <code>false</code>, если нет
     */
    public boolean isReady() {
        return (classes != null && objects != null);
    }

    /**
     * Формирование набора исходных данных
     *
     * @return набор исходных данных
     * @throws DataException если формирование набора в данный момент не возможно или формируемый набор является
     *                       некорректным
     */
    public Data build() throws DataException {
        if (!isReady()) {
            throw new IllegalStateException("Can't build a training data. " +
                    "Classes and testing set should be initialized first");
        }

        return new Data(classes, objects);
    }
}
