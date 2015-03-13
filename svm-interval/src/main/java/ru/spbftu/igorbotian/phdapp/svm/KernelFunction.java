package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

/**
 * Функция ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
interface KernelFunction {

    /**
     * Вычисление ядра для заданной пары векторов с вещественными значениями
     *
     * @param x     первый вектор
     * @param y     второй вектор
     * @return вещественное число
     */
    double compute(double[] x, double[] y);

    /**
     * Вычисление ядра для пары объектов
     *
     * @param x     первый объект
     * @param y     второй объект
     * @return вещественное число
     */
    double compute(UnclassifiedObject x, UnclassifiedObject y);
}
