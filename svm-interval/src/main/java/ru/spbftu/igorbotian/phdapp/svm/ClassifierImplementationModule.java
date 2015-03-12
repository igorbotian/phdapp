package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Внутренний модуль реализации средства решения задачи классификации объектов
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class ClassifierImplementationModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(Optimizer.class).to(ActiveDualSetOptimizer.class);
    }
}
