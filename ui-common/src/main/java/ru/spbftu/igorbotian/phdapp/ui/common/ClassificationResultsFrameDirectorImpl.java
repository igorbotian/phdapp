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

import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phd.output.csv.MultiClassificationReportCSVWriter;
import ru.spbftu.igorbotian.phd.output.csv.SingleClassificationReportCSVWriter;
import ru.spbftu.igorbotian.phd.output.summary.MultiClassificationReportSummaryWriter;
import ru.spbftu.igorbotian.phd.output.summary.SingleClassificationReportSummaryWriter;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleClassificationReport;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Реализация интерфейса <code>ClassificationResultsFrameDirector</code>
 *
 * @see ClassificationResultsFrameDirector
 */
class ClassificationResultsFrameDirectorImpl implements ClassificationResultsFrameDirector {

    private final Logger LOGGER = Logger.getLogger(ClassificationResultsFrameDirectorImpl.class);

    private final SingleClassificationReportSummaryWriter singleClassificationReportSummaryWriter;
    private final MultiClassificationReportSummaryWriter multiClassificationReportSummaryWriter;
    private final SingleClassificationReportCSVWriter singleClassificationReportCSVWriter;
    private final MultiClassificationReportCSVWriter multiClassificationReportCSVWriter;

    public ClassificationResultsFrameDirectorImpl(SingleClassificationReportSummaryWriter singleClassificationReportSummaryWriter,
                                           MultiClassificationReportSummaryWriter multiClassificationReportSummaryWriter,
                                           SingleClassificationReportCSVWriter singleClassificationReportCSVWriter,
                                           MultiClassificationReportCSVWriter multiClassificationReportCSVWriter) {

        this.singleClassificationReportSummaryWriter = Objects.requireNonNull(singleClassificationReportSummaryWriter);
        this.multiClassificationReportSummaryWriter = Objects.requireNonNull(multiClassificationReportSummaryWriter);
        this.singleClassificationReportCSVWriter = Objects.requireNonNull(singleClassificationReportCSVWriter);
        this.multiClassificationReportCSVWriter = Objects.requireNonNull(multiClassificationReportCSVWriter);
    }

    @Override
    public List<String> getReportSummary(SingleClassificationReport report) {
        try {
            StringWriter writer = new StringWriter();
            singleClassificationReportSummaryWriter.writeTo(report, new PrintWriter(writer));
            return divideByLines(writer.toString());
        } catch (IOException e) {
            LOGGER.error("Unable to obtain report summary", e);
        }

        return new ArrayList<>();
    }

    @Override
    public List<String> getReportSummary(MultiClassificationReport report) {
        try {
            StringWriter writer = new StringWriter();
            multiClassificationReportSummaryWriter.writeTo(report, new PrintWriter(writer));
            return divideByLines(writer.toString());
        } catch (IOException e) {
            LOGGER.error("Unable to obtain report summary", e);
        }

        return new ArrayList<>();
    }

    private List<String> divideByLines(String str) {
        List<String> lines = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(str, "\n");

        while (tokenizer.hasMoreElements()) {
            lines.add((String) tokenizer.nextElement());
        }

        return lines;
    }

    @Override
    public void exportReportToCSV(SingleClassificationReport report, Path file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file.toFile(), false));
        singleClassificationReportCSVWriter.writeTo(report, writer, true);
    }

    @Override
    public void exportReportToCSV(MultiClassificationReport report, Path file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file.toFile(), false));
        multiClassificationReportCSVWriter.writeTo(report, writer, true);
    }
}
