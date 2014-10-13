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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Singleton;
import org.apache.commons.io.IOUtils;
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.TrainingData;
import ru.spbftu.igorbotian.phdapp.common.pdu.TrainingDataPDU;
import ru.spbftu.igorbotian.phdapp.conf.ConfigFolderPath;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

import javax.inject.Inject;
import java.io.*;

/**
 * Реализация средства для работы с исходными данными, использующее в своей основе формат JSON
 *
 * @see InputDataManager, FileBasedInputDataManager
 */
@Singleton
class JsonInputDataManager extends FileBasedInputDataManager {

    /**
     * Средство преобразования объектов в строковое представление в формате JSON и обратно
     */
    private final Gson gson = new Gson();

    @Inject
    JsonInputDataManager(Configuration config, @ConfigFolderPath String pathToConfigFolder) {
        super(config, pathToConfigFolder);
    }

    @Override
    protected TrainingData deserialize(InputStream stream) throws IOException, DataException {
        if (stream == null) {
            throw new NullPointerException("Input stream cannot be null");
        }

        try {
            return gson.fromJson(new InputStreamReader(stream), TrainingDataPDU.class).toObject();
        } catch (JsonSyntaxException e) {
            throw new DataException("Failed to deserialize input data", e);
        }
    }

    @Override
    protected void serialize(TrainingData data, OutputStream stream) throws IOException {
        if (data == null) {
            throw new NullPointerException("Data cannot be null");
        }

        if (stream == null) {
            throw new NullPointerException("Input stream cannot be null");
        }

        IOUtils.write(gson.toJson(TrainingDataPDU.toPDU(data)), stream);
    }

    @Override
    protected String supportedFileExtension() {
        return "json";
    }
}
