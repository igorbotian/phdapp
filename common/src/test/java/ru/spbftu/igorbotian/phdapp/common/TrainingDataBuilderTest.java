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

/**
 * Модульные тесты для класса <code>TrainingDataBuilder</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.AbstractDataTest
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingDataBuilder
 */
public class TrainingDataBuilderTest extends AbstractDataTest {

    /**
     * Тестовые данные, которые содержат обучающую выборку
     */
    private TrainingData data;

    /**
     * Тестовые данные, которые не содержат обучающую выборку
     */
    private TrainingData dataWitoutTrainingSet;

    /**
     * Объект тестируемого класса
     */
    private TrainingDataBuilder dataBuilder;

    @Before
    public void setUp() throws DataException {
        Set<String> classNames = randomStrings(2);
        Set<String> paramNames = randomStrings(2);

        Set<DataObject> testingSet = randomObjects(2, paramNames);
        Set<TrainingDataObject> trainingSet = randomTrainingObjects(2, paramNames, classNames);

        data = DataFactory.newTrainingData(DataFactory.newClasses(classNames), testingSet, trainingSet);
        dataWitoutTrainingSet =
                DataFactory.newTrainingData(DataFactory.newClasses(classNames), testingSet, Collections.emptySet());
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

    @Test(expected = DataException.class)
    public void testNonReadyBuild() throws DataException {
        dataBuilder.build();
    }

    @Test
    public void testBuildWithNoTrainingSetPresence() throws DataException {
        dataWitoutTrainingSet.classes().forEach(dataBuilder::defineClass);
        dataWitoutTrainingSet.testingSet().forEach(dataBuilder::addObject);
        Assert.assertEquals(dataWitoutTrainingSet, dataBuilder.build());
    }

    @Test
    public void testBuildWithTrainingSetPresented() throws DataException {
        data.classes().forEach(dataBuilder::defineClass);
        data.testingSet().forEach(dataBuilder::addObject);
        data.trainingSet().forEach(dataBuilder::addTrainingObject);
        Assert.assertEquals(data, dataBuilder.build());
    }
}
