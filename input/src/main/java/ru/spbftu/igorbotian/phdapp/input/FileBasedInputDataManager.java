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
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.TrainingData;
import ru.spbftu.igorbotian.phdapp.conf.ConfigFolderPath;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Средство для работы с наборами исходных данных, хранящихся в виде файлов
 *
 * @see InputDataManager
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
     * Разрешение файлов, содержащих исходные данные поддерживаемого формата
     */
    private final String fileExtension;
    /**
     * Директория для хранения наборов исходных данных
     */
    private Path dataFolder;

    @Inject
    public FileBasedInputDataManager(Configuration config, @ConfigFolderPath String pathToConfigFolder, String fileExtension) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(pathToConfigFolder);
        Objects.requireNonNull(fileExtension);

        if (StringUtils.isEmpty(pathToConfigFolder)) {
            throw new IllegalArgumentException("Configuration folder cannot be null or empty");
        }

        this.config = config;
        this.fileExtension = fileExtension;
        dataFolder = initDataFolder(Paths.get(pathToConfigFolder));
        rememberPathToDataFolderOnExit();
    }

    private Path initDataFolder(Path configFolder) {
        assert (configFolder != null);

        Path dataFolder;

        if (config.hasSetting(DATA_FOLDER_CONFIG_SETTING)) {
            dataFolder = Paths.get(config.getString(DATA_FOLDER_CONFIG_SETTING));
        } else {
            Path parentFolder = Files.exists(configFolder)
                    ? configFolder.getParent()
                    : Paths.get(".").toAbsolutePath().getParent();
            dataFolder = parentFolder.resolve(DATA_FOLDER_NAME);
        }

        try {
            if (!Files.exists(dataFolder)) {
                Files.createDirectory(dataFolder);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create a folder intended to store input data");
        }

        return dataFolder;
    }

    private void rememberPathToDataFolderOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                config.setString(DATA_FOLDER_CONFIG_SETTING, dataFolder.toString());
            }
        });
    }

    /**
     * Получение директории для хранения наборов исходных данных по умолчанию
     *
     * @return директория
     */
    public Path defaultInputDataFolder() {
        return dataFolder;
    }

    /**
     * Задание директории для хранения наборов исходных данных по умолчанию
     *
     * @param folder директория
     * @throws java.lang.NullPointerException если директория не задана
     */
    public void setDefaultInputDataFolder(Path folder) {
        if (folder == null) {
            throw new NullPointerException("Folder cannot be null");
        }

        this.dataFolder = folder;
    }

    @Override
    public Set<String> listIds() throws IOException {
        Predicate<Path> byExtension = file -> file.getFileName().endsWith("." + fileExtension);
        Function<Path, String> toFileName = file -> file.getFileName().toString();

        return Files.walk(dataFolder)
                .filter(byExtension)
                .map(toFileName)
                .collect(
                        Collectors.toCollection(LinkedHashSet::new)
                );
    }

    @Override
    public TrainingData getById(String id) throws IOException, DataException {
        return deserialize(Files.newInputStream(dataFolder.resolve(id + "." + fileExtension)));
    }

    @Override
    public void save(String id, TrainingData data) throws IOException {
        serialize(data, Files.newOutputStream(dataFolder.resolve(id + "." + fileExtension), StandardOpenOption.CREATE));
    }

    /**
     * Десериализация набора исходных данных из файла
     *
     * @param stream файловый поток
     * @return набор исходных данных
     * @throws IOException                    в случае проблемы получения данных из файлового потока
     * @throws DataException                  если из данных, хранящихся в файле, невозможно сформировать набор исходных данных
     * @throws java.lang.NullPointerException если файловый поток не задан
     */
    protected abstract TrainingData deserialize(InputStream stream) throws IOException, DataException;

    /**
     * Сериализация заданного набора исходных данных в файл
     *
     * @param data   набор исходных данных
     * @param stream файловый поток
     * @throws IOException                    в случае проблемы записи данных в файловый поток
     * @throws DataException                  в случае проблемы формирования сериализованного представления набора исходных данных
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     */
    protected abstract void serialize(TrainingData data, OutputStream stream) throws IOException, DataException;
}
