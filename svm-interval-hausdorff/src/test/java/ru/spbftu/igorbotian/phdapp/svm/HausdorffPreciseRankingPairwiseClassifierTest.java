package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Тест для средства классификации данных, в котором используется расстояние Хаусдорфа.
 * В данном тесте обучающая выборка состоит только из точных оценок предпочтений.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class HausdorffPreciseRankingPairwiseClassifierTest extends BasePreciseRankingPairwiseClassifierTest {

    @Override
    protected PhDAppModule rankingPairwiseClassifierModule() {
        return new IntervalPairwiseClassifierModule();
    }

    @Test
    public void testClassifier() throws ClassificationException {
        super.testClassifier();
    }
}
