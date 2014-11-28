/*
 * Copyright (c) 2014 Igor Botian
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
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.svm.analytics.report;

import java.util.List;

/**
 * Отчёт, содержащий метрики по работе классификатора для различных заданных параметров
 */
public interface MultiIterationReport extends Report {

    /**
     * Среднее значение метрики <code>Accuracy</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float averageAccuracy();

    /**
     * Минимальное значение метрики <code>Accuracy</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float minAccuracy();

    /**
     * Максимальное значение метрики <code>Accuracy</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float maxAccuracy();

    /**
     * Среднее значение метрики <code>Precision</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float averagePrecision();

    /**
     * Минимальное значение метрики <code>Precision</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float minPrecision();

    /**
     * Максимальное значение метрики <code>Precision</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float maxPrecision();

    /**
     * Среднее значение метрики <code>Recall</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float averageRecall();

    /**
     * Минимальное значение метрики <code>Recall</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float minRecall();

    /**
     * Максимальное значение метрики <code>Recall</code>
     *
     * @return вещественное значение в диапазоне [0.0;1.0]
     */
    float maxRecall();

    /**
     * Количество произведённых классификаций с различными значениями параметров
     *
     * @return целое положительное число
     */
    int numberOfIterations();

    /**
     * Множество отчётов с метриками по каждой произведённой классификации
     *
     * @return список объектов типа <code>SingleIterationReport</code>, отсортированный в порядке увеличения значений
     * изменяемых параметров
     */
    List<SingleIterationReport> iterationReports();
}
