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

package ru.spbftu.igorbotian.phdapp.output.summary;

import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleClassificationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Реализация интерфейса <code>ReportSummaryWriter</code> для отчётов типа <code>MultiClassificationReport</code>
 *
 * @see ReportSummaryWriter
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiClassificationReport
 */
class MultiClassificationReportSummaryWriterImpl implements ReportSummaryWriter<MultiClassificationReport> {

    private static final String AVERAGE_ACCURACY_LABEL = "averageAccuracy";
    private static final String MIN_ACCURACY_LABEL = "minAccuracy";
    private static final String MAX_ACCURACY_LABEL = "maxAccuracy";
    private static final String AVERAGE_PRECISION_LABEL = "averagePrecision";
    private static final String MIN_PRECISION_LABEL = "minPrecision";
    private static final String MAX_PRECISION_LABEL = "maxPrecision";
    private static final String AVERAGE_RECALL_LABEL = "averageRecall";
    private static final String MIN_RECALL_LABEL = "minRecall";
    private static final String MAX_RECALL_LABEL = "maxRecall";
    private static final String NUMBER_OF_ITERATIONS_LABEL = "numberOfClassifications";
    private static final String MIN_PRECISION_CLASSIFICATION_LABEL = "minPrecisionClassificationLabel";
    private static final String MAX_PRECISION_CLASSIFICATION_LABEL = "maxPrecisionClassificationLabel";

    private final Localization localization;
    private final ReportSummaryWriter<SingleClassificationReport> singleReportWriter;

    public MultiClassificationReportSummaryWriterImpl(Localization localization,
                                                      ReportSummaryWriter<SingleClassificationReport> singleReportWriter) {
        this.localization = Objects.requireNonNull(localization);
        this.singleReportWriter = Objects.requireNonNull(singleReportWriter);
    }

    @Override
    public Class<MultiClassificationReport> getTargetReportClass() {
        return MultiClassificationReport.class;
    }

    @Override
    public void writeTo(MultiClassificationReport report, PrintWriter writer) throws IOException {
        Objects.requireNonNull(report);
        Objects.requireNonNull(writer);

        SummaryWriter summary = new SummaryWriter(writer);

        summary.writeItem(localization.getLabel(AVERAGE_ACCURACY_LABEL), report.averageAccuracy());
        summary.writeItem(localization.getLabel(MIN_ACCURACY_LABEL), report.minAccuracy());
        summary.writeItem(localization.getLabel(MAX_ACCURACY_LABEL), report.maxAccuracy());
        summary.writeItem(localization.getLabel(AVERAGE_PRECISION_LABEL), report.averagePrecision());
        summary.writeItem(localization.getLabel(MIN_PRECISION_LABEL), report.maxPrecision());
        summary.writeItem(localization.getLabel(MAX_PRECISION_LABEL), report.maxPrecision());
        summary.writeItem(localization.getLabel(AVERAGE_RECALL_LABEL), report.averageRecall());
        summary.writeItem(localization.getLabel(MIN_RECALL_LABEL), report.minRecall());
        summary.writeItem(localization.getLabel(MAX_RECALL_LABEL), report.maxRecall());
        summary.writeItem(localization.getLabel(NUMBER_OF_ITERATIONS_LABEL), report.numberOfClassifications());

        summary.writeEmptyLine();
        summary.writeHeader(MAX_PRECISION_CLASSIFICATION_LABEL + ":");
        singleReportWriter.writeTo(report.max(), summary);

        summary.writeEmptyLine();
        summary.writeHeader(MIN_PRECISION_CLASSIFICATION_LABEL + ":");
        singleReportWriter.writeTo(report.min(), summary);
    }
}
