package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль, содержащий реализацю попарного классификатора, который поддерживает интервальные экспертные оценки
 */
public class IntervalPairwiseClassifierModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(IntervalClassifierParameterFactory.class).to(IntervalClassifierParameterFactoryImpl.class);
        bind(PairwiseClassifier.class).to(IntervalPairwiseClassifier.class);
    }
}
