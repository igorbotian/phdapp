package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Map;

/**
 * Средство решения задачи квадратичного программирования
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
interface QuadraticProgrammingSolver<T> {

    /**
     * Решения задачи квадратичного программирования с заданными параметрами
     *
     * @param trainingSet      обучающая выборка
     * @param kernelFunction   функция ядра, применяемая в ходе решения
     * @param penalty параметр штрафа
     * @return ассоциативный массив, в котором каждой паре объектов из экспертной оценки соответствует
     * некоторое вещественное число (как элемент решения задачи)
     * @throws QuadraticProgrammingException в случае возникновения ошибки в процессе решения
     * @throws NullPointerException  если обучающая выборка или функция ядра не заданы
     */
    Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solve(PairwiseTrainingSet trainingSet,
                                                                    KernelFunction<T> kernelFunction,
                                                                    double penalty)
            throws QuadraticProgrammingException;
}
