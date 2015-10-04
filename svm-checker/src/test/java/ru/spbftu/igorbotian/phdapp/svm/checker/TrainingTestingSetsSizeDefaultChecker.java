package ru.spbftu.igorbotian.phdapp.svm.checker;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.IntervalPairwiseClassifierModule;

import java.util.Collections;
import java.util.Set;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на анализ завимимости точности классификации от размера обучающей выборки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.TrainingSetSizeRatioAnalyzer
 * @see TrainingTestingSetsSizeChecker
 */
public class TrainingTestingSetsSizeDefaultChecker extends TrainingTestingSetsSizeChecker {

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new IntervalPairwiseClassifierModule());
    }
}
