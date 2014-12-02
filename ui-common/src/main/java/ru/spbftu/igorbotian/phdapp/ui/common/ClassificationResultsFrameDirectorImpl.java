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
import ru.spbftu.igorbotian.phd.output.csv.MultiIterationReportCsvWriter;
import ru.spbftu.igorbotian.phd.output.csv.SingleIterationReportCsvWriter;
import ru.spbftu.igorbotian.phd.output.summary.MultiIterationReportSummaryWriter;
import ru.spbftu.igorbotian.phd.output.summary.SingleIterationReportSummaryWriter;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiIterationReport;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleIterationReport;

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

    private final SingleIterationReportSummaryWriter singleIterationReportSummaryWriter;
    private final MultiIterationReportSummaryWriter multiIterationReportSummaryWriter;
    private final SingleIterationReportCsvWriter singleIterationReportCsvWriter;
    private final MultiIterationReportCsvWriter multiIterationReportCsvWriter;

    public ClassificationResultsFrameDirectorImpl(SingleIterationReportSummaryWriter singleIterationReportSummaryWriter,
                                           MultiIterationReportSummaryWriter multiIterationReportSummaryWriter,
                                           SingleIterationReportCsvWriter singleIterationReportCsvWriter,
                                           MultiIterationReportCsvWriter multiIterationReportCsvWriter) {

        this.singleIterationReportSummaryWriter = Objects.requireNonNull(singleIterationReportSummaryWriter);
        this.multiIterationReportSummaryWriter = Objects.requireNonNull(multiIterationReportSummaryWriter);
        this.singleIterationReportCsvWriter = Objects.requireNonNull(singleIterationReportCsvWriter);
        this.multiIterationReportCsvWriter = Objects.requireNonNull(multiIterationReportCsvWriter);
    }

    @Override
    public List<String> getReportSummary(SingleIterationReport report) {
        try {
            StringWriter writer = new StringWriter();
            singleIterationReportSummaryWriter.writeTo(report, new PrintWriter(writer));
            return divideByLines(writer.toString());
        } catch (IOException e) {
            LOGGER.error("Unable to obtain report summary", e);
        }

        return new ArrayList<>();
    }

    @Override
    public List<String> getReportSummary(MultiIterationReport report) {
        try {
            StringWriter writer = new StringWriter();
            multiIterationReportSummaryWriter.writeTo(report, new PrintWriter(writer));
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
    public void exportReportToCSV(SingleIterationReport report, Path file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file.toFile(), false));
        singleIterationReportCsvWriter.writeTo(report, writer, true);
    }

    @Override
    public void exportReportToCSV(MultiIterationReport report, Path file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file.toFile(), false));
        multiIterationReportCsvWriter.writeTo(report, writer, true);
    }
}
