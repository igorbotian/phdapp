package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingModule;

import java.util.*;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>ActiveDualSetOptimizer</code>
 * Тестовая обучающая выборка состоит из следующих экспертных оценок:
 * {x1, x2} > {z1};
 * {x1, x3} > {z1, z2}.
 * <p>
 * При этом в данном тесте объекты имеют следующие значения:
 * x1 = 11.0, x2 = 12.0, x3 = 13.0;
 * z1 = 1.0, z2 = 2.0
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ActiveDualSetOptimizerTest {

    /**
     * Идентификатора тестового параметра
     */
    private static final String PARAM_ID = "test_param";

    /**
     * Точность сравнения вещественных чисел
     */
    private static final double PRECISION = 0.00001;

    /**
     * Значение параметра штрафа
     */
    private static final double PENALTY = 0.5;

    /**
     * Значение свободного параметра Гауссова ядра
     */
    private static final double SIGMA = 0.5;

    /**
     * Функция ядра, применяемая при решении задачи квадратичного программирования
     */
    private static final KernelFunction kernelFunction = new GaussianKernelFunction(SIGMA);

    /**
     * Ожидаемые значения переменных оптимизации
     */
    private static Map<Pair<String, String>, Double> expectedSolution;

    /**
     * Фабрика объектов предметной области
     */
    private static DataFactory dataFactory;

    /**
     * Средство решения задачи квадратичного программирования
     */
    private static Optimizer optimizer;

    /**
     * Обучающая выборка
     */
    private static PairwiseTrainingSet trainingSet;

    @BeforeClass
    public static void init() {
        Injector injector = Guice.createInjector(Arrays.asList(
                        new DataModule(),
                        new QuadraticProgrammingModule(),
                        new ClassifierImplementationModule())
        );
        dataFactory = injector.getInstance(DataFactory.class);
        optimizer = injector.getInstance(Optimizer.class);

        trainingSet = makeTrainingSet();
        expectedSolution = makeExpectedSolution();
    }

    private static PairwiseTrainingSet makeTrainingSet() {
        LinkedHashSet<Judgement> trainingSet = new LinkedHashSet<>();

        trainingSet.add(dataFactory.newPairwiseTrainingObject(
                makeJudgementItemSet(
                        makeJudgementItem("x1", 11.0),
                        makeJudgementItem("x2", 12.0)
                ),
                makeJudgementItemSet(
                        makeJudgementItem("z1", 1.0)
                )
        ));

        trainingSet.add(dataFactory.newPairwiseTrainingObject(
                makeJudgementItemSet(
                        makeJudgementItem("x1", 11.0),
                        makeJudgementItem("x3", 13.0)
                ),
                makeJudgementItemSet(
                        makeJudgementItem("z1", 1.0),
                        makeJudgementItem("z2", 2.0)
                )
        ));

        return dataFactory.newPairwiseTrainingSet(trainingSet);
    }

    private static Set<UnclassifiedObject> makeJudgementItemSet(UnclassifiedObject... judgements) {
        Set<UnclassifiedObject> set = new LinkedHashSet<>();
        Stream.of(judgements).forEach(set::add);
        return set;
    }

    private static UnclassifiedObject makeJudgementItem(String id, double value) {
        return dataFactory.newUnclassifiedObject(
                id,
                Collections.singleton(dataFactory.newParameter(PARAM_ID, value, BasicDataTypes.REAL))
        );
    }

    private static Map<Pair<String, String>, Double> makeExpectedSolution() {
        Map<Pair<String, String>, Double> solution = new HashMap<>();

        solution.put(new Pair<>("x1", "z1"), 0.0215824);
        solution.put(new Pair<>("x2", "z1"), 0.4136706);
        solution.put(new Pair<>("x1", "z2"), 0.2284175);
        solution.put(new Pair<>("x3", "z1"), 0.0215824);
        solution.put(new Pair<>("x3", "z2"), 0.2284175);

        return solution;
    }

    //-------------------------------------------------------------------------

    /*
     Соответствующее решение задачи на языке R:

     require('quadprog')

     sigma <- 0.5
     fix <- 0.000001

     x1 <- 11
     x2 <- 12
     x3 <- 13
     z1 <- 1
     z2 <- 2

     dvec <- c(1, 1, 1, 1, 1)
     bvec <- c(0, -0.5, 0, -0.5)

     k <- function(a, b) {
	      result <- exp(- ((a - b) * (a - b)) / (2 * sigma * sigma))
          return (result)
     }
     m <- function(x1, z1, x2, z2) {
          result <- k(x1, x2) - k(x1, z2) - k(z1, x2) + k(z1, z2)
          return (result)
     }

     dmat <- matrix(c(
          m(x1, z1, x1, z1) + fix, m(x1, z1, x2, z1), m(x1, z1, x1, z2), m(x1, z1, x3, z1), m(x1, z1, x3, z2),
          m(x2, z1, x1, z1), m(x2, z1, x2, z1) + fix, m(x2, z1, x1, z2), m(x2, z1, x3, z1), m(x2, z1, x3, z2),
          m(x1, z2, x1, z1), m(x1, z2, x2, z1), m(x1, z2, x1, z2) + fix, m(x1, z2, x3, z1), m(x1, z2, x3, z2),
          m(x3, z1, x1, z1), m(x3, z1, x2, z1), m(x3, z1, x1, z2), m(x3, z1, x3, z1) + fix, m(x3, z1, x3, z2),
          m(x3, z2, x1, z1), m(x3, z2, x2, z1), m(x3, z2, x1, z2), m(x3, z2, x3, z1), m(x3, z2, x3, z2) + fix
     ), byrow=T, nrow=5, ncol=5)

     amat <- matrix(c(
          1, 1, 0, 0, 0,
          -1, -1, 0, 0, 0,
          1, 0, 1, 1, 1,
          -1, 0, -1, -1, -1
     ), byrow=T, nrow=4, ncol=5)

     solve.QP(dmat, dvec, t(amat), bvec)

     $solution
     [1] 0.02158241 0.41367060 0.22841759 0.02158241 0.22841759

     $value
     [1] -0.5006529

     $unconstrained.solution
     [1] 0.1059149 0.3092328 0.2605312 0.1059149 0.2605312

     $iterations
     [1] 2 0

     $Lagrangian
     [1] 0.0000000 0.0000000 0.0000000 0.1752705

     $iact
     [1] 4
     */

    @Test
    public void testSolution() throws OptimizationException {
        Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solution =
                optimizer.optimize(trainingSet, kernelFunction, PENALTY);

        Assert.assertEquals(expectedSolution.size(), solution.size());

        for (Pair<UnclassifiedObject, UnclassifiedObject> pair : solution.keySet()) {
            Pair<String, String> key = new Pair<>(pair.first.id(), pair.second.id());

            Assert.assertTrue(expectedSolution.containsKey(key));
            Assert.assertEquals(expectedSolution.get(key), solution.get(pair), PRECISION);
        }
    }
}
