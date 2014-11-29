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

package ru.spbftu.igorbotian.phd.output.csv;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiIterationReport;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleIterationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Реализация класса <code>MultiIterationReportCsv</code>
 *
 * @see ru.spbftu.igorbotian.phd.output.csv.MultiIterationReportCsvWriter
 */
class MultiIterationReportCsvWriterImpl implements MultiIterationReportCsvWriter {

    private static final String AVERAGE_ACCURACY_LABEL = "averageAccuracy";
    private static final String MIN_ACCURACY_LABEL = "minAccuracy";
    private static final String MAX_ACCURACY_LABEL = "maxAccuracy";
    private static final String AVERAGE_PRECISION_LABEL = "averagePrecision";
    private static final String MIN_PRECISION_LABEL = "minPrecision";
    private static final String MAX_PRECISION_LABEL = "maxPrecision";
    private static final String AVERAGE_RECALL_LABEL = "averageRecall";
    private static final String MIN_RECALL_LABEL = "minRecall";
    private static final String MAX_RECALL_LABEL = "maxRecall";
    private static final String NUMBER_OF_ITERATIONS_LABEL = "numberOfIterations";

    private final Localization localization;
    private final SingleIterationReportCsvWriter singleReportWriter;

    @Inject
    public MultiIterationReportCsvWriterImpl(Localization localization, SingleIterationReportCsvWriter writer) {
        this.localization = Objects.requireNonNull(localization);
        this.singleReportWriter = Objects.requireNonNull(writer);
    }

    @Override
    public void writeTo(MultiIterationReport report, PrintWriter writer, boolean includeHeader) throws IOException {
        Objects.requireNonNull(report);
        Objects.requireNonNull(writer);

        CsvWriter csv = new CsvWriter(writer);

        if (includeHeader) {
            writeHeaderTo(csv);
        }

        writeContentsTo(report, csv);
    }

    private void writeHeaderTo(CsvWriter writer) throws IOException {
        assert (writer != null);

        writer.writeLine(
                localization.getLabel(AVERAGE_ACCURACY_LABEL),
                localization.getLabel(MIN_ACCURACY_LABEL),
                localization.getLabel(MAX_ACCURACY_LABEL),
                localization.getLabel(AVERAGE_PRECISION_LABEL),
                localization.getLabel(MIN_PRECISION_LABEL),
                localization.getLabel(MAX_PRECISION_LABEL),
                localization.getLabel(AVERAGE_RECALL_LABEL),
                localization.getLabel(MIN_RECALL_LABEL),
                localization.getLabel(MAX_RECALL_LABEL),
                localization.getLabel(NUMBER_OF_ITERATIONS_LABEL)
        );
    }

    private void writeContentsTo(MultiIterationReport report, CsvWriter writer) throws IOException {
        assert (report != null);
        assert (writer != null);

        writer.writeLine(
                Float.toString(report.averageAccuracy()),
                Float.toString(report.minAccuracy()),
                Float.toString(report.maxAccuracy()),
                Float.toString(report.averagePrecision()),
                Float.toString(report.minPrecision()),
                Float.toString(report.maxPrecision()),
                Float.toString(report.averageRecall()),
                Float.toString(report.minRecall()),
                Float.toString(report.maxRecall()),
                Integer.toString(report.numberOfIterations())
        );

        for (SingleIterationReport iterationReport : report.iterationReports()) {
            singleReportWriter.writeTo(iterationReport, writer, false);
        }
    }
}