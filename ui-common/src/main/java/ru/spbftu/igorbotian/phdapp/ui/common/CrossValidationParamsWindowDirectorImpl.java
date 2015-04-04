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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.MutableCrossValidatorParameter;

import java.util.Objects;

/**
 * Реализация интерфейса <code>CrossValidationParamsWindowDirector</code>
 *
 * @see CrossValidationParamsWindowDirector
 */
@Singleton
class CrossValidationParamsWindowDirectorImpl implements CrossValidationParamsWindowDirector {

    private final MutableCrossValidatorParameter<Integer> maxJudgementGroupSizeParameter;
    private final MutableCrossValidatorParameter<Double> penaltyParameter;
    private final MutableCrossValidatorParameter<Double> gaussianKernelParameter;
    private final MutableCrossValidatorParameter<Integer> samplesToGenerateCount;
    private final MutableCrossValidatorParameter<Integer> sampleSize;
    private final MutableCrossValidatorParameter<Integer> trainingTestingSetsSizeRatio;
    private final MutableCrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio;
    private final ApplicationConfiguration appConfig;

    @Inject
    public CrossValidationParamsWindowDirectorImpl(ApplicationConfiguration appConfig,
                                                   CrossValidatorParameterFactory crossValidatorParameterFactory) {

        this.appConfig = Objects.requireNonNull(appConfig);
        maxJudgementGroupSizeParameter = newIntegerParameter(crossValidatorParameterFactory.maxJudgementGroupSize());
        penaltyParameter = newDoubleParameter(crossValidatorParameterFactory.penaltyParameter());
        gaussianKernelParameter = newDoubleParameter(crossValidatorParameterFactory.gaussianKernelParameter());
        samplesToGenerateCount = newIntegerParameter(crossValidatorParameterFactory.samplesToGenerateCount());
        sampleSize = newIntegerParameter(crossValidatorParameterFactory.sampleSize());
        trainingTestingSetsSizeRatio
                = newIntegerParameter(crossValidatorParameterFactory.trainingTestingSetsSizeRatio());
        preciseIntervalJudgmentsCountRatio
                = newIntegerParameter(crossValidatorParameterFactory.preciseIntervalJudgmentsCountRatio());
    }

    private MutableCrossValidatorParameter<Double> newDoubleParameter(CrossValidatorParameter<Double> param) {
        return new PersistentCrossValidatorParameter<>(appConfig, param, Double::parseDouble);
    }

    private MutableCrossValidatorParameter<Integer> newIntegerParameter(CrossValidatorParameter<Integer> param) {
        return new PersistentCrossValidatorParameter<>(appConfig, param, Integer::parseInt);
    }

    @Override
    public MutableCrossValidatorParameter<Integer> maxJudgementGroupSize() {
        return maxJudgementGroupSizeParameter;
    }

    @Override
    public MutableCrossValidatorParameter<Double> penaltyParameter() {
        return penaltyParameter;
    }

    @Override
    public MutableCrossValidatorParameter<Double> gaussianKernelParameter() {
        return gaussianKernelParameter;
    }

    @Override
    public MutableCrossValidatorParameter<Integer> samplesToGenerateCount() {
        return samplesToGenerateCount;
    }

    @Override
    public MutableCrossValidatorParameter<Integer> sampleSize() {
        return sampleSize;
    }

    @Override
    public MutableCrossValidatorParameter<Integer> trainingTestingSetsSizeRatio() {
        return trainingTestingSetsSizeRatio;
    }

    @Override
    public MutableCrossValidatorParameter<Integer> preciseIntervalJudgmentsCountRatio() {
        return preciseIntervalJudgmentsCountRatio;
    }
}
