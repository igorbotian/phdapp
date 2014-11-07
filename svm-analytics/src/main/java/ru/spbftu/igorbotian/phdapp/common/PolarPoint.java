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
 * Точка в двумерном пространстве, заданная в полярных координатах.
 * Элемент выборки, используемой в ходе проведения анализа работы классификаторов.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 */
public class PolarPoint implements ClassifiedObject {

    /**
     * Точка, не относящаяся ни к одному классу
     */
    public static final DataClass UNCLASSIFIED = DataFactory.newClass("unclassified");

    /**
     * Обозначение радиуса
     */
    private static final String R_LABEL = "R";

    /**
     * Обозначение угла наклона
     */
    private static final String PHI_LABEL = "Phi";

    /**
     * Полярный радиус
     */
    private final double r;

    /**
     * Полярный угол (в радианах)
     */
    private final double phi;

    /**
     * Класс, которому принадлежит данная точка
     */
    private final DataClass dataClass;

    /**
     * Параметры данной точки как элемента выборки
     */
    private Set<Parameter<?>> params;

    /**
     * Создание точки, принадлежащей заданному классу, по полярным координатам
     *
     * @param r   полярный радиус (неотрицательный)
     * @param phi полярный угол (в радианах)
     */
    public PolarPoint(double r, double phi, DataClass dataClass) {
        if (r < 0) {
            throw new IllegalArgumentException("Polar radius cannot have a negative value");
        }

        if (dataClass == null) {
            throw new NullPointerException("Data class cannot be null");
        }

        this.r = r;
        this.phi = phi;
        this.dataClass = dataClass;
        this.params = Collections.unmodifiableSet(new HashSet<Parameter<?>>(Arrays.asList(
                DataFactory.newParameter(R_LABEL, r, BasicDataTypes.REAL),
                DataFactory.newParameter(PHI_LABEL, phi, BasicDataTypes.REAL)
        )));
    }

    /**
     * Создание точки, не принадлежащей ни одному классу, по полярным координатам
     *
     * @param r   полярный радиус (неотрицательный)
     * @param phi полярный угол (в радианах)
     */
    public PolarPoint(double r, double phi) {
        this(r, phi, UNCLASSIFIED);
    }

    /**
     * Получение полярного радиуса
     *
     * @return неотрицательное вещественное число
     */
    public double r() {
        return r;
    }

    /**
     * Получение полярного угла (в радианах)
     *
     * @return вещественное число
     */
    public double phi() {
        return phi;
    }

    @Override
    public String id() {
        return toString();
    }

    @Override
    public DataClass dataClass() {
        return dataClass;
    }

    /**
     * Поворот данной точки на заданный угол
     * @param radians угол (в радианах), на который будет повёрнута данная точка
     * @return полярные координаты точки, получившейся в результате поворота на заданный угол
     */
    public PolarPoint rotate(double radians) {
        return new PolarPoint(r, phi + radians, dataClass);
    }

    /**
     * Перевод полярных координат данной точки в Декартовы координаты
     *
     * @return данная точка, заданная в Декартовых координатах
     */
    public Point toDecart() {
        return new Point(r * Math.cos(phi), r * Math.sin(phi), dataClass);
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return params;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, phi, dataClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof PolarPoint)) {
            return false;
        }

        PolarPoint other = (PolarPoint) obj;
        return (r == other.r
                && phi == other.phi
                && dataClass.equals(other.dataClass));
    }

    @Override
    public String toString() {
        return UNCLASSIFIED.equals(dataClass)
                ? String.format("(%.5f;%.5f)", r, phi)
                : String.format("(%.5f;%.5f;%s)", r, phi, dataClass);
    }
}
