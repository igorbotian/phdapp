package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.Pair;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Модульные тесты для класса <code>KernelMatrix</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class KernelMatrixTest extends BaseQuadProgTest {

    /**
     * Значение свободного параметра Гауссова ядра
     */
    private static final double SIGMA = 1.0;

    /**
     * Функция Гауссова ядра
     */
    private static final GaussianKernelFunction kernelFunction = new GaussianKernelFunction(SIGMA);

    /**
     * Ожидаемое значение матрицы согласно заданным ограничениям
     */
    private double[][] expectedMatrix;

    @Before
    public void setUp() {
        super.setUp();

        Set<Pair<Double, Double>> combination = new LinkedHashSet<>();

        for (int[] xx : preferable) {
            for (double x : xx) {
                for (int[] zz : inferior) {
                    for (double z : zz) {
                        combination.add(new Pair<>(x, z));
                    }
                }
            }
        }

        int pairCount = combination.size();
        int i = 0;
        expectedMatrix = new double[pairCount][pairCount];

        for (Pair<Double, Double> pair : combination) {
            int j = 0;

            for (Pair<Double, Double> anotherPair : combination) {
                if (pair.equals(anotherPair)) {
                    expectedMatrix[i][j] = 1.0;
                } else {
                    expectedMatrix[i][j] = kernel(kernelFunction,
                            pair.first, pair.second,
                            anotherPair.first, anotherPair.second
                    );
                }

                j++;
            }

            i++;
        }
    }

    private double kernel(GaussianKernelFunction kernelFunction, double x1, double z1, double x2, double z2) {
        double[] x11 = new double[]{x1};
        double[] z11 = new double[]{z1};
        double[] x22 = new double[]{x2};
        double[] z22 = new double[]{z2};

        return kernelFunction.compute(x11, x22) - kernelFunction.compute(x11, z22)
                - kernelFunction.compute(z11, x22) - kernelFunction.compute(z11, z22);
    }

    //-------------------------------------------------------------------------

    @Test
    public void testValues() {
        double[][] matrix = new KernelMatrix(trainingSet, kernelFunction).values();

        Assert.assertEquals(expectedMatrix.length, matrix.length);

        for (int i = 0; i < matrix.length; i++) {
            Assert.assertArrayEquals(expectedMatrix[i], matrix[i], PRECISION);
        }
    }
}
