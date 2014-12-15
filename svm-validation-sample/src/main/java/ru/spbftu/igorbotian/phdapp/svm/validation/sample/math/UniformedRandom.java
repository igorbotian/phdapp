package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import java.util.Random;

/**
 * Механизм генерации случайных чисел по нормальному распределению
 *
 * @see <a href="http://en.wikipedia.org/wiki/Normal_distribution">http://en.wikipedia.org/wiki/Normal_distribution</a>
 */
public final class UniformedRandom {

    private static final Random random = new Random(System.currentTimeMillis());

    private UniformedRandom() {
        //
    }

    /**
     * Получения случайного вещественного числа, сгенерированного по нормальному распределению
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

        double upperBound = max - min;
        double uniformedRandom = MathUtils.translate(random.nextDouble(), 0.0, 1.0, 0.0, upperBound);
        return min + uniformedRandom % upperBound;
    }
}
