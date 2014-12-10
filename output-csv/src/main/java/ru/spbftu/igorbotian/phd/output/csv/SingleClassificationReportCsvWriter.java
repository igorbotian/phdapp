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

import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleClassificationReport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Реализация сохранения отчёта единичной классификации в формате CSV
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleClassificationReport
 * @see ru.spbftu.igorbotian.phd.output.csv.ReportCSVWriter
 */
class SingleClassificationReportCSVWriter implements ReportCSVWriter<SingleClassificationReport> {

    private static final String SAMPLE_SIZE_LABEL = "sampleSize";
    private static final String CONSTANT_COST_PARAMETER_LABEL = "cParameter";
    private static final String GAUSSIAN_KERNEL_PARAMETER_LABEL = "sigmaParameter";
    private static final String PERCENT_OF_JUDGED_SAMPLE_ITEMS_LABEL = "percentOfJudgedSampleItems";
    private static final String PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_LABEL = "preciseIntervalJudgedSampleItemsRatio";
    private static final String ACCURACY_LABEL = "accuracy";
    private static final String PRECISION_LABEL = "precision";
    private static final String RECALL_LABEL = "recall";

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
            writeHeaderTo(csv);
        }

        writeContentsTo(report, csv);
    }

    private void writeHeaderTo(CSVWriter csv) throws IOException {
        csv.writeLine(localization.getLabel(SAMPLE_SIZE_LABEL),
                localization.getLabel(CONSTANT_COST_PARAMETER_LABEL),
                localization.getLabel(GAUSSIAN_KERNEL_PARAMETER_LABEL),
                localization.getLabel(PERCENT_OF_JUDGED_SAMPLE_ITEMS_LABEL),
                localization.getLabel(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_LABEL),
                localization.getLabel(ACCURACY_LABEL),
                localization.getLabel(PRECISION_LABEL),
                localization.getLabel(RECALL_LABEL)
        );
    }

    private void writeContentsTo(SingleClassificationReport report, CSVWriter csv) throws IOException {
        csv.writeLine(
                Integer.toString(report.sampleSize()),
                Float.toString(report.constantCostParameter()),
                Float.toString(report.gaussianKernelParameter()),
                Float.toString(report.judgedSampleItemsRatio()),
                Float.toString(report.preciseIntervalSampleItemsRatio()),
                Float.toString(report.accuracy()),
                Float.toString(report.precision()),
                Float.toString(report.recall())
        );
    }
}
