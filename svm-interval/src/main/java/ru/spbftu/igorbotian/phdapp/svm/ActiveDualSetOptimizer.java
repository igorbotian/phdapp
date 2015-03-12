package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.common.Judgement;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.quadprog.ActiveDualSetAlgorithm;

import java.util.*;

/**
 * Средство решения исходной задачи оптимизации с помощью алгоритма, предложенного Гольфарбом и Иднани
 *
 * @see ru.spbftu.igorbotian.phdapp.quadprog.ActiveDualSetAlgorithm
 * @author Igor Botian <igor.botian@gmail.com>
 */
class ActiveDualSetOptimizer implements Optimizer {

    private final ActiveDualSetAlgorithm qpSolver;

    @Inject
    public ActiveDualSetOptimizer(ActiveDualSetAlgorithm qpSolver) {
        this.qpSolver = Objects.requireNonNull(qpSolver);
    }

    @Override
    public Map<Judgement, List<Double>> optimize(PairwiseTrainingSet trainingSet, KernelFunction kernelFunction,
                                                 double penaltyParameter) throws OptimizationException {
        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(kernelFunction);

        LinkedHashSet<? extends Judgement> judgements = new LinkedHashSet<>(trainingSet.judgements());
        QuadraticFunctionMatrix quadProgMatrix = new QuadraticFunctionMatrix(judgements, kernelFunction);
        QuadraticFunctionVector quadProgVector = new QuadraticFunctionVector(judgements);
        ConstraintMatrix constraintMatrix = new ConstraintMatrix(penaltyParameter, judgements);

        try {
            double[] multipliers = qpSolver.apply(
                    quadProgMatrix.values(),
                    quadProgVector.values(),
                    constraintMatrix.values(),
                    constraintMatrix.constraints()
            );
            return associateMultipliersWithJudgements(judgements, multipliers);
        } catch (Exception e) {
            throw new OptimizationException("Error occurred while solving dual optimization problem", e);
        }
    }

    private Map<Judgement, List<Double>> associateMultipliersWithJudgements(Set<? extends Judgement> judgements,
                                                                            double[] multipliers) {
        assert judgements != null;
        assert multipliers != null;

        Map<Judgement, List<Double>> result = new LinkedHashMap<>();

        // TODO

        return result;
    }
}
