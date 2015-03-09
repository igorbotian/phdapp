package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * Матрица ядер, используемая в алгоритме Гольдфарба-Иднани для решения задачи квадратичного программирования.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class KernelMatrix {

    /**
     * Функция ядра
     */
    private final KernelFunction kernelFunction;

    /**
     * Сочетание из элементов множеств, составляющих заданную экспертную оценку (одно из них предпочтительнее другого)
     */
    private final LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> combinationOfAllJudgementElements;

    /**
     * Создание матрицы ядер на основе заданной обучающей выборки
     *
     * @param trainingSet обучающая выборка
     * @throws NullPointerException     если обучающая выборка не задана
     * @throws IllegalArgumentException если обучающая выборка пустая
     */
    public KernelMatrix(LinkedHashSet<? extends PairwiseTrainingObject> trainingSet, KernelFunction kernelFunction) {
        Objects.requireNonNull(kernelFunction);
        Objects.requireNonNull(trainingSet);

        if (trainingSet.isEmpty()) {
            throw new IllegalArgumentException("Training set cannot be empty");
        }

        this.kernelFunction = kernelFunction;
        this.combinationOfAllJudgementElements = combinationOfAllJudgementElements(trainingSet);
    }

    /**
     * Получение значение элементов матрицы
     *
     * @return двумерная матрица, элементы которой являются вещественными числами
     */
    public double[][] values() {
        int size = combinationOfAllJudgementElements.size();
        double[][] matrix = new double[size][size];
        int i = 0;

        for (Pair<UnclassifiedObject, UnclassifiedObject> pair : combinationOfAllJudgementElements) {
            int j = 0;

            for (Pair<UnclassifiedObject, UnclassifiedObject> anotherPair : combinationOfAllJudgementElements) {
                matrix[i][j] = computeKernelValue(pair, anotherPair);
                j++;
            }

            i++;
        }

        return matrix;
    }

    /**
     * Вычисление ядра для заданных пар элементов обучающей выборки
     */
    private double computeKernelValue(Pair<UnclassifiedObject, UnclassifiedObject> first,
                                      Pair<UnclassifiedObject, UnclassifiedObject> second) {
        assert first != null;
        assert second != null;

        if (first.equals(second)) {
            return 1.0;
        }

        double[] x1 = makeParameterVector(first.first);
        double[] z1 = makeParameterVector(first.second);
        double[] x2 = makeParameterVector(second.first);
        double[] z2 = makeParameterVector(second.second);

        return kernelFunction.compute(x1, x2) - kernelFunction.compute(x1, z2)
                - kernelFunction.compute(z1, x2) - kernelFunction.compute(z1, z2);
    }

    private double[] makeParameterVector(UnclassifiedObject obj) {
        assert obj != null;

        double[] result = new double[obj.parameters().size()];
        int i = 0;

        for (Parameter param : obj.parameters()) { // TODO non-linked structure
            if (!BasicDataTypes.REAL.equals(param.valueType())
                    && !BasicDataTypes.INTEGER.equals(param.valueType())) {
                throw new IllegalArgumentException("Unsupported parameter value type: "
                        + param.valueType().name());
            }

            result[i] = Double.valueOf(param.value().toString());
            i++;
        }

        return result;
    }

    /**
     * Сочетание из элементов множеств, составляющих заданную экспертную оценку (одно из них предпочтительнее другого)
     *
     * @return множество из пар, в которых первый элемент взят из множества предпочтительных элементов какой-либо
     * экспертной оценки, а второй - из множества менее предпочтительных элементов этой или другой экспертной оценки
     */
    private LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> combinationOfAllJudgementElements(
            LinkedHashSet<? extends PairwiseTrainingObject> trainingSet) {

        assert trainingSet != null;

        LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> result = new LinkedHashSet<>();

        for (PairwiseTrainingObject judgement : trainingSet) {
            for (UnclassifiedObject preferableElement : judgement.preferable()) {
                for (PairwiseTrainingObject anotherJudgement : trainingSet) {
                    for (UnclassifiedObject inferiorElement : anotherJudgement.inferior()) {
                        result.add(new Pair<>(preferableElement, inferiorElement));
                    }
                }
            }
        }

        return result;
    }
}
