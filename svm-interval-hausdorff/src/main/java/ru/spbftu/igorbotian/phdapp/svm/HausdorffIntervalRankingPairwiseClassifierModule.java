package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Модуль, содержащий реализацию классификатора
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class HausdorffIntervalRankingPairwiseClassifierModule extends IntervalPairwiseClassifierModule {

    @Override
    protected void bindIntervalRankingPairwiseClassifier() {
        bind(IntervalRankingPairwiseClassifier.class).to(HausdorffIntervalRankingPairwiseClassifier.class);
    }
}
