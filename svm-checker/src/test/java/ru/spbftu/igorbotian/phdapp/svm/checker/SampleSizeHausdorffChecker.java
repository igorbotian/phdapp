package ru.spbftu.igorbotian.phdapp.svm.checker;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.HausdorffIntervalRankingPairwiseClassifierModule;

import java.util.Collections;
import java.util.Set;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ зависимости точности классификации от размера обучающей выборки
 * и использующий расстояние Хаусдорфа в ядре классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AccuracyDependenceOnSampleSizeAnalyzer
 * @see SampleSizeChecker
 */
public class SampleSizeHausdorffChecker extends SampleSizeChecker {

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new HausdorffIntervalRankingPairwiseClassifierModule());
    }
}
