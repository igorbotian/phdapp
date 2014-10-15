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
import java.util.HashSet;
import java.util.Set;

/**
 * Модульные тесты для класса <code>TrainingData</code>.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingData
 */
public class TrainingDataTest extends BaseDataTest<TrainingData> {

    private Set<DataClass> setOfClasses;
    private Set<DataObject> testingSet;
    private Set<TrainingDataObject> trainingSet;

    private TrainingData obj;
    private TrainingData differentObj;
    private TrainingData objWithSameClassesAndDifferentTrainingSet;
    private TrainingData similarObj;

    @Before
    public void setUp() {
        setOfClasses = new HashSet<>();
        setOfClasses.add(DataFactory.newClass("firstClass"));
        setOfClasses.add(DataFactory.newClass("secondClass"));

        Set<DataObjectParameter> setOfParams = Collections.singleton(DataFactory.newObjectParameter("param", "value"));
        testingSet = Collections.singleton(DataFactory.newObject("testingObj", setOfParams));
        trainingSet = Collections.singleton(DataFactory.newTrainingObject("trainingObj", setOfParams, setOfClasses.iterator().next()));

        Set<DataClass> anotherSetOfClasses = new HashSet<>();
        anotherSetOfClasses.add(DataFactory.newClass("thirdClass"));
        anotherSetOfClasses.add(DataFactory.newClass("fourthClass"));

        Set<DataObjectParameter> anotherSetOfParams = Collections.singleton(DataFactory.newObjectParameter("anotherParam", "value"));
        Set<DataObject> anotherTestingSet = Collections.singleton(DataFactory.newObject("anotherTestingObj", anotherSetOfParams));
        Set<TrainingDataObject> anotherTrainingSet = Collections.singleton(DataFactory.newTrainingObject("anotherTrainingObj",
                anotherSetOfParams, anotherSetOfClasses.iterator().next()));

        obj = DataFactory.newTrainingData(setOfClasses, testingSet, trainingSet);
        differentObj = DataFactory.newTrainingData(anotherSetOfClasses, anotherTestingSet, anotherTrainingSet);
        objWithSameClassesAndDifferentTrainingSet = DataFactory.newTrainingData(anotherSetOfClasses, testingSet, anotherTrainingSet);
        similarObj = DataFactory.newTrainingData(setOfClasses, testingSet, trainingSet);
    }

    @Test
    public void testTestingSet() {
        TrainingData data = DataFactory.newTrainingData(setOfClasses, testingSet, trainingSet);

        Assert.assertEquals(testingSet.size(), data.testingSet().size());
        Assert.assertTrue(testingSet.containsAll(data.testingSet()));
    }

    @Test
    public void testTrainingSet() {
        TrainingData data = DataFactory.newTrainingData(setOfClasses, testingSet, trainingSet);

        Assert.assertEquals(trainingSet.size(), data.trainingSet().size());
        Assert.assertTrue(trainingSet.containsAll(data.trainingSet()));
    }

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
        testHashCode(obj, objWithSameClassesAndDifferentTrainingSet, similarObj);
    }

    @Test
    public void testEquals() {
        testEquals(obj, differentObj, similarObj);
        testEquals(obj, objWithSameClassesAndDifferentTrainingSet, similarObj);
    }
}
