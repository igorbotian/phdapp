package ru.spbftu.igorbotian.phdapp.svm;

/**
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ClusterCentroidIntervalRankingPairwiseClassifierModule extends IntervalPairwiseClassifierModule {

    @Override
    protected void bindIntervalRankingPairwiseClassifier() {
        bind(IntervalRankingPairwiseClassifier.class).to(ClusterCentroidIntervalRankingPairwiseClassifier.class);
    }
}
