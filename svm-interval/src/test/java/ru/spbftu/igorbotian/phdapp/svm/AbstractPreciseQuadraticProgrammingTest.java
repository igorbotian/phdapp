package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Тест для обучающей выборки, состоящей только из точных сравнений:
 * {x1} > {z1};
 * {x2} > {z2}.
 * При этом в тесте объекты имеют следующие значения:
 * x1 = 11.0, x2 = 12.0;
 * z1 = 1.0, z2 = 2.0;
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public abstract class AbstractPreciseQuadraticProgrammingTest extends BaseQuadraticProgrammingTest {

    @Override
    protected Set<PhDAppModule> injectModules() {
        return Stream.of(
                new IntervalPairwiseClassifierModule(),
                new PreciseRankingPairwiseClassifierModule()
        ).collect(Collectors.toSet());
    }

    @Override
    protected PairwiseTrainingSet makeTrainingSet() {
        LinkedHashSet<Judgement> trainingSet = new LinkedHashSet<>();

        trainingSet.add(dataFactory.newPairwiseTrainingObject(
                makeJudgementItemSet(
                        makeJudgementItem("x1", 11.0)
                ),
                makeJudgementItemSet(
                        makeJudgementItem("z1", 1.0)
                )
        ));

        trainingSet.add(dataFactory.newPairwiseTrainingObject(
                makeJudgementItemSet(
                        makeJudgementItem("x2", 12.0)
                ),
                makeJudgementItemSet(
                        makeJudgementItem("z2", 2.0)
                )
        ));

        return dataFactory.newPairwiseTrainingSet(trainingSet);
    }

    private static Set<UnclassifiedObject> makeJudgementItemSet(UnclassifiedObject... judgements) {
        Set<UnclassifiedObject> set = new LinkedHashSet<>();
        Stream.of(judgements).forEach(set::add);
        return set;
    }

    @Override
    protected Map<Pair<String, String>, Double> makeExpectedSolution() {
        Map<Pair<String, String>, Double> solution = new HashMap<>();

        solution.put(new Pair<>("x1", "z1"), 0.4403983);
        solution.put(new Pair<>("x2", "z2"), 0.4403983);

        return solution;
    }
}
