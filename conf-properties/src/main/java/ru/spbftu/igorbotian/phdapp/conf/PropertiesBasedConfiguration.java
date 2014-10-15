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
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

/**
 * Средство управления конфигурацией приложения, хранящейся в файле формата <code>.properties/.conf</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.conf.Configuration
 */
@Singleton
class PropertiesBasedConfiguration implements Configuration {

    /**
     * Название файла конфигурации приложения в формате .properties / .conf
     */
    public static final String CONF_FILE_NAME = "phdapp.conf";
    private final Logger LOGGER = Logger.getLogger(PropertiesBasedConfiguration.class);
    /**
     * Директория для хранения конфигурационных файлов
     */
    private final Path CONF_FILE;

    /**
     * Контейнер для хранения конфигурационных настроек
     */
    private final Properties config = new Properties();

    /**
     * Флаг того, что текущая конфигурация изменилась.
     * В этом случае файл конфигурация физически будет перезаписан.
     */
    private boolean configChanged;

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

        try {
            load();
        } catch (IOException e) {
            LOGGER.error("Failed to read configuration", e);
        }

        registerShutdownHook();
    }

    private void load() throws IOException {
        if (Files.exists(CONF_FILE)) {
            config.load(Files.newBufferedReader(CONF_FILE));
        } else {
            LOGGER.info("No configuration found. All settings are set to default");
        }
    }

    private void store() throws IOException {
        if (configChanged()) {
            config.store(Files.newBufferedWriter(CONF_FILE), "PhD application configuration");
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    store();
                    LOGGER.debug("Configuration stored");
                } catch (IOException e) {
                    LOGGER.error("Failed to store configuration", e);
                }
            }
        });
    }

    private boolean configChanged() {
        return configChanged;
    }

    private void fireConfigChanged() {
        if (!configChanged) {
            configChanged = true;
            LOGGER.debug("Configuration changed");
        }
    }

    private void setValue(String param, Object value) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(value);

        if (StringUtils.isEmpty(param)) {
            throw new IllegalArgumentException("Parameter cannot be empty");
        }

        Object existingValue = config.getProperty(param);

        if (existingValue == null || !existingValue.equals(value)) {
            fireConfigChanged();
        }

        config.setProperty(param, value.toString());
    }

    private <T> T getValue(String param, Function<String, T> parser) {
        assert (parser != null);

        Objects.requireNonNull(param);

        if (StringUtils.isEmpty(param)) {
            throw new IllegalArgumentException("Parameter cannot be empty");
        }

        String value = config.getProperty(param);
        return (value == null) ? null : parser.apply(value);
    }

    @Override
    public boolean hasSetting(String param) {
        Objects.requireNonNull(param);

        if (StringUtils.isEmpty(param)) {
            throw new IllegalArgumentException("Param cannot be empty");
        }

        return config.getProperty(param) != null;
    }

    @Override
    public Boolean getBoolean(String param) {
        return getValue(param, Boolean::parseBoolean);
    }

    @Override
    public void setBoolean(String param, boolean value) {
        setValue(param, value);
    }

    @Override
    public Integer getInt(String param) {
        return getValue(param, Integer::parseInt);
    }

    @Override
    public void setInt(String param, int value) {
        setValue(param, value);
    }

    @Override
    public Long getLong(String param) {
        return getValue(param, Long::parseLong);
    }

    @Override
    public void setLong(String param, long value) {
        setValue(param, value);
    }

    @Override
    public Float getFloat(String param) {
        return getValue(param, Float::parseFloat);
    }

    @Override
    public void setFloat(String param, float value) {
        setValue(param, value);
    }

    @Override
    public Double getDouble(String param) {
        return getValue(param, Double::parseDouble);
    }

    @Override
    public void setDouble(String param, double value) {
        setValue(param, value);
    }

    @Override
    public String getString(String param) {
        return config.getProperty(param);
    }

    @Override
    public void setString(String param, String value) {
        setValue(param, value);
    }
}
