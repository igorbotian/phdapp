package ru.spbftu.igorbotian.phdapp.svm.checker;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.HausdorffIntervalRankingPairwiseClassifierModule;

import java.util.Collections;
import java.util.Set;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от размера обучающей выборки
 * и использующий расстояние Хаусдорфа в ядре классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.TrainingSetSizeRatioAnalyzer
 * @see TrainingTestingSetsSizeChecker
 */
public class TrainingTestingSetsSizeHausdorffChecker extends TrainingTestingSetsSizeChecker {

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new HausdorffIntervalRankingPairwiseClassifierModule());
    }
}
