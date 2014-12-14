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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.svm.analytics.math.ExponentialRandom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.SampleGenerator
 */
@Singleton
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

    /**
     * Фабрика математических примитивов, используемых в кросс-валидации классификатора
     */
    private final MathDataFactory mathDataFactory;

    /**
     * Первая опорная точка
     */
    private final Point firstPoint;

    /**
     * Вторая опорная точка
     */
    private final Point secondPoint;

    /**
     * Прямая, проходящая через опорные точки
     */
    private final Line separatingLine;

    /**
     * Общее количество генерируемых точек
     */
    private int numberOfPoints;

    /**
     * Множество точек, генерируемых вокруг первой опорной точки
     */
    private Set<Point> firstSet = new HashSet<>();

    /**
     * Множество точек, генерируемых вокруг второй опорной точки
     */
    private Set<Point> secondSet = new HashSet<>();

    @Inject
    public SampleGeneratorImpl(DataFactory dataFactory, MathDataFactory mathDataFactory) {
        Objects.requireNonNull(dataFactory);
        Objects.requireNonNull(mathDataFactory);

        this.mathDataFactory = mathDataFactory;

        firstPoint = mathDataFactory.newPoint(dispersionRadius, dispersionRadius,
                dataFactory.newClass("FirstSetOfObjects"));
        secondPoint = mathDataFactory.newPolarPoint(
                firstPoint.toPolar().r() + distanceBetweenSupportingPoints,
                firstPoint.toPolar().phi(),
                dataFactory.newClass("SecondSetOfObjects")
        ).toCartesian();

        separatingLine = determineSeparatingLine(firstPoint, secondPoint);

        regeneratePoints(DEFAULT_POINTS_COUNT);
    }

    /*
     * 1. Вычисляем середину отрезка, соединяющего опорные точки
     * 2. Переносим туда начало координат
     * 3. Переводим любую из опорных точек в полярную систему координат
     * 4. Поворачиваем её на 90 градусов
     * 5. Получившуюся точку переводим в Декартовы координаты
     * 6. Прямая, разделяющая два множества будет проходить через найденную середину и вычисленную точку
     */
    private Line determineSeparatingLine(Point a, Point b) {
        Point middle = mathDataFactory.newPoint((a.x() + b.x()) / 2, (a.y() + b.y()) / 2);
        Point translatedB = b.shift(-middle.x(), -middle.y());
        PolarPoint rotatedPolarB = translatedB.toPolar().rotate(Math.PI / 2);
        Point pointOnSeparatingLine = rotatedPolarB.toCartesian().shift(middle.x(), middle.y());

        return mathDataFactory.newLine(middle, pointOnSeparatingLine);
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
            if (polarPoints.add(mathDataFactory.newPolarPoint(
                    ExponentialRandom.nextDouble(0.0, dispersionRadius),
                    ExponentialRandom.nextDouble(0.0, 2 * Math.PI)))) {
                i++;
            }
        }

        Set<Point> result = new HashSet<>();

        for (PolarPoint polarPoint : polarPoints) {
            Point cartesianPoint = polarPoint.toCartesian();
            result.add(mathDataFactory.newPoint(cartesianPoint.x() + supportingPoint.x(),
                    cartesianPoint.y() + supportingPoint.y()
            ));
        }

        return result;
    }

    @Override
    public synchronized int numberOfPoints() {
        return numberOfPoints;
    }

    @Override
    public Range<Double> xCoordinateRange() {
        return mathDataFactory.newRange(0.0, Math.max(firstPoint.x(),
                secondPoint.x()) + dispersionRadius, Double::compare);
    }

    @Override
    public Range<Double> yCoordinateRange() {
        return mathDataFactory.newRange(0.0, Math.max(firstPoint.y(),
                secondPoint.y()) + dispersionRadius, Double::compare);
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
