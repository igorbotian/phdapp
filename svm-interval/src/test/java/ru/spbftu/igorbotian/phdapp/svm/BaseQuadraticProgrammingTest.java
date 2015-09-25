package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.BeforeClass;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingModule;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Базовый класс для модульных тестов в данном пакете.
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
public abstract class BaseQuadraticProgrammingTest {

    /**
     * Идентификатора тестового параметра
     */
    protected static final String PARAM_ID = "test_param";

    /**
     * Точность сравнения вещественных чисел
     */
    protected final double PRECISION = 0.00001;

    /**
     * Значение параметра штрафа
     */
    protected static final double PENALTY = 0.5;

    /**
     * Значение свободного параметра Гауссова ядра
     */
    protected static final double SIGMA = 0.5;

    /**
     * Ядро, применяемое при решении задачи квадратичного программирования
     */
    protected static final Kernel<UnclassifiedObject> kernel
            = new GaussianMercerKernel<>(new GaussianKernelFunctionImpl(SIGMA));

    /**
     * Ожидаемые значения переменных оптимизации
     */
    protected static Map<Pair<String, String>, Double> expectedSolution;

    /**
     * Фабрика объектов предметной области
     */
    protected static DataFactory dataFactory;

    /**
     * Средство решения задачи квадратичного программирования
     */
    protected static QuadraticProgrammingSolver qpSolver;

    /**
     * Обучающая выборка
     */
    protected static PairwiseTrainingSet trainingSet;

    @BeforeClass
    public static void init() {
        Injector injector = Guice.createInjector(Arrays.asList(
                        new ApplicationConfigurationModule(Paths.get("..")),
                        new DataModule(),
                        new QuadraticProgrammingModule(),
                        new IntervalPairwiseClassifierModule())
        );
        dataFactory = injector.getInstance(DataFactory.class);
        qpSolver = injector.getInstance(QuadraticProgrammingSolver.class);

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

    protected static UnclassifiedObject makeJudgementItem(String id, double value) {
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
}
