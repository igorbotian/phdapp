package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

/**
 * Модульные тесты для класса <code>ActiveDualSetQuadraticProgrammingSolver</code>,
 * в которых обучающая выборка состоит из точных значений.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see AbstractIntervalQuadraticProgrammingTest
 */
public class ActiveDualSetPreciseQuadraticProgrammingSolverTest extends AbstractPreciseQuadraticProgrammingTest {

    @Override
    public void setUp() throws QuadraticProgrammingException {
        super.setUp();
        kernel = new GaussianMercerKernel<>(new GaussianKernelFunctionImpl(SIGMA));
    }

    /*
     Соответствующее решение задачи на языке R:

     require('quadprog')

     sigma <- 0.5
     fix <- 0.000001

     x1 <- 11
     x2 <- 12
     z1 <- 1
     z2 <- 2

     dvec <- c(1, 1)
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
       m(x1, z1, x1, z1) + fix, m(x1, z1, x2, z2),
       m(x2, z2, x1, z1), m(x2, z2, x2, z2) + fix
     ), byrow=T, nrow=2, ncol=2)

     amat <- matrix(c(
       1, 0,
       -1, 0,
       0, 1,
       0, -1
     ), byrow=T, nrow=4, ncol=2)

     solve.QP(dmat, dvec, t(amat), bvec)

     $solution
     [1] 0.4403983 0.4403983

     $value
     [1] -0.4403983

     $unconstrained.solution
     [1] 0.4403983 0.4403983

     $iterations
     [1] 1 0

     $Lagrangian
     [1] 0 0 0 0

     $iact
     [1] 0
     */

    @Test
    public void testSolution() throws QuadraticProgrammingException {
        super.testSolution();
    }
}
