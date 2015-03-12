package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Judgement;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;

import java.util.List;
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
     * @param penaltyParameter параметр штрафа
     * @return ассоциативный массив, в котором каждой экспертной оценке соответствует
     * @throws OptimizationException в случае возникновения ошибки в процессе решения задачи оптимизации
     * @throws NullPointerException если обучающая выборка или функция ядра не заданы
     */
    Map<Judgement, List<Double>> optimize(PairwiseTrainingSet trainingSet, KernelFunction kernelFunction,
                                                 double penaltyParameter) throws OptimizationException;
}
