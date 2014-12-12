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

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;

/**
 * Модуль для сохранения отчётов по работе классификатора в формате CSV
 *
 * @see ReportCSVWriterFactory
 */
public class CSVOutputDataManagementModule extends PhDAppModule {

    @Override
    protected void configure() {
        bind(ReportCSVWriterFactory.class).to(ReportCSVWriterFactoryImpl.class);
    }
}
