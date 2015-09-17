package ru.spbftu.igorbotian.phdapp.common;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;

/**
 * Утилитарный класс для работы с матрицами
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public final class MatrixUtils {

    private MatrixUtils() {
        //
    }

    /**
     * Определение того, является ли заданная матрица положительно определённой или нет
     */
    public static boolean isPositiveDefinite(double[][] matrix) {
        try {
            new CholeskyDecomposition(new Array2DRowRealMatrix(matrix));
        } catch (NonPositiveDefiniteMatrixException | NonSymmetricMatrixException e) {
            return false;
        }

        return true;
    }
}
