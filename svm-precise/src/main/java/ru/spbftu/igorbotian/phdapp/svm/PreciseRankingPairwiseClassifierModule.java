package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль, содержащий реализацию ранжирующего попарного классификатора, поддерживающего только точные экспертные оценки (негрупповые)
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class PreciseRankingPairwiseClassifierModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(PreciseRankingPairwiseClassifier.class).to(PreciseRankingPairwiseClassifierImpl.class);
    }
}
