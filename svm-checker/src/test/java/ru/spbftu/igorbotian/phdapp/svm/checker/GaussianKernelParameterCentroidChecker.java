package ru.spbftu.igorbotian.phdapp.svm.checker;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.ClusterCentroidIntervalRankingPairwiseClassifierModule;

import java.util.Collections;
import java.util.Set;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от параметра Гауссова ядра
 * и использующий расстояние между центрами подмножеств обучающей выборке в ядре классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnClassifierParametersAnalyzer
 * @see GaussianKernelParameterChecker
 */
public class GaussianKernelParameterCentroidChecker extends GaussianKernelParameterChecker {

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new ClusterCentroidIntervalRankingPairwiseClassifierModule());
    }
}
