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
import org.junit.Before;
import org.junit.Test;

/**
 * Модульные тесты для класса <code>PolarPoint</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.PolarPoint
 */
public class PolarPointTest extends MathPrimitivesBaseTest<PolarPoint> {

    private final double delta = 0.0001;

    private final double r = 1.0;
    private final double phi = Math.PI / 2;

    private PolarPoint obj;
    private PolarPoint similarObj;
    private PolarPoint differentObj;

    @Before
    public void setUp() {
        super.setUp();

        obj = newPolarPoint(r, phi);
        similarObj = newPolarPoint(r, phi);
        differentObj = newPolarPoint(2.0, Math.PI / 3);
    }

    @Test
    public void testR() {
        Assert.assertEquals(r, obj.r(), delta);
    }

    @Test
    public void testPhi() {
        Assert.assertEquals(phi, obj.phi(), delta);
    }

    @Test
    public void testRotate() {
        Assert.assertEquals(newPolarPoint(r, Math.PI), newPolarPoint(r, Math.PI / 2).rotate(Math.PI / 2));
    }

    @Test
    public void testToDecart() {
        double c = Math.sqrt(2) / 2; // значение каждой из Декартовых координат, при которых полярный радиус до точки = 1.0

        assertPointsEqual(newPoint(0.0, 0.0), newPolarPoint(0.0, 0.0).toCartesian());
        assertPointsEqual(newPoint(1.0, 0.0), newPolarPoint(1.0, 0.0).toCartesian());
        assertPointsEqual(newPoint(c, c), newPolarPoint(1.0, Math.PI / 4).toCartesian());
        assertPointsEqual(newPoint(0.0, 1.0), newPolarPoint(1.0, Math.PI / 2).toCartesian());
        assertPointsEqual(newPoint(-c, c), newPolarPoint(1.0, 3 * Math.PI / 4).toCartesian());
        assertPointsEqual(newPoint(-1.0, 0.0), newPolarPoint(1.0, Math.PI).toCartesian());
        assertPointsEqual(newPoint(-c, -c), newPolarPoint(1.0, 5 * Math.PI / 4).toCartesian());
        assertPointsEqual(newPoint(0.0, -1.0), newPolarPoint(1.0, 3 * Math.PI / 2).toCartesian());
        assertPointsEqual(newPoint(c, -c), newPolarPoint(1.0, 7 * Math.PI / 4).toCartesian());
    }

    private void assertPointsEqual(Point a, Point b) {
        Assert.assertEquals(a.x(), b.x(), delta);
        Assert.assertEquals(a.y(), b.y(), delta);
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
