/*
 * Copyright (c) 2015 Igor Botian
 *
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
 */

package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>QuadraticVector</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class QuadraticFunctionVectorTest {

    /**
     * Точность сравнения вещественных чисел
     */
    private static final double PRECISION = 0.01;

    /**
     * Название параметра, используемого в объектах обучающей выборки
     */
    private static final String PARAM_ID = "param";

    /**
     * Фабрика объектов предметной области
     */
    private DataFactory dataFactory;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new DataModule());
        dataFactory = injector.getInstance(DataFactory.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTrainingSet() {
        new QuadraticFunctionVector(randomTrainingSet(PARAM_ID, 0));
    }

    @Test
    public void testValues() {
        List<Integer> vectorSizes = Stream.of(1, 2, 3, 5, 8, 13, 21).collect(Collectors.toList());

        for(Integer size : vectorSizes) {
            QuadraticFunctionVector vector = new QuadraticFunctionVector(randomTrainingSet(PARAM_ID, size));
            double[] values = vector.values();
            Assert.assertEquals((int) size, values.length);

            for(double value : values) {
                Assert.assertEquals(1, value, PRECISION);
            }
        }
    }

    private LinkedHashSet<? extends Judgement> randomTrainingSet(String paramId, int count) {
        LinkedHashSet<Judgement> result = new LinkedHashSet<>();

        for(int i = 0; i < count; i++) {
            result.add(randomJudgement(paramId));
        }

        return result;
    }

    private Judgement randomJudgement(String paramId) {
        return dataFactory.newPairwiseTrainingObject(
                Collections.singleton(randomObject(paramId)),
                Collections.singleton(randomObject(paramId))
        );
    }

    private UnclassifiedObject randomObject(String paramId) {
        return dataFactory.newUnclassifiedObject(
                randomString(),
                Collections.singleton(dataFactory.newParameter(paramId, randomString(), BasicDataTypes.STRING))
        );
    }

    private static String randomString() {
        return UUID.randomUUID().toString();
    }
}
