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

package ru.spbftu.igorbotian.phdapp.ui.common;

import ru.spbftu.igorbotian.phdapp.common.Range;

/**
 * Модель окна, отображающего компоненты для задания параметров классификации
 */
public interface ClassifierParamsFrameDirector {

    /* ПАРАМЕТР ПОСТОЯННОЙ СТОИМОСТИ */

    double constantCostParameter();

    void setConstantCostParameter(double value);

    double constantCostParameterMin();

    double constantCostParameterMax();

    Range<Double> intervalConstantCostParameter();

    void setIntervalConstantCostParameter(Range<Double> value);

    double intervalConstantCostParameterStepSize();

    void setIntervalConstantCostParameterStepSize(double stepSize);

    /* ПАРАМЕТР ГАУССОВА ЯДРА */

    double gaussianKernelParameter();

    void setGaussianKernelParameter(double value);

    double gaussianKernelParameterMin();

    double gaussianKernelParameterMax();

    Range<Double> intervalGaussianKernelParameter();

    void setIntervalGaussianKernelParameter(Range<Double> value);

    double intervalGaussianKernelParameterStepSize();

    void setIntervalGaussianKernelParameterStepSize(double stepSize);

    /* КОЛИЧЕСТВО СГЕНЕРИРОВАННЫХ ВЫБОРОК */

    int samplesToGenerateCount();

    void setSamplesToGenerateCount(int count);

    int samplesToGenerateCountMin();

    int samplesToGenerateCountMax();

    int intervalSamplesToGenerateCountStepSize();

    void setIntervalSamplesToGenerateCountStepSize(int stepSize);

    /* РАЗМЕР ВЫБОРКИ */

    int sampleSize();

    void setSampleSize(int size);

    int sampleSizeMin();

    int sampleSizeMax();

    Range<Integer> intervalSampleSize();

    void setIntervalSampleSize(Range<Integer> sampleSize);

    int intervalSampleSizeStepSize();

    void setIntervalSampleSizeStepSize(int stepSize);

    /* ПРОЦЕНТНОЕ СООТНОШЕНИЕ ЭЛЕМЕНТОВ ВХОДЯЩИХ В ОБУЧАЮЩУЮ И ТЕСТИРУЮЩУЮ ВЫБОРКИ */

    int learningTrainingSetsSizeRatio();

    void setLearningTrainingSetsSizeRatio(int ratio);

    int learningTrainingSetsSizeRatioMin();

    int learningTrainingSetsSizeRatioMax();

    void setIntervalLearningTrainingSetsSizeRatio(Range<Integer> ratio);

    Range<Integer> intervalLearningTrainingSetsSizeRatio();

    int intervalLearningTrainingSetsSizeRatioStepSize();

    void setIntervalLearningTrainingSetsSizeRatioStepSize(int stepSize);

    /* ПРОЦЕНТНОЕ СООТНОШЕНИЕ ТОЧНЫХ И ИНТЕРВАЛЬНЫХ ПРЕДПОЧТЕНИЙ В ОБУЧАЮЩЕЙ ВЫБОРКЕ */

    int preciseIntervalJudgementsRatio();

    void setPreciseIntervalJudgementsRatio(int ratio);

    int preciseIntervalJudgementsRatioMin();

    int preciseIntervalJudgementsRatioMax();

    void setIntervalPreciseIntervalJudgementsRatio(Range<Integer> ratio);

    Range<Integer> intervalPreciseIntervalJudgementsRatio();

    int intervalPreciseIntervalJudgementsRatioStepSize();

    void setIntervalPreciseIntervalJudgementsRatioStepSize(int stepSize);
}
