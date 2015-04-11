package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

/**
 * Модуль для генерации выборки для средства кросс-валидации ранжирующего попарного классификатора, поддерживающего
 * только точные экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class SvmValidationPreciseSampleManagementModule extends SvmValidationIntervalSampleManagementModule {

    @Override
    protected void configure() {
        super.configure();
        bind(PreciseCrossValidationSampleManager.class).to(PreciseCrossValidationSampleManagerImpl.class);
    }

    @Override
    protected void bindCrossValidationSampleManager() {
        bind(CrossValidationSampleManager.class).to(PreciseCrossValidationSampleManagerImpl.class);
    }
}
