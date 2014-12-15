package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Базовый модуль средств кросс-валидации классификатора
 */
public class SvmValidationCommonsModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(CrossValidatorParameterFactory.class).to(CrossValidatorParameterFactoryImpl.class);
    }
}
