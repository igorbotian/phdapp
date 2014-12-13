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
        return super.newMutableParameter("constantCostParameter", Double.class,
                100.0, -1000000.0, 1000000.0, Double::compare);
    }

    @Override
    public MutableClassifierParameter<Double> newGaussianKernelParameter() {
        return new DefaultClassifierParameterImpl<>("gaussianKernelParameter", Double.class,
                0.1, -1000000.0, 1000000.0, Double::compare);
    }
}
