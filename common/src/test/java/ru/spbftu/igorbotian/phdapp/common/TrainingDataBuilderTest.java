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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>TrainingDataBuilder</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingDataBuilder
 */
public class TrainingDataBuilderTest {

    /**
     * Тестовые данные, которые содержат обучающую выборку
     */
    private final TrainingData data;

    /**
     * Тестовые данные, которые не содержат обучающую выборку
     */
    private final TrainingData dataWitoutTrainingSet;

    /**
     * Объект тестируемого класса
     */
    private TrainingDataBuilder dataBuilder;

    public TrainingDataBuilderTest() {
        Set<DataClass> classes = Stream.of(
                new DataClass("first"),
                new DataClass("second")
        ).collect(Collectors.toSet());

        Set<DataObjectParameter> params = Collections.singleton(new DataObjectParameter("param", "value"));

        Set<DataObject> testingSet = Stream.of(
                new DataObject("fisrt", params),
                new DataObject("second", params)
        ).collect(Collectors.toSet());

        Set<TrainingDataObject> trainingSet = Stream.of(
                new TrainingDataObject("third", params, classes.iterator().next()),
                new TrainingDataObject("fourth", params, classes.iterator().next())
        ).collect(Collectors.toSet());

        data = new TrainingData(classes, testingSet, trainingSet);
        dataWitoutTrainingSet = new TrainingData(classes, testingSet, Collections.emptySet());
    }

    @Before
    public void setUp() {
        dataBuilder = new TrainingDataBuilder();
    }

    @Test
    public void testReadyWithTrainingSetPresented() {
        data.classes().forEach(dataBuilder::defineClass);
        data.testingSet().forEach(dataBuilder::addObject);
        data.trainingSet().forEach(dataBuilder::addTrainingObject);
        Assert.assertTrue(dataBuilder.isReady());
    }

    @Test
    public void testReadyWithNoTrainingSetPresence() {
        dataWitoutTrainingSet.classes().forEach(dataBuilder::defineClass);
        dataWitoutTrainingSet.testingSet().forEach(dataBuilder::addObject);
        Assert.assertTrue(dataBuilder.isReady());
    }

    @Test
    public void testNoClassesDefined() {
        data.testingSet().forEach(dataBuilder::addObject);
        Assert.assertFalse(dataBuilder.isReady());
    }

    @Test
    public void testNoObjectsDefined() {
        data.classes().forEach(dataBuilder::defineClass);
        Assert.assertFalse(dataBuilder.isReady());
    }

    @Test(expected = IllegalStateException.class)
    public void testNonReadyBuild() {
        dataBuilder.build();
    }

    @Test
    public void testBuildWithNoTrainingSetPresence() {
        dataWitoutTrainingSet.classes().forEach(dataBuilder::defineClass);
        dataWitoutTrainingSet.testingSet().forEach(dataBuilder::addObject);
        Assert.assertEquals(dataWitoutTrainingSet, dataBuilder.build());
    }

    @Test
    public void testBuildWithTrainingSetPresented() {
        data.classes().forEach(dataBuilder::defineClass);
        data.testingSet().forEach(dataBuilder::addObject);
        data.trainingSet().forEach(dataBuilder::addTrainingObject);
        Assert.assertEquals(data, dataBuilder.build());
    }
}
