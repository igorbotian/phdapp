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
import ru.spbftu.igorbotian.phd.output.csv.ReportCSVWriterFactory;
import ru.spbftu.igorbotian.phd.output.summary.MultiClassificationReportSummaryWriter;
import ru.spbftu.igorbotian.phd.output.summary.SingleClassificationReportSummaryWriter;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.SampleGenerator;

import java.util.Objects;

/**
 * Реализация интерфейса <code>UIHelper</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.common.UIHelper
 */
@Singleton
class UIHelperImpl implements UIHelper {

    private final Localization localization;

    private final SampleCanvasDirector sampleCanvasDirector;
    private final ClassifierParamsFrameDirector classifierParamsFrameDirector;
    private final ClassificationResultsFrameDirector classificationResultsFrameDirector;

    @Inject
    public UIHelperImpl(Localization localization,
                        ApplicationConfiguration configuration,
                        SampleGenerator sampleGenerator,
                        SingleClassificationReportSummaryWriter singleClassificationReportSummaryWriter,
                        MultiClassificationReportSummaryWriter multiClassificationReportSummaryWriter,
                        ReportCSVWriterFactory reportCSVWriterFactory) {

        Objects.requireNonNull(localization);
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(sampleGenerator);
        Objects.requireNonNull(singleClassificationReportSummaryWriter);
        Objects.requireNonNull(reportCSVWriterFactory);
        Objects.requireNonNull(multiClassificationReportSummaryWriter);

        this.localization = localization;
        sampleCanvasDirector = new SampleCanvasDirectorImpl(sampleGenerator);
        classifierParamsFrameDirector = new ClassifierParamsFrameDirectorImpl(configuration);
        classificationResultsFrameDirector = new ClassificationResultsFrameDirectorImpl(
                singleClassificationReportSummaryWriter,
                multiClassificationReportSummaryWriter,
                reportCSVWriterFactory
        );
    }

    @Override
    public String getLabel(String label) {
        return localization.getLabel(label);
    }

    @Override
    public SampleCanvasDirector sampleCanvasDirector() {
        return sampleCanvasDirector;
    }

    @Override
    public ClassifierParamsFrameDirector classifierParamsFrameDirector() {
        return classifierParamsFrameDirector;
    }

    @Override
    public ClassificationResultsFrameDirector classificationResultsFrameDirector() {
        return classificationResultsFrameDirector;
    }
}
