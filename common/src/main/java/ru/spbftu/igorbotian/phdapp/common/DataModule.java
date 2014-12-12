package ru.spbftu.igorbotian.phdapp.common;

import ru.spbftu.igorbotian.phdapp.common.impl.DataFactoryModule;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль объектов предметной области
 */
public class DataModule extends PhDAppModule {

    @Override
    protected void configure() {
        install(new DataFactoryModule());
    }
}
