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

import org.apache.commons.lang3.StringUtils;
import ru.spbftu.igorbotian.phdapp.conf.ConfigFolderPath;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

import javax.inject.Inject;
import java.io.File;

/**
 * Средство для работы с наборами исходных данных, хранящихся в виде файлов
 */
public abstract class FileBasedInputDataManager implements InputDataManager {

    /**
     * Название директории для хранения наборов исходных данных
     */
    static final String DATA_FOLDER_NAME = "data";

    /**
     * Настройка конфигурации приложения, указывающая на директорию для хранения наборов исходных данных
     */
    static final String DATA_FOLDER_CONFIG_SETTING = "phdapp.data.folder";

    /**
     * Средства конфигурации приложения
     */
    private final Configuration config;

    /**
     * Директория для хранения наборов исходных данных
     */
    private File dataFolder;

    @Inject
    public FileBasedInputDataManager(Configuration config, @ConfigFolderPath String pathToConfigFolder) {
        if (config == null) {
            throw new NullPointerException("Configuration cannot be null");
        }

        if (StringUtils.isEmpty(pathToConfigFolder)) {
            throw new IllegalArgumentException("Configuration folder cannot be null or empty");
        }

        this.config = config;
        dataFolder = initDataFolder(new File(pathToConfigFolder));
        rememberPathToDataFolderOnExit();
    }

    private File initDataFolder(File configFolder) {
        assert (configFolder != null);

        File dataFolder;

        if (config.hasSetting(DATA_FOLDER_CONFIG_SETTING)) {
            dataFolder = new File(config.getString(DATA_FOLDER_CONFIG_SETTING));
        } else {
            File parentFolder = configFolder.exists() ? configFolder.getParentFile() : new File(".").getParentFile();
            dataFolder = new File(parentFolder, DATA_FOLDER_NAME);
        }

        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            throw new IllegalStateException("Unable to create a folder intended to store input data");
        }

        return dataFolder;
    }

    private void rememberPathToDataFolderOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                config.setString(DATA_FOLDER_CONFIG_SETTING, dataFolder.getAbsolutePath());
            }
        });
    }

    /**
     * Получение директории для хранения наборов исходных данных по умолчанию
     *
     * @return директория
     */
    public File defaultInputDataFolder() {
        return dataFolder;
    }

    /**
     * Задание директории для хранения наборов исходных данных по умолчанию
     *
     * @param folder директория
     * @throws java.lang.NullPointerException если директория не задана
     */
    public void setDefaultInputDataFolder(File folder) {
        if (folder == null) {
            throw new NullPointerException("Folder cannot be null");
        }

        this.dataFolder = folder;
    }
}
