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
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Модульные тесты для класса <code>IntegerDataGenerator</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.IntegerDataGenerator
 */
public class IntegerDataGeneratorTest {

    private static final DataClass FIRST_PARAM_ODD_CLASS = () -> "FIRST_PARAM_IS_ODD";
    private static final DataClass FIRST_PARAM_EVEN_CLASS = () -> "FIRST_PARAM_IS_EVEN";

    private static DataClass classIdentifier(Set<Parameter<?>> params) {
        for (Parameter<?> param : params) {
            if ((Integer) param.value() % 2 == 0) {
                return FIRST_PARAM_EVEN_CLASS;
            }
        }

        return FIRST_PARAM_ODD_CLASS;
    }

    @Test
    public void testGenerateData() throws DataException {
        int anySize = 5;
        Set<? extends DataClass> classes = DataFactory.newClasses("first", "second");
        Range<Integer> firstRange = new Range<>(-10, 10, Integer::compare);
        Range<Integer> secondRange = new Range<>(0, 5, Integer::compare);

        Map<String, Range<Integer>> paramValueRange = new HashMap<>();
        paramValueRange.put("firstParam", firstRange);
        paramValueRange.put("secondParam", secondRange);

        UnclassifiedData data = IntegerDataGenerator.generateData(anySize, classes, paramValueRange);
        Assert.assertTrue(classes.size() == data.classes().size() && classes.containsAll(data.classes()));
        Assert.assertEquals(anySize, data.objects().size());

        for (UnclassifiedObject obj : data.objects()) {
            Assert.assertEquals(paramValueRange.keySet().size(), obj.parameters().size());

            for (Parameter<?> param : obj.parameters()) {
                int value = (Integer) param.value();

                Assert.assertTrue(paramValueRange.keySet().contains(param.name()));
                Assert.assertTrue((value >= firstRange.lowerBound() && value <= firstRange.upperBound())
                        || (value >= secondRange.lowerBound() && value <= secondRange.upperBound()));
            }
        }
    }

    @Test
    public void testGenerateTrainingData() throws DataException {
        int anySize = 50;
        Range<Integer> range = new Range<>(0, 100, Integer::compare);
        Map<String, Range<Integer>> paramValueRange = new HashMap<>();
        paramValueRange.put("firstParam", range);
        paramValueRange.put("secondParam", range);

        PointwiseTrainingSet data = IntegerDataGenerator.generateTrainingData(anySize, paramValueRange,
                IntegerDataGeneratorTest::classIdentifier);

        Assert.assertEquals(anySize, data.objects().size());

        for (PointwiseTrainingObject obj : data.objects()) {
            Assert.assertEquals(paramValueRange.keySet().size(), obj.parameters().size());
            Assert.assertEquals(classIdentifier(obj.parameters()), obj.realClass());

            for (Parameter<?> param : obj.parameters()) {
                int value = (Integer) param.value();

                Assert.assertTrue(paramValueRange.keySet().contains(param.name()));
                Assert.assertTrue(value >= range.lowerBound() && value <= range.upperBound());
            }
        }
    }
}
