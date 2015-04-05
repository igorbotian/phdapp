package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль для генерации выборки для средства кросс-валидации ранжирующего попарного классификатора, поддерживающего
 * только точные экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class SvmValidationPreciseSampleManagementModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(CrossValidationSampleManager.class).to(PreciseCrossValidationSampleManagerImpl.class);
        bind(PreciseCrossValidationSampleManager.class).to(PreciseCrossValidationSampleManagerImpl.class);
    }
}
