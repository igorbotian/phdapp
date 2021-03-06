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

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
     * Фабрика объектов предметной области
     */
    protected static final DataFactory dataFactory;

    static {
        Injector injector = Guice.createInjector(new DataModule());
        dataFactory = injector.getInstance(DataFactory.class);
    }

    /**
     * Создание строки со случайным содержимым
     *
     * @return строка в формате <code>UUID</code>
     */
    protected static String randomString() {
        return UUID.randomUUID().toString();
    }

    // TODO некрасивое решение
    private static <P, T> Set<T> newSet(int count, Supplier<T> ctorWithoutArgs,
                                        Function<P, T> ctorWithArg, P ctorParam) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot have a negative value");
        }

        if (ctorParam == null) {
            if (ctorWithoutArgs == null) {
                throw new IllegalStateException("Constructor without arguments should be initialized");
            }
        } else {
            if (ctorWithArg == null) {
                throw new IllegalStateException("Constructor with an argument should be initialized");
            }
        }

        Set<T> objects = new HashSet<>();

        for (int i = 0; i < count; i++) {
            objects.add((ctorParam == null) ? ctorWithoutArgs.get() : ctorWithArg.apply(ctorParam));
        }

        return objects;
    }

    private static <T> Set<T> newSet(int count, Supplier<T> supplier) {
        return newSet(count, supplier, null, null);
    }

    private static <P, T> Set<T> newSet(int count, Function<P, T> func, P param) {
        return newSet(count, null, func, param);
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
        return dataFactory.newClass(randomString());
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
     * Создание объекта класса <code>Parameter</code> со случайным значением параметров
     *
     * @return объект класса
     */
    protected static Parameter<String> randomObjectParameter() {
        return randomStringObjectParameter(randomString());
    }

    /**
     * Получение объекта класса <code>Parameter</code> со случайным значением
     *
     * @param name название параметра (непустое)
     * @return объект класса
     * @throws java.lang.NullPointerException если название не задано
     */
    protected static Parameter<String> randomStringObjectParameter(String name) {
        Objects.requireNonNull(name);
        return dataFactory.newParameter(name, randomString(), BasicDataTypes.STRING);
    }

    /**
     * Создание множества объектов класса <code>Parameter</code> со случайным значениями параметров
     *
     * @param count количество объектов в множестве
     * @return множество объектов
     * @throws java.lang.IllegalArgumentException если количество имеет отрицательное значение
     */
    protected static Set<Parameter<?>> randomStringObjectParameters(int count) {
        return newSet(count, AbstractDataTest::randomObjectParameter);
    }

    /**
     * Создание множества объектов класса <code>Parameter</code> со случайным значениями
     *
     * @param names названия параметров, для которых будут созданы объекты со случайным значением
     * @return множество объектов; их количество равно количеству названий параметров
     * @throws java.lang.NullPointerException если названия параметров не заданы
     */
    protected static Set<Parameter<?>> randomStringObjectParameters(Set<String> names) {
        Objects.requireNonNull(names);
        return names.stream().map(AbstractDataTest::randomStringObjectParameter).collect(Collectors.toSet());
    }

    /**
     * Создание объекта класса <code>UnclassifiedObject</code> со случайными идентификатором и значениями параметров
     *
     * @param paramNames названия параметров
     * @return объект класса; имеет случайные идентификатор и значения параметров
     * @throws java.lang.NullPointerException если названия параметров не заданы
     */
    protected static UnclassifiedObject randomObject(Set<String> paramNames) {
        Objects.requireNonNull(paramNames);
        return dataFactory.newUnclassifiedObject(randomString(), randomStringObjectParameters(paramNames));
    }

    /**
     * Создание множества объектов класса <code>Parameter</code> со случайным значениями
     *
     * @param count      количество объектов в множестве
     * @param paramNames названия параметров, для которых будут созданы объекты со случайным значением
     * @return множество объектов; их количество равно количеству названий параметров
     * @throws java.lang.IllegalArgumentException если количество имеет отрицательное значение
     * @throws java.lang.NullPointerException     если названия параметров не заданы
     */
    protected static Set<UnclassifiedObject> randomObjects(int count, Set<String> paramNames) {
        Objects.requireNonNull(paramNames);
        return newSet(count, AbstractDataTest::randomObject, paramNames);
    }

    /**
     * Создание множества объектов класса <code>Parameter</code> со случайным значениями параметров
     *
     * @param count          количество объектов в множестве
     * @param numberOfParams количество параметров, которые будут иметь случайные названия и параметры
     * @return множество объектов
     * @throws java.lang.IllegalArgumentException если количество объектов или параметров имеет отрицательное значение
     */
    protected static Set<UnclassifiedObject> randomObjects(int count, int numberOfParams) {
        return randomObjects(
                count,
                randomStrings(numberOfParams)
        );
    }
}
