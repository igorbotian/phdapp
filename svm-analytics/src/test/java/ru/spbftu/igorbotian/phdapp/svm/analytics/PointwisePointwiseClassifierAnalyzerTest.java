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

package ru.spbftu.igorbotian.phdapp.svm.analytics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Модульные тесты для класса <code>PointwiseClassifierAnalyzer</code>
 *
 * @see PointwiseClassifierAnalyzer
 */
public class PointwisePointwiseClassifierAnalyzerTest {

    private final float delta = 0.001f;

    private float accuracy;
    private float precision;
    private float recall;

    private Report report;

    @Before
    public void setUp() throws DataException {
        // три объекта
        String firstObjId = randomString();
        String secondObjId = randomString();
        String thirdObjId = randomString();

        // один параметр; разное значение для каждого объекта
        String paramName = randomString();
        Set<Parameter<?>> firstObjParams = toSet(DataFactory.newParameter(paramName, randomString(), BasicDataTypes.STRING));
        Set<Parameter<?>> secondObjParams = toSet(DataFactory.newParameter(paramName, randomString(), BasicDataTypes.STRING));
        Set<Parameter<?>> thirdObjParams = toSet(DataFactory.newParameter(paramName, randomString(), BasicDataTypes.STRING));

        // два класса
        DataClass firstObjClass = DataFactory.newClass("firstClass");
        DataClass secondObjClass = DataFactory.newClass("secondClass");
        Set<DataClass> classes = toSet(firstObjClass, secondObjClass);

        // один объект относится к первому классу, два ко второму
        PointwiseTrainingSet pointwiseTrainingSet = DataFactory.newPointwiseTrainingSet(
                classes, toSet(
                        DataFactory.newPointwiseTrainingObject(firstObjId, firstObjParams, firstObjClass),
                        DataFactory.newPointwiseTrainingObject(secondObjId, secondObjParams, secondObjClass),
                        DataFactory.newPointwiseTrainingObject(thirdObjId, thirdObjParams, secondObjClass /* корректный */)
                )
        );

        // один объект правильно классифицирован как относящийся к первому классу
        // один объект неправильно классифицирован как относящийся ко первому классу
        // один объект правильно классифицирован как относящийся ко второму классу
        ClassifiedData classifiedData = DataFactory.newClassifiedData(
                classes, toSet(
                        DataFactory.newClassifiedObject(firstObjId, firstObjParams, firstObjClass),
                        DataFactory.newClassifiedObject(secondObjId, secondObjParams, secondObjClass),
                        DataFactory.newClassifiedObject(thirdObjId, thirdObjParams, firstObjClass /* некорректный */)
                )
        );

        // вручную посчитанные значения метрик
        accuracy = (/* первый класс */ calculateAccuracy(1, 2, 1, 0) + /* второй класс */ calculateAccuracy(2, 1, 1, 1))
                / classes.size();
        precision = (/* первый класс */ calculatePrecision(1, 1) + /* второй класс */ calculatePrecision(2, 1))
                / classes.size();
        recall = (/* первый класс */ calculateRecall(2, 1) + /* второй класс */ calculateRecall(1, 1))
                / classes.size();

        report = new PointwiseClassifierAnalyzerImpl().analyze(classifiedData, pointwiseTrainingSet);
    }

    private float calculateAccuracy(int numberOfObjects,
                                    int numberOfClassifiedObjects,
                                    int numberOfCorrectlyClassifiedObjects,
                                    int numberOfIncorrectlyClassifiedObjects) {
        return (numberOfClassifiedObjects - numberOfCorrectlyClassifiedObjects + numberOfIncorrectlyClassifiedObjects)
                / numberOfObjects;
    }

    private float calculatePrecision(int numberOfObjects, int numberOfCorrectlyClassifiedObjects) {
        return (numberOfCorrectlyClassifiedObjects / numberOfObjects);
    }

    private float calculateRecall(int numberOfClassifiedObjects, int numberOfCorrectlyClassifiedObjects) {
        return (numberOfCorrectlyClassifiedObjects / numberOfClassifiedObjects);
    }

    @SafeVarargs
    private final <T> Set<T> toSet(T... objects) {
        return new HashSet<>(Arrays.asList(objects));
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void testAccuracy() {
        Assert.assertEquals(accuracy, report.accuracy(), delta);
    }

    @Test
    public void testPrecision() {
        Assert.assertEquals(precision, report.precision(), delta);
    }

    @Test
    public void testRecall() {
        Assert.assertEquals(recall, report.recall(), delta);
    }
}
