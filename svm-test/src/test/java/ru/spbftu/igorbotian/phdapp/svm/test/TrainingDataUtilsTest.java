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

package ru.spbftu.igorbotian.phdapp.svm.test;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Модульные тесты для класса <code>TrainingDataUtils</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.test.TrainingDataUtils
 */
public class TrainingDataUtilsTest {

    private final Set<? extends DataClass> classes = DataFactory.newClasses("firstClass", "secondClass");
    private final Set<DataObjectParameter<?>> params = Collections.singleton(
            DataFactory.newObjectParameter("param", "value", BasicDataValueTypes.STRING));
    private final Set<? extends DataObject> testingSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            DataFactory.newObject("firstObj", params),
            DataFactory.newObject("secondObj", params)
    )));
    private final Set<? extends TrainingDataObject> trainingSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            DataFactory.newTrainingObject("firstObj", params, classes.iterator().next()),
            DataFactory.newTrainingObject("secondObj", params, classes.iterator().next()),
            DataFactory.newTrainingObject("thirdObj", params, classes.iterator().next())
    )));

    @Test
    public void testShuffle() throws DataException {
        int expectedTrainingSetSize = 1;
        float ratio = (float) expectedTrainingSetSize / trainingSet.size();
        TrainingData data = DataFactory.newTrainingData(classes, testingSet, trainingSet);
        TrainingData shuffledData = TrainingDataUtils.shuffle(data, ratio);
        Assert.assertEquals(expectedTrainingSetSize, shuffledData.trainingSet().size());

        Set<? extends TrainingDataObject> resultTrainingSet = new HashSet<>(shuffledData.trainingSet());
        resultTrainingSet.removeAll(trainingSet);
        Assert.assertEquals(0, resultTrainingSet.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleWithTrainingSetRatioLessThanMinimum() throws DataException{
        TrainingDataUtils.shuffle(DataFactory.newTrainingData(classes, testingSet, trainingSet), -1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleTrainingSetRatioLowerBound() throws DataException{
        TrainingDataUtils.shuffle(DataFactory.newTrainingData(classes, testingSet, trainingSet), 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleTrainingSetRatioUpperBound() throws DataException{
        TrainingDataUtils.shuffle(DataFactory.newTrainingData(classes, testingSet, trainingSet), 1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleWithTrainingSetRatioGreaterThanMaximum() throws DataException{
        TrainingDataUtils.shuffle(DataFactory.newTrainingData(classes, testingSet, trainingSet), 2.0f);
    }
}
