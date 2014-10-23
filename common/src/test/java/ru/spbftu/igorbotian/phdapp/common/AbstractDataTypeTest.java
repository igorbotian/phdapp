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
 * Базовый тест для класса <code>DataType</code>
 *
 * @see DataType
 */
public class AbstractDataTypeTest {

    @Test
    public void testName() {
        String name = "dataTypeName";
        DataType<Object> dataType = new TestDataType<>(name, Object.class);
        Assert.assertEquals(name, dataType.name());
    }

    @Test
    public void testJavaClass() {
        Class<Integer> javaClass = Integer.class;
        DataType<Integer> dataType = new TestDataType<>("anyTypeName", javaClass);
        Assert.assertEquals(javaClass, dataType.javaClass());
    }

    private static class TestDataType<T> extends AbstractDataType<T> {

        private TestDataType(String name, Class<T> javaClass) {
            super(name, javaClass);
        }
    }
}
