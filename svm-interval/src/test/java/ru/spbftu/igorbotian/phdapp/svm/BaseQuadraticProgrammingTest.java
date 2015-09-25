package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Before;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

import java.util.Collections;
import java.util.Map;

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
    protected static DataFactory dataFactory;
    /**
     * Средство решения задачи квадратичного программирования
     */
    protected static QuadraticProgrammingSolver qpSolver;
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
        //
    }

    protected static UnclassifiedObject makeJudgementItem(String id, double value) {
        return dataFactory.newUnclassifiedObject(
                id,
                Collections.singleton(dataFactory.newParameter(PARAM_ID, value, BasicDataTypes.REAL))
        );
    }
}
