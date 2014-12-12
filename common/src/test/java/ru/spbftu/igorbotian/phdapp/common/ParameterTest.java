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
import org.junit.Test;

/**
 * Модульные тесты для класса <code>Parameter</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Parameter
 */
public class ParameterTest extends BaseDataTest<Parameter> {

    private final Parameter obj = dataFactory.newParameter("obj", "value", BasicDataTypes.STRING);
    private final Parameter differentObj =
            dataFactory.newParameter("differentObj", "anotherValue", BasicDataTypes.STRING);
    private final Parameter objWithSameNameAndDifferentValue =
            dataFactory.newParameter("obj", "anotherValue", BasicDataTypes.STRING);
    private final Parameter similarObj = dataFactory.newParameter("obj", "value", BasicDataTypes.STRING);

    @Test
    public void testName() {
        String paramName = "test";
        Assert.assertEquals(paramName, dataFactory.newParameter(paramName, "anyValue", BasicDataTypes.STRING).name());
    }

    @Test
    public void testValue() {
        String paramValue = "test";
        Assert.assertEquals(paramValue, dataFactory.newParameter("anyParam", paramValue, BasicDataTypes.STRING).value());
    }

    @Test
    public void testValueType() {
        DataType<String> dataType = BasicDataTypes.STRING;
        Assert.assertEquals(dataType, dataFactory.newParameter("anyParam", "anyValue", dataType).valueType());
    }

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
        testHashCode(obj, objWithSameNameAndDifferentValue, similarObj);
    }

    @Test
    public void testEquals() {
        testEquals(obj, differentObj, similarObj);
        testEquals(obj, objWithSameNameAndDifferentValue, similarObj);
    }
}
