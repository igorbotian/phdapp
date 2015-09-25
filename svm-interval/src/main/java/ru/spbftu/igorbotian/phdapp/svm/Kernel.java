package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;

/**
 * Ядро
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface Kernel<T> {

    /**
     * Вычисление значения ядра для заданных пар объектов с вещественными параметрами
     *
     * @param first  первый объект
     * @param second второй объект
     * @return вещественное число
     * @throws NullPointerException если хотя бы один из аргументов не задан
     */
    double compute(Pair<T, T> first, Pair<T, T> second);
}
