package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль для генерации выборки для средства кросс-валидации ранжирующего попарного классификатора, поддерживающего
 * групповые экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class SvmValidationIntervalSampleManagementModule extends PhDAppModule {

    @Override
    protected void configure() {
        bindCrossValidationSampleManager();
        bind(IntervalCrossValidationSampleManager.class).to(IntervalCrossValidationSampleManagerImpl.class);
    }

    protected void bindCrossValidationSampleManager() {
        bind(CrossValidationSampleManager.class).to(IntervalCrossValidationSampleManagerImpl.class);
    }
}
