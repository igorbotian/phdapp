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
import ru.spbftu.igorbotian.phdapp.output.csv.ReportCSVWriter;
import ru.spbftu.igorbotian.phdapp.output.csv.ReportCSVWriterFactory;
import ru.spbftu.igorbotian.phdapp.output.summary.ReportSummaryWriter;
import ru.spbftu.igorbotian.phdapp.output.summary.ReportSummaryWriterFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.*;

/**
 * Реализация интерфейса <code>CrossValidationResultsWindowDirector</code>
 *
 * @see CrossValidationResultsWindowDirector
 */
class CrossValidationResultsWindowDirectorImpl implements CrossValidationResultsWindowDirector {

    private final Logger LOGGER = Logger.getLogger(CrossValidationResultsWindowDirectorImpl.class);

    private final ReportSummaryWriterFactory reportSummaryWriterFactory;
    private final ReportCSVWriterFactory reportCSVWriterFactory;

    public CrossValidationResultsWindowDirectorImpl(ReportSummaryWriterFactory reportSummaryWriterFactory,
                                                    ReportCSVWriterFactory reportCSVWriterFactory) {

        this.reportSummaryWriterFactory = Objects.requireNonNull(reportSummaryWriterFactory);
        this.reportCSVWriterFactory = Objects.requireNonNull(reportCSVWriterFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Report> List<String> getReportSummary(R report) {
        try {
            StringWriter writer = new StringWriter();
            ReportSummaryWriter<R> summaryWriter = reportSummaryWriterFactory.get((Class<R>) report.getClass());
            summaryWriter.writeTo(report, new PrintWriter(writer));
            return Arrays.asList(writer.toString().split("\n"));
        } catch (IOException e) {
            LOGGER.error("Unable to obtain report summary", e);
        }

        return new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Report> void exportReportToCSV(R report, Path file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file.toFile(), false));
        ReportCSVWriter<R> csvWriter = reportCSVWriterFactory.get((Class<R>) report.getClass());
        csvWriter.writeTo(report, writer, true);
    }
}
