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

package ru.spbftu.igorbotian.phdapp.svm.analytics.math;

import ru.spbftu.igorbotian.phdapp.common.Point;
import ru.spbftu.igorbotian.phdapp.common.PolarPoint;

import java.util.Objects;

/**
 * Вспомогательный класс, который предоставляет возможность проводить различные математические операции
 */
public final class MathUtils {

    private MathUtils() {
        //
    }

    /**
     * Перевод Декартовых координат заданной точки в полярные координаты
     *
     * @param point точка, заданная в Декартовых координатах
     * @return заданная точка в полярных координатах
     * @throws java.lang.NullPointerException если точка не задана
     */
    public static PolarPoint toPolar(Point point) {
        Objects.requireNonNull(point);

        double x = point.x();
        double y = point.y();
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

        return new PolarPoint(r, phi);
    }

    /**
     * Перевод полярных координат заданной точки в Декартовы координаты
     *
     * @param point точка, заданная в полярных координатах
     * @return данная точка, заданная в полярных координатах
     * @throws java.lang.NullPointerException если точка не задана
     */
    public static Point toDecart(PolarPoint point) {
        Objects.requireNonNull(point);

        double r = point.r();
        double phi = point.phi();

        return new Point(r * Math.cos(phi), r * Math.sin(phi), point.dataClass());
    }

    /**
     * Вычисление расстояния между двумя точками, заданными в Декартовых координатах
     *
     * @param a первая точка
     * @param b вторая точка
     * @return положительное вещественное число
     * @throws java.lang.NullPointerException если хотя бы одна из точек не задана
     */
    public static double distance(Point a, Point b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        return Math.sqrt(Math.pow(Math.abs(b.x() - a.x()), 2.0) + Math.pow(Math.abs(b.y() - a.y()), 2.0));
    }

    /**
     * Переход от исходного значения, заданного в определённых пределах, к соответствующему, заданному в новых пределах
     *
     * @param src     исходное значение
     * @param fromMin исходный нижний предел
     * @param fromMax исходный верхний предел
     * @param toMin   новый нижний предел
     * @param toMax   новый верхний предел
     * @return значение, которое получено путём перехода исходного от одних пределов к другим
     * @throws java.lang.IllegalArgumentException если исходное значение меньше нижнего или больше верхнего предела;
     *                                            если новый нижний предел имеет большее значение, чем новый верхний предел
     */
    public static double translate(double src, double fromMin, double fromMax, double toMin, double toMax) {
        if (fromMin >= src || src >= fromMax) {
            throw new IllegalArgumentException("The source value should be less then the lower bound " +
                    "and greater then the upper bound");
        }

        if (toMin > toMax) {
            throw new IllegalArgumentException("The target upper bound should be greater then the target lower bound");
        }

        return toMin + (Math.abs(src - fromMin) / Math.abs(fromMax - fromMin)) * Math.abs(toMax - toMin);
    }
}
