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
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.*;
import java.util.function.Function;

/**
 * Модульные тесты для класса <code>InputDataUtils</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.test.InputDataUtils
 */
public class InputDataUtilsTest {

    private final Set<? extends DataClass> classes = DataFactory.newClasses("firstClass", "secondClass");
    private final Set<Parameter<?>> params = Collections.singleton(
            DataFactory.newParameter("param", "value", BasicDataTypes.STRING));
    private final Set<? extends UnclassifiedObject> testingSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            DataFactory.newUnclassifiedObject("firstObj", params),
            DataFactory.newUnclassifiedObject("secondObj", params)
    )));
    private final Set<? extends ClassifiedObject> trainingSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            DataFactory.newClassifiedObject("firstObj", params, classes.iterator().next()),
            DataFactory.newClassifiedObject("secondObj", params, classes.iterator().next()),
            DataFactory.newClassifiedObject("thirdObj", params, classes.iterator().next())
    )));
    private final Function<UnclassifiedObject, UnclassifiedObject> blurFunction =
            obj -> DataFactory.newUnclassifiedObject(UUID.randomUUID().toString(), obj.parameters());

    @Test
    public void testShuffle() throws DataException {
        int expectedTrainingSetSize = 1;
        float ratio = (float) expectedTrainingSetSize / trainingSet.size();
        ClassifiedData data = DataFactory.newClassifiedData(classes, trainingSet);
        InputData shuffledData = InputDataUtils.shuffle(data, ratio);
        Assert.assertEquals(expectedTrainingSetSize, shuffledData.trainingSet().size());

        Set<? extends ClassifiedObject> resultTrainingSet = new HashSet<>(shuffledData.trainingSet());
        resultTrainingSet.removeAll(trainingSet);
        Assert.assertEquals(0, resultTrainingSet.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleWithTrainingSetRatioLessThanMinimum() throws DataException {
        InputDataUtils.shuffle(DataFactory.newClassifiedData(classes, trainingSet), -1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleTrainingSetRatioLowerBound() throws DataException {
        InputDataUtils.shuffle(DataFactory.newClassifiedData(classes, trainingSet), 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleTrainingSetRatioUpperBound() throws DataException {
        InputDataUtils.shuffle(DataFactory.newClassifiedData(classes, trainingSet), 1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShuffleWithTrainingSetRatioGreaterThanMaximum() throws DataException {
        InputDataUtils.shuffle(DataFactory.newClassifiedData(classes, trainingSet), 2.0f);
    }

    @Test
    public void testBlur() throws DataException {
        int power = 5;
        UnclassifiedData blurredData = InputDataUtils.blur(DataFactory.newUnclassifiedData(classes, testingSet),
                power, blurFunction);

        Assert.assertEquals(5 * testingSet.size(), blurredData.objects().size());
        Assert.assertEquals(classes, blurredData.classes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlurWithNonPositivePower() throws DataException {
        InputDataUtils.blur(DataFactory.newUnclassifiedData(classes, testingSet), -1, blurFunction);
    }
}
