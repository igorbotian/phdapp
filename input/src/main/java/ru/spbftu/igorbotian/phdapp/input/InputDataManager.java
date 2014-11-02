package ru.spbftu.igorbotian.phdapp.input;

/**
 * Copyright (c) 2014 Igor Botian
 * <p/>
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.InputData;
import ru.spbftu.igorbotian.phdapp.common.PointwiseInputData;

import java.io.IOException;
import java.util.Set;

/**
 * Средство для работы с исходными данными
 */
public interface InputDataManager {

    /**
     * Получение множество названий наборов исходных данных
     *
     * @return множество названий наборов исходных данных
     * @throws IOException в случае проблемы получения данного множества
     */
    Set<String> listIds() throws IOException;

    /**
     * Получение набора исходных данных по названию
     *
     * @param id название набора исходных данных (непустое)
     * @return набор исходных данных
     * @throws java.lang.NullPointerException                   если название набора исходных данных не задано
     * @throws java.io.IOException                              в случае проблемы получения набора
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException в случае проблемы формирования данного набора (проблемы его десериализации)
     */
    PointwiseInputData getById(String id) throws IOException, DataException;

    /**
     * Сохранение заданного набора исходных данных (сериализация)
     *
     * @param id   название набора исходных данных
     * @param data набор исходных данных
     * @throws java.lang.NullPointerException                   если хотя бы один из параметров не задан
     * @throws IOException                                      в случае проблем сериализации
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException в случае проблем сериализации
     */
    void save(String id, PointwiseInputData data) throws IOException, DataException;
}
