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

package ru.spbftu.igorbotian.phdapp.log;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Вспомогательный класс, отвечающий за инициализацию и конфигурацию средств логирования.
 * В данном случае это log4j.
 */
public final class Log4j {

    /**
     * Предопределенное название файла конфигурации log4j в формате <code>.properties</code>
     */
    private static final String LOG4J_PROPERTIES = "log4j.properties";

    /**
     * Предопределенное название файла конфигурации log4j в формате <code>.xml</code>
     */
    private static final String LOG4J_XML = "log4j.xml";

    private Log4j() {
        //
    }

    /**
     * Инициализация log4j.
     * Конфигурационный файл должен находиться либо в директории, определяемой системным свойством
     * <code>phdapp.conf.folder</code>, либо в текущей папке.
     *
     * @param configFolder директория для хранения конфигурационных файлов
     * @throws java.lang.NullPointerException если директория не задана
     */
    public static void init(Path configFolder) {
        Objects.requireNonNull(configFolder);
        Path log4jXml = configFolder.resolve(LOG4J_XML);

        if (Files.exists(log4jXml)) {
            DOMConfigurator.configure(log4jXml.toAbsolutePath().toString());
        } else {
            Path log4jProps = configFolder.resolve(LOG4J_PROPERTIES);

            if (Files.exists(log4jProps)) {
                PropertyConfigurator.configure(log4jProps.toAbsolutePath().toString());
            }
        }
    }
}
