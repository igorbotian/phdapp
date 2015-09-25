package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingModule;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Базовый класс для модульных тестов в данном пакете.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public abstract class BaseQuadraticProgrammingTest {

    /**
     * Идентификатора тестового параметра
     */
    protected static final String PARAM_ID = "test_param";
    /**
     * Значение параметра штрафа
     */
    protected static final double PENALTY = 0.5;
    /**
     * Значение свободного параметра Гауссова ядра
     */
    protected static final double SIGMA = 0.5;
    /**
     * Фабрика объектов предметной области
     */
    protected DataFactory dataFactory;
    /**
     * Средство решения задачи квадратичного программирования
     */
    protected QuadraticProgrammingSolver qpSolver;
    /**
     * Точность сравнения вещественных чисел
     */
    protected final double PRECISION = 0.00001;
    /**
     * Ядро, применяемое при решении задачи квадратичного программирования
     */
    protected Kernel<UnclassifiedObject> kernel;

    /**
     * Ожидаемые значения переменных оптимизации
     */
    protected Map<Pair<String, String>, Double> expectedSolution;

    /**
     * Обучающая выборка
     */
    protected PairwiseTrainingSet trainingSet;

    @Before
    public void setUp() throws QuadraticProgrammingException {
        Injector injector = createInjector();
        dataFactory = injector.getInstance(DataFactory.class);
        qpSolver = injector.getInstance(QuadraticProgrammingSolver.class);
        trainingSet = makeTrainingSet();
        expectedSolution = makeExpectedSolution();
    }

    private Injector createInjector() {
        Set<PhDAppModule> modules = new LinkedHashSet<>();
        modules.add(new ApplicationConfigurationModule(Paths.get("..")));
        modules.add(new DataModule());
        modules.add(new QuadraticProgrammingModule());
        modules.addAll(injectModules());

        return Guice.createInjector(modules);
    }

    protected abstract Set<PhDAppModule> injectModules();

    protected abstract PairwiseTrainingSet makeTrainingSet();

    protected abstract Map<Pair<String, String>, Double> makeExpectedSolution();

    protected UnclassifiedObject makeJudgementItem(String id, double value) {
        return dataFactory.newUnclassifiedObject(
                id,
                Collections.singleton(dataFactory.newParameter(PARAM_ID, value, BasicDataTypes.REAL))
        );
    }

    protected void testSolution() throws QuadraticProgrammingException {
        Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solution =
                qpSolver.solve(trainingSet, kernel, PENALTY);

        Assert.assertEquals(expectedSolution.size(), solution.size());

        for (Pair<UnclassifiedObject, UnclassifiedObject> pair : solution.keySet()) {
            Pair<String, String> key = new Pair<>(pair.first.id(), pair.second.id());

            Assert.assertTrue(expectedSolution.containsKey(key));
            Assert.assertEquals(expectedSolution.get(key), solution.get(pair), PRECISION);
        }
    }
}
