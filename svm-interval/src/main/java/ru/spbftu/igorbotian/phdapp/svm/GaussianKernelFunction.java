package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.BasicDataTypes;
import ru.spbftu.igorbotian.phdapp.common.GaussianKernel;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;

/**
 * Функция Гауссова ядра
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class GaussianKernelFunction implements KernelFunction<UnclassifiedObject> {

    /**
     * Свободный параметр
     */
    private final double sigma;

    public GaussianKernelFunction(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public double compute(double[] x, double[] y) {
        return GaussianKernel.compute(x, y, sigma);
    }

    @Override
    public double compute(UnclassifiedObject x, UnclassifiedObject y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);

        return GaussianKernel.compute(parametersOf(x), parametersOf(y), sigma);
    }

    private double[] parametersOf(UnclassifiedObject obj) {
        assert obj != null;
        double[] result = new double[obj.parameters().size()];
        int i = 0;

        for (Parameter param : obj.parameters()) {
            if (param.valueType().equals(BasicDataTypes.INTEGER)) {
                result[i] = (Integer) param.value();
            } else if (param.valueType().equals(BasicDataTypes.REAL)) {
                result[i] = (Double) param.value();
            } else {
                throw new RuntimeException("Unsupported parameter type: " + param.valueType());
            }

            i++;
        }

        return result;
    }
}
