package ru.spbftu.igorbotian.phdapp.svm.analytics;

/**
 * Copyright (c) 2014 Igor Botian
 * <p>
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

import ru.spbftu.igorbotian.phdapp.common.ClassifiedData;
import ru.spbftu.igorbotian.phdapp.common.PointwiseTrainingSet;

/**
 * Анализитор корректности работы поточечного классификатора, основывающийся на результатах его работы
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.PointwiseClassifier
 */
public interface PointwiseClassifierAnalyzer {

    /**
     * Анализ работы классификатора на основе сравнения реальных классов оъектов заданной выборки
     * с полученными в результате классификации
     *
     * @param classifiedData результаты классификации
     * @param realData       настоящие классы объектов из набора исходных данных, подлежащего классификации
     * @return отчёт о корректности работы классификатора, содержащий множество метрик
     * @throws java.lang.IllegalArgumentException если количество классифицированных и реальных объектов не совпадает
     * @see ru.spbftu.igorbotian.phdapp.svm.analytics.Report
     */
    Report analyze(ClassifiedData classifiedData, PointwiseTrainingSet realData);
}
