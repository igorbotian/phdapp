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

import org.apache.commons.lang3.RandomUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Базовый тест для всех тестов, связанных с тестированием как объектов предметной области,
 * так и утилит, работающих с ними.
 * Предоставляет вспомогательные методы, использующиеся в тестах
 */
public abstract class AbstractDataTest {

    /**
     * Создание строки со случайным содержимым
     *
     * @return строка в формате <code>UUID</code>
     */
    protected static String randomString() {
        return UUID.randomUUID().toString();
    }

    private static <T> Set<T> newSet(int count, Supplier<T> supplier) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot have a negative value");
        }

        Set<T> objects = new HashSet<>();

        for (int i = 0; i < count; i++) {
            objects.add(supplier.get());
        }

        return objects;
    }

    private static <P, T> Set<T> newSet(int count, Function<P, T> func, P param) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot have a negative value");
        }

        Set<T> objects = new HashSet<>();

        for (int i = 0; i < count; i++) {
            objects.add(func.apply(param));
        }

        return objects;
    }

    /**
     * Создание множества строк со случайным содержимым
     *
     * @param count количество строк в множестве
     * @return множество строк
     * @throws java.lang.IllegalArgumentException если количество имеет отрицательное значение
     */
    protected static Set<String> randomStrings(int count) {
        return newSet(count, BaseDataTest::randomString);
    }

    /**
     * Создание объекта класса <code>DataClass</code> со случайным значением параметров
     *
     * @return объект класса
     */
    protected static DataClass randomClass() {
        return DataFactory.newClass(randomString());
    }

    /**
     * Создание множества объектов класса <code>DataClass</code> со случайным значением их параметров
     *
     * @param count количество объектов в множестве
     * @return множество объектов
     * @throws java.lang.IllegalArgumentException если количество имеет отрицательное значение
     */
    protected static Set<DataClass> randomClasses(int count) {
        return newSet(count, AbstractDataTest::randomClass);
    }

    /**
     * Создание объекта класса <code>DataObjectParameter</code> со случайным значением параметров
     *
     * @return объект класса
     */
    protected static DataObjectParameter randomObjectParameter() {
        return randomObjectParameter(randomString());
    }

    /**
     * Получение объекта класса <code>DataObjectParameter</code> со случайным значением
     *
     * @param name название параметра (непустое)
     * @return объект класса
     * @throws java.lang.NullPointerException если название не задано
     */
    protected static DataObjectParameter randomObjectParameter(String name) {
        Objects.requireNonNull(name);
        return DataFactory.newObjectParameter(name, randomString());
    }

    /**
     * Создание множества объектов класса <code>DataObjectParameter</code> со случайным значениями параметров
     *
     * @param count количество объектов в множестве
     * @return множество объектов
     * @throws java.lang.IllegalArgumentException если количество имеет отрицательное значение
     */
    protected static Set<DataObjectParameter> randomObjectParameters(int count) {
        return newSet(count, AbstractDataTest::randomObjectParameter);
    }

    /**
     * Создание множества объектов класса <code>DataObjectParameter</code> со случайным значениями
     *
     * @param names названия параметров, для которых будут созданы объекты со случайным значением
     * @return множество объектов; их количество равно количеству названий параметров
     * @throws java.lang.NullPointerException если названия параметров не заданы
     */
    protected static Set<DataObjectParameter> randomObjectParameters(Set<String> names) {
        Objects.requireNonNull(names);
        return names.stream().map(AbstractDataTest::randomObjectParameter).collect(Collectors.toSet());
    }

    /**
     * Создание объекта класса <code>DataObject</code> со случайными идентификатором и значениями параметров
     *
     * @param paramNames названия параметров
     * @return объект класса; имеет случайные идентификатор и значения параметров
     * @throws java.lang.NullPointerException если названия параметров не заданы
     */
    protected static DataObject randomObject(Set<String> paramNames) {
        Objects.requireNonNull(paramNames);
        return DataFactory.newObject(randomString(), randomObjectParameters(paramNames));
    }

    /**
     * Создание множества объектов класса <code>DataObjectParameter</code> со случайным значениями
     *
     * @param count      количество объектов в множестве
     * @param paramNames названия параметров, для которых будут созданы объекты со случайным значением
     * @return множество объектов; их количество равно количеству названий параметров
     * @throws java.lang.IllegalArgumentException если количество имеет отрицательное значение
     * @throws java.lang.NullPointerException     если названия параметров не заданы
     */
    protected static Set<DataObject> randomObjects(int count, Set<String> paramNames) {
        Objects.requireNonNull(paramNames);
        return newSet(count, AbstractDataTest::randomObject, paramNames);
    }

    /**
     * Создание множества объектов класса <code>DataObjectParameter</code> со случайным значениями параметров
     *
     * @param count          количество объектов в множестве
     * @param numberOfParams количество параметров, которые будут иметь случайные названия и параметры
     * @return множество объектов
     * @throws java.lang.IllegalArgumentException если количество объектов или параметров имеет отрицательное значение
     */
    protected static Set<DataObject> randomObjects(int count, int numberOfParams) {
        return randomObjects(
                count,
                randomStrings(numberOfParams)
        );
    }

    /**
     * Создание объекта класса <code>TrainingDataObject</code> со случайными значениями параметров
     *
     * @param className  реальный класс объекта
     * @param paramNames названия параметров
     * @return объект класса
     * @throws java.lang.NullPointerException если название класса или множество параметров не задано
     */
    protected static TrainingDataObject randomTrainingObject(String className, Set<String> paramNames) {
        Objects.requireNonNull(className);
        Objects.requireNonNull(paramNames);

        return DataFactory.newTrainingObject(
                randomString(),
                randomObjectParameters(paramNames),
                DataFactory.newClass(className)
        );
    }

    /**
     * Создание множества объектов класса <code>TrainingDataObject</code> соу случайными значениями параметров
     *
     * @param count              количество объектов в множестве
     * @param paramNames         названия параметров
     * @param possibleClassNames названия реальных классов, которые могут иметь созданные объекты
     * @return множество объектов
     * @throws java.lang.IllegalArgumentException если количество объектов имеет отрицательное значение
     * @throws java.lang.NullPointerException     если названия параметров или названия реальных классов не задано
     */
    protected static Set<TrainingDataObject> randomTrainingObjects(int count, Set<String> paramNames,
                                                                   Set<String> possibleClassNames) {
        Objects.requireNonNull(paramNames);
        Objects.requireNonNull(possibleClassNames);

        Set<TrainingDataObject> objects = new HashSet<>();
        List<String> listOfPossibleClasses = possibleClassNames.stream().collect(Collectors.toList());

        for (int i = 0; i < count; i++) {
            String className = listOfPossibleClasses.get(RandomUtils.nextInt(0, possibleClassNames.size()));
            objects.add(randomTrainingObject(className, paramNames));
        }

        return objects;
    }
}
