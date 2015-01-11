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

package ru.spbftu.igorbotian.phdapp.output.csv;

import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Реализация сохранения отчёта единичной классификации в формате CSV
 *
 * @see ReportCSVWriter
 */
class SingleClassificationReportCSVWriter implements ReportCSVWriter<SingleClassificationReport> {

    private static final String ACCURACY_LABEL = "accuracy";
    private static final String PRECISION_LABEL = "precision";
    private static final String RECALL_LABEL = "recall";
    private static final String F_MEASURE_LABEL = "fMeasure";

    private final Localization localization;

    public SingleClassificationReportCSVWriter(Localization localization) {
        this.localization = Objects.requireNonNull(localization);
    }

    @Override
    public Class<SingleClassificationReport> getTargetReportClass() {
        return SingleClassificationReport.class;
    }

    @Override
    public void writeTo(SingleClassificationReport report, PrintWriter writer, boolean includeHeader) throws IOException {
        Objects.requireNonNull(report);
        Objects.requireNonNull(writer);

        CSVWriter csv = new CSVWriter(writer);

        if (includeHeader) {
            writeHeaderTo(report, csv);
        }

        writeContentsTo(report, csv);
    }

    void writeHeaderTo(SingleClassificationReport report, CSVWriter csv) throws IOException {
        String[] headerItems = new String[report.classifierParameters().size()
                + report.crossValidatorParameters().size() + 4 /* accuracy, precision, recall, f-measure */];
        int i = 0;

        for(ClassifierParameter<?> param : report.classifierParameters()) {
            headerItems[i] = localization.getLabel(param.name());
            i++;
        }

        for(CrossValidatorParameter<?> param : report.crossValidatorParameters()) {
            headerItems[i] = localization.getLabel(param.name());
            i++;
        }

        headerItems[i] = localization.getLabel(ACCURACY_LABEL);
        i++;
        headerItems[i] = localization.getLabel(PRECISION_LABEL);
        i++;
        headerItems[i] = localization.getLabel(RECALL_LABEL);
        i++;
        headerItems[i] = localization.getLabel(F_MEASURE_LABEL);

        csv.writeLine(headerItems);
    }

    private void writeContentsTo(SingleClassificationReport report, CSVWriter csv) throws IOException {
        String[] lineItems = new String[report.classifierParameters().size()
                + report.crossValidatorParameters().size() + 4 /* accuracy, precision, recall, f-measure */];
        int i = 0;

        for(ClassifierParameter<?> param : report.classifierParameters()) {
            lineItems[i] = param.value().toString();
            i++;
        }

        for(CrossValidatorParameter<?> param : report.crossValidatorParameters()) {
            lineItems[i] = param.value().value().toString();
            i++;
        }

        lineItems[i] = Double.toString(report.accuracy());
        i++;
        lineItems[i] = Double.toString(report.precision());
        i++;
        lineItems[i] = Double.toString(report.recall());
        i++;
        lineItems[i] = Double.toString(report.fMeasure());

        csv.writeLine(lineItems);
    }
}
