package ru.spbftu.igorbotian.phdapp.common;

import java.util.Arrays;

/**
 * Средство вычисления Евклидова расстояния между векторами в N-мерном пространстве
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public final class EuclideanDistance {

    private EuclideanDistance() {
        //
    }

    public static double compute(double[] x, double[] y) {
        int size = Math.max(x.length, y.length);
        double[] first = complement(x, size);
        double[] second = complement(y, size);
        double sum = 0.0;

        for (int i = 0; i < size; i++) {
            double delta = first[i] - second[i];
            sum += delta * delta;
        }

        return Math.sqrt(sum);
    }

    private static double[] complement(double[] src, int size) {
        assert size >= src.length;

        if (size == src.length) {
            return src;
        }

        double[] result = new double[size];

        Arrays.fill(result, 0);
        System.arraycopy(src, 0, result, 0, src.length);

        return result;
    }
}
