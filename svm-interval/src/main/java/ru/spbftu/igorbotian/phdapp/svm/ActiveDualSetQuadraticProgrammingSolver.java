package ru.spbftu.igorbotian.phdapp.svm;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.quadprog.ActiveDualSetAlgorithm;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Средство решения задачи квадратичного программирования по методу, предложенному Гольфарбом и Иднани
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see ru.spbftu.igorbotian.phdapp.quadprog.ActiveDualSetAlgorithm
 */
class ActiveDualSetQuadraticProgrammingSolver implements QuadraticProgrammingSolver {

    /**
     * Количество знаков после запятой после округления значений
     */
    private static final int PRECISION = 10;

    private static final double TINY_VALUE = 1 / Double.parseDouble("1" + StringUtils.repeat('0', PRECISION));

    /**
     * Средство решения задачи квадратичного программирования
     */
    private final ActiveDualSetAlgorithm qpSolver;

    @Inject
    public ActiveDualSetQuadraticProgrammingSolver(ActiveDualSetAlgorithm qpSolver) {
        this.qpSolver = Objects.requireNonNull(qpSolver);
    }

    @Override
    public Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solve(PairwiseTrainingSet trainingSet,
                                                                           Kernel<UnclassifiedObject> kernel,
                                                                           double penalty)
            throws QuadraticProgrammingException {

        Objects.requireNonNull(trainingSet);
        Objects.requireNonNull(kernel);

        Set<Judgement> judgements = new LinkedHashSet<>(trainingSet.judgements());
        Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables = identifyVariables(judgements);

        double[][] qfMatrix = quadraticFunctionMatrix(variables, kernel);
        double[] qfVector = quadraticFunctionVector(variables);
        double[][] cMatrix = constraintMatrix(judgements, variables);
        double[] cVector = constraintVector(judgements, penalty);

        try {
            return solve(variables, qfMatrix, qfVector, cMatrix, cVector);
        } catch (QuadraticProgrammingException e) {
            if (!MatrixUtils.isPositiveDefinite(qfMatrix)) {
                tryToFixPositiveDefinition(qfMatrix);

                try {
                    return solve(variables, qfMatrix, qfVector, cMatrix, cVector);
                } catch (QuadraticProgrammingException ex) {
                    if(!MatrixUtils.isPositiveDefinite(qfMatrix)) {
                        throw new QuadraticProgrammingException("Quadratic function matrix should be positive definite");
                    }
                }
            }

            throw new QuadraticProgrammingException("Error occurred while solving dual optimization problem", e);
        }
    }

    /**
     * Попытка эквивалентного преобразования матрицы с целью приведения её к виду положительно определённой матрицы
     */
    private void tryToFixPositiveDefinition(double[][] matrix) {
        add(matrix, Math.abs(findMin(matrix)));
        fixZeroes(matrix);
    }

    /**
     * Поиск в матрице ячейки с наименьшим значением
     */
    private double findMin(double[][] matrix) {
        double min = Double.MAX_VALUE;

        for (double[] row : matrix) {
            for (double value : row) {
                if (value < min) {
                    min = value;
                }
            }
        }

        return min;
    }

    /**
     * Добавление к значению каждой ячейки матрицы заданного числа
     */
    private void add(double[][] matrix, double value) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] += value;
            }
        }
    }

    /**
     * Замена каждого нулевого значения в матрице на некое малое число
     */
    private void fixZeroes(double[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if(Math.abs(matrix[i][j]) == 0.0) {
                    matrix[i][j] = TINY_VALUE;
                }
            }
        }
    }

    /**
     * Решение задачи квадратичного программирования заданным способом
     */
    private Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> solve(
            Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables, double[][] qfMatrix, double[] qfVector,
            double[][] cMatrix, double[] cVector)
            throws QuadraticProgrammingException {

        double[] multipliers = qpSolver.apply(
                qfMatrix,
                qfVector,
                cMatrix,
                cVector
        );

        return associateMultipliersWithVariables(variables, multipliers);
    }

    /**
     * Соответствие неизвестных в задаче квадратичного программирования значениям, полученным в ходе её решения
     */
    private Map<Pair<UnclassifiedObject, UnclassifiedObject>, Double> associateMultipliersWithVariables(
            Set<Pair<UnclassifiedObject, UnclassifiedObject>> variables, double[] multipliers)
            throws QuadraticProgrammingException {

        assert variables != null;
        assert !variables.isEmpty();
        assert multipliers != null;

        if (variables.size() != multipliers.length) {
            throw new QuadraticProgrammingException("Number of computed Lagrangian multipliers is not equal " +
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
     * Выявление неизвестных в задаче квадратичного программирования для конкретной обучающей выборки
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
                                               Kernel<UnclassifiedObject> kernel) {
        assert variables != null;
        assert !variables.isEmpty();
        assert kernel != null;

        List<Pair<UnclassifiedObject, UnclassifiedObject>> variablesList = variables.stream().collect(Collectors.toList());
        double[][] matrix = new double[variables.size()][variables.size()];

        for (int i = 0; i < variablesList.size(); i++) {
            for (int j = i; j < variablesList.size(); j++) {
                matrix[i][j] = matrix[j][i] = round(kernel.compute(variablesList.get(i), variablesList.get(j)), PRECISION);
            }
        }

        return matrix;
    }

    private double round(double value, int mantissa) {
        double scale = Math.pow(10.0, mantissa);
        return Math.round(value * scale) / scale;
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
     * Одна строка соответствует нижнему ограничению, а вторая - верхнему
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
