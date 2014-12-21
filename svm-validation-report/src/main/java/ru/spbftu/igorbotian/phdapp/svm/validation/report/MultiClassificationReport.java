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

package ru.spbftu.igorbotian.phdapp.svm.validation.report;

import java.util.List;

/**
 * Отчёт, содержащий метрики по работе классификатора для различных заданных параметров
 */
public interface MultiClassificationReport extends Report {

    /**
     * Количество произведённых классификаций с различными значениями параметров
     *
     * @return целое положительное число
     */
    int numberOfClassifications();

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
     * Среднее значение метрики <code>Мера Ризбергена</code>
     *
     * @return вещественное число
     */
    float averageFMeasure();

    /**
     * Минимальное значение метрики <code>Мера Ризбергена</code>
     *
     * @return вещественное число
     */
    float minFMeasure();

    /**
     * Максимальное значение метрики <code>Мера Ризбергена</code>
     *
     * @return вещественное число
     */
    float maxFMeasure();

    /**
     * Получение значений параметров, при которых точность классификации является наихудшей среди заданных наборов параметров
     *
     * @return отчёт по работе классификатора, в котором отражены значения параметров классификации
     */
    SingleClassificationReport max();

    /**
     * Получение значений параметров, при которых точность классификации является наилучшей среди заданных наборов параметров
     *
     * @return отчёт по работе классификатора, в котором отражены значения параметров классификации
     */
    SingleClassificationReport min();

    /**
     * Множество отчётов с метриками по каждой произведённой классификации
     *
     * @return список объектов типа <code>SingleClassificationReport</code>, отсортированный в порядке увеличения значений
     * изменяемых параметров
     */
    List<SingleClassificationReport> classifications();
}
