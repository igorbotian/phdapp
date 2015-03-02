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

import ru.spbftu.igorbotian.phdapp.common.PairwiseTrainingObject;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * Вектор квадратичной функции, используемый в алгоритме Гольдфарба-Иднани для решения задачи квадратичного программирования.
 * В текущей реализации он всегда состоит из единиц.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
class QuadraticFunctionVector {

    /**
     * Размер вектора
     */
    private final int size;

    /**
     * Создание вектора квадратичной функции
     *
     * @param trainingSet обучающая выборка, на основе которой будет сформирован данный вектор
     * @throws NullPointerException     если обучающая выборка не задана
     * @throws IllegalArgumentException если обучающая выборка пустая
     */
    public QuadraticFunctionVector(LinkedHashSet<? extends PairwiseTrainingObject> trainingSet) {
        Objects.requireNonNull(trainingSet);

        if (trainingSet.isEmpty()) {
            throw new IllegalArgumentException("Training set cannot be null");
        }

        this.size = trainingSet.size();
    }

    /**
     * Значения элементов вектора
     *
     * @return одномерный массив из единиц, размер которого равен количеству элементов в обучающей выборке
     */
    public int[] values() {
        int[] result = new int[size];
        Arrays.fill(result, 1);
        return result;
    }
}
