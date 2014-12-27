package ru.spbftu.igorbotian.phdapp.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            throw new IllegalArgumentException(String.format("A lower bound (%.5f) cannot be greater " +
                    "than an upper bound (%.5f)", min, max));
        }

        double upperBound = max - min;
        double uniformedRandom = MathUtils.translate(random.nextDouble(), 0.0, 1.0, 0.0, upperBound);
        return min + uniformedRandom % upperBound;
    }

    /**
     * Получения случайного целого числа, сгенерированного по нормальному распределению
     *
     * @param min нижний предел для генерируемых чисел
     * @param max верхний предел для генерируемых чисел
     * @return вещественное число, не меньшее нижнего предела и не большее верхнего предела
     * @throws java.lang.IllegalArgumentException если нижний предел по значению больше верхнего
     */
    public static int nextInteger(int min, int max) {
        if (min - max > 0) {
            throw new IllegalArgumentException(String.format("A lower bound (%d) cannot be greater " +
                    "than an upper bound (%d)", min, max));
        }

        return (min == max) ? min : random.nextInt(max - min + 1) + min;
    }

    /**
     * Получение последовательности целых чисел в заданном диапазоне в случайном порядке следования
     *
     * @param min нижняя граница диапазона последовательности целых чисел
     * @param max верхняя граница диапазона последовательности целых чисел
     * @return список чисел из заданного диапазона, идущих в случайном порядке
     * @throws java.lang.IllegalArgumentException если нижняя граница по значению больше верхней
     */
    public static List<Integer> nextIntegerSequence(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("A lower bound (%d) cannot be greater " +
                    "than an upper bound (%df)", min, max));
        }

        List<Integer> sequence = new ArrayList<>(max - min);

        for (int i = min; i <= max; i++) {
            sequence.add(i);
        }

        Collections.shuffle(sequence);

        return sequence;
    }
}
