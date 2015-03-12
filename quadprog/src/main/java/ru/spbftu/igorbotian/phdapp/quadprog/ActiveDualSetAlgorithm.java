package ru.spbftu.igorbotian.phdapp.quadprog;

/**
 * Метод решения задачи квадратичного программирования, предложенный Гольдфарбом и Иднани.
 * Описание в оригинальной статье "A numerical stable dual method for solving strictly convex quadratic programs".
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="http://www.javaquant.net/papers/goldfarbidnani.pdf">http://www.javaquant.net/papers/goldfarbidnani.pdf</a>
 */
public interface ActiveDualSetAlgorithm {

    /**
     * Решение задачи квадратичного программирования по методу Гольдфарба-Иднани.
     * Соответствует средству решения из библиотеки "quadprog" для языка программирования R
     *
     * @param matrix           матрица квадратичной функции (Dmat)
     * @param vector           вектор квадратичной функции (dvec)
     * @param constraintMatrix матрица ограничений (Amat)
     * @param constraintVector вектор ограничений (bvec)
     * @return вектор, представляющий собой решение задачи квадратичного программирования
     * @throws Exception в случае невозможности решения задачи квадратичного программирования
     * @see <a href="http://cran.r-project.org/web/packages/quadprog/quadprog.pdf">quadprog</a>
     */
    double[] apply(double[][] matrix, double[] vector,
                   double[][] constraintMatrix, double[] constraintVector) throws Exception;
}
