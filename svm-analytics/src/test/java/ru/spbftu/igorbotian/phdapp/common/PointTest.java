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

package ru.spbftu.igorbotian.phdapp.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * Модульные тесты для класса <code>Point</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 */
public class PointTest extends BaseDataTest<Point> {

    private final double delta = 0.001;

    private final double x = 5.0;
    private final double y = -10.0;
    private final DataClass clazz = dataFactory.newClass("class");

    private final Point obj = new Point(x, y, clazz, dataFactory);
    private final Point similarObj = new Point(x, y, clazz, dataFactory);
    private final Point differentObj = new Point(y, x, clazz, dataFactory);

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
        Assert.assertEquals(new Point(-1.0, 2.0, dataFactory), new Point(-2.0, 1.0, dataFactory).shift(1.0, 1.0));
    }

    @Test
    public void testDistance() {
        Assert.assertEquals(1.0, new Point(0.0, 0.0, dataFactory).distanceTo(new Point(1.0, 0.0, dataFactory)), delta);
        Assert.assertEquals(1.0, new Point(-1.0, 0.0, dataFactory).distanceTo(new Point(-2.0, 0.0, dataFactory)), delta);
    }

    @Test
    public void testToPolar() {
        double c = Math.sqrt(2) / 2; // значение каждой из Декартовых координат, при которых полярный радиус до точки = 1.0

        assertPointsEqual(new PolarPoint(1.0, 0.0, dataFactory), new Point(1.0, 0.0, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, Math.PI / 4, dataFactory), new Point(c, c, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, Math.PI / 2, dataFactory), new Point(0.0, 1.0, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, 3 * Math.PI / 4, dataFactory), new Point(-c, c, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, Math.PI, dataFactory), new Point(-1.0, 0.0, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, 5 * Math.PI / 4, dataFactory), new Point(-c, -c, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, 3 * Math.PI / 2, dataFactory), new Point(0.0, -1.0, dataFactory).toPolar());
        assertPointsEqual(new PolarPoint(1.0, 7 * Math.PI / 4, dataFactory), new Point(c, -c, dataFactory).toPolar());
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
