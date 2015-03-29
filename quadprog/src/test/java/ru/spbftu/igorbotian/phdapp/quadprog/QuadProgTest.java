package ru.spbftu.igorbotian.phdapp.quadprog;

import org.junit.Assert;
import org.junit.Test;

/**
 * Модульные тесты для класса <code>QuadProg</code>
 * Соответствуют тестам из реализации пакета 'quadprog' на языке JavaScript
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="https://github.com/albertosantini/node-quadprog/tree/master/test">https://github.com/albertosantini/node-quadprog/tree/master/test</a>
 */
public class QuadProgTest {

    /**
     * Точность сравнения вещественных чисел
     */
    private static final double PRECISION = 0.000001;

    @Test
    public void testFirst() throws Exception {
        Double[] dvec = new Double[]{null, 0.0, 5.0, 0.0};
        Double[] bvec = new Double[]{null, -8.0, 2.0, 0.0};
        Double[][] amat = new Double[][]{
                null,
                {null, -4.0, 2.0, 0.0},
                {null, -3.0, 1.0, -2.0},
                {null, 0.0, 0.0, 1.0}
        };
        Double[][] dmat = new Double[][]{
                null,
                {null, 1.0, 0.0, 0.0},
                {null, 0.0, 1.0, 0.0},
                {null, 0.0, 0.0, 1.0},
        };

        Double[] expectedSolution = new Double[]{null, 0.47619047619047616, 1.0476190476190477, 2.0952380952380953};
        Double[] solution = QuadProg.solveQP(dmat, dvec, amat, bvec).solution;

        assertEquals(expectedSolution, solution);
    }

    @Test
    public void testSecond() throws Exception {
        Double[] dvec = new Double[]{null, 6.712571e-05, 4.222380e-05, 7.134523e-05, 2.902528e-05, 2.797279e-05, 4.038785e-05, 4.581844e-05};
        Double[] bvec = new Double[]{null, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5, -0.5, -0.5, -0.5, -0.5, -0.5, -0.5};
        Double[][] amat = new Double[][]{
                null,
                {null, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {null, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {null, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0},
                {null, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0},
                {null, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0},
                {null, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0},
                {null, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0}
        };
        Double[][] dmat = new Double[][]{
                null,
                {null, 4.523032e-04, 5.097330e-04, 5.724848e-04, 5.049878e-04, -1.126059e-05, 1.955939e-04, 3.306526e-04},
                {null, 5.097330e-04, 6.951339e-04, 6.417501e-04, 6.697231e-04, -1.935067e-06, 2.421462e-04, 3.854708e-04},
                {null, 5.724848e-04, 6.417501e-04, 8.401752e-04, 6.540224e-04, -1.253942e-05, 2.540068e-04, 4.705115e-04},
                {null, 5.049878e-04, 6.697231e-04, 6.540224e-04, 8.528426e-04, 1.678568e-05, 2.643017e-04, 3.590997e-04},
                {null, -1.126059e-05, -1.935067e-06, -1.253942e-05, 1.678568e-05, 6.029220e-05, 2.831196e-05, -2.742019e-05},
                {null, 1.955939e-04, 2.421462e-04, 2.540068e-04, 2.643017e-04, 2.831196e-05, 1.417604e-04, 1.441332e-04},
                {null, 3.306526e-04, 3.854708e-04, 4.705115e-04, 3.590997e-04, -2.742019e-05, 1.441332e-04, 2.275453e-03}
        };

        Double[] expectedSolution = new Double[]{null, 0.09047051922254573, 0.0, 0.0, 0.0, 0.5, 0.4000853270556898, 0.009444153721764449};
        Double[] solution = QuadProg.solveQP(dmat, dvec, amat, bvec).solution;

        assertEquals(expectedSolution, solution);
    }

    private void assertEquals(Double[] expected, Double[] actual) {
        Assert.assertEquals(expected.length, actual.length);
        Assert.assertTrue(actual[0] == null);

        for (int i = 1; i < expected.length; i++) {
            Assert.assertEquals(expected[i], actual[i], PRECISION);
        }
    }
}
