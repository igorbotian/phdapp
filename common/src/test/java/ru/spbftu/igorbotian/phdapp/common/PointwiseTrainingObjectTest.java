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

import java.util.HashSet;
import java.util.Set;

/**
 * Модульные тесты для класса <code>PointwiseTrainingObject</code>
 *
 * @see PointwiseTrainingObject
 */
public class PointwiseTrainingObjectTest extends BaseDataTest<PointwiseTrainingObject> {

    private DataClass realClass;
    private Set<Parameter<?>> setOfParams;

    private PointwiseTrainingObject obj;
    private PointwiseTrainingObject differentObj;
    private PointwiseTrainingObject similarObj;

    @Before
    public void setUp() {
        realClass = randomClass();
        setOfParams = randomStringObjectParameters(2);

        DataClass anotherRealClass = randomClass();
        Set<Parameter<?>> anotherSetOfParams = randomStringObjectParameters(3);

        obj = dataFactory.newPointwiseTrainingObject("obj", setOfParams, realClass);
        differentObj = dataFactory.newPointwiseTrainingObject("differentObj", anotherSetOfParams, anotherRealClass);
        similarObj = dataFactory.newPointwiseTrainingObject("obj", new HashSet<>(setOfParams), realClass);
    }

    @Test
    public void testRealClass() {
        Assert.assertEquals(realClass, dataFactory.newPointwiseTrainingObject("name", setOfParams, realClass).realClass());
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
