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

package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import ru.spbftu.igorbotian.phdapp.common.ClassifiedObject;

/**
 * Точка в двумерном пространстве, заданная в декартовых координатах.
 * Элемент выборки, используемой в ходе проведения анализа работы классификаторов.
 */
public interface Point extends ClassifiedObject {

    /**
     * Получение координаты по оси абсцисс
     *
     * @return целое число
     */
    double x();

    /**
     * Получение координаты по оси ординат
     *
     * @return целое число
     */
    double y();

    /**
     * Смещение данной точки на заданные расстояния по каждой из координат
     *
     * @param dx смещение по оси асбцисс
     * @param dy смещение по оси ординат
     * @return точка, получившаяюся в результате заданного смещения по оси абсцисс и ординат
     */
    Point shift(double dx, double dy);

    /**
     * Перевод Декартовых координат данной точки в полярные координаты
     *
     * @return заданная точка в полярных координатах
     */
    PolarPoint toPolar();

    /**
     * Вычисление расстояния от данной до задачнной точки
     *
     * @param b вторая точка
     * @return положительное вещественное число
     * @throws java.lang.NullPointerException если вторая точка не задана
     */
    double distanceTo(Point b);
}
