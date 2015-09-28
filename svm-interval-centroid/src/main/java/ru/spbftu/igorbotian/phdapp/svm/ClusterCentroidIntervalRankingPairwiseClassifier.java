package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.common.DataFactory;
import ru.spbftu.igorbotian.phdapp.common.Judgement;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Средство классификации интервальных данных с помощью расстояния между центрами кластеров,
 * в виде которых представляются эти данные
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class ClusterCentroidIntervalRankingPairwiseClassifier extends AbstractIntervalRankingPairwiseClassifier {

    /**
     * Фабрика объектов предметной области
     */
    private final DataFactory dataFactory;

    /**
     * Свободный параметр Гауссова ядра
     */
    private double sigma = IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_DEFAULT_VALUE;

    @Inject
    public ClusterCentroidIntervalRankingPairwiseClassifier(QuadraticProgrammingSolver qpSolver, DataFactory dataFactory) {
        super(Objects.requireNonNull(qpSolver));
        this.dataFactory = Objects.requireNonNull(dataFactory);
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(params);

        sigma = getParameter(IntervalClassifierParameterFactory.GAUSSIAN_KERNEL_PARAM_ID, params);
        super.train(convertToPrecise(trainingSet), params);
    }

    @Override
    public boolean classify(UnclassifiedObject first,
                            UnclassifiedObject second,
                            Set<? extends ClassifierParameter<?>> params)
            throws ClassificationException {

        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        return super.classify(
                new UnclassifiedObjectSet(Collections.singleton(first)),
                new UnclassifiedObjectSet(Collections.singleton(second)),
                params
        );
    }

    private PairwiseTrainingSet convertToPrecise(PairwiseTrainingSet trainingSet) {
        assert trainingSet != null;

        return dataFactory.newPairwiseTrainingSet(
                trainingSet.judgements().stream().map(this::toPreciseJudgement).collect(Collectors.toSet())
        );
    }

    private Judgement toPreciseJudgement(Judgement judgement) {
        assert judgement != null;
        return dataFactory.newJudgement(
                Collections.singleton(new UnclassifiedObjectSet(judgement.preferable())),
                Collections.singleton(new UnclassifiedObjectSet(judgement.inferior()))
        );
    }

    @Override
    protected Kernel<UnclassifiedObject> getKernel() {
        return new GaussianMercerKernel<>(new ClusterGaussianKernelFunctionImpl(sigma));
    }
}
