package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;

import java.util.Objects;

/**
 * Ядро Мерсера
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="http://research.microsoft.com/apps/pubs/default.aspx?id=65610">http://research.microsoft.com/apps/pubs/default.aspx?id=65610</a>
 */
public class GaussianMercerKernel<T> implements Kernel<T> {

    private GaussianKernelFunction<T> kernelFunction;

    public GaussianMercerKernel(GaussianKernelFunction<T> kernelFunction) {
        this.kernelFunction = Objects.requireNonNull(kernelFunction);
    }

    /**
     * Вычисление значения ядра Мерсера для заданных пар объектов с вещественными параметрами
     *
     * @param first          первый объект
     * @param second         второй объект
     * @return вещественное число
     * @throws NullPointerException если хотя бы один из аргументов не задан
     */
    @Override
    public double compute(Pair<T, T> first, Pair<T, T> second) {

        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        return kernelFunction.compute(first.first, second.first)
                - kernelFunction.compute(first.first, second.second)
                - kernelFunction.compute(first.second, second.first)
                + kernelFunction.compute(first.second, second.second);
    }
}
