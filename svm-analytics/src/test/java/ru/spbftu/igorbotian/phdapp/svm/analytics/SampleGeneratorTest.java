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

package ru.spbftu.igorbotian.phdapp.svm.analytics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.Point;
import ru.spbftu.igorbotian.phdapp.svm.analytics.math.MathUtils;

import java.util.Set;

/**
 * Модульные тесты для класса <code>SampleGenerator</code>
 */
public class SampleGeneratorTest {

    private final int[] NUMBERS_OF_POINTS = {50, 100, 250, 500, 1000, 2000};
    private SampleGenerator sampleGenerator;

    @Before
    public void setUp() {
        sampleGenerator = new SampleGeneratorImpl();
    }

    @Test
    public void testSeparability() {
        for(int count : NUMBERS_OF_POINTS) {
            sampleGenerator.regeneratePoints(count);

            ensurePointsAreCloserToFirstPoint(sampleGenerator.firstSetOfPoints(),
                    sampleGenerator.firstSupportingPoint(), sampleGenerator.secondSupportingPoint());
            ensurePointsAreCloserToFirstPoint(sampleGenerator.secondSetOfPoints(),
                    sampleGenerator.secondSupportingPoint(), sampleGenerator.firstSupportingPoint());
        }
    }

    private void ensurePointsAreCloserToFirstPoint(Set<Point> points, Point firstPoint, Point secondPoint) {
        int i = 0;

        for(Point point : points) {
            if(firstPoint.equals(nearestSupportingPoint(point, firstPoint, secondPoint))) {
                i++;
            }
        }

        Assert.assertTrue(i > (points.size() - i));
    }

    private Point nearestSupportingPoint(Point point, Point firstSupportingPoint, Point secondSupportingPoint) {
        double toFirstSupportingPoint = MathUtils.distance(point, firstSupportingPoint);
        double toSecondSupportingPoint = MathUtils.distance(point, secondSupportingPoint);

        return (toFirstSupportingPoint < toSecondSupportingPoint) ? firstSupportingPoint : secondSupportingPoint;
    }
}
