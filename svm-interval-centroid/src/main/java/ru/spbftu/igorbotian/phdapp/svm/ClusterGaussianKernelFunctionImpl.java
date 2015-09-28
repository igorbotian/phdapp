package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ClusterGaussianKernelFunctionImpl extends GaussianKernelFunction<UnclassifiedObject> {

    public ClusterGaussianKernelFunctionImpl(double sigma) {
        super(sigma);
    }

    @Override
    protected BiFunction<UnclassifiedObject, UnclassifiedObject, Double> distanceFunction() {
        return this::computeDistance;
    }

    @SuppressWarnings("unchecked")
    private double computeDistance(UnclassifiedObject first, UnclassifiedObject second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        if(!(first instanceof UnclassifiedObjectSet)) {
            throw new IllegalArgumentException("First parameter should be a set of unclassified objects");
        }

        if(!(second instanceof UnclassifiedObjectSet)) {
            throw new IllegalArgumentException("Second parameter should be a set of unclassified objects");
        }

        return ClusterCentroid.computeDistanceBetween(
                (Set<? extends UnclassifiedObject>) first.parameters().iterator().next().value(),
                (Set<? extends UnclassifiedObject>) second.parameters().iterator().next().value()
        );
    }
}
