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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Средство управления конфигурацией приложения, хранящейся в файле формата <code>.properties/.conf</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.conf.Configuration
 */
public class PropertiesBasedConfiguration extends AbstractConfiguration {

    /**
     * Загрузка настроек конфигурации из заданного файла.
     * Настройки, загруженные до этого, будут стёрты.
     *
     * @param file путь к файлу, содержащего настройки конфигурации в формате <code>.properties/.conf</code>
     * @throws IOException          в случае проблем чтения настроек из файла
     * @throws NullPointerException если путь не задан
     */
    public void loadFromFile(Path file) throws IOException {
        Objects.requireNonNull(file);

        clear();
        makeConfigParams(makeProperties(file)).forEach(super::setString);
    }

    /**
     * Сохранение настроек конфигурации в заданный файл в формате <code>.properties/.conf</code>
     * @param file файл, куда буду сохранены настройки конфигурации
     * @param comments комментарии к настройкам
     * @throws IOException в случае проблем сохранения настроек в файл
     * @throws NullPointerException если хотя бы один из параметров не задан
     */
    public void saveToFile(Path file, String comments) throws IOException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(comments);

        makeProperties().store(Files.newBufferedWriter(file), comments);
    }

    private Map<String, String> makeConfigParams(Properties props) {
        Map<String, String> params = new HashMap<>();
        props.stringPropertyNames().forEach(param -> params.put(param, props.getProperty(param)));
        return params;
    }

    private Properties makeProperties() {
        Map<String, String> configParams = new HashMap<>();
        params().forEach(p -> configParams.put(p, getString(p)));
        return makeProperties(configParams);
    }

    private Properties makeProperties(Path file) throws IOException {
        Properties props = new Properties();
        props.load(Files.newBufferedReader(file));
        return props;
    }

    private Properties makeProperties(Map<String, String> configParams) {
        Properties props = new Properties();
        configParams.forEach(props::put);
        return props;
    }
}
