package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль формирования различных отчётов по работе классификатора
 */
public class SvmValidationReportManagementModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(ReportFactory.class).to(ReportFactoryImpl.class);
    }
}
