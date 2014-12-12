package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Подмодуль фабрики объектов предметной области
 */
public class DataFactoryModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(DataFactory.class).to(DataFactoryImpl.class);
    }
}
