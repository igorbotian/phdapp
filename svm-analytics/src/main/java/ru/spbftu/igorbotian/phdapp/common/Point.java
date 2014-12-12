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

import java.util.*;

/**
 * Точка в двумерном пространстве, заданная в декартовых координатах.
 * Элемент выборки, используемой в ходе проведения анализа работы классификаторов.
 */
public class Point implements ClassifiedObject {

    /**
     * Обозначение класса, когда точка не принадлежит никакому классу
     */
    private static final String UNCLASSIFIED_CLASS_NAME = "Unclassified";

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
     * Фабрика создания объектов предметной области
     */
    private DataFactory dataFactory;

    /**
     * Создание точки в двумерном пространстве
     *
     * @param x           координата по оси абсцисс
     * @param y           координата по оси ординат
     * @param dataClass   класс, к которому принадлежит данная точка
     * @param dataFactory фабрика объектов предметной области
     * @throws java.lang.NullPointerException если класс или фабрика не заданы
     */
    public Point(double x, double y, DataClass dataClass, DataFactory dataFactory) {
        this.x = x;
        this.y = y;
        this.dataClass = Objects.requireNonNull(dataClass);
        this.dataFactory = Objects.requireNonNull(dataFactory);

        params = Collections.unmodifiableSet(new HashSet<Parameter<?>>(Arrays.asList(
                dataFactory.newParameter(X_DIMENSION_LABEL, x, BasicDataTypes.REAL),
                dataFactory.newParameter(Y_DIMENSION_LABEL, y, BasicDataTypes.REAL)
        )));
    }

    /**
     * Создание точки в двумерном пространстве
     *
     * @param x           координата по оси абсцисс
     * @param y           координата по оси ординат
     * @param dataFactory фабрика объектов предметной области
     * @throws java.lang.NullPointerException если фабрика объектов предметной области не задана
     */
    public Point(double x, double y, DataFactory dataFactory) {
        this(x, y, Objects.requireNonNull(dataFactory).newClass(UNCLASSIFIED_CLASS_NAME), dataFactory);
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
        return new Point(x + dx, y + dy, dataClass, dataFactory);
    }

    /**
     * Перевод Декартовых координат данной точки в полярные координаты
     *
     * @return заданная точка в полярных координатах
     */
    public PolarPoint toPolar() {
        double x = x();
        double y = y();
        double r = Math.sqrt(x * x + y * y);
        double phi;

        if (x > 0 && y >= 0) {
            phi = Math.atan(y / x);
        } else if (x > 0 && y < 0) {
            phi = Math.atan(y / x) + 2 * Math.PI;
        } else if (x < 0) {
            phi = Math.atan(y / x) + Math.PI;
        } else if (x == 0 && y > 0) {
            phi = Math.PI / 2;
        } else if (x == 0 && y < 0) {
            phi = 3 * Math.PI / 2;
        } else { // x == 0 && y == 0
            r = 0;
            phi = 0;
        }

        return new PolarPoint(r, phi, dataFactory);
    }

    /**
     * Вычисление расстояния от данной до задачнной точки
     *
     * @param b вторая точка
     * @return положительное вещественное число
     * @throws java.lang.NullPointerException если вторая точка не задана
     */
    public double distanceTo(Point b) {
        Objects.requireNonNull(b);

        return Math.sqrt(Math.pow(Math.abs(b.x() - x()), 2.0) + Math.pow(Math.abs(b.y() - y()), 2.0));
    }

    @Override
    public DataClass dataClass() {
        return dataClass;
    }

    @Override
    public String id() {
        return String.format("(%.5f;%.5f;%s)", x, y, dataClass.toString());
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
