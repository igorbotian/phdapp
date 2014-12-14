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

import java.util.Random;

/**
 * Механизм генерации случайных чисел по экспоненциальному распределению
 *
 * @see <a href="https://en.wikipedia.org/wiki/Exponential_distribution">https://en.wikipedia.org/wiki/Exponential_distribution</a>
 */
public final class ExponentialRandom {

    private static final Random random = new Random(System.currentTimeMillis());

    /**
     * Значение интенсивности (или обратного коэффициента масштаба)
     */
    private static final double LAMBDA = 1.0;
    private static final double LOG_LAMBDA = Math.log(LAMBDA);

    /**
     * Левая граница по оси абсцисс диапазона обратной функции, в котором будут генерироваться случайные числа.
     * В заданном диапазоне располагается наибольший разброс значений экспоненциальной функции.
     */
    private static final double X_LOWER_BOUND = 0.0;

    /**
     * Правая граница по оси абсцисс диапазона обратной функции, в котором будут генерироваться случайные числа.
     * В заданном диапазоне располагается наибольший разброс значений экспоненциальной функции.
     */
    private static final double X_UPPER_BOUND = 2.0;

    private ExponentialRandom() {
        //
    }

    /**
     * Получения случайного вещественного числа, сгенерированного по экспоненциальному распределению
     *
     * @param min нижний предел для генерируемых чисел
     * @param max верхний предел для генерируемых чисел
     * @return вещественное число, не меньшее нижнего предела и не большее верхнего предела
     * @throws java.lang.IllegalArgumentException если нижний предел по значению больше верхнего
     */
    public static double nextDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("The lower bound cannot be greater than the upper bound");
        }

        double upperBound = Math.abs(max - min);
        double uniformedRandom = MathUtils.translate(random.nextDouble(), 0.0, 1.0, X_LOWER_BOUND, X_UPPER_BOUND);
        double nonUniformedRandom = MathUtils.translate(density(uniformedRandom), 0.0, LAMBDA, 0.0, upperBound);
        return min + nonUniformedRandom % (max - min);
    }

    private static double density(double x) {
        assert (x >= 0.0);

        double logDensity = logDensity(x);
        return (logDensity == Double.NEGATIVE_INFINITY ? 0 : Math.exp(logDensity));
    }

    private static double logDensity(double x) {
        if (x < 0) {
            return Double.NEGATIVE_INFINITY;
        }

        return -x / LAMBDA - LOG_LAMBDA;
    }
}
