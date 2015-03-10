package ru.spbftu.igorbotian.phdapp.quadprog;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль, предоставляющий средства решения задач квадратичного программирования
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class QuadraticProgrammingModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(QuadraticProgrammingSolver.class).to(QuadraticProgrammingSolverImpl.class);
    }
}
