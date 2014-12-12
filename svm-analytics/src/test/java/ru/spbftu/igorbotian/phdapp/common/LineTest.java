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
 * Модульные тесты для класса <code>Line</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Line
 */
public class LineTest extends MathPrimitivesBaseTest<Line> {

    private final double delta = 0.0001;

    private final double a = 1.0;
    private final double b = 2.0;
    private final double c = 3.0;

    private Line obj;
    private Line similarObj;
    private Line differentObj;

    @Before
    public void setUp() {
        super.setUp();

        obj = newLine(a, b, c);
        similarObj = newLine(a, b, c);
        differentObj = newLine(a + 1.0, b + 1.0, c + 1.0);
    }

    @Test
    public void testA() {
        Assert.assertEquals(a, obj.a(), delta);
    }

    @Test
    public void testB() {
        Assert.assertEquals(b, obj.b(), delta);
    }

    @Test
    public void testC() {
        Assert.assertEquals(c, obj.c(), delta);
    }

    @Test
    public void testX() {
        Assert.assertEquals(1.0, newLine(-2.0, -2.0, 4.0).x(1.0), delta);
    }

    @Test
    public void testY() {
        Assert.assertEquals(1.0, newLine(-2.0, -2.0, 4.0).x(1.0), delta);
    }

    @Test
    public void testAngle() {
        Assert.assertEquals(- Math.PI / 4, newLine(-2.0, -2.0, 4.0).angle(), delta);
    }

    @Test
    public void testHashCode() {
        super.testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testEquals() {
        super.testHashCode(obj, differentObj, similarObj);
    }

    @Test
    public void testConstructionByTwoPoints() {
        Point a = newPoint(0.0, 0.0);
        Point b = newPoint(1.0, 1.0);
        Line line = newLine(a, b);

        Assert.assertEquals(-1.0, line.a(), delta);
        Assert.assertEquals(1.0, line.b(), delta);
        Assert.assertEquals(0.0, line.c(), delta);
    }
}
