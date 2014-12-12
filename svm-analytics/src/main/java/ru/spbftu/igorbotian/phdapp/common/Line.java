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

/**
 * Прямая в двумерном пространстве, задаваемая уравнением вида <code>Ax + Bx + C = 0</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 * @see <a href="https://en.wikipedia.org/wiki/Line_(geometry)">https://en.wikipedia.org/wiki/Line_(geometry)</a>
 */
public interface Line {

    /**
     * Получение значения коэффициента A
     *
     * @return вещественное число
     */
    double a();

    /**
     * Получение значения коэффициента B
     *
     * @return вещественное число
     */
    double b();

    /**
     * Получение значения коэффициента C
     *
     * @return вещественное число
     */
    double c();

    /**
     * Вычисление значения параметра <code>Y</code> для заданного значения параметра <code>X</code>
     *
     * @param x значение параметра <code>X</code>
     * @return вещественное число
     */
    double y(double x);

    /**
     * Вычисление значения параметра <code>X</code> для заданного значения параметра <code>Y</code>
     *
     * @param y значение параметра <code>Y</code>
     * @return вещественное число
     */
    double x(double y);

    /**
     * Получение значения угла, под которым проходит данная прямая по отношению к оси абсцисс
     *
     * @return вещественное число в диапазоне [-PI/2; PI/2]
     */
    double angle();
}
