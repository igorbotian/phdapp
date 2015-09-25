package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class HausdorffActiveDualSetPreciseQuadraticProgrammingSolverTest extends AbstractPreciseQuadraticProgrammingTest {

    @Override
    public void setUp() throws QuadraticProgrammingException {
        super.setUp();
        kernel = new HausdorffGaussianMercerKernel(SIGMA, dataFactory);
    }

    @Override
    protected Set<PhDAppModule> injectModules() {
        return Stream.of(
                new HausdorffIntervalRankingPairwiseClassifierModule()
        ).collect(Collectors.toSet());
    }

    @Override
    protected PairwiseTrainingSet makeTrainingSet() {
        return UnclassifiedObjectSet.toPreciseJudgements(dataFactory, super.makeTrainingSet());
    }

    @Test
    public void testSolution() throws QuadraticProgrammingException {
        Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solution =
                qpSolver.solve(trainingSet, kernel, PENALTY);

        Assert.assertEquals(expectedSolution.size(), solution.size());

        for (Pair<UnclassifiedObject, UnclassifiedObject> pair : solution.keySet()) {
            UnclassifiedObject first = (UnclassifiedObject) (pair.first).parameters().iterator().next().value();
            UnclassifiedObject second = (UnclassifiedObject) (pair.second).parameters().iterator().next().value();
            Pair<String, String> key = new Pair<>(first.id(), second.id());

            Assert.assertTrue((expectedSolution.containsKey(key)));
            Assert.assertEquals(expectedSolution.get(key), solution.get(pair), PRECISION);
        }
    }
}
