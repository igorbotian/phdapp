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
 * Модульные тесты для класса <code>TrainingDataObject</code>
 * @see TrainingDataObject
 */
public class TrainingDataObjectTest extends BaseDataTest<TrainingDataObject> {

    private DataClass realClass;
    private Set<DataObjectParameter> setOfParams;

    private TrainingDataObject obj;
    private TrainingDataObject differentObj;
    private TrainingDataObject similarObj;

    @Before
    public void setUp() {
        realClass = new DataClass("class");
        setOfParams = Collections.singleton(new DataObjectParameter("param", "value"));

        DataClass anotherRealClass = new DataClass("anotherClass");
        Set<DataObjectParameter> anotherSetOfParams = Collections.singleton(new DataObjectParameter("anotherParam", "value"));

        obj = new TrainingDataObject("obj", setOfParams, realClass);
        differentObj = new TrainingDataObject("differentObj", anotherSetOfParams, anotherRealClass);
        similarObj = new TrainingDataObject("obj", new HashSet<>(setOfParams), realClass);
    }

    @Test
    public void testRealClass() {
        Assert.assertEquals(realClass, new TrainingDataObject("id", setOfParams, realClass).realClass());
    }

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testEquals() {
        testEquals(obj, differentObj, similarObj);
    }
}
