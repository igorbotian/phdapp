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

package ru.spbftu.igorbotian.phd.output.summary;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleIterationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Реализация класса <code>SingleIterationReportSummaryWriter</code>
 *
 * @see ru.spbftu.igorbotian.phd.output.summary.SingleIterationReportSummaryWriter
 */
class SingleIterationReportSummaryWriterImpl implements SingleIterationReportSummaryWriter {

    private static final String SAMPLE_SIZE_LABEL = "sampleSize";
    private static final String CONSTANT_COST_PARAMETER_LABEL = "cParameter";
    private static final String GAUSSIAN_KERNEL_PARAMETER_LABEL = "sigmaParameter";
    private static final String PERCENT_OF_JUDGED_SAMPLE_ITEMS_LABEL = "percentOfJudgedSampleItems";
    private static final String PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_LABEL = "preciseIntervalJudgedSampleItemsRatio";
    private static final String ACCURACY_LABEL = "accuracy";
    private static final String PRECISION_LABEL = "precision";
    private static final String RECALL_LABEL = "recall";

    private final Localization localization;

    @Inject
    public SingleIterationReportSummaryWriterImpl(Localization localization) {
        this.localization = Objects.requireNonNull(localization);
    }

    @Override
    public void writeTo(SingleIterationReport report, PrintWriter writer) throws IOException {
        Objects.requireNonNull(report);
        Objects.requireNonNull(writer);

        SummaryWriter summary = new SummaryWriter(writer);
        summary.writeItem(localization.getLabel(SAMPLE_SIZE_LABEL), report.sampleSize());
        summary.writeItem(localization.getLabel(CONSTANT_COST_PARAMETER_LABEL), report.constantCostParameter());
        summary.writeItem(localization.getLabel(GAUSSIAN_KERNEL_PARAMETER_LABEL), report.gaussianKernelParameter());
        summary.writeItem(localization.getLabel(PERCENT_OF_JUDGED_SAMPLE_ITEMS_LABEL), report.judgedSampleItemsRatio());
        summary.writeItem(localization.getLabel(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_LABEL),
                report.preciseIntervalSampleItemsRatio());
        summary.writeItem(localization.getLabel(ACCURACY_LABEL), report.accuracy());
        summary.writeItem(localization.getLabel(PRECISION_LABEL), report.precision());
        summary.writeItem(localization.getLabel(RECALL_LABEL), report.recall());
    }
}
