package ru.spbftu.igorbotian.phdapp.svm;

import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Тест для средства классификации данных, в котором используется расстояние между центрами кластеров.
 * В данном тесте обучающая выборка состоит только из точных оценок предпочтений.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ClusterCentroidPreciseRankingPairwiseClassifierTest extends BasePreciseRankingPairwiseClassifierTest {

    @Override
    protected PhDAppModule rankingPairwiseClassifierModule() {
        return new ClusterCentroidIntervalRankingPairwiseClassifierModule();
    }

    @Test
    public void testClassifier() throws ClassificationException {
        super.testClassifier();
    }
}
