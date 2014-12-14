/*
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

package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataClass;

/**
 * Модульные тесты для класса <code>Point</code>
 *
 * @see Point
 */
public class PointTest extends MathPrimitivesBaseTest<Point> {

    private final double delta = 0.001;

    private final double x = 5.0;
    private final double y = -10.0;
    private final DataClass clazz = dataFactory.newClass("class");

    private Point obj;
    private Point similarObj;
    private Point differentObj;

    @Before
    public void setUp() {
        super.setUp();

        obj = newPoint(x, y, clazz);
        similarObj = newPoint(x, y, clazz);
        differentObj = newPoint(y, x, clazz);
    }

    @Test
    public void testX() {
        Assert.assertEquals(x, obj.x(), delta);
    }

    @Test
    public void testY() {
        Assert.assertEquals(y, obj.y(), delta);
    }

    @Test
    public void testShift() {
        Assert.assertEquals(newPoint(-1.0, 2.0), newPoint(-2.0, 1.0).shift(1.0, 1.0));
    }

    @Test
    public void testDistance() {
        Assert.assertEquals(1.0, newPoint(0.0, 0.0).distanceTo(newPoint(1.0, 0.0)), delta);
        Assert.assertEquals(1.0, newPoint(-1.0, 0.0).distanceTo(newPoint(-2.0, 0.0)), delta);
    }

    @Test
    public void testToPolar() {
        double c = Math.sqrt(2) / 2; // значение каждой из Декартовых координат, при которых полярный радиус до точки = 1.0

        assertPointsEqual(newPolarPoint(1.0, 0.0), newPoint(1.0, 0.0).toPolar());
        assertPointsEqual(newPolarPoint(1.0, Math.PI / 4), newPoint(c, c).toPolar());
        assertPointsEqual(newPolarPoint(1.0, Math.PI / 2), newPoint(0.0, 1.0).toPolar());
        assertPointsEqual(newPolarPoint(1.0, 3 * Math.PI / 4), newPoint(-c, c).toPolar());
        assertPointsEqual(newPolarPoint(1.0, Math.PI), newPoint(-1.0, 0.0).toPolar());
        assertPointsEqual(newPolarPoint(1.0, 5 * Math.PI / 4), newPoint(-c, -c).toPolar());
        assertPointsEqual(newPolarPoint(1.0, 3 * Math.PI / 2), newPoint(0.0, -1.0).toPolar());
        assertPointsEqual(newPolarPoint(1.0, 7 * Math.PI / 4), newPoint(c, -c).toPolar());
    }

    private void assertPointsEqual(PolarPoint a, PolarPoint b) {
        Assert.assertEquals(a.r(), b.r(), delta);
        Assert.assertEquals(a.phi(), b.phi(), delta);
    }

    @Test
    public void testHashCode() {
        super.testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testEquals() {
        super.testEquals(obj, differentObj, similarObj);
    }
}
