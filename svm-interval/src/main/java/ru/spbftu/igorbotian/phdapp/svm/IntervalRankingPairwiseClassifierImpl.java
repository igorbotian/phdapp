package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Objects;
import java.util.Set;

/**
 * Реализация ранжирующего попарного классификатора, поддерживающего групповые экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see AbstractIntervalRankingPairwiseClassifier
 */
class IntervalRankingPairwiseClassifierImpl extends AbstractIntervalRankingPairwiseClassifier {

    /**
     * Свободный параметр Гауссова ядра
     */
    private double sigma = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE;

    @Inject
    public IntervalRankingPairwiseClassifierImpl(QuadraticProgrammingSolver qpSolver) {
        super(Objects.requireNonNull(qpSolver));
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        sigma = getParameter(IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID, params);
        super.train(trainingSet, params);
    }

    @Override
    protected Kernel<UnclassifiedObject> getKernel() {
        return new GaussianMercerKernel<>(new GaussianKernelFunctionImpl(sigma));
    }
}
