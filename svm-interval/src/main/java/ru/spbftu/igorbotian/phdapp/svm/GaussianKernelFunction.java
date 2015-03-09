package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.GaussianKernel;

/**
 * Функция Гауссова ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class GaussianKernelFunction implements KernelFunction {

    /**
     * Свободный параметр
     */
    private final double sigma;

    public GaussianKernelFunction(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double compute(double[] x, double[] y) {
        return GaussianKernel.compute(x, y, sigma);
    }
}
