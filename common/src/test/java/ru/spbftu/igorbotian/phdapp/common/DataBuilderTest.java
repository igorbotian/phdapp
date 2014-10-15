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
 * Модульные тесты для класса <code>DataBuilder</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataBuilder
 */
public class DataBuilderTest {

    /**
     * Тестовые данные
     */
    private final Data data;

    /**
     * Объект тестируемого класса
     */
    private DataBuilder dataBuilder;

    public DataBuilderTest() {
        Set<DataClass> classes = Stream.of(
                DataFactory.newClass("first"),
                DataFactory.newClass("second")
        ).collect(Collectors.toSet());

        Set<DataObjectParameter> params = Collections.singleton(DataFactory.newObjectParameter("param", "value"));

        Set<DataObject> objects = Stream.of(
                DataFactory.newObject("fisrt", params),
                DataFactory.newObject("second", params)
        ).collect(Collectors.toSet());

        data = DataFactory.newData(classes, objects);
    }

    @Before
    public void setUp() {
        dataBuilder = new DataBuilder();
    }

    @Test
    public void testReady() {
        data.classes().forEach(dataBuilder::defineClass);
        data.objects().forEach(dataBuilder::addObject);
        Assert.assertTrue(dataBuilder.isReady());
    }

    @Test
    public void testNoClassesDefined() {
        data.objects().forEach(dataBuilder::addObject);
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
    public void testBuild() {
        data.classes().forEach(dataBuilder::defineClass);
        data.objects().forEach(dataBuilder::addObject);
        Assert.assertEquals(data, dataBuilder.build());
    }
}
