package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Map;

/**
 * Модульные тесты для класса <code>ActiveDualSetQuadraticProgrammingSolver</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see BaseQuadraticProgrammingTest
 */
public class ActiveDualSetQuadraticProgrammingSolverTest extends BaseQuadraticProgrammingTest {

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
        Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solution =
                qpSolver.solve(trainingSet, kernelFunction, PENALTY);

        Assert.assertEquals(expectedSolution.size(), solution.size());

        for (Pair<UnclassifiedObject, UnclassifiedObject> pair : solution.keySet()) {
            Pair<String, String> key = new Pair<>(pair.first.id(), pair.second.id());

            Assert.assertTrue(expectedSolution.containsKey(key));
            Assert.assertEquals(expectedSolution.get(key), solution.get(pair), PRECISION);
        }
    }
}
