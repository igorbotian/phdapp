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

package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Line;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;

import java.nio.file.Paths;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Модульные тесты для класса <code>CrossValidationSampleGenerator</code>
 */
public class CrossValidationSampleGeneratorTest {

    private final int[] NUMBERS_OF_POINTS = {50, 100, 250, 500, 1000, 2000};
    private CrossValidationSampleGenerator sampleGenerator;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(
                new ApplicationConfigurationModule(Paths.get(".")),
                new DataModule(),
                new SvmValidationSampleManagementModule()
        );

        sampleGenerator = injector.getInstance(CrossValidationSampleGenerator.class);
    }

    @Test
    public void testNumberOfPoints() {
        for (int count : NUMBERS_OF_POINTS) {
            sampleGenerator.regeneratePoints(count);
            Assert.assertEquals(count, sampleGenerator.numberOfPoints());
            Assert.assertEquals(count / 2, sampleGenerator.firstSetOfPoints().size());
            Assert.assertEquals(count / 2, sampleGenerator.secondSetOfPoints().size());
        }
    }

    @Test
    public void testSeparability() {
        for (int count : NUMBERS_OF_POINTS) {
            sampleGenerator.regeneratePoints(count);

            ensurePointsAreCloserToFirstPoint(sampleGenerator.firstSetOfPoints(),
                    sampleGenerator.firstSupportingPoint(), sampleGenerator.secondSupportingPoint());
            ensurePointsAreCloserToFirstPoint(sampleGenerator.secondSetOfPoints(),
                    sampleGenerator.secondSupportingPoint(), sampleGenerator.firstSupportingPoint());
        }
    }

    private void ensurePointsAreCloserToFirstPoint(Set<Point> points, Point firstPoint, Point secondPoint) {
        int i = 0;

        for (Point point : points) {
            if (firstPoint.equals(nearestSupportingPoint(point, firstPoint, secondPoint))) {
                i++;
            }
        }

        Assert.assertTrue(i > (points.size() - i));
    }

    private Point nearestSupportingPoint(Point point, Point firstSupportingPoint, Point secondSupportingPoint) {
        double toFirstSupportingPoint = point.distanceTo(firstSupportingPoint);
        double toSecondSupportingPoint = point.distanceTo(secondSupportingPoint);

        return (toFirstSupportingPoint < toSecondSupportingPoint) ? firstSupportingPoint : secondSupportingPoint;
    }

    @Test
    public void testSeparatingLine() {
        for (int count : NUMBERS_OF_POINTS) {
            sampleGenerator.regeneratePoints(count);
            ensureLineSeparatesMostOfThePoints(sampleGenerator.firstSetOfPoints(),
                    sampleGenerator.separatingLine(), CrossValidationSampleGeneratorTest::isLeftOrUnder);
            ensureLineSeparatesMostOfThePoints(sampleGenerator.secondSetOfPoints(),
                    sampleGenerator.separatingLine(), CrossValidationSampleGeneratorTest::isRightOrAbove);
        }
    }

    private void ensureLineSeparatesMostOfThePoints(Set<Point> points, Line separatingLine,
                                                    BiFunction<Point, Line, Boolean> checker) {
        int i = 0;

        for (Point point : points) {
            if (checker.apply(point, separatingLine)) {
                i++;
            }
        }

        Assert.assertTrue(i > (points.size() - i));
    }

    private static boolean isLeftOrUnder(Point point, Line line) {
        double x = line.x(point.y());

        if (point.x() < x) { // левее
            return true;
        } else if (point.x() > x) { // правее
            return false;
        } else { // горизонтальная линия
            double y = line.y(point.x());

            return (point.y() <= y); // ниже
        }
    }

    private static boolean isRightOrAbove(Point point, Line line) {
        double x = line.x(point.y());

        if (point.x() > x) { // правее
            return true;
        } else if (point.x() < x) { // левее
            return false;
        } else { // горизонтальная линия
            double y = line.y(point.x());

            return (point.y() >= y); // выше
        }
    }
}
