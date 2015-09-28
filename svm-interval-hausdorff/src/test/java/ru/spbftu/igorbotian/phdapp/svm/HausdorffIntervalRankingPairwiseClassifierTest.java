package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Тест для средства классификации данных, использующего расстояние Хаусдорфа, в случае интервального характера данных
 * в обучающей выборке
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class HausdorffIntervalRankingPairwiseClassifierTest extends BaseIntervalRankingPairwiseClassifierTest {

    @Override
    protected PhDAppModule rankingPairwiseClassifierModule() {
        return new IntervalPairwiseClassifierModule();
    }

    @Test
    public void testClassifier() throws ClassificationException {
        super.testClassifier();
    }
}
