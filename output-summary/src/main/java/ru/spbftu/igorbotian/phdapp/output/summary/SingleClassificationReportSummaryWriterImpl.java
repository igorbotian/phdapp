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
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Реализация интерфейса <code>ReportSummaryWriter</code> для отчётов типа <code>SingleClassificationReport</code>
 *
 * @see ReportSummaryWriter
 */
class SingleClassificationReportSummaryWriterImpl implements ReportSummaryWriter<SingleClassificationReport> {

    private static final String ACCURACY_LABEL = "accuracy";
    private static final String PRECISION_LABEL = "precision";
    private static final String RECALL_LABEL = "recall";
    private static final String F_MEASURE_LABEL = "fMeasure";

    private final Localization localization;

    public SingleClassificationReportSummaryWriterImpl(Localization localization) {
        this.localization = Objects.requireNonNull(localization);
    }

    @Override
    public Class<SingleClassificationReport> getTargetReportClass() {
        return SingleClassificationReport.class;
    }

    @Override
    public void writeTo(SingleClassificationReport report, PrintWriter writer) throws IOException {
        Objects.requireNonNull(report);
        Objects.requireNonNull(writer);

        SummaryWriter summary = new SummaryWriter(writer);

        for (ClassifierParameter<?> param : report.classifierParameters()) {
            summary.writeItem(localization.getLabel(param.name()), param.value().toString());
        }

        for (CrossValidatorParameter<?> param : report.crossValidatorParameters()) {
            summary.writeItem(localization.getLabel(param.name()), param.value().toString());
        }

        summary.writeItem(localization.getLabel(ACCURACY_LABEL), report.accuracy());
        summary.writeItem(localization.getLabel(PRECISION_LABEL), report.precision());
        summary.writeItem(localization.getLabel(RECALL_LABEL), report.recall());
        summary.writeItem(localization.getLabel(F_MEASURE_LABEL), report.fMeasure());
    }
}
