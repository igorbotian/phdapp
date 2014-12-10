package ru.spbftu.igorbotian.phdapp.svm.analytics.report;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Подмодуль фабрики различных отчётов по работе классификатора
 */
public class ReportFactoryModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(ReportFactory.class).to(ReportFactoryImpl.class);
    }
}
