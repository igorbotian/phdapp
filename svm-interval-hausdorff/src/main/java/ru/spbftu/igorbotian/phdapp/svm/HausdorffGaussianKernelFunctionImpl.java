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
        assert set != null;

        Set<List<Double>> vectors = new HashSet<>();

        for (Parameter<?> param : set.parameters()) {
            assert param.value() instanceof UnclassifiedObject;
            vectors.add(asVectorParams(UnclassifiedObjectUtils.toNumericalVector((UnclassifiedObject) param.value())));
        }

        return vectors;
    }

    private List<Double> asVectorParams(double[] values) {
        List<Double> vectorParams = new ArrayList<>(values.length);

        for (double value : values) {
            vectorParams.add(value);
        }

        return vectorParams;
    }
}
