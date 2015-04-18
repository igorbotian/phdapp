package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактная еализация ранжирующего попарного классификатора, поддерживающего групповые экспертные оценки
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see IntervalRankingPairwiseClassifier
 */
public abstract class AbstractIntervalRankingPairwiseClassifier implements IntervalRankingPairwiseClassifier {

    /**
     * Решающая функция, построенная на основе результатов обучения классификатора с помощью заданной обучающей выбороки
     */
    private DecisionFunction<UnclassifiedObject> decisionFunction;

    /**
     * Средство решения задачи квадратичного программирования, применяемое в ходе построения решающей функции
     * для заданной обучающей выборки
     */
    private QuadraticProgrammingSolver qpSolver;

    public AbstractIntervalRankingPairwiseClassifier(QuadraticProgrammingSolver qpSolver) {
        this.qpSolver = Objects.requireNonNull(qpSolver);
    }

    @Override
    public void train(PairwiseTrainingSet trainingSet, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(params);

        double penalty = getParameter(IntervalClassifierParameterFactory.PENALTY_PARAM_ID, params);
        decisionFunction = buildDecisionFunction(trainingSet, penalty);
    }

    /**
     * Получение ядра, применяемого в процессе решения задачи квадратичного программирования
     */
    protected abstract KernelFunction<UnclassifiedObject> getKernelFunction();

    private DecisionFunction<UnclassifiedObject> buildDecisionFunction(PairwiseTrainingSet trainingSet,
                                                                       double penalty)
            throws ClassifierTrainingException {

        assert trainingSet != null;

        try {
            KernelFunction<UnclassifiedObject> kernelFunction = getKernelFunction();
            Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> lagrangianMultipliers
                    = qpSolver.solve(trainingSet, kernelFunction, penalty);

            return new DecisionFunction<>(lagrangianMultipliers, kernelFunction);
        } catch (QuadraticProgrammingException e) {
            throw new ClassifierTrainingException("Can't build a decision function for a given training set", e);
        }
    }

    protected double getParameter(String paramId, Set<? extends ClassifierParameter<?>> params)
            throws ClassifierTrainingException {

        assert paramId != null;

        for (ClassifierParameter param : params) {
            if (paramId.equals(param.name())) {
                if (!Double.class.getName().equals(param.valueClass().getName())) {
                    throw new ClassifierTrainingException("Parameter " + paramId + " should be of double type");
                }

                return (Double) param.value();
            }
        }

        throw new ClassifierTrainingException(paramId + " parameter should be set");
    }

    @Override
    public boolean classify(UnclassifiedObject first, UnclassifiedObject second,
                            Set<? extends ClassifierParameter<?>> params) throws ClassificationException {

        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        Objects.requireNonNull(params);

        return decisionFunction.isPreferable(first, second);
    }
}
