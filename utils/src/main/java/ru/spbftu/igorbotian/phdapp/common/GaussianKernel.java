package ru.spbftu.igorbotian.phdapp.common;

import java.util.Arrays;

/**
 * Гауссово ядро (RBF-ядро), основанное на вычислении радиально-базисной функции
 * @see <a href="http://en.wikipedia.org/wiki/Radial_basis_function_kernel">http://en.wikipedia.org/wiki/Radial_basis_function_kernel</a>
 */
public final class GaussianKernel {

    private GaussianKernel() {
        //
    }

    /**
     * Вычисление RBF-ядра по заданному расстоянию между векторами
     * @param distance расстояние между векторами/объектами
     * @param sigma свободный параметр
     * @return вещественное число
     * @throws IllegalArgumentException если заданное расстояние имеет отрицательное значение
     */
    public static double compute(double distance, double sigma) {
        if(distance < 0.0) {
            throw new IllegalArgumentException("Distance cannot have a negative value");
        }

        return Math.exp(- (distance * distance) / (2 * sigma * sigma));
    }

    /**
     * Вычисление RBF-ядра по заданным векторам
     * @param x вектор с координатами первой точки
     * @param y вектор с координатами второй точки
     * @param sigma свободный параметр
     * @return вещественное число
     * @throws NullPointerException если хотя бы один из векторов не задан
     */
    public static double compute(double[] x, double[] y, double sigma) {
        if(x == null) {
            throw new NullPointerException("Vector x cannot be null");
        }

        if(y == null) {
            throw new NullPointerException("Vector y cannot be null");
        }

        double distance = squaredEuclideanDistance(x, y);
        return Math.exp(- distance / (2 * sigma * sigma));
    }

    private static double squaredEuclideanDistance(double[] x, double[] y) {
        int size = Math.max(x.length, y.length);
        double[] first = complement(x, size);
        double[] second = complement(y, size);
        double sum = 0.0;

        for(int i = 0; i < size; i++) {
            double delta = first[i] - second[i];
            sum += delta * delta;
        }

        return sum;
    }

    private static double[] complement(double[] src, int size) {
        assert size >= src.length;

        if(size == src.length) {
            return src;
        }

        double[] result = new double[size];

        Arrays.fill(result, 0);
        System.arraycopy(src, 0, result, 0, src.length);

        return result;
    }
}
