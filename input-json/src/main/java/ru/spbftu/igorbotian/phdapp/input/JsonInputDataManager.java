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

import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import ru.spbftu.igorbotian.phdapp.conf.ConfigFolderPath;

import javax.inject.Inject;
import java.io.File;

/**
 * Реализация средства для работы с исходными данными, использующее в своей основе формат JSON
 * @see InputDataManager
 */
@Singleton
class JsonInputDataManager implements InputDataManager {

    /**
     * Название директории для хранения наборов исходных данных
     */
    private static final String DATA_FOLDER_NAME = "data";

    /**
     * Директория для хранения наборов исходных данных
     */
    private final File dataFolder;

    @Inject
    public JsonInputDataManager(@ConfigFolderPath String pathToConfigFolder) {
        if (StringUtils.isEmpty(pathToConfigFolder)) {
            throw new IllegalArgumentException("Configuration folder cannot be null or empty");
        }

        File configFolder = new File(pathToConfigFolder);
        dataFolder = new File(configFolder.exists() ? configFolder.getParent() : "..", DATA_FOLDER_NAME);

        if(!dataFolder.exists()) {
            if(!dataFolder.mkdir()) {
                throw new IllegalStateException("Unable to create a folder intended to store input data");
            }
        }
    }

    @Override
    public File inputDataFolder() {
        return dataFolder;
    }
}
