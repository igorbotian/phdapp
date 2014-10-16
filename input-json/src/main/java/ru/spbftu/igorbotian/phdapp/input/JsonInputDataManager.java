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
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.TrainingData;
import ru.spbftu.igorbotian.phdapp.common.pdu.TrainingDataPDU;
import ru.spbftu.igorbotian.phdapp.conf.ConfigFolderPath;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Реализация средства для работы с исходными данными, использующее в своей основе формат JSON
 *
 * @see ru.spbftu.igorbotian.phdapp.input.InputDataManager
 * @see ru.spbftu.igorbotian.phdapp.input.FileBasedInputDataManager
 */
@Singleton
class JsonInputDataManager extends FileBasedInputDataManager {

    /**
     * Расширение JSON-файлов
     */
    private static final String JSON_FILE_EXT = "json";

    /**
     * Средство преобразования объектов в строковое представление в формате JSON и обратно
     */
    private final Gson gson = new Gson();

    @Inject
    JsonInputDataManager(Configuration config, @ConfigFolderPath String pathToConfigFolder) {
        super(config, pathToConfigFolder, JSON_FILE_EXT);
    }

    @Override
    protected TrainingData deserialize(InputStream stream) throws IOException, DataException {
        Objects.requireNonNull(stream);

        try {
            return gson.fromJson(
                    new InputStreamReader(stream, StandardCharsets.UTF_8), TrainingDataPDU.class).toObject();
        } catch (JsonSyntaxException e) {
            throw new DataException("Failed to deserialize input data", e);
        }
    }

    @Override
    protected void serialize(TrainingData data, OutputStream stream) throws IOException {
        Objects.requireNonNull(data);
        Objects.requireNonNull(stream);

        stream.write(gson.toJson(TrainingDataPDU.toPDU(data)).getBytes(StandardCharsets.UTF_8));
    }
}
