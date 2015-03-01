/*
 * Copyright (c) 2015 Igor Botian
 *
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.common.Combination;
import ru.spbftu.igorbotian.phdapp.common.Pair;
import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingObject;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.*;

/**
 * Матрица ограничений, используемая в алгоритме Гольдфарба-Иднани для решения задачи квадратичного программирования.
 * Содержит информацию об ограничениях вида 0 <= A1 + A2 + ... <= C, где Ai - искомые величины, а C - параметр штрафа
 * (постоянной стоимости).
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class ConstraintMatrix {

    /**
     * Параметр штрафа (постоянной стоимости)
     */
    private final double penaltyParameter;

    /**
     * Множество экспертных оценок с учётом порядка, заданного пользователем
     */
    private final LinkedHashSet<PairwiseTrainingObject> judgements;

    /**
     * Ассоциативный массив, ключом которого является экспертная оценка, а значение - сочетание всех её элементов
     */
    private final Map<PairwiseTrainingObject, Set<Pair<UnclassifiedObject, UnclassifiedObject>>> combinations;

    /**
     * Список столбцов матрицы, являющимся множеством из сочетаний элементов всех экспертных оценок
     */
    private final LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> columns;

    /**
     * Создание матрицы ограничений
     *
     * @param penaltyParameter параметр штрафа
     * @param trainingSet      обучающая выборка (непустая)
     * @throws NullPointerException     если обучающая выборка не задана
     * @throws IllegalArgumentException если обучающая выборка пустая
     */
    public ConstraintMatrix(double penaltyParameter, LinkedHashSet<? extends PairwiseTrainingObject> trainingSet) {
        Objects.requireNonNull(trainingSet);

        if (trainingSet.isEmpty()) {
            throw new IllegalArgumentException("Training set cannot be empty");
        }

        this.judgements = new LinkedHashSet<>(trainingSet);
        this.penaltyParameter = penaltyParameter;
        this.combinations = composeCombinations();
        this.columns = composeColumns();
    }

    /**
     * Получение строки матрицы ограничений для заданной экспертной оценки
     *
     * @param judgement экспертная оценка
     * @return двумерный массив из вещественных чисел, элементы которого соответствуют паре из сочетания элементов
     * из всех экспертных оценок, а строки - верхнему и нижнему ограничению, соответствующим экспертной оценке
     */
    public double[][] coefficientsForJudgement(PairwiseTrainingObject judgement) {
        Objects.requireNonNull(judgement);

        Set<Pair<UnclassifiedObject, UnclassifiedObject>> combination = combinations.get(judgement);
        double[][] result = new double[2][columns.size()];
        int i = 0;

        for(Pair<UnclassifiedObject, UnclassifiedObject> pair : columns) {
            if(combination.contains(pair) || combination.contains(pair.swap())) {
                result[0][i] = 1.0; // нижний предел
                result[1][i] = -1.0; // верхний предел
            } else {
                result[0][i] = 0.0; // нижний предел
                result[1][i] = 0.0; // верхний предел
            }

            i++;
        }

        return result;
    }

    /**
     * Получение матрицы коэффициентов, входящих в ограничения
     *
     * @return двумерный массив из вещественных чисел, столбцы которого соответствуют элементу из сочетания элементов
     * из всех экспертных оценок, а строка - верхнее или нижнее ограничения для соответствующей экспертной оценки
     */
    public double[][] coefficientVector() {
        double[][] result = new double[2 * judgements.size()][];
        int i = 0;

        for (PairwiseTrainingObject judgement : judgements) {
            for (double[] row : coefficientsForJudgement(judgement)) {
                result[i] = row;
                i++;
            }
        }

        return result;
    }

    /**
     * Получение вектора ограничений
     *
     * @return массив из вещественных чисел, каждое из которых может иметь значение либо 0, либо -С
     * (отрицательное значение заданного параметра штрафа)
     */
    public double[] constraintVector() {
        double[] result = new double[2 * judgements.size()];

        for (int i = 0; i < result.length; i += 2) {
            result[i] = 0.0; // нижний предел
            result[i + 1] = -penaltyParameter; // верхний предел
        }


        return result;
    }

    /**
     * Формирование ассоциативного массива, ключом которого является экспертная оценка,
     * а значение - сочетание всех её элементов
     */
    private Map<PairwiseTrainingObject, Set<Pair<UnclassifiedObject, UnclassifiedObject>>> composeCombinations()
    {
        Map<PairwiseTrainingObject, Set<Pair<UnclassifiedObject, UnclassifiedObject>>> result
                = new HashMap<>();

        judgements.forEach(judgement -> result.put(judgement, combinationOfJudgementElements(judgement)));

        return result;
    }

    /**
     * Получение сочетания элементов из всех экспертных оценок (множество множеств пар, полученных при сочетании элементов
     * каждой каждой экспертной оценки)
     */
    private LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> composeColumns() {
        LinkedHashSet<Pair<UnclassifiedObject, UnclassifiedObject>> result = new LinkedHashSet<>();

        for(PairwiseTrainingObject judgement : judgements) {
            for(Pair<UnclassifiedObject, UnclassifiedObject> pair : combinations.get(judgement)) {
                if(!result.contains(pair) && !result.contains(pair.swap())) {
                    result.add(pair);
                }
            }
        }

        return result;
    }

    /**
     * Сочетание всех элементов, входящих в экспертную оценку
     */
    @SuppressWarnings("unchecked")
    private Set<Pair<UnclassifiedObject, UnclassifiedObject>> combinationOfJudgementElements(
            PairwiseTrainingObject judgement) {
        assert judgement != null;

        return Combination.of(
                (Set<UnclassifiedObject>) judgement.preferable(),
                (Set<UnclassifiedObject>) judgement.inferior()
        );
    }
}
