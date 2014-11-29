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

import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleIterationReport;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Средство сохранения отчёта по работе классификатора с заданными параметрами в формате CSV
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleIterationReport
 * @see ru.spbftu.igorbotian.phd.output.csv.CsvWriter
 */
public interface SingleIterationReportCsvWriter {

    /**
     * Сохрание заданного отчёта по работе классификатора в указанный символьный поток
     *
     * @param report        отчёт по работе классификатора
     * @param writer        выходной символьный поток
     * @param includeHeader если значение равно <code>true</code>, то первая строчка будет содержать локализованный заголовок
     *                      (название каждого столбца данных)
     * @throws java.io.IOException            в случае ошибки ввода/вывода при сохранении отчёта в поток
     * @throws java.lang.NullPointerException если хотя бы один из аргументов не задан
     */
    void writeTo(SingleIterationReport report, PrintWriter writer, boolean includeHeader) throws IOException;
}