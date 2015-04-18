package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Функция ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface KernelFunction<T> {

    /**
     * Вычисление ядра для пары объектов
     *
     * @param x первый объект
     * @param y второй объект
     * @return вещественное число
     */
    double compute(T x, T y);
}
