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

import ru.spbftu.igorbotian.phdapp.common.Range;

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
    private static final double LAMBDA = 2.5;

    /**
     * Количество прямоугольников, на которое будет разбит график обратной функции.
     * Чем больше, тем лучше окончательный результат.
     */
    private static final int CHUNK_COUNT = 10000;

    /**
     * Левая граница по оси абсцисс диапазона обратной функции, в котором будут генерироваться случайные числа.
     * В заданном диапазоне располагается наибольший разброс значений экспоненциальной функции.
     */
    private static final double X_LOWER_BOUND = 0.0;

    /**
     * Правая граница по оси абсцисс диапазона обратной функции, в котором будут генерироваться случайные числа.
     * В заданном диапазоне располагается наибольший разброс значений экспоненциальной функции.
     */
    private static final double X_UPPER_BOUND = 4.0;

    private ExponentialRandom() {
        //
    }

    /**
     * Генерация заданного количества случайных чисел в заданном диапазоне
     *
     * @param count количество чисел, которое необходимо сгенерировать
     * @param min   нижняя граница диапазона, в котором будут сгенерированы случайные числа (граничное значение включено)
     * @param max   верхняя граница диапазона, в котором будут сгенерированы случайные числа (граничное значение включено)
     * @return массив случайных чисел
     * @throws java.lang.IllegalArgumentException если количество чисел является неположительным числом;
     *                                            если значение верхней границы меньше нижней
     */
    public static int[] nextIntegers(int count, int min, int max) {
        return nextIntegers(count, new Range<>(min, max, Integer::compare));
    }

    /**
     * Генерация заданного количества случайных чисел в заданном диапазоне
     *
     * @param count количество чисел, которое необходимо сгенерировать
     * @param range   диапазон, в котором будут сгенерированы случайные числа (граничные значения включены)
     * @return массив случайных чисел
     * @throws java.lang.IllegalArgumentException если количество чисел является неположительным числом;
     *                                            если значение верхней границы меньше нижней
     */
    public static int[] nextIntegers(int count, Range<Integer> range) {
        if(count <= 0) {
            throw new IllegalArgumentException("Required random number count should have a positive value");
        }

        double[] data = generateData(CHUNK_COUNT);
        int[] results = createHistogram(data, count, X_LOWER_BOUND, X_UPPER_BOUND);

        for (int i = 0; i < results.length; i++) {
            results[i] = range.lowerBound() + results[i] % (range.upperBound() - range.lowerBound() + 1);
        }

        return results;
    }

    private static int[] createHistogram(double[] data, int buckets, double min, double max) {
        assert data != null;
        assert data.length > 0;
        assert buckets > 0;
        assert max > min;

        int[] results = new int[buckets];
        double divisor = (max - min) / buckets;

        for (double datum : data) {
            int index = (int) ((datum - min) / divisor);

            if (0.0 <= index && index < buckets) {
                results[index] += 1;
            }
        }

        return results;
    }

    private static double[] generateData(int chunkCount) {
        assert (chunkCount > 0);

        double[] data = new double[chunkCount];

        for (int i = 0; i < chunkCount; i++) {
            data[i] = exponentialQuantile(random.nextDouble(), LAMBDA);
        }

        return data;
    }

    private static double exponentialQuantile(double x, double lambda) {
        assert (0.0 <= x && x < 1.0);
        assert (lambda > 0.0);

        return (-1 / lambda) * Math.log(1 - x);
    }
}
