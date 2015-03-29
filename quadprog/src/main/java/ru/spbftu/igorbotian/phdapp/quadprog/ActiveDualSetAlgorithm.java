package ru.spbftu.igorbotian.phdapp.quadprog;

/**
 * Метод решения задачи квадратичного программирования, предложенный Гольдфарбом и Иднани.
 * Описание в оригинальной статье "A numerical stable dual method for solving strictly convex quadratic programs".
 *
 * @see <a href="http://www.javaquant.net/papers/goldfarbidnani.pdf">http://www.javaquant.net/papers/goldfarbidnani.pdf</a>
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface ActiveDualSetAlgorithm {

    /**
     * Решение задачи квадратичного программирования по методу Гольдфарба-Иднани.
     * Соответствует средству решения из библиотеки "quadprog" для языка программирования R
     *
     * @param matrix           матрица квадратичной функции (её размер количеству элементов сочетания составляющих
     *                         экспертных оценок) (Dmat)
     * @param vector           вектор квадратичной функции (его размер равен количеству неизвестных) (dvec)
     * @param constraintMatrix матрица ограничений вида A * x >= b (количество строк в ней равно количеству ограничений,
     *                         а столбцов - количеству неизвестных) (Amat)
     * @param constraintVector вектор ограничений (его размер равен количеству ограничений) (bvec)
     * @return вектор, представляющий собой решение задачи квадратичного программирования (b).
     * Порядок элементов вектора соответствует заголовкам строк/столбцов матрицы квадратичной функции
     * @throws Exception в случае невозможности решения задачи квадратичного программирования
     *                   или некорректно сформированных входных данных
     * @see <a href="http://cran.r-project.org/web/packages/quadprog/quadprog.pdf">quadprog</a>
     */
    double[] apply(double[][] matrix, double[] vector,
                   double[][] constraintMatrix, double[] constraintVector) throws QuadraticProgrammingException;
}
