package ru.spbftu.igorbotian.phdapp.svm.validation;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль, содержащий механизм параметризации средства кросс-валидации классификатора
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class CrossValidationParametrizationModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(CrossValidatorParameterFactory.class).to(CrossValidatorParameterFactoryImpl.class);
    }
}
