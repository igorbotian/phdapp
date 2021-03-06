package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.EuclideanDistance;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Средство вычисления Гауссова ядра для объектов, подлежащих классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class GaussianKernelFunctionImpl extends GaussianKernelFunction<UnclassifiedObject> {

    public GaussianKernelFunctionImpl(double sigma) {
        super(sigma);
    }

    @Override
    protected BiFunction<UnclassifiedObject, UnclassifiedObject, Double> distanceFunction() {
        return this::euclideanDistance;
    }

    private Double euclideanDistance(UnclassifiedObject x, UnclassifiedObject y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);

        Map<String, Integer> paramIndexes = UnclassifiedObjectUtils.composeMapOfParamIndexes(x);

        return EuclideanDistance.compute(
                UnclassifiedObjectUtils.toNumericalVector(x, paramIndexes),
                UnclassifiedObjectUtils.toNumericalVector(y, paramIndexes)
        );
    }
}
