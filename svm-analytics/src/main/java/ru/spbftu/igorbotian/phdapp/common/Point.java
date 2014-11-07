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

import ru.spbftu.igorbotian.phdapp.common.impl.DataFactory;

import java.util.*;

/**
 * Точка в двумерном пространстве, заданная в декартовых координатах.
 * Элемент выборки, используемой в ходе проведения анализа работы классификаторов.
 */
public class Point implements ClassifiedObject {

    /**
     * Точка, не относящаяся ни к одному классу
     */
    public static final DataClass UNCLASSIFIED = DataFactory.newClass("unclassified");

    /**
     * Обозначение оси ординат
     */
    private static final String X_DIMENSION_LABEL = "X";

    /**
     * Обозначение оси ординат
     */
    private static final String Y_DIMENSION_LABEL = "Y";

    /**
     * Координата по оси абсцисс
     */
    private final double x;

    /**
     * Координата по оси ординат
     */
    private final double y;

    /**
     * Параметры точки как неклассифицированного объекта
     */
    private final Set<Parameter<?>> params;

    /**
     * Класс, которому принадлежит данная точка
     */
    private DataClass dataClass;

    /**
     * Создание точки в двумерном пространстве
     *
     * @param x         координата по оси абсцисс
     * @param y         координата по оси ординат
     * @param dataClass класс, к которому принадлежит данная точка
     * @throws java.lang.NullPointerException если класс не задан
     */
    public Point(double x, double y, DataClass dataClass) {
        if (dataClass == null) {
            throw new NullPointerException("Data class cannot be null");
        }

        this.x = x;
        this.y = y;
        this.dataClass = dataClass;

        params = Collections.unmodifiableSet(new HashSet<Parameter<?>>(Arrays.asList(
                DataFactory.newParameter(X_DIMENSION_LABEL, x, BasicDataTypes.REAL),
                DataFactory.newParameter(Y_DIMENSION_LABEL, y, BasicDataTypes.REAL)
        )));
    }

    /**
     * Создание точки в двумерном пространстве
     *
     * @param x координата по оси абсцисс
     * @param y координата по оси ординат
     */
    public Point(double x, double y) {
        this(x, y, UNCLASSIFIED);
    }

    /**
     * Получение координаты по оси абсцисс
     *
     * @return целое число
     */
    public double x() {
        return x;
    }

    /**
     * Получение координаты по оси ординат
     *
     * @return целое число
     */
    public double y() {
        return y;
    }

    /**
     * Смещение данной точки на заданные расстояния по каждой из координат
     *
     * @param dx смещение по оси асбцисс
     * @param dy смещение по оси ординат
     * @return точка, получившаяюся в результате заданного смещения по оси абсцисс и ординат
     */
    public Point shift(double dx, double dy) {
        return new Point(x + dx, y + dy, dataClass);
    }

    @Override
    public DataClass dataClass() {
        return dataClass;
    }

    @Override
    public String id() {
        return UNCLASSIFIED.equals(dataClass)
                ? String.format("(%.5f;%.5f", x, y)
                : String.format("(%.5f;%.5f;%s)", x, y, dataClass.toString());
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return params;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dataClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Point)) {
            return false;
        }

        Point other = (Point) obj;
        return (x == other.x && y == other.y && dataClass.equals(other.dataClass));
    }

    @Override
    public String toString() {
        return id();
    }
}
