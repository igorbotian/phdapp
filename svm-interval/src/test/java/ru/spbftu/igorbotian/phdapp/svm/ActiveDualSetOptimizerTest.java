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
    private static final double PRECISION = 0.01;

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

        solution.put(new Pair<>("x1", "z1"), 0.02158369);
        solution.put(new Pair<>("x2", "z1"), 0.41366764);
        solution.put(new Pair<>("x1", "z2"), 0.22841631);
        solution.put(new Pair<>("x3", "z1"), 0.02158369);
        solution.put(new Pair<>("x3", "z2"), 0.22841631);

        return solution;
    }

    //-------------------------------------------------------------------------

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
