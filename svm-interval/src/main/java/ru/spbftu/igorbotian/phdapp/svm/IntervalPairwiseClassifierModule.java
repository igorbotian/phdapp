package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingModule;

/**
 * Модуль, содержащий реализацю попарного классификатора, который поддерживает интервальные экспертные оценки
 */
public class IntervalPairwiseClassifierModule extends PhDAppModule {

    @Override
    protected void configure() {
        install(new QuadraticProgrammingModule());
        install(new ClassifierImplementationModule());
        bind(IntervalClassifierParameterFactory.class).to(IntervalClassifierParameterFactoryImpl.class);
        bind(PairwiseClassifier.class).to(IntervalPairwiseClassifier.class);
    }
}
