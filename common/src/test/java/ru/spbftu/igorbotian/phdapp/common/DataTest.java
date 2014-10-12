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
 * Модульные тесты для класса <code>Data</code>
 *
 * @see Data
 */
public class DataTest extends BaseDataTest<Data> {

    private Set<DataClass> setOfClasses;
    private Set<DataObject> setOfObjects;

    private Data obj;
    private Data differentObj;
    private Data objWithSameClassesAndDifferentObjects;
    private Data similarObj;

    @Before
    public void setUp() {
        setOfClasses = new HashSet<>();
        setOfClasses.add(new DataClass("firstClass"));
        setOfClasses.add(new DataClass("secondClass"));

        Set<DataObjectParameter> setOfParams = Collections.singleton(new DataObjectParameter("param", "value"));
        setOfObjects = Collections.singleton(new DataObject("obj", setOfParams));

        Set<DataClass> anotherSetOfClasses = new HashSet<>();
        anotherSetOfClasses.add(new DataClass("thirdClass"));
        anotherSetOfClasses.add(new DataClass("fourthClass"));

        Set<DataObjectParameter> anotherSetOfParams = Collections.singleton(new DataObjectParameter("anotherParam", "value"));
        Set<DataObject> anotherSetOfObjects = Collections.singleton(new DataObject("anotherObj", anotherSetOfParams));

        obj = new Data(setOfClasses, setOfObjects);
        differentObj = new Data(anotherSetOfClasses, anotherSetOfObjects);
        objWithSameClassesAndDifferentObjects = new Data(setOfClasses, anotherSetOfObjects);
        similarObj = new Data(setOfClasses, setOfObjects);
    }

    @Test
    public void testClasses() {
        Data data = new Data(setOfClasses, setOfObjects);

        Assert.assertEquals(setOfClasses.size(), data.classes().size());
        Assert.assertTrue(setOfClasses.containsAll(data.classes()));
    }

    @Test
    public void testObjects() {
        Data data = new Data(setOfClasses, setOfObjects);

        Assert.assertEquals(setOfObjects.size(), data.objects().size());
        Assert.assertTrue(setOfObjects.containsAll(data.objects()));
    }

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
        testHashCode(obj, objWithSameClassesAndDifferentObjects, similarObj);
    }

    @Test
    public void testEquals() {
        testEquals(obj, differentObj, similarObj);
        testEquals(obj, objWithSameClassesAndDifferentObjects, similarObj);
    }
}
