package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Базовый тест для средства классификации данных при точном характере данных в обучающей выборке
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public abstract class BasePreciseRankingPairwiseClassifierTest extends BaseRankingPairwiseClassifierTest {

    @Override
    protected PairwiseTrainingSet makeTrainingSet() {
        return makeTrainingSet(Stream.of(
                makeJudgement(makeObject("x11", 11.0), makeObject("z1", 1.0)),
                makeJudgement(makeObject("x13", 13.0), makeObject("z3", -5.0)),
                makeJudgement(makeObject("x7", 7.0), makeObject("z2", 2.0))
        ).collect(Collectors.toSet()));
    }

    @Override
    protected Set<Pair<UnclassifiedObject, UnclassifiedObject>> pairsToClassify() {
        return Stream.of(
                new Pair<>(makeObject("x15", 15.0), makeObject("z0", 0.0)),
                new Pair<>(makeObject("x5", 5.0), makeObject("z-3", -3.0)),
                new Pair<>(makeObject("x10", 10.0), makeObject("z3", 3.0))
        ).collect(Collectors.toSet());
    }
}
