package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.HausdorffDistance;
import ru.spbftu.igorbotian.phdapp.common.Parameter;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Средство вычисления Гауссова ядра для объектов, подлежащих классификации
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class HausdorffGaussianKernelFunctionImpl extends GaussianKernelFunction<UnclassifiedObject> {

    public HausdorffGaussianKernelFunctionImpl(double sigma) {
        super(sigma);
    }

    @Override
    protected BiFunction<UnclassifiedObject, UnclassifiedObject, Double> distanceFunction() {
        return this::hausdorffDistance;
    }

    private Double hausdorffDistance(UnclassifiedObject first, UnclassifiedObject second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        if (!(first instanceof UnclassifiedObjectSet)) {
            throw new IllegalArgumentException("First set should have UnclassifiedObjectSet type: "
                    + first.getClass().getName());
        }

        if (!(second instanceof UnclassifiedObjectSet)) {
            throw new IllegalArgumentException("Second set should have UnclassifiedObjectSet type: "
                    + first.getClass().getName());
        }

        return HausdorffDistance.compute(
                toNumericalVectors((UnclassifiedObjectSet) first),
                toNumericalVectors((UnclassifiedObjectSet) second)
        );
    }

    private Set<List<Double>> toNumericalVectors(UnclassifiedObjectSet set) {
        Set<List<Double>> vectors = new HashSet<>();
        Map<String, Integer> paramIndexes = UnclassifiedObjectUtils.composeMapOfParamIndexes(
                (UnclassifiedObject) set.parameters().iterator().next().value()
        );

        for (Parameter<?> param : set.parameters()) {
            assert param.value() instanceof UnclassifiedObject;
            vectors.add(UnclassifiedObjectUtils.toNumericalVector((UnclassifiedObject) param.value(), paramIndexes));
        }

        return vectors;
    }
}
