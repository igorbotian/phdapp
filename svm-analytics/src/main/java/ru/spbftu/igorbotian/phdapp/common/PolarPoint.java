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
 * Точка в двумерном пространстве, заданная в полярных координатах.
 * Элемент выборки, используемой в ходе проведения анализа работы классификаторов.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.Point
 */
public interface PolarPoint extends ClassifiedObject {

    /**
     * Получение полярного радиуса
     *
     * @return неотрицательное вещественное число
     */
    double r();

    /**
     * Получение полярного угла (в радианах)
     *
     * @return вещественное число
     */
    double phi();

    /**
     * Поворот данной точки на заданный угол
     *
     * @param radians угол (в радианах), на который будет повёрнута данная точка
     * @return полярные координаты точки, получившейся в результате поворота на заданный угол
     */
    PolarPoint rotate(double radians);

    /**
     * Перевод полярных координат данной точки в Декартовы координаты
     *
     * @return данная точка, заданная в полярных координатах
     */
    Point toCartesian();
}
