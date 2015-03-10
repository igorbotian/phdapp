package ru.spbftu.igorbotian.phdapp.quadprog;

/**
 * Средство решения задачи квадратичного программирования
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface QuadraticProgrammingSolver {

    /**
     * Решение задачи квадратичного программирования по методу Гольдфарба-Иднани.
     * Соответствует средству решения из библиотеки "quadprog" для языка программирования R
     *
     * @param matrix матрица квадратичной функции (Dmat)
     * @param vector вектор квадратичной функции (dvec)
     * @param constraintMatrix матрица ограничений (Amat)
     * @param constraintVector вектор ограничений (bvec)
     *
     * @return вектор, представляющий собой решение задачи квадратичного программирования
     *
     * @see <a href="http://cran.r-project.org/web/packages/quadprog/quadprog.pdf">quadprog</a>
     */
    double[] solve(double[][] matrix, double[] vector,
                   double[][] constraintMatrix, double[] constraintVector);
}
