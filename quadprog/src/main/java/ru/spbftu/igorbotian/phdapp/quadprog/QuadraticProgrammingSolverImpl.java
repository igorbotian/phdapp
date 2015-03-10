package ru.spbftu.igorbotian.phdapp.quadprog;

import java.util.Objects;

/**
 * Реализация средства решения задачи квадратичного программирования
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see QuadraticProgrammingSolver
 */
class QuadraticProgrammingSolverImpl implements QuadraticProgrammingSolver {

    public QuadraticProgrammingSolverImpl() {
        //
    }

    @Override
    public double[] solve(double[][] matrix, double[] vector,
                          double[][] constraintMatrix, double[] constraintVector) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(vector);
        Objects.requireNonNull(constraintMatrix);
        Objects.requireNonNull(constraintVector);

        return new double[] {0.0, 1.0, 2.0}; // TODO
    }
}
