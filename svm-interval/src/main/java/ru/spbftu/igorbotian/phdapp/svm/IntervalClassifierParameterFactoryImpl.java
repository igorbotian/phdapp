package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Реализация набора параметров конфигурации классификатора
 *
 * @see IntervalClassifierParameterFactory
 */
class IntervalClassifierParameterFactoryImpl extends AbstractClassifierParameterFactory
        implements IntervalClassifierParameterFactory {

    @Override
    public MutableClassifierParameter<Double> newConstantCostParameter() {
        return newConstantCostParameter(CONSTANT_COST_PARAM_DEFAULT_VALUE);
    }

    @Override
    public MutableClassifierParameter<Double> newConstantCostParameter(double value) {
        if (value < CONSTANT_COST_PARAM_MIN || value > CONSTANT_COST_PARAM_MAX) {
            throw new IllegalArgumentException("Constant cost parameter value can't be less than the minimum " +
                    "or greater than the maximum");
        }

        return super.newMutableParameter(CONSTANT_COST_PARAM_ID, Double.class,
                value, CONSTANT_COST_PARAM_MIN, CONSTANT_COST_PARAM_MAX, Double::compare);
    }

    @Override
    public MutableClassifierParameter<Double> newGaussianKernelParameter() {
        return newGaussianKernelParameter(GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE);
    }

    @Override
    public MutableClassifierParameter<Double> newGaussianKernelParameter(double value) {
        if (value < GAUSSIAN_KERNEL_PARAM_MIN || value > GAUSSIAN_KERNEL_PARAM_MAX) {
            throw new IllegalArgumentException("Gaussian kernel parameter value can't be less than the minimum " +
                    "or greater than the maximum");
        }

        return new DefaultClassifierParameterImpl<>(GAUSSIAN_KERNEL_PARAM_ID, Double.class,
                value, GAUSSIAN_KERNEL_PARAM_MIN, GAUSSIAN_KERNEL_PARAM_MAX, Double::compare);
    }
}
