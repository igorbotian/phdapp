package ru.spbftu.igorbotian.phdapp.svm.validation.sample;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;

import java.util.Objects;

/**
 * Реализация средства формирования выборки для кросс-валидации классификатора
 */
@Singleton
class CrossValidationSampleManagerImpl implements CrossValidationSampleManager {

    /**
     * Средство генерации выборки для кросс-валидации классификатора
     */
    private CrossValidationSampleGenerator sampleGenerator;

    @Inject
    public CrossValidationSampleManagerImpl(DataFactory dataFactory, MathDataFactory mathDataFactory) {
        sampleGenerator = new CrossValidationSampleGeneratorImpl(
                Objects.requireNonNull(dataFactory),
                Objects.requireNonNull(mathDataFactory)
        );
    }

    @Override
    public void setSampleGenerator(CrossValidationSampleGenerator sampleGenerator) {
        this.sampleGenerator = Objects.requireNonNull(sampleGenerator);
    }

    @Override
    public CrossValidationSampleGenerator sampleGenerator() {
        return sampleGenerator;
    }
}
