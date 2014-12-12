package ru.spbftu.igorbotian.phdapp.common;

import ru.spbftu.igorbotian.phdapp.common.impl.InputDataFactoryModule;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль объектов исходных данных
 */
public class InputDataModule extends PhDAppModule {

    @Override
    protected void configure() {
        install(new InputDataFactoryModule());
    }
}
