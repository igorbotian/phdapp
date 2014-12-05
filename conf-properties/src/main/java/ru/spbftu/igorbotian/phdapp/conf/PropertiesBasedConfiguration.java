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

package ru.spbftu.igorbotian.phdapp.conf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Средство управления конфигурацией приложения, хранящейся в файле формата <code>.properties/.conf</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.conf.Configuration
 */
@Singleton
class PropertiesBasedConfiguration extends AbstractConfiguration {

    private final Logger LOGGER = Logger.getLogger(PropertiesBasedConfiguration.class);

    /**
     * Название файла конфигурации приложения в формате .properties / .conf
     */
    public static final String CONF_FILE_NAME = "phdapp.conf";

    /**
     * Директория для хранения конфигурационных файлов
     */
    private final Path CONF_FILE;

    /**
     * Контейнер для хранения конфигурационных настроек
     */
    private final Properties config = new Properties();

    /**
     * Конструктор объекта
     *
     * @param pathToConfigFolder директория для хранения конфигурационных файлов
     * @throws java.lang.IllegalArgumentException если директория физически не является директорией
     * @throws java.lang.IllegalStateException    если директория не существует и её невозможно создать
     * @throws java.lang.NullPointerException     если директория не задана
     */
    @Inject
    public PropertiesBasedConfiguration(@ConfigFolderPath String pathToConfigFolder) {
        Objects.requireNonNull(pathToConfigFolder);

        if (StringUtils.isEmpty(pathToConfigFolder)) {
            throw new IllegalArgumentException("Configuration folder cannot be empty");
        }

        Path configFolder = Paths.get(pathToConfigFolder);

        if (!Files.isDirectory(configFolder)) {
            throw new IllegalArgumentException("Configuration folder parameter should point to a folder, not a file");
        }

        if (!Files.exists(configFolder)) {
            try {
                Files.createDirectory(configFolder);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to create configuration folder: "
                        + configFolder.toAbsolutePath().toString());
            }
        }

        CONF_FILE = configFolder.resolve(CONF_FILE_NAME);
        LOGGER.info("Path to configuration file: " + CONF_FILE.toAbsolutePath().toString());
    }

    @Override
    protected Map<String, String> load() throws IOException {
        return makeConfigParams(loadPropertiesFromConfigFile());
    }

    @Override
    protected void store(Map<String, String> configParams) throws IOException {
        storePropertiesToConfigFile(makeProperties(configParams));
    }

    private Properties loadPropertiesFromConfigFile() throws IOException {
        Properties props = new Properties();
        props.load(Files.newBufferedReader(CONF_FILE));
        return props;
    }

    private void storePropertiesToConfigFile(Properties props) throws IOException {
        props.store(Files.newBufferedWriter(CONF_FILE), "PhD application configuration");
    }

    private Map<String, String> makeConfigParams(Properties props) {
        Map<String, String> params = new HashMap<>();
        props.stringPropertyNames().forEach(param -> params.put(param, props.getProperty(param)));
        return params;
    }

    private Properties makeProperties(Map<String, String> configParams) {
        Properties props = new Properties();
        configParams.forEach(props::put);
        return props;
    }
}
