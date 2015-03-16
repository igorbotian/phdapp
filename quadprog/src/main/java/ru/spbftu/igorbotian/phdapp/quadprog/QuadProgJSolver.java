package ru.spbftu.igorbotian.phdapp.quadprog;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import edu.iastate.econ.tesfatsi.QuadProgJ;

import java.util.Objects;

/**
 * Реализация метода решения задачи квадратичного программирования, предложенного Гольдфарбом и Иднани.
 * Используется реализация из пакета DCOPFJ.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ActiveDualSetAlgorithm
 * @see <a href="http://www2.econ.iastate.edu/tesfatsi/DCOPFJHome.htm">http://www2.econ.iastate.edu/tesfatsi/DCOPFJHome.htm</a>
 */
class QuadProgJSolver implements ActiveDualSetAlgorithm {

    @Override
    public double[] apply(double[][] matrix, double[] vector, double[][] iqConstraintMatrix, double[] iqConstraintVector)
            throws Exception {

        Objects.requireNonNull(matrix);
        Objects.requireNonNull(vector);
        Objects.requireNonNull(iqConstraintMatrix);
        Objects.requireNonNull(iqConstraintVector);

        double[][] eqConstraintMatrix = new double[iqConstraintMatrix.length][0];
        double[] eqConstraintVector = new double[0];

        QuadProgJ solver = new QuadProgJ(
                new DenseDoubleMatrix2D(matrix),
                new DenseDoubleMatrix1D(vector),
                new DenseDoubleMatrix2D(transpose(eqConstraintMatrix)),
                new DenseDoubleMatrix1D(eqConstraintVector),
                new DenseDoubleMatrix2D(transpose(iqConstraintMatrix)),
                new DenseDoubleMatrix1D(iqConstraintVector)
        );
        return solver.getMinX();
    }

    private double[][] transpose(double[][] matrix) {
        double[][] transposed = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }
}
