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

package ru.spbftu.igorbotian.phdapp.ui.swing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.output.csv.ReportCSVWriterFactory;
import ru.spbftu.igorbotian.phdapp.output.summary.ReportSummaryWriterFactory;
import ru.spbftu.igorbotian.phdapp.svm.IntervalRankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.RankingPairwiseClassifier;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.IntervalPairwiseClassifierCrossValidators;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.CrossValidationSampleManager;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;
import ru.spbftu.igorbotian.phdapp.ui.common.AbstractUIHelper;
import ru.spbftu.igorbotian.phdapp.ui.common.ErrorDialogs;

import java.util.Objects;

/**
 * Реализация интерфейса <code>UIHelper</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.common.UIHelper
 */
@Singleton
class SwingUIHelperImpl extends AbstractUIHelper implements SwingUIHelper {

    private final CrossValidationParamsWidgets widgets;
    private final SwingMainFrameDirector mainFrameDirector;
    private final ErrorDialogs errorDialogs;

    @Inject
    public SwingUIHelperImpl(Localization localization,
                             ApplicationConfiguration configuration,
                             CrossValidationSampleManager sampleManager,
                             ReportSummaryWriterFactory reportSummaryWriterFactory,
                             ReportCSVWriterFactory reportCSVWriterFactory,
                             CrossValidatorParameterFactory crossValidatorParameterFactory,
                             MathDataFactory mathDataFactory,
                             IntervalRankingPairwiseClassifier classifier,
                             IntervalPairwiseClassifierCrossValidators validators,
                             ErrorDialogs errorDialogs) {

        super(localization, configuration, sampleManager, reportSummaryWriterFactory, reportCSVWriterFactory,
                crossValidatorParameterFactory, mathDataFactory, classifier);

        this.widgets = new CrossValidationParamsWidgetsImpl(this, configuration);
        this.mainFrameDirector = new MainFrameDirectorImpl(this, validators);
        this.errorDialogs = Objects.requireNonNull(errorDialogs);
    }

    @Override
    public SwingMainFrameDirector mainFrameDirector() {
        return mainFrameDirector;
    }

    @Override
    public CrossValidationParamsWidgets widgets() {
        return widgets;
    }

    @Override
    public ErrorDialogs errorDialog() {
        return errorDialogs;
    }
}
