/**
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

package ru.spbftu.igorbotian.phdapp.input;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.utils.ShutdownHook;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Модуль для работы с исходными данными в формате JSON
 *
 * @see ru.spbftu.igorbotian.phdapp.input.JsonInputDataManager
 */
public class JsonInputDataManagementModule extends PhDAppModule {

    /**
     * Конструктор объекта
     *
     * @param configFolder директория для хранения конфигурационных файлов
     * @throws java.lang.NullPointerException если директория не задана
     */
    public JsonInputDataManagementModule(Path configFolder) {
        Objects.requireNonNull(configFolder);
    }

    @Override
    protected void configure() {
        bind(InputDataManager.class, JsonInputDataManager.class);
        multiBind(ShutdownHook.class, JsonInputDataManager.class);
    }
}
