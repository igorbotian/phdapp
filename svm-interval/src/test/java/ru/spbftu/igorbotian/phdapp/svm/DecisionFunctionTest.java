package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

/**
 * Модульные тесты для класса <code>DecisionFunction</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see BaseQuadraticProgrammingTest
 */
public class DecisionFunctionTest extends BaseQuadraticProgrammingTest {

    /**
     * Решающая функция, построенная на основе для заданной обучающей выборки
     */
    private DecisionFunction<UnclassifiedObject> decisionFunction;

    @Before
    public void setUp() throws QuadraticProgrammingException {
        decisionFunction = new DecisionFunction<>(qpSolver.solve(trainingSet, kernelFunction, PENALTY), kernelFunction);
    }

    @Test
    public void testPreference() throws DecisionException{
        Assert.assertTrue(decisionFunction.isPreferable(makeJudgementItem("x07", 7.0), makeJudgementItem("x01", 1.0)));
        Assert.assertFalse(decisionFunction.isPreferable(makeJudgementItem("x01", 1.0), makeJudgementItem("x07", 7.0)));

        Assert.assertTrue(decisionFunction.isPreferable(makeJudgementItem("x11", 11.0), makeJudgementItem("x01", 1.0)));
        Assert.assertFalse(decisionFunction.isPreferable(makeJudgementItem("x01", 1.0), makeJudgementItem("x11", 11.0)));

        Assert.assertTrue(decisionFunction.isPreferable(makeJudgementItem("x13", 13.0), makeJudgementItem("x07", 7.0)));
        Assert.assertFalse(decisionFunction.isPreferable(makeJudgementItem("x07", 7.0), makeJudgementItem("x13", 13.0)));
    }
}
