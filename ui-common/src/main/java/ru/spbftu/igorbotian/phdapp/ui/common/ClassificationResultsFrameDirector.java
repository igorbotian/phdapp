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

import ru.spbftu.igorbotian.phdapp.svm.analytics.report.MultiClassificationReport;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.SingleClassificationReport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Модель окна, отображающего результаты классификации
 */
public interface ClassificationResultsFrameDirector {

    /**
     * Получение строк резюме для указанных результатов классиикации
     */
    public List<String> getReportSummary(SingleClassificationReport report);

    /**
     * Получение строк резюме для указанных результатов классиикации
     */
    public List<String> getReportSummary(MultiClassificationReport report);

    /**
     * Сохранение указанных результатов классификации в заданный файл
     */
    public void exportReportToCSV(SingleClassificationReport report, Path file) throws IOException;

    /**
     * Сохранение указанных результатов классификации в заданный файл
     */
    public void exportReportToCSV(MultiClassificationReport report, Path file) throws IOException;
}
