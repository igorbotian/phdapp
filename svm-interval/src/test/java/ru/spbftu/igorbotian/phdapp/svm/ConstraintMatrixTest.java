/*
 * Copyright (c) 2015 Igor Botian
 *
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.Judgement;

/**
 * Модульные тесты для класса <code>ConstraintMatrix</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ConstraintMatrixTest extends BaseQuadProgTest {

    /**
     * Значение параметра штрафа (любое значение)
     */
    private static final double PENALTY_PARAM = 0.5;

    /**
     * Ожидаемое значение строк и столбцов матрицы ограничений
     */
    private double[][] expectedCoefficients = new double[][]{
            {1.0, 1.0, 0.0},
            {-1.0, -1.0, 0.0},
            {0.0, 0.0, 1.0},
            {0.0, 0.0, -1.0}
    };

    //-------------------------------------------------------------------------

    @Test
    public void testCoefficientsForJudgement() {
        ConstraintMatrix matrix = new ConstraintMatrix(PENALTY_PARAM, trainingSet);
        int i = 0;

        for(Judgement judgement : trainingSet) {
            double[][] coeffs = matrix.forJudgement(judgement);

            Assert.assertEquals(2, coeffs.length);
            Assert.assertArrayEquals(expectedCoefficients[i], coeffs[0], PRECISION);
            Assert.assertArrayEquals(expectedCoefficients[i + 1], coeffs[1], PRECISION);

            i += 2;
        }
    }

    @Test
    public void testConstraintVector() {
        ConstraintMatrix matrix = new ConstraintMatrix(PENALTY_PARAM, trainingSet);
        double[] data = matrix.constraints();
        Assert.assertArrayEquals(new double[]{0.0, -PENALTY_PARAM, 0.0, -PENALTY_PARAM}, data, PRECISION);
    }

    @Test
    public void testCoefficientVector() {
        ConstraintMatrix matrix = new ConstraintMatrix(PENALTY_PARAM, trainingSet);
        double[][] data = matrix.values();

        Assert.assertEquals(expectedCoefficients.length, data.length);

        for (int i = 0; i < expectedCoefficients.length; i++) {
            Assert.assertArrayEquals(expectedCoefficients[i], data[i], PRECISION);
        }
    }
}
