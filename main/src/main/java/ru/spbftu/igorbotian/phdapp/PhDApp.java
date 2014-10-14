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

package ru.spbftu.igorbotian.phdapp;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.apache.commons.lang3.StringUtils;
import ru.spbftu.igorbotian.phdapp.conf.PropertiesBasedConfigurationModule;
import ru.spbftu.igorbotian.phdapp.input.JsonInputDataManagementModule;
import ru.spbftu.igorbotian.phdapp.locale.java.JavaI18NLocalizationModule;
import ru.spbftu.igorbotian.phdapp.log.Log4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Основной класс приложения.
 * Содержит точку входа в программу.
 */
public class PhDApp {

    /**
     * Название системного свойства, значение которого указывает на директорию для хранения конфигурационных файлов
     */
    private static final String CONFIG_FOLDER_SYSTEM_PROPERTY = "phdapp.conf.folder";

    /**
     * Директория для хранения конфигурационных файлов
     */
    private static final Path CONFIG_FOLDER = getConfigFolder();
    /**
     * Список из модулей приложения
     */
    public static final Set<? extends AbstractModule> INJECTION_MODULES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    new PropertiesBasedConfigurationModule(CONFIG_FOLDER),
                    new JsonInputDataManagementModule(CONFIG_FOLDER),
                    new JavaI18NLocalizationModule()
            ))
    );

    private static Path getConfigFolder() {
        String customConfFolderName = System.getProperty(CONFIG_FOLDER_SYSTEM_PROPERTY);

        if (StringUtils.isNotEmpty(customConfFolderName)) {
            Path customConfigFolder = Paths.get(customConfFolderName);

            if (Files.exists(customConfigFolder)) {
                return customConfigFolder;
            }
        }

        return Paths.get(".");
    }

    /**
     * Точка входа в программу
     *
     * @param args аргументы приложения
     */
    public static void main(String[] args) {
        Log4j.init(CONFIG_FOLDER);
        Guice.createInjector(INJECTION_MODULES);
    }
}