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
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

/**
 * Модульные тесты для класса <code>Point</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 */
public class PointTest extends BaseDataTest<Point> {

    private final double delta = 0.001;

    private final double x = 5.0;
    private final double y = -10.0;
    private final DataClass clazz = DataFactory.newClass("class");

    private final Point obj = new Point(x, y, clazz);
    private final Point similarObj = new Point(x, y, clazz);
    private final Point differentObj = new Point(y, x, clazz);

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
        Assert.assertEquals(new Point(-1.0, 2.0), new Point(-2.0, 1.0).shift(1.0, 1.0));
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
