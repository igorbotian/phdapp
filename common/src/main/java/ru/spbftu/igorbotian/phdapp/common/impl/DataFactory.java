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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Фабрика объектов предметной области
 */
public final class DataFactory {

    private DataFactory() {
        //
    }

    /**
     * Создание объекта типа <code>DataClass</code>
     *
     * @param name идентификатор класса (не может быть пустым)
     * @throws java.lang.NullPointerException     если идентификатор класса не задан
     * @throws java.lang.IllegalArgumentException если идентификатор класса пустой
     * @see ru.spbftu.igorbotian.phdapp.common.DataClass
     */
    public static DataClass newClass(String name) {
        return new DataClassImpl(name);
    }

    /**
     * Создание множества объектов классов классификации на основе их имён
     *
     * @param names названия классов (каждое не может быть пустым)
     * @return множество классов
     */
    public static Set<DataClass> newClasses(String... names) {
        return Stream.of(names).map(DataFactory::newClass).collect(Collectors.toSet());
    }

    /**
     * Создание множества объектов классов классификации на основе их имён
     *
     * @param names названия классов (каждое не может быть пустым)
     * @return множество классов
     */
    public static Set<DataClass> newClasses(Set<String> names) {
        return names.stream().map(DataFactory::newClass).collect(Collectors.toSet());
    }

    /**
     * Создание объекта типа <code>Parameter</code>
     *
     * @param name  название объекта (непустое)
     * @param value значение объекта
     * @return объект типа <code>Parameter</code> с заданными параметрами
     * @throws java.lang.IllegalArgumentException если название объекта пустое
     * @throws java.lang.NullPointerException     если хотя бы один из параметров не задан
     * @see ru.spbftu.igorbotian.phdapp.common.Parameter
     */
    public static <V> Parameter<V> newParameter(String name, V value, DataType<V> valueType) {
        return new ParameterImpl<>(name, value, valueType);
    }

    /**
     * Создание объекта типа <code>UnclassifiedObject</code>
     *
     * @param id     строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params множество параметров, которыми характеризуется объект (непустое)
     * @return объект типа <code>UnclassifiedObject</code> с заданными параметрами
     * @throws java.lang.NullPointerException     если множества параметров равно <code>null</code>
     * @throws java.lang.IllegalArgumentException если идентификатор объекта пустой или множество параметров
     *                                            не содержит ни одного элемента
     * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject
     */
    public static UnclassifiedObject newUnclassifiedObject(String id, Set<Parameter<?>> params) {
        return new UnclassifiedObjectImpl(id, params);
    }

    /**
     * Создание объекта типа <code>ClassifiedObject</code>
     *
     * @param id        строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params    множество параметров, которыми характеризуется объект (непустое)
     * @param realClass реальный класса классификации, которому соответствует объект
     * @return объект типа <code>ClassifiedObject</code> с заданными параметрами
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedObject
     */
    public static ClassifiedObject newClassifiedObject(String id, Set<Parameter<?>> params, DataClass realClass) {
        return new ClassifiedObjectImpl(id, params, realClass);
    }

    /**
     * Создание объекта типа <code>UnclassifiedData</code>
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество объектов
     * @return объект типа <code>UnclassifiedData</code> с заданными параметрами
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если набор классов содержит меньше,
     *                                                          чем минимально необходимое, количество элементов;
     *                                                          если множество объектов является пустым;
     *                                                          если множество объектов имеет хотя бы один объект,
     *                                                          отличающийся от других множеством определяюмых его параметров
     * @throws java.lang.NullPointerException                   если множество классов или множество объектов равно <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedData
     */
    public static UnclassifiedData newUnclassifiedData(Set<? extends DataClass> classes,
                                                       Set<? extends UnclassifiedObject> objects) throws DataException {
        return new UnclassifiedDataImpl(classes, objects);
    }

    /**
     * Создание объекта типа <code>ClassifiedData</code>
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество классифицированных объектов
     * @return объект типа <code>ClassifiedData</code> с заданными параметрами
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если набор классов содержит меньше,
     *                                                          чем минимально необходимое, количество элементов;
     *                                                          если множество объектов является пустым;
     *                                                          если множество объектов имеет хотя бы один объект,
     *                                                          отличающийся от других множеством определяюмых его параметров
     * @throws java.lang.NullPointerException                   если множество классов или множество объектов равно <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.ClassifiedData
     */
    public static ClassifiedData newClassifiedData(Set<? extends DataClass> classes,
                                                   Set<? extends ClassifiedObject> objects) throws DataException {
        return new ClassifiedDataImpl(classes, objects);
    }
}
