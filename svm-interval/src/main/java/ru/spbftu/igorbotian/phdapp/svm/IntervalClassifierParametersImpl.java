package ru.spbftu.igorbotian.phdapp.svm;

/**
 * Реализация набора параметров конфигурации классификатора
 *
 * @see IntervalClassifierParameters
 */
class IntervalClassifierParametersImpl extends AbstractClassifierParameterFactory
        implements IntervalClassifierParameters {

    @Override
    public MutableClassifierParameter<Double> constantCostParameter() {
        return super.newMutableParameter("constantCostParameter", Double.class,
                100.0, -1000000.0, 1000000.0, Double::compare);
    }

    @Override
    public MutableClassifierParameter<Double> gaussianKernelParameter() {
        return new DefaultClassifierParameterImpl<>("gaussianKernelParameter", Double.class,
                0.1, -1000000.0, 1000000.0, Double::compare);
    }
}
