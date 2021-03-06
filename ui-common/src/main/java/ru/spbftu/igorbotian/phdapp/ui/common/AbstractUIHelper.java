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
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.output.csv.ReportCSVWriterFactory;
import ru.spbftu.igorbotian.phdapp.output.summary.ReportSummaryWriterFactory;
import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;

import java.util.Objects;

/**
 * Реализация интерфейса <code>UIHelper</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.common.UIHelper
 */
@Singleton
public abstract class AbstractUIHelper implements UIHelper {

    private final Localization localization;

    private final CrossValidationSampleCanvasDirector sampleCanvasDirector;
    private final CrossValidationParamsWindowDirector crossValidationParamsWindowDirector;
    private final CrossValidationResultsWindowDirector crossValidationResultsWindowDirector;
    private final CrossValidationProgressWindowDirector crossValidationProgressWindowDirector;

    @Inject
    public AbstractUIHelper(Localization localization,
                            ApplicationConfiguration configuration,
                            CrossValidationSampleManager sampleManager,
                            ReportSummaryWriterFactory reportSummaryWriterFactory,
                            ReportCSVWriterFactory reportCSVWriterFactory,
                            CrossValidatorParameterFactory crossValidatorParameterFactory,
                            MathDataFactory mathDataFactory,
                            RankingPairwiseClassifier classifier) {

        Objects.requireNonNull(localization);
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(sampleManager);
        Objects.requireNonNull(reportSummaryWriterFactory);
        Objects.requireNonNull(reportCSVWriterFactory);
        Objects.requireNonNull(crossValidatorParameterFactory);
        Objects.requireNonNull(classifier);

        this.localization = localization;
        sampleCanvasDirector = new CrossValidationSampleCanvasDirectorImpl(sampleManager, mathDataFactory);
        crossValidationParamsWindowDirector
                = new CrossValidationParamsWindowDirectorImpl(configuration, crossValidatorParameterFactory);
        crossValidationResultsWindowDirector =
                new CrossValidationResultsWindowDirectorImpl(reportSummaryWriterFactory, reportCSVWriterFactory,
                        configuration);
        crossValidationProgressWindowDirector =
                new CrossValidationProgressWindowDirectorImpl(this, classifier);
    }

    @Override
    public String getLabel(String label) {
        return localization.getLabel(label);
    }

    @Override
    public CrossValidationSampleCanvasDirector sampleCanvasDirector() {
        return sampleCanvasDirector;
    }

    @Override
    public CrossValidationParamsWindowDirector crossValidatorParamsFrameDirector() {
        return crossValidationParamsWindowDirector;
    }

    @Override
    public CrossValidationResultsWindowDirector crossValidationResultWindowDirector() {
        return crossValidationResultsWindowDirector;
    }

    @Override
    public CrossValidationProgressWindowDirector crossValidationProgressWindowDirector() {
        return crossValidationProgressWindowDirector;
    }
}
