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

import ru.spbftu.igorbotian.phdapp.common.DataClass;
import ru.spbftu.igorbotian.phdapp.common.Line;
import ru.spbftu.igorbotian.phdapp.common.Point;
import ru.spbftu.igorbotian.phdapp.common.Range;
import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.Set;

/**
 * Генерация точек в двумерном пространстве по заданному правилу ниже.
 * Задаются две опорные точки, вокруг каждой из которых генерируются точки в случайном порядке таким образом, чтобы
 * большинство точек, генерируемых вокруг одной опорной точки, было ближе к именно к ней, нежели чем к другой.
 * Таким образом, эти два множества должны быть разделимы.
 * Сгенерированная таким образом выборка затем используется для проверки корректности работы классификатора.
 */
public interface SampleGenerator {

    /**
     * Класс, соответствующий множеству точек вокруг первой опорной точки
     */
    public static DataClass FIRST_SET_OF_POINTS = DataFactory.newClass("FirstSetOfObjects");

    /**
     * Класс, соответствующий множеству точек вокруг второй опорной точки
     */
    public static DataClass SECOND_SET_OF_POINTS = DataFactory.newClass("FirstSetOfObjects");

    /**
     * Регенерация случайно разбросанных точек вокруг опорных точек.
     *
     * @param count количество точек, которые будут сгенерированы вокруг каждой из опорных точек
     * @throws java.lang.IllegalArgumentException если значение количества точек имеет неположительное значение
     */
    void regeneratePoints(int count);

    /**
     * Получение количества случайных точек, которое сгенерировано вокруг каждой из опорных точек
     *
     * @return целое положительное число
     */
    int numberOfPoints();

    /**
     * Полученение пределов по оси абсцисс, в которых генерируются случайные точки и содержатся опорные точки
     *
     * @return диапазон
     */
    Range<Double> xCoordinateRange();

    /**
     * Получение пределов по оси ординат, в которых генерируются случайные точки и содержатся опорные точки
     *
     * @return диапазон
     */
    Range<Double> yCoordinateRange();

    /**
     * Получение линии, разделяющей два множества случайно сгенерированных точек.
     * Если опорные точки соединить отрезком, то разделяющая линия будет проходить перпендикулярно по центру этого отрезка.
     *
     * @return линия, разделяющая множества случайно сгенерированных точек
     */
    Line separatingLine();

    /**
     * Получение координаты первой опорной точки
     *
     * @return координата первой точки
     */
    Point firstSupportingPoint();

    /**
     * Получение координаты второй опорной точки
     *
     * @return координата второй точки
     */
    Point secondSupportingPoint();

    /**
     * Получение точек, сгенерированных случайным образом вокруг первой опорной точки
     *
     * @return непустое множество точек
     */
    Set<Point> firstSetOfPoints();

    /**
     * Получение точек, сгенерированных случайным образом вокруг второй опорной точки
     *
     * @return непустое множество точек
     */
    Set<Point> secondSetOfPoints();
}
