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

import java.util.Set;
import java.util.stream.Collectors;

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
    public static Set<DataClass> newClasses(Set<String> names) {
        return names.stream().map(DataFactory::newClass).collect(Collectors.toSet());
    }

    /**
     * Создание объекта типа <code>DataObjectParameter</code>
     *
     * @param name  название объекта (непустое)
     * @param value значение объекта
     * @throws java.lang.IllegalArgumentException если название объекта пустое
     * @throws java.lang.NullPointerException     если название или значение объекта не задано
     * @see ru.spbftu.igorbotian.phdapp.common.DataObjectParameter
     */
    public static DataObjectParameter newObjectParameter(String name, String value) {
        return new DataObjectParameterImpl(name, value);
    }

    /**
     * Создание объекта типа <code>DataObject</code>
     *
     * @param id     строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params множество параметров, которыми характеризуется объект (непустое)
     * @throws java.lang.NullPointerException     если множества параметров равно <code>null</code>
     * @throws java.lang.IllegalArgumentException если идентификатор объекта пустой или множество параметров
     *                                            не содержит ни одного элемента
     * @see ru.spbftu.igorbotian.phdapp.common.DataObject
     */
    public static DataObject newObject(String id, Set<DataObjectParameter> params) {
        return new DataObjectImpl(id, params);
    }

    /**
     * Создание объекта типа <code>TrainingDataObject</code>
     *
     * @param id        строковое представление идентификатора объекта (не может быть <code>null</code> или пустым)
     * @param params    множество параметров, которыми характеризуется объект (непустое)
     * @param realClass реальный класса классификации, которому соответствует объект
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     * @see ru.spbftu.igorbotian.phdapp.common.TrainingDataObject
     */
    public static TrainingDataObject newTrainingObject(String id, Set<DataObjectParameter> params, DataClass realClass) {
        return new TrainingDataObjectImpl(id, params, realClass);
    }

    /**
     * Создание объекта типа <code>Data</code>
     *
     * @param classes непустой набор классов классификации размером не меньше двух
     * @param objects непустое множество объектов
     * @throws DataException                  если набор классов содержит меньше, чем минимально необходимое количество элементов;
     *                                        если множество объектов является пустым;
     *                                        если множество объектов имеет хотя бы один объект,
     *                                        отличающихся от других множеством определяюмых его параметров
     * @throws java.lang.NullPointerException если множество классов или множество объектов равно <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.Data
     */
    public static Data newData(Set<? extends DataClass> classes, Set<? extends DataObject> objects) throws DataException {
        return new DataImpl(classes, objects);
    }

    /**
     * Создание объекта типа <code>TrainingData</code>
     *
     * @param classes     непустой набор классов классификации размером не меньше двух
     * @param testingSet  тестирующая выборка (непустое множество объектов)
     * @param trainingSet обучающая выборка (непустое множество объектов, для которых известны реальные классы классификации)
     * @throws java.lang.NullPointerException если обучающая выборка равна <code>null</code>
     * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
     */
    public static TrainingData newTrainingData(Set<? extends DataClass> classes, Set<? extends DataObject> testingSet,
                                               Set<? extends TrainingDataObject> trainingSet) throws DataException {
        return new TrainingDataImpl(classes, testingSet, trainingSet);
    }
}
