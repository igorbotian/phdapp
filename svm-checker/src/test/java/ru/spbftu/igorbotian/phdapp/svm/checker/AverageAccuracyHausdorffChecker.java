package ru.spbftu.igorbotian.phdapp.svm.checker;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.HausdorffIntervalRankingPairwiseClassifierModule;

import java.util.Collections;
import java.util.Set;

/**
 * Механизм автоматизированной кросс-валидации ранжирующего попарного классификатора,
 * ориентированной на среднее значение точности серии попарных классификаций
 * и использующий расстояние Хаусдорфа в ядре классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.AverageAccuracyValidator
 * @see AverageAccuracyChecker
 */
public class AverageAccuracyHausdorffChecker extends AverageAccuracyChecker {

    @Override
    protected Set<PhDAppModule> injectClassifierModules() {
        return Collections.singleton(new HausdorffIntervalRankingPairwiseClassifierModule());
    }
}
