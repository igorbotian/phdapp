package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль
 */
public class MathDataFactoryModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(MathDataFactory.class).to(MathDataFactoryImpl.class);
    }
}
