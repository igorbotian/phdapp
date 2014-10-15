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
 * Фабрика объектов предметной области со случайным значением
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
public final class RandomDataFactory {

    private RandomDataFactory() {
        //
    }

    private static String newString() {
        return UUID.randomUUID().toString();
    }

    private static <T> Set<T> newSet(int count, Supplier<T> ctor) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot have a negative value");
        }

        Set<T> objects = new HashSet<>();

        for (int i = 0; i < count; i++) {
            objects.add(ctor.get());
        }

        return objects;
    }

    private static <P, T> Set<T> newSet(int count, Function<P, T> ctor, P param) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot have a negative value");
        }

        Set<T> objects = new HashSet<>();

        for (int i = 0; i < count; i++) {
            objects.add(ctor.apply(param));
        }

        return objects;
    }

    public static Set<String> newStrings(int count) {
        return newSet(count, RandomDataFactory::newString);
    }

    public static DataClass newClass() {
        return DataFactory.newClass(newString());
    }

    public static Set<DataClass> newClasses(int count) {
        return newSet(count, RandomDataFactory::newClass);
    }

    public static DataObjectParameter newObjectParameter(String name) {
        return DataFactory.newObjectParameter(name, newString());
    }

    public static DataObjectParameter newObjectParameter() {
        return newObjectParameter(newString());
    }

    public static Set<DataObjectParameter> newObjectParameters(int count) {
        return newSet(count, RandomDataFactory::newObjectParameter);
    }

    public static Set<DataObjectParameter> newObjectParameters(int count, String name) {
        return newSet(count, RandomDataFactory::newObjectParameter, name);
    }

    public static Set<DataObjectParameter> newObjectParameters(Set<String> names) {
        return names.stream().map(RandomDataFactory::newObjectParameter).collect(Collectors.toSet());
    }

    public static DataObject newObject() {
        return newObject(Collections.singleton(newString()));
    }

    public static DataObject newObject(Set<String> paramNames) {
        return DataFactory.newObject(newString(), newObjectParameters(paramNames));
    }

    public static Set<DataObject> newObjects(int count, Set<String> paramNames) {
        return newSet(count, RandomDataFactory::newObject, paramNames);
    }

    public static Set<DataObject> newObjects(int count, int numberOfParams) {
        return newObjects(
                count,
                newStrings(numberOfParams)
        );
    }

    public static TrainingDataObject newTrainingObject(Set<String> paramNames) {
        return DataFactory.newTrainingObject(
                newString(),
                newObjectParameters(paramNames),
                RandomDataFactory.newClass()
        );
    }

    public static TrainingDataObject newTrainingObject(String className, Set<String> paramNames) {
        return DataFactory.newTrainingObject(
                newString(),
                newObjectParameters(paramNames),
                DataFactory.newClass(className)
        );
    }

    public static Set<TrainingDataObject> newTrainingObjects(int count, Set<String> paramNames,
                                                             Set<String> possibleClassNames) {
        Set<TrainingDataObject> objects = new HashSet<>();

        List<String> listOfPossibleClasses = possibleClassNames.stream().collect(Collectors.toList());

        for (int i = 0; i < count; i++) {
            String className = listOfPossibleClasses.get(RandomUtils.nextInt(0, possibleClassNames.size()));
            objects.add(RandomDataFactory.newTrainingObject(className, paramNames));
        }

        return objects;
    }

    public static Data newData(int numberOfObjects, int numberOfClasses, int numberOfParams) {
        return DataFactory.newData(
                newClasses(numberOfClasses),
                newObjects(numberOfObjects, numberOfParams)
        );
    }

    public static Data newData(int numberOfObjects, Set<String> classNames, Set<String> paramNames) {
        return DataFactory.newData(
                DataFactory.newClasses(classNames),
                newObjects(numberOfObjects, paramNames)
        );
    }

    public static TrainingData newTrainingData(Set<String> classNames, Set<String> paramNames, int testingSetSize, int trainingSetSize) {
        return DataFactory.newTrainingData(
                DataFactory.newClasses(classNames),
                newObjects(testingSetSize, paramNames),
                newTrainingObjects(trainingSetSize, paramNames, classNames)
        );
    }

    public static TrainingData newTrainingData(Set<String> classNames, Set<DataObject> testingSet, Set<TrainingDataObject> trainingSet) {
        return DataFactory.newTrainingData(
                DataFactory.newClasses(classNames),
                testingSet,
                trainingSet
        );
    }

    public static TrainingData newTrainingData(int numberOfClasses, int numberOfParams, int testingSetSize,
                                               int trainingSetSize) {
        return newTrainingData(newStrings(numberOfClasses), newStrings(numberOfParams), testingSetSize, trainingSetSize);
    }
}
