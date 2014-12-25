package ru.spbftu.igorbotian.phdapp.svm;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация набора параметров конфигурации классификатора
 *
 * @see IntervalClassifierParameterFactory
 */
class IntervalClassifierParameterFactoryImpl extends AbstractClassifierParameterFactory
        implements IntervalClassifierParameterFactory {

    @Override
    public MutableClassifierParameter<Double> penaltyParameter() {
        return penaltyParameter(PENALTY_PARAM_DEFAULT_VALUE);
    }

    @Override
    public MutableClassifierParameter<Double> penaltyParameter(double value) {
        if (value < PENALTY_PARAM_MIN_VALUE || value > PENALTY_PARAM_MAX_VALUE) {
            throw new IllegalArgumentException("Penalty parameter value can't be less than the minimum " +
                    "or greater than the maximum");
        }

        return super.newMutableParameter(PENALTY_PARAM_ID, Double.class,
                value, PENALTY_PARAM_MIN_VALUE, PENALTY_PARAM_MAX_VALUE, Double::compare);
    }

    @Override
    public MutableClassifierParameter<Double> gaussianKernelParameter() {
        return gaussianKernelParameter(GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE);
    }

    @Override
    public MutableClassifierParameter<Double> gaussianKernelParameter(double value) {
        if (value < GAUSSIAN_KERNEL_PARAM_MIN_VALUE || value > GAUSSIAN_KERNEL_PARAM_MAX_VALUE) {
            throw new IllegalArgumentException("Gaussian kernel parameter value can't be less than the minimum " +
                    "or greater than the maximum");
        }

        return new DefaultClassifierParameterImpl<>(GAUSSIAN_KERNEL_PARAM_ID, Double.class,
                value, GAUSSIAN_KERNEL_PARAM_MIN_VALUE, GAUSSIAN_KERNEL_PARAM_MAX_VALUE, Double::compare);
    }

    @Override
    public Set<ClassifierParameter<?>> defaultValues() {
        return Stream.of(
                penaltyParameter(),
                gaussianKernelParameter()
        ).collect(Collectors.toSet());
    }
}
