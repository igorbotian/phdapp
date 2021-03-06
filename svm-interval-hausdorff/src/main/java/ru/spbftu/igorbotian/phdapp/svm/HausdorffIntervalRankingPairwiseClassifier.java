package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;
import java.util.Set;

/**
 * @author Igor Botian <igor.botian@gmail.com>
 */
@Singleton
public class HausdorffIntervalRankingPairwiseClassifier extends AbstractIntervalRankingPairwiseClassifier {

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Свободный параметр Гауссова ядра
     */
    private double sigma = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE;

    @Inject
    public HausdorffIntervalRankingPairwiseClassifier(QuadraticProgrammingSolver qpSolver, DataFactory dataFactory) {
        super(Objects.requireNonNull(qpSolver));
        this.dataFactory = Objects.requireNonNull(dataFactory);
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        sigma = getParameter(IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID, params);
        super.train(HausdorffTrainingSetTransformer.transformToPrecise(trainingSet, dataFactory), params);
    }

    @Override
    protected Kernel<UnclassifiedObject> getKernel() {
        return new GaussianMercerKernel<>(new GaussianKernelFunctionImpl(sigma));
    }
}
