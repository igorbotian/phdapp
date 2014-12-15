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

/**
 * Вспомогательный класс, который предоставляет возможность проводить различные математические операции
 */
public final class MathUtils {

    private MathUtils() {
        //
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
        if (fromMin > src || src > fromMax) {
            throw new IllegalArgumentException("The source value should be less then the lower bound " +
                    "and greater then the upper bound");
        }

        if(fromMax == fromMin) {
            throw new IllegalArgumentException("The source lower bound cannot be equal to the upper bound");
        }

        if (toMin > toMax) {
            throw new IllegalArgumentException("The target upper bound should be greater then the target lower bound");
        }

        return toMin + ((src - fromMin) / (fromMax - fromMin)) * (toMax - toMin);
    }
}
