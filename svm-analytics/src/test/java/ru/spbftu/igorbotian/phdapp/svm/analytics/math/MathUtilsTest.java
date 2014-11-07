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

package ru.spbftu.igorbotian.phdapp.svm.analytics.math;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.Point;
import ru.spbftu.igorbotian.phdapp.common.PolarPoint;

/**
 * Модульные тесты для класса <code>MathUtils</code>
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.math.MathUtils
 */
public class MathUtilsTest {

    private final double delta = 0.0001;

    @Test
    public void testToPolar() {
        double c = Math.sqrt(2) / 2; // значение каждой из Декартовых координат, при которых полярный радиус до точки = 1.0

        assertPointsEqual(new PolarPoint(1.0, 0.0), MathUtils.toPolar(new Point(1.0, 0.0)));
        assertPointsEqual(new PolarPoint(1.0, Math.PI / 4), MathUtils.toPolar(new Point(c, c)));
        assertPointsEqual(new PolarPoint(1.0, Math.PI / 2), MathUtils.toPolar(new Point(0.0, 1.0)));
        assertPointsEqual(new PolarPoint(1.0, 3 * Math.PI / 4), MathUtils.toPolar(new Point(-c, c)));
        assertPointsEqual(new PolarPoint(1.0, Math.PI), MathUtils.toPolar(new Point(-1.0, 0.0)));
        assertPointsEqual(new PolarPoint(1.0, 5 * Math.PI / 4), MathUtils.toPolar(new Point(-c, -c)));
        assertPointsEqual(new PolarPoint(1.0, 3 * Math.PI / 2), MathUtils.toPolar(new Point(0.0, -1.0)));
        assertPointsEqual(new PolarPoint(1.0, 7 * Math.PI / 4), MathUtils.toPolar(new Point(c, -c)));
    }

    private void assertPointsEqual(PolarPoint a, PolarPoint b) {
        Assert.assertEquals(a.r(), b.r(), delta);
        Assert.assertEquals(a.phi(), b.phi(), delta);
    }

    @Test
    public void testToDecart() {
        double c = Math.sqrt(2) / 2; // значение каждой из Декартовых координат, при которых полярный радиус до точки = 1.0

        assertPointsEqual(new Point(0.0, 0.0), MathUtils.toDecart(new PolarPoint(0.0, 0.0)));
        assertPointsEqual(new Point(1.0, 0.0), MathUtils.toDecart(new PolarPoint(1.0, 0.0)));
        assertPointsEqual(new Point(c, c), MathUtils.toDecart(new PolarPoint(1.0, Math.PI / 4)));
        assertPointsEqual(new Point(0.0, 1.0), MathUtils.toDecart(new PolarPoint(1.0, Math.PI / 2)));
        assertPointsEqual(new Point(-c, c), MathUtils.toDecart(new PolarPoint(1.0, 3 * Math.PI / 4)));
        assertPointsEqual(new Point(-1.0, 0.0), MathUtils.toDecart(new PolarPoint(1.0, Math.PI)));
        assertPointsEqual(new Point(-c, -c), MathUtils.toDecart(new PolarPoint(1.0, 5 * Math.PI / 4)));
        assertPointsEqual(new Point(0.0, -1.0), MathUtils.toDecart(new PolarPoint(1.0, 3 * Math.PI / 2)));
        assertPointsEqual(new Point(c, -c), MathUtils.toDecart(new PolarPoint(1.0, 7 * Math.PI / 4)));
    }

    private void assertPointsEqual(Point a, Point b) {
        Assert.assertEquals(a.x(), b.x(), delta);
        Assert.assertEquals(a.y(), b.y(), delta);
    }

    @Test
    public void testDistance() {
        Assert.assertEquals(1.0, MathUtils.distance(new Point(0.0, 0.0), new Point(1.0, 0.0)), delta);
        Assert.assertEquals(1.0, MathUtils.distance(new Point(-1.0, 0.0), new Point(-2.0, 0.0)), delta);
    }

    @Test
    public void testTranslateOfPositives() {
        Assert.assertEquals(2.0, MathUtils.translate(1.0, 0.0, 5.0, 0.0, 10.0), delta);
    }

    @Test
    public void testTranslateOfNegatives() {
        Assert.assertEquals(-2.0, MathUtils.translate(-1.0, -5.0, -0.0, -10.0, 0.0), delta);
    }
}
