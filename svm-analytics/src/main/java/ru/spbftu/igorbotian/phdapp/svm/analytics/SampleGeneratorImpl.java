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

import ru.spbftu.igorbotian.phdapp.common.Line;
import ru.spbftu.igorbotian.phdapp.common.Point;
import ru.spbftu.igorbotian.phdapp.common.PolarPoint;
import ru.spbftu.igorbotian.phdapp.common.Range;
import ru.spbftu.igorbotian.phdapp.svm.analytics.math.ExponentialRandom;
import ru.spbftu.igorbotian.phdapp.svm.analytics.math.MathUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.SampleGenerator
 */
class SampleGeneratorImpl implements SampleGenerator {

    /**
     * Количество случайных точек по умолчанию, которое необходимо сгенерировать вокруг каждой опорной точки
     */
    private static final int DEFAULT_POINTS_COUNT = 50;

    /**
     * Расстояние между двумя опорными точками
     */
    private static final double distanceBetweenSupportingPoints = 10.0;

    /**
     * Радиус разброса случайных чисел вокруг каждой опорной точки
     */
    private static final double dispersionRadius = 1.1 * distanceBetweenSupportingPoints;

    private final Point firstPoint = new Point(distanceBetweenSupportingPoints, distanceBetweenSupportingPoints,
            FIRST_SET_OF_POINTS);
    private final Point secondPoint = new Point(2 * distanceBetweenSupportingPoints, 2 * distanceBetweenSupportingPoints,
            SECOND_SET_OF_POINTS);
    private final Line separatingLine = determineSeparatingLine(firstPoint, secondPoint);
    private int numberOfPoints;
    private Set<Point> firstSet = new HashSet<>();
    private Set<Point> secondSet = new HashSet<>();

    /*
     * 1. Вычисляем середину отрезка, соединяющего опорные точки
     * 2. Переносим туда начало координат
     * 3. Переводим любую из опорных точек в полярную систему координат
     * 4. Поворачиваем её на 90 градусов
     * 5. Получившуюся точку переводим в Декартовы координаты
     * 6. Прямая, разделяющая два множества будет проходить через найденную середину и вычисленную точку
     */
    private static Line determineSeparatingLine(Point a, Point b) {
        Point middle = new Point((a.x() + b.x()) / 2, (a.y() + b.y()) / 2);
        Point translatedB = b.shift(-middle.x(), -middle.y());
        PolarPoint rotatedPolarB = MathUtils.toPolar(translatedB).rotate(Math.PI / 2);
        Point pointOnSeparatingLine = MathUtils.toDecart(rotatedPolarB).shift(middle.x(), middle.y());

        return new Line(middle, pointOnSeparatingLine);
    }

    public SampleGeneratorImpl() {
        regeneratePoints(DEFAULT_POINTS_COUNT);
    }

    @Override
    public synchronized void regeneratePoints(int count) {
        secondSet.clear();
        firstSet.clear();
        firstSet = generateRandomPoints(count, firstPoint);
        secondSet = generateRandomPoints(count, secondPoint);
        numberOfPoints = count;
    }

    private Set<Point> generateRandomPoints(int count, Point supportingPoint) {
        Set<PolarPoint> polarPoints = new HashSet<>();
        int i = 0;

        while (i < count) {
            if (polarPoints.add(new PolarPoint(
                    ExponentialRandom.nextDouble(0.0, dispersionRadius),
                    ExponentialRandom.nextDouble(0.0, 2 * Math.PI)))) {
                i++;
            }
        }

        Set<Point> result = new HashSet<>();

        for (PolarPoint polarPoint : polarPoints) {
            Point decartPoint = MathUtils.toDecart(polarPoint);
            result.add(new Point(decartPoint.x() + supportingPoint.x(), decartPoint.y() + supportingPoint.y()));
        }

        return result;
    }

    @Override
    public synchronized int numberOfPoints() {
        return numberOfPoints;
    }

    @Override
    public Range<Double> xCoordinateRange() {
        return new Range<>(0.0, distanceBetweenSupportingPoints + 2 * dispersionRadius, Double::compare);
    }

    @Override
    public Range<Double> yCoordinateRange() {
        return new Range<>(0.0, distanceBetweenSupportingPoints + 2 * dispersionRadius, Double::compare);
    }

    @Override
    public Line separatingLine() {
        return separatingLine;
    }

    @Override
    public Point firstSupportingPoint() {
        return firstPoint;
    }

    @Override
    public Point secondSupportingPoint() {
        return secondPoint;
    }

    @Override
    public synchronized Set<Point> firstSetOfPoints() {
        return Collections.unmodifiableSet(firstSet);
    }

    @Override
    public synchronized Set<Point> secondSetOfPoints() {
        return Collections.unmodifiableSet(secondSet);
    }
}