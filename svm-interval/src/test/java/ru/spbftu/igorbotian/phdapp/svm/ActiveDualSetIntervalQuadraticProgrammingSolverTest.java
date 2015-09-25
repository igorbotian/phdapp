package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>ActiveDualSetQuadraticProgrammingSolver</code>,
 * в которых обучающая выборка состоит из интервальных значений.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see AbstractIntervalQuadraticProgrammingTest
 */
public class ActiveDualSetIntervalQuadraticProgrammingSolverTest extends AbstractIntervalQuadraticProgrammingTest {

    @Override
    public void setUp() throws QuadraticProgrammingException {
        super.setUp();
        kernel = new GaussianMercerKernel<>(new GaussianKernelFunctionImpl(SIGMA));
    }

    @Override
    protected Set<PhDAppModule> injectModules() {
        return Stream.of(new IntervalPairwiseClassifierModule()).collect(Collectors.toSet());
    }

    /*
     Соответствующее решение задачи на языке R:

     require('quadprog')

     sigma <- 0.5
     fix <- 0.000001

     x1 <- 11
     x2 <- 12
     x3 <- 13
     z1 <- 1
     z2 <- 2

     dvec <- c(1, 1, 1, 1, 1)
     bvec <- c(0, -0.5, 0, -0.5)

     k <- function(a, b) {
	      result <- exp(- ((a - b) * (a - b)) / (2 * sigma * sigma))
          return (result)
     }
     m <- function(x1, z1, x2, z2) {
          result <- k(x1, x2) - k(x1, z2) - k(z1, x2) + k(z1, z2)
          return (result)
     }

     dmat <- matrix(c(
          m(x1, z1, x1, z1) + fix, m(x1, z1, x2, z1), m(x1, z1, x1, z2), m(x1, z1, x3, z1), m(x1, z1, x3, z2),
          m(x2, z1, x1, z1), m(x2, z1, x2, z1) + fix, m(x2, z1, x1, z2), m(x2, z1, x3, z1), m(x2, z1, x3, z2),
          m(x1, z2, x1, z1), m(x1, z2, x2, z1), m(x1, z2, x1, z2) + fix, m(x1, z2, x3, z1), m(x1, z2, x3, z2),
          m(x3, z1, x1, z1), m(x3, z1, x2, z1), m(x3, z1, x1, z2), m(x3, z1, x3, z1) + fix, m(x3, z1, x3, z2),
          m(x3, z2, x1, z1), m(x3, z2, x2, z1), m(x3, z2, x1, z2), m(x3, z2, x3, z1), m(x3, z2, x3, z2) + fix
     ), byrow=T, nrow=5, ncol=5)

     amat <- matrix(c(
          1, 1, 0, 0, 0,
          -1, -1, 0, 0, 0,
          1, 0, 1, 1, 1,
          -1, 0, -1, -1, -1
     ), byrow=T, nrow=4, ncol=5)

     solve.QP(dmat, dvec, t(amat), bvec)

     $solution
     [1] 0.02158241 0.41367060 0.22841759 0.02158241 0.22841759

     $value
     [1] -0.5006529

     $unconstrained.solution
     [1] 0.1059149 0.3092328 0.2605312 0.1059149 0.2605312

     $iterations
     [1] 2 0

     $Lagrangian
     [1] 0.0000000 0.0000000 0.0000000 0.1752705

     $iact
     [1] 4
     */

    @Test
    public void testSolution() throws QuadraticProgrammingException {
        super.testSolution();
    }
}
