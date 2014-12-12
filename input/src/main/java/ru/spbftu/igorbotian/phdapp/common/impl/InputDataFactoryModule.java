package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.InputDataFactory;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Подмодуль фабрики объектов наборов исходных данных
 */
public class InputDataFactoryModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(InputDataFactory.class).to(InputDataFactoryImpl.class);
    }
}
