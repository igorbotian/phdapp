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

import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Модель окна, отображающего результаты кросс-валидации классификатора
 */
public interface CrossValidatorResultsFrameDirector {

    /**
     * Получение строк сводки для указанных результатов кросс-валидации
     */
    public <R extends Report> List<String> getReportSummary(R report);

    /**
     * Сохранение указанных результатов кросс-валидации в заданный файл
     */
    public <R extends Report> void exportReportToCSV(R report, Path file) throws IOException;
}
