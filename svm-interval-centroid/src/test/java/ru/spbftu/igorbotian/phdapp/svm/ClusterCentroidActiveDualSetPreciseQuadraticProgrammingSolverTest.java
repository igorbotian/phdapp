package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>ActiveDualSetQuadraticProgrammingSolver</code>,
 * в которых обучающая выборка состоит из интервальных значений, а в решении используется расстояние между центрами кластера.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see AbstractIntervalQuadraticProgrammingTest
 */
public class ClusterCentroidActiveDualSetPreciseQuadraticProgrammingSolverTest extends AbstractPreciseQuadraticProgrammingTest {

    @Override
    public void setUp() throws QuadraticProgrammingException {
        super.setUp();
        kernel = new GaussianMercerKernel<>(new GaussianKernelFunctionImpl(SIGMA));
    }

    @Override
    protected Set<PhDAppModule> injectModules() {
        return Stream.of(
                new ClusterCentroidIntervalRankingPairwiseClassifierModule()
        ).collect(Collectors.toSet());
    }

    @Test
    public void testSolution() throws QuadraticProgrammingException {
        super.testSolution();
    }
}
