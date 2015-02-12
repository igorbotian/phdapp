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

import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.PointwiseInputData;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.utils.ShutdownHook;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Средство для работы с наборами исходных данных, хранящихся в виде файлов
 *
 * @see ru.spbftu.igorbotian.phdapp.input.InputDataManager
 */
public abstract class FileBasedInputDataManager implements InputDataManager, ShutdownHook {

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
    private final ApplicationConfiguration config;

    /**
     * Разрешение файлов, содержащих исходные данные поддерживаемого формата
     */
    private final String fileExtension;

    /**
     * Директория для хранения наборов исходных данных
     */
    private Path dataFolder;

    @Inject
    public FileBasedInputDataManager(ApplicationConfiguration config, String fileExtension) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(fileExtension);

        if (fileExtension.isEmpty()) {
            throw new IllegalArgumentException("File extension cannot be empty");
        }

        this.config = config;
        this.fileExtension = fileExtension;
        dataFolder = initDataFolder();
    }

    private Path initDataFolder() {
        Path folder;

        if (config.hasParam(DATA_FOLDER_CONFIG_SETTING)) {
            folder = Paths.get(config.getString(DATA_FOLDER_CONFIG_SETTING));
        } else {
            Path parentFolder = Paths.get(".").toAbsolutePath().getParent();
            folder = parentFolder.resolve(DATA_FOLDER_NAME);
        }

        return folder;
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
        Objects.requireNonNull(folder);
        this.dataFolder = folder;
    }

    @Override
    public void onExit() {
        config.setString(DATA_FOLDER_CONFIG_SETTING, dataFolder.toString());
    }

    @Override
    public Set<String> listIds() throws IOException {
        Predicate<Path> byExtension = file -> file.getFileName().endsWith("." + fileExtension);
        Function<Path, String> toFileName = file -> file.getFileName().toString();

        if (!Files.exists(dataFolder)) {
            return Collections.emptySet();
        }

        return Files.walk(dataFolder)
                .filter(byExtension)
                .map(toFileName)
                .collect(
                        Collectors.toCollection(LinkedHashSet::new)
                );
    }

    @Override
    public PointwiseInputData getById(String id) throws IOException, DataException {
        Path targetFile = dataFolder.resolve(id + "." + fileExtension);

        if (!Files.exists(targetFile)) {
            throw new FileNotFoundException("File corresponding to the given ID doesn't exist: "
                    + targetFile.toAbsolutePath().toString());
        }

        return deserialize(Files.newInputStream(targetFile));
    }

    @Override
    public void save(String id, PointwiseInputData data) throws IOException, DataException {
        Path targetFile = dataFolder.resolve(id + "." + fileExtension);

        if (!Files.exists(dataFolder)) {
            Files.createDirectories(dataFolder);
        }

        serialize(data, Files.newOutputStream(targetFile, StandardOpenOption.CREATE));
    }

    /**
     * Десериализация набора исходных данных из файла
     *
     * @param stream файловый поток
     * @return набор исходных данных
     * @throws java.io.IOException                              в случае проблемы получения данных из файлового потока
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если из данных, хранящихся в файле, невозможно сформировать набор исходных данных
     * @throws java.lang.NullPointerException                   если файловый поток не задан
     */
    protected abstract PointwiseInputData deserialize(InputStream stream) throws IOException, DataException;

    /**
     * Сериализация заданного набора исходных данных в файл
     *
     * @param data   набор исходных данных
     * @param stream файловый поток
     * @throws java.io.IOException                              в случае проблемы записи данных в файловый поток
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException в случае проблемы формирования сериализованного представления набора исходных данных
     * @throws java.lang.NullPointerException                   если хотя бы один из параметров не задан
     */
    protected abstract void serialize(PointwiseInputData data, OutputStream stream) throws IOException, DataException;
}
