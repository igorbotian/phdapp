package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Map;

/**
 * Средство решения исходной задачи оптимизации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
interface Optimizer {

    /**
     * Поиск множителей Лагранжа в исходной задаче оптимизации
     *
     * @param trainingSet      обучающая выборка
     * @param kernelFunction   функция ядра
     * @param penalty параметр штрафа
     * @return ассоциативный массив, в котором каждой паре объектов из экспертной оценки соответствует
     * найденный множитель Лагранжа
     * @throws OptimizationException в случае возникновения ошибки в процессе решения задачи оптимизации
     * @throws NullPointerException  если обучающая выборка или функция ядра не заданы
     */
    Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> optimize(PairwiseTrainingSet trainingSet,
                                                                       KernelFunction kernelFunction,
                                                                       double penalty)
            throws OptimizationException;
}
