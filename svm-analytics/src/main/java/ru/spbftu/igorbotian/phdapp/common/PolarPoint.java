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
 * Точка в двумерном пространстве, заданная в полярных координатах.
 * Элемент выборки, используемой в ходе проведения анализа работы классификаторов.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 */
public class PolarPoint implements ClassifiedObject {

    /**
     * Обозначение класса, когда точка не принадлежит никакому классу
     */
    private static final String UNCLASSIFIED_CLASS_NAME = "Unclassified";

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
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Параметры данной точки как элемента выборки
     */
    private Set<Parameter<?>> params;

    /**
     * Создание точки, принадлежащей заданному классу, по полярным координатам
     *
     * @param r           полярный радиус (неотрицательный)
     * @param phi         полярный угол (в радианах)
     * @param dataFactory фабрика объектов предметной области
     * @throws java.lang.NullPointerException если класс или фабрика не заданы
     */
    public PolarPoint(double r, double phi, DataClass dataClass, DataFactory dataFactory) {
        if (r < 0) {
            throw new IllegalArgumentException("Polar radius cannot have a negative value");
        }

        this.r = r;
        this.phi = phi;
        this.dataClass = Objects.requireNonNull(dataClass);
        this.dataFactory = Objects.requireNonNull(dataFactory);
        this.params = Collections.unmodifiableSet(new HashSet<Parameter<?>>(Arrays.asList(
                dataFactory.newParameter(R_LABEL, r, BasicDataTypes.REAL),
                dataFactory.newParameter(PHI_LABEL, phi, BasicDataTypes.REAL)
        )));
    }

    /**
     * Создание точки, не принадлежащей ни одному классу, по полярным координатам
     *
     * @param r           полярный радиус (неотрицательный)
     * @param phi         полярный угол (в радианах)
     * @param dataFactory фабрика объектов предметной области
     * @throws java.lang.NullPointerException если фабрика объектов предметной области не задана
     */
    public PolarPoint(double r, double phi, DataFactory dataFactory) {
        this(r, phi, Objects.requireNonNull(dataFactory).newClass(UNCLASSIFIED_CLASS_NAME), dataFactory);
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
     *
     * @param radians угол (в радианах), на который будет повёрнута данная точка
     * @return полярные координаты точки, получившейся в результате поворота на заданный угол
     */
    public PolarPoint rotate(double radians) {
        return new PolarPoint(r, phi + radians, dataClass, dataFactory);
    }

    /**
     * Перевод полярных координат данной точки в Декартовы координаты
     *
     * @return данная точка, заданная в полярных координатах
     */
    public Point toCartesian() {
        return new Point(r * Math.cos(phi), r * Math.sin(phi), dataClass(), dataFactory);
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
        return String.format("(%.5f;%.5f;%s)", r, phi, dataClass);
    }
}
