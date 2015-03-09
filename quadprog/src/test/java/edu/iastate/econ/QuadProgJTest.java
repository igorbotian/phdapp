package edu.iastate.econ;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Проверка идентичности результатов решения задачи квадратичного программирования с помощью алгоритма Гольдфарба-Иднани,
 * написанного на Java и на R.
 * Исходная реализация на R доступна <a href="http://cran.r-project.org/web/packages/quadprog/">здесь</a>.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
@Ignore
public class QuadProgJTest {

    /**
     * Точность сравнения вещественных чисел
     */
    private static final double PRECISION = 0.1;

    /*
     * > require(quadprog)
     * Loading required package: quadprog
     * > Dmat <- matrix(0,3,3)
     * > diag(Dmat) <- 1
     * > dvec <- c(0,5,0)
     * > Amat <- matrix(c(-4,-3,0,2,1,0,0,-2,1),3,3)
     * > bvec <- c(-8,-2,0)
     * > solve.QP(Dmat,dvec,Amat,bvec=bvec)
     * $solution
     * [1] 0 1 2
     *
     * $value
     * [1] -2.5
     *
     * $unconstrained.solution
     * [1] 0 5 0
     *
     * $iterations
     * [1] 2 0
     *
     * $Lagrangian
     * [1] 0 0 2
     *
     * $iact
     * [1] 3
     */
    @Test
    public void testCompliance() {
        double[] quadraticFunctionVector = new double[] {0.0, 5.0, 0.0};
        double[] constraintCoefficientVector = new double[] {-8.0, -2.0, 0.0};
        double[][] constraintMatrix = new double[][] {
                {-4.0, -3.0, 0.0},
                {2.0, 1.0, 0.0},
                {0.0, -2.0, 1.0}
        };
        double[][] kernelMatrix = new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };
        double[] expectedResult = new double[] {0.0, 1.0, 2.0};

        QuadProgJ quadProgEngine = new QuadProgJ(
                new DenseDoubleMatrix2D(kernelMatrix),
                new DenseDoubleMatrix1D(quadraticFunctionVector),
                new DenseDoubleMatrix2D(new double[][] {
                        {},
                        {},
                        {}
                }),
                new DenseDoubleMatrix1D(new double[] {}),
                new DenseDoubleMatrix2D(constraintMatrix),
                new DenseDoubleMatrix1D(constraintCoefficientVector)
        );

        Assert.assertArrayEquals(expectedResult, quadProgEngine.getMinX(), PRECISION);
    }
}
