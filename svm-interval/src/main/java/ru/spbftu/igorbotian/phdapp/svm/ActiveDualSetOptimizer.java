package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.common.Judgement;
import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingSet;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;
import ru.spbftu.igorbotian.phdapp.quadprog.ActiveDualSetAlgorithm;

import java.util.*;

/**
 * Средство решения исходной задачи оптимизации с помощью алгоритма, предложенного Гольфарбом и Иднани
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.quadprog.ActiveDualSetAlgorithm
 */
class ActiveDualSetOptimizer implements Optimizer {

    /**
     * Средство решения задачи квадратичного программирования по методу Гольдфарба и Иднани
     */
    private final ActiveDualSetAlgorithm qpSolver;

    @Inject
    public ActiveDualSetOptimizer(ActiveDualSetAlgorithm qpSolver) {
        this.qpSolver = Objects.requireNonNull(qpSolver);
    }

    @Override
    public Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> optimize(PairwiseTrainingSet trainingSet,
                                                                              KernelFunction kernelFunction,
                                                                              double penalty)
            throws OptimizationException {

        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(kernelFunction);

        Set<Judgement> judgements = new LinkedHashSet<>(trainingSet.judgements());
        Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables = identifyVariables(judgements);

        for(Pair<UnclassifiedObject, UnclassifiedObject> variable : variables) {
            System.out.println(variable);
        }

        double[][] quadraticFunctionMatrix = quadraticFunctionMatrix(variables, kernelFunction);
        double[] quadraticFunctionVector = quadraticFunctionVector(variables);
        double[][] constraintMatrix = constraintMatrix(judgements, variables);
        double[] constraintVector = constraintVector(judgements, penalty);

        try {
            double[] multipliers = qpSolver.apply(
                    quadraticFunctionMatrix,
                    quadraticFunctionVector,
                    constraintMatrix,
                    constraintVector
            );
            System.out.println(Arrays.toString(multipliers));
            return associateMultipliersWithVariables(variables, multipliers);
        } catch (Exception e) {
            throw new OptimizationException("Error occurred while solving dual optimization problem", e);
        }
    }

    /**
     * Соответствие переменным в задаче оптимизации значениям, полученным в ходе её решения
     */
    private Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> associateMultipliersWithVariables(
            Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables, double[] multipliers)
            throws OptimizationException {

        assert variables != null;
        assert !variables.isEmpty();
        assert multipliers != null;

        if (variables.size() != multipliers.length) {
            throw new OptimizationException("Number of computed Lagrangian multipliers is not equal " +
                    "to a number of optimization variables: expected = " + variables.size()
                    + "; actual = " + multipliers.length);
        }

        Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solution = new HashMap<>();
        int i = 0;

        for (Pair<UnclassifiedObject, UnclassifiedObject> variable : variables) {
            solution.put(variable, multipliers[i]);

            i++;
        }

        return solution;
    }

    /**
     * Выявление переменных в задаче оптимизации для конкретной обучающей выборки
     */
    private LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> identifyVariables(Set<Judgement> judgements) {

        assert judgements != null;

        LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> variables = new LinkedHashSet<>();

        for (Judgement judgement : judgements) {
            for (UnclassifiedObject preferable : judgement.preferable()) {
                for (UnclassifiedObject inferior : judgement.inferior()) {
                    variables.add(new Pair<>(preferable, inferior));
                }
            }
        }

        return variables;
    }

    /**
     * Формирование матрицы квадратичной функции
     */
    private double[][] quadraticFunctionMatrix(Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables,
                                               KernelFunction kernelFunction) {
        assert variables != null;
        assert !variables.isEmpty();
        assert kernelFunction != null;

        double[][] matrix = new double[variables.size()][variables.size()];
        int i = 0;

        for (Pair<UnclassifiedObject, UnclassifiedObject> first : variables) {
            int j = 0;

            for (Pair<UnclassifiedObject, UnclassifiedObject> second : variables) {
                matrix[i][j] = MercerKernel.compute(first, second, kernelFunction);
                j++;
            }

            i++;
        }

        return matrix;
    }

    /**
     * Формирование вектора квадратичной функции
     */
    private double[] quadraticFunctionVector(Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables) {
        assert variables != null;
        assert !variables.isEmpty();

        double[] vector = new double[variables.size()];
        Arrays.fill(vector, 1.0);
        return vector;
    }

    /**
     * Формирование матрицы ограничений
     */
    private double[][] constraintMatrix(Set<Judgement> judgements,
                                        Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables) {
        assert judgements != null;
        assert !judgements.isEmpty();
        assert variables != null;
        assert !variables.isEmpty();

        double[][] matrix = new double[judgements.size() * 2][];
        int i = 0;

        for (Judgement judgement : judgements) {
            double[][] constraints = constraintForJudgement(judgement, variables);

            matrix[i] = constraints[0];
            i++;

            matrix[i] = constraints[1];
            i++;
        }

        return matrix;
    }

    /**
     * Формирование двух строк матрицы ограничений для заданного ограничения.
     * Одна строка соответствует нижнему ограничений, а вторая - верхнему
     */
    private double[][] constraintForJudgement(Judgement judgement,
                                              Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables) {
        assert judgement != null;
        assert variables != null;
        assert !variables.isEmpty();

        double[][] row = new double[2][variables.size()];
        int i = 0;

        for (Pair<UnclassifiedObject, UnclassifiedObject> variable : variables) {
            if (judgement.preferable().contains(variable.first)
                    && judgement.inferior().contains(variable.second)) {
                row[0][i] = 1.0;
                row[1][i] = -1.0;
            } else {
                row[0][i] = 0.0;
                row[0][i] = 0.0;
            }

            i++;
        }

        return row;
    }

    /**
     * Формирование вектора ограничений
     */
    private double[] constraintVector(Set<Judgement> judgements, double penalty) {
        assert judgements != null;
        assert !judgements.isEmpty();

        double[] vector = new double[2 * judgements.size()];

        for (int i = 1; i < vector.length; i += 2) {
            vector[i] = -penalty;
        }

        return vector;
    }
}
