package ru.spbftu.igorbotian.phdapp.svm.checker;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.ClusterCentroidIntervalRankingPairwiseClassifierModule;

import java.util.Collections;
import java.util.Set;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ зависимости точности классификации от размера обучающей выборки
 * и использующий расстояние между центрами подмножеств обучающей выборки в ядре классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnSampleSizeAnalyzer
 * @see SampleSizeChecker
 */
public class SampleSizeCentroidChecker extends SampleSizeChecker {

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new ClusterCentroidIntervalRankingPairwiseClassifierModule());
    }
}
