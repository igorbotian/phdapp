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
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Модульные тесты для класса <code>UnclassifiedObject</code>
 *
 * @see UnclassifiedObject
 */
public class UnclassifiedObjectTest extends BaseDataTest<UnclassifiedObject> {

    private Set<Parameter<?>> setOfParams;

    private UnclassifiedObject obj;
    private UnclassifiedObject differentObj;
    private UnclassifiedObject objWithSameNameAndDifferentParams;
    private UnclassifiedObject similarObj;

    @Before
    public void setUp() {
        setOfParams = randomStringObjectParameters(2);
        Set<Parameter<?>> anotherSetOfParams = randomStringObjectParameters(2);

        obj = DataFactory.newUnclassifiedObject("obj", setOfParams);
        differentObj = DataFactory.newUnclassifiedObject("differentObj", anotherSetOfParams);
        objWithSameNameAndDifferentParams = DataFactory.newUnclassifiedObject("obj", anotherSetOfParams);
        similarObj = DataFactory.newUnclassifiedObject("obj", new HashSet<>(setOfParams));
    }

    @Test
    public void testId() {
        String id = "test";
        Assert.assertEquals(id, DataFactory.newUnclassifiedObject(id, setOfParams).id());
    }

    @Test
    public void testParams() {
        Set<Parameter<?>> setOfParams = randomStringObjectParameters(2);
        UnclassifiedObject obj = DataFactory.newUnclassifiedObject(randomString(), setOfParams);

        Assert.assertEquals(setOfParams.size(), obj.parameters().size());
        Assert.assertTrue(setOfParams.containsAll(obj.parameters()));
    }

    @Test
    public void testParamsWithDifferentTypes() {
        Set<Parameter<?>> setOfParams = new HashSet<>();
        setOfParams.add(DataFactory.newParameter(randomString(), 1, BasicDataTypes.INTEGER));
        setOfParams.add(DataFactory.newParameter(randomString(), 1.0, BasicDataTypes.REAL));
        setOfParams.add(DataFactory.newParameter(randomString(), randomString(), BasicDataTypes.STRING));

        UnclassifiedObject obj = DataFactory.newUnclassifiedObject(randomString(), setOfParams);

        for(Parameter param : setOfParams) {
            Assert.assertTrue(obj.parameters().contains(param));
        }
    }

    @Test
    public void testHashCode() {
        testHashCode(obj, differentObj, similarObj);
        testHashCode(obj, objWithSameNameAndDifferentParams, similarObj);
    }

    @Test
    public void testEquals() {
        testEquals(obj, differentObj, similarObj);
        testEquals(obj, objWithSameNameAndDifferentParams, similarObj);
    }
}
