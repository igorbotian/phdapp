package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactoryModule;

/**
 * Модуль для генерации выборки для средства кросс-валидации классификатора
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.sample.SampleGenerator
 */
public class SvmValidationSampleManagementModule extends PhDAppModule {

    @Override
    protected void configure() {
        install(new MathDataFactoryModule());
        bind(SampleGenerator.class, SampleGeneratorImpl.class);
    }
}
