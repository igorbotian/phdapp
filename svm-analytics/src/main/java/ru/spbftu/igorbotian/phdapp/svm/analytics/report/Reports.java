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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Фабрика отчётов по работе классификатора
 */
public final class Reports {

    private Reports() {
        //
    }

    /**
     * Создание отчёта, содержащего различные метрики по работе классификатора с заданными параметрами
     *
     * @param sampleSize                      количество уникальных элементов в выборке (обучащая + тестирующая) (положительное целое число)
     * @param constantCostParameter      постоянный параметр стоиомсти (положительное вещественное число)
     * @param gaussianKernelParameter    параметр Гауссова ядра (вещественное число)
     * @param judgedSampleItemsRatio          количество элементов выборки, входящих в обучающую выборки
     *                                        (в процентном соотношении) (значение в пределах [0.0;1.0])
     * @param preciseIntervalSampleItemsRatio количество интервальных предпочтений среди общего количества предпочтений в обучающей выборке
     *                                        (в процентном соотношении) (значение в пределах [0.0;1.0])
     * @param accuracy                        метрика <code>Accuracy</code> (значение в пределах [0.0;1.0])
     * @param precision                       метрика <code>Precision</code> (значение в пределах [0.0;1.0])
     * @param recall                          метрика <code>Recall</code> (значение в пределах [0.0;1.0])
     * @return отчёт заданного типа
     * @throws java.lang.IllegalArgumentException если значение хотя бы одного параметра выходит за пределы допустимых значений
     */
    public static SingleIterationReport newSingleIterationReport(final int sampleSize,
                                                                 final float constantCostParameter,
                                                                 final float gaussianKernelParameter,
                                                                 final float judgedSampleItemsRatio,
                                                                 final float preciseIntervalSampleItemsRatio,
                                                                 final float accuracy,
                                                                 final float precision,
                                                                 final float recall) {
        return new SingleIterationReport() {

            @Override
            public float accuracy() {
                return checkLimits(accuracy, 0.0f, 1.0f);
            }

            @Override
            public float precision() {
                return checkLimits(precision, 0.0f, 1.0f);
            }

            @Override
            public float recall() {
                return checkLimits(recall, 0.0f, 1.0f);
            }

            @Override
            public float constantCostParameter() {
                return checkLimits(constantCostParameter, 0.0f, Float.MAX_VALUE);
            }

            @Override
            public float gaussianKernelParameter() {
                return gaussianKernelParameter;
            }

            @Override
            public int sampleSize() {
                return (int) checkLimits(sampleSize, 0.0f, Float.MAX_VALUE);
            }

            @Override
            public float judgedSampleItemsRatio() {
                return checkLimits(judgedSampleItemsRatio, 0.0f, 1.0f);
            }

            @Override
            public float preciseIntervalSampleItemsRatio() {
                return checkLimits(preciseIntervalSampleItemsRatio, 0.0f, 1.0f);
            }
        };
    }

    private static float checkLimits(float n, float min, float max) {
        if(min > n || n > max) {
            throw new IllegalArgumentException(String.format("A given number (%.5f) should be in range of [%.5f;%.5f]",
                    n, min, max));
        }

        return n;
    }

    /**
     * Создание сводного отчёта по работе классификатора на основе ряда одиночных классификаций
     * @param iterationReports список из отчётов по работе одиночных классификаций
     * @return отчёт заданного типа
     * @throws java.lang.NullPointerException если список отчётов не задан
     * @throws java.lang.IllegalArgumentException если список отчётов пустой
     */
    public static MultiIterationReport newMultiIterationReport(List<SingleIterationReport> iterationReports) {
        Objects.requireNonNull(iterationReports);

        if(iterationReports.isEmpty()) {
            throw new IllegalArgumentException("A list of iteration reports cannot be empty");
        }

        final int numberOfIterations = iterationReports.size();
        float accuracySum = 0.0f;
        float minAccuracy = 1.0f;
        float maxAccuracy = 0.0f;
        float precisionSum = 0.0f;
        float minPrecision = 1.0f;
        float maxPrecision = 0.0f;
        float recallSum = 0.0f;
        float minRecall = 1.0f;
        float maxRecall = 0.0f;

        for (SingleIterationReport report : iterationReports) {
            accuracySum += report.accuracy();

            if (report.accuracy() > maxAccuracy) {
                maxAccuracy = report.accuracy();
            }

            if (report.accuracy() < minAccuracy) {
                minAccuracy = report.accuracy();
            }

            precisionSum += report.precision();

            if (report.precision() > maxPrecision) {
                maxPrecision = report.precision();
            }

            if (report.precision() < minPrecision) {
                minPrecision = report.precision();
            }

            recallSum += report.recall();

            if (report.recall() > maxRecall) {
                maxRecall = report.recall();
            }

            if (report.recall() < minRecall) {
                minRecall = report.recall();
            }
        }

        return newMultiIterationReport(accuracySum / numberOfIterations, minAccuracy, maxAccuracy,
                precisionSum / numberOfIterations, minPrecision, maxPrecision,
                recallSum / numberOfIterations, minRecall, maxRecall,
                iterationReports);
    }

    private static MultiIterationReport newMultiIterationReport(final float avgAccuracy,
                                                                final float minAccuracy,
                                                                final float maxAccuracy,
                                                                final float avgPrecision,
                                                                final float minPrecision,
                                                                final float maxPrecision,
                                                                final float avgRecall,
                                                                final float minRecall,
                                                                final float maxRecall,
                                                                final List<SingleIterationReport> iterationReports) {
        return new MultiIterationReport() {
            @Override
            public float averageAccuracy() {
                return avgAccuracy;
            }

            @Override
            public float minAccuracy() {
                return minAccuracy;
            }

            @Override
            public float maxAccuracy() {
                return maxAccuracy;
            }

            @Override
            public float averagePrecision() {
                return avgPrecision;
            }

            @Override
            public float minPrecision() {
                return minPrecision;
            }

            @Override
            public float maxPrecision() {
                return maxPrecision;
            }

            @Override
            public float averageRecall() {
                return avgRecall;
            }

            @Override
            public float minRecall() {
                return minRecall;
            }

            @Override
            public float maxRecall() {
                return maxRecall;
            }

            @Override
            public int numberOfIterations() {
                return iterationReports.size();
            }

            @Override
            public List<SingleIterationReport> iterationReports() {
                return Collections.unmodifiableList(iterationReports);
            }
        };
    }
}
