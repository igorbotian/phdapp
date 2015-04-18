package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.GaussianKernel;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Функция Гауссова ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public abstract class GaussianKernelFunction<T> implements KernelFunction<T> {

    /**
     * Свободный параметр
     */
    private final double sigma;

    public GaussianKernelFunction(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double compute(T x, T y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);

        return GaussianKernel.compute(distanceFunction().apply(x, y), sigma);
    }

    /**
     * Получение функции, которая возвращает расстояние между двумя объектами заданного типа
     */
    protected abstract BiFunction<T, T, Double> distanceFunction();
}
