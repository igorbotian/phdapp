package ru.spbftu.igorbotian.phdapp.quadprog;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;

import java.nio.file.Paths;

/**
 * Проверка корректности результатов решения задачи квадратичного программирования с помощью алгоритма Гольдфарба-Иднани.
 * Исходная реализация на R доступна <a href="http://cran.r-project.org/web/packages/quadprog/">здесь</a>.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ActiveDualSetAlgorithmTest {

    /**
     * Точность сравнения вещественных чисел
     */
    private static final double PRECISION = 0.000001;
    /**
     * Матрица квадратичной функции
     */
    private final double[][] matrix = new double[][]{
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
    };
    /**
     * Вектор квадратичной функции
     */
    private final double[] vector = new double[]{0, 5, 0};
    /**
     * Матрица ограничений
     */
    private final double[][] constraintMatrix = new double[][]{
            {-4, -3, 0},
            {2, 1, 0},
            {0, -2, 1}
    };
    /**
     * Вектор ограничений
     */
    private final double[] constraintVector = new double[]{-8, -2, 0};
    /**
     * Ожидаемое значения решения задачи квадратичного программирования
     */
    private final double[] expectedSolution = new double[]{0, 1, 2};
    private ActiveDualSetAlgorithm solver;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(
                new QuadraticProgrammingModule(),
                new ApplicationConfigurationModule(Paths.get("."))
        );
        solver = injector.getInstance(ActiveDualSetAlgorithm.class);
    }

    /*
     Тест для проверки соответствия результатов решения задачи квадратичного программирования.
     Эквивалентный код на языке R, решающий данную задачу для заданных значений:

     require(quadprog)
     Dmat <- matrix(c(1,0,0, 0,1,0, 0,0,1), byrow=TRUE, nrow=3, ncol=3)
     dvec <- c(0,5,0)
     Amat <- matrix(c(-4,-3,0, 2,1,0, 0,-2,1), byrow=TRUE, nrow=3, ncol=3)
     bvec <- c(-8,-2,0)
     solve.QP(Dmat,dvec,t(Amat),bvec)

     $solution
     [1] 0 1 2

     $value
     [1] -2.5

     $unconstrained.solution
     [1] 0 5 0

     $iterations
     [1] 2 0

     $Lagrangian
     [1] 0 0 2

     $iact
     [1] 3
     */
    @Test
    public void testSolver() throws Exception {
        double[] solution = solver.apply(matrix, vector, constraintMatrix, constraintVector);
        Assert.assertArrayEquals(expectedSolution, solution, PRECISION);
    }
}
