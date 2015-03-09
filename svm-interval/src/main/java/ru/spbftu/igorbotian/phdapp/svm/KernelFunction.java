package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Функция ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface KernelFunction {

    /**
     * Вычисление ядра
     *
     * @param x     вектор с координатами первой точки
     * @param y     вектор с координатами второй точки
     * @return вещественное число
     */
    public double compute(double[] x, double[] y);
}
