package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;

/**
 * Ядро Мерсера
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="http://research.microsoft.com/apps/pubs/default.aspx?id=65610">http://research.microsoft.com/apps/pubs/default.aspx?id=65610</a>
 */
final class MercerKernel {

    private MercerKernel() {
        //
    }

    /**
     * Вычисление значения ядра Мерсера для заданных пар объектов с вещественными параметрами
     *
     * @param first          первый объект
     * @param second         второй объект
     * @param kernelFunction функция ядра
     * @return вещественное число
     * @throws NullPointerException если хотя бы один из аргументов не задан
     */
    public static double compute(Pair<UnclassifiedObject, UnclassifiedObject> first,
                                 Pair<UnclassifiedObject, UnclassifiedObject> second,
                                 KernelFunction kernelFunction) {

        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        Objects.requireNonNull(kernelFunction);

        return kernelFunction.compute(first.first, second.first)
                - kernelFunction.compute(first.first, second.second)
                - kernelFunction.compute(first.second, second.first)
                + kernelFunction.compute(first.second, second.second);
    }
}
