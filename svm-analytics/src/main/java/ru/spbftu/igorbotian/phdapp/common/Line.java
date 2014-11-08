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

import java.util.Objects;

/**
 * Прямая в двумерном пространстве, задаваемая уравнением вида <code>Ax + Bx + C = 0</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 * @see <a href="https://en.wikipedia.org/wiki/Line_(geometry)">https://en.wikipedia.org/wiki/Line_(geometry)</a>
 */
public class Line {

    /**
     * Коэффициент A
     */
    private final double a;

    /**
     * Коэффициент B
     */
    private final double b;

    /**
     * Коэффициент С
     */
    private final double c;

    /**
     * Создание прямой по имеющимся декартовым координатам двух точек, лежащих на ней
     *
     * @param a декартовы координаты первой точки, лежащей на прямой
     * @param b декартовы координаты второй точки, лежащей на прямой
     * @throws java.lang.NullPointerException     если хотя бы одна из точек не задана
     * @throws java.lang.IllegalArgumentException если точки совпадают
     */
    public Line(Point a, Point b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        this.a = a.y() - b.y();
        this.b = b.x() - a.x();
        this.c = a.x() * b.y() - b.x() * a.y();
    }

    /**
     * Создание прямой по имеющимся значениям коэффициентов A, B, C
     *
     * @param a коэффициент A
     * @param b коэффициент B
     * @param c коэффициент C
     * @throws java.lang.IllegalArgumentException если коэффициенты A и B одновременно равны нулю
     */
    public Line(double a, double b, double c) {
        if (a == 0 && b == 0) {
            throw new IllegalArgumentException("A and coefficients cannot be equal to zero at the same time");
        }

        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Получение значения коэффициента A
     *
     * @return вещественное число
     */
    public double a() {
        return a;
    }

    /**
     * Получение значения коэффициента B
     *
     * @return вещественное число
     */
    public double b() {
        return b;
    }

    /**
     * Получение значения коэффициента C
     *
     * @return вещественное число
     */
    public double c() {
        return c;
    }

    /**
     * Вычисление значения параметра <code>Y</code> для заданного значения параметра <code>X</code>
     * @param x значение параметра <code>X</code>
     * @return вещественное число
     */
    public double y(double x) {
        return (- c - a * x) / b;
    }

    /**
     * Вычисление значения параметра <code>X</code> для заданного значения параметра <code>Y</code>
     * @param y значение параметра <code>Y</code>
     * @return вещественное число
     */
    public double x(double y) {
        return (-c - b * y) / a;
    }

    /**
     * Получение значения угла, под которым проходит данная прямая по отношению к оси абсцисс
     * @return вещественное число в диапазоне [-PI/2; PI/2]
     */
    public double angle() {
        return Math.atan(/* k = */ - a / b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Line)) {
            return false;
        }

        Line other = (Line) obj;
        return (a == other.a
                && b == other.b
                && c == other.c);
    }

    @Override
    public String toString() {
        return String.format("%.5f * x + %.5f * y + %.5f", a, b, c);
    }
}
