package ru.spbftu.igorbotian.phdapp.conf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.utils.ShutdownHook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Реализация конфигурации приложения
 *
 * @see ru.spbftu.igorbotian.phdapp.conf.Configuration
 * @see ru.spbftu.igorbotian.phdapp.utils.ShutdownHook
 */
@Singleton
class ApplicationConfigurationImpl extends PropertiesBasedConfiguration implements ApplicationConfiguration, ShutdownHook {

    private static final Logger LOGGER = Logger.getLogger(PropertiesBasedConfiguration.class);

    /**
     * Название файла конфигурации приложения в формате .properties / .conf
     */
    public static final String CONF_FILE_NAME = "phdapp.conf";

    /**
     * Директория для хранения конфигурационных файлов
     */
    private final Path CONF_FILE;

    /**
     * Конструктор объекта
     *
     * @param pathToConfigFolder директория для хранения конфигурационных файлов
     * @throws IllegalArgumentException если директория физически не является директорией
     * @throws IllegalStateException    если директория не существует и её невозможно создать
     * @throws NullPointerException     если директория не задана
     */
    @Inject
    public ApplicationConfigurationImpl(@ConfigFolderPath String pathToConfigFolder) {
        Objects.requireNonNull(pathToConfigFolder);

        if (pathToConfigFolder.isEmpty()) {
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
                        + configFolder.toAbsolutePath().toString(), e);
            }
        }

        CONF_FILE = resolveConfigFile(configFolder, CONF_FILE_NAME);
        LOGGER.info("Path to configuration file: " + CONF_FILE.toAbsolutePath().toString());

        if(Files.exists(CONF_FILE)) {
            loadConfiguration();
        } else{
            LOGGER.info("Configuration file does not exist");
        }
    }

    private Path resolveConfigFile(Path configFolder, String configFileName) {
        Path configFile = configFolder.resolve(configFileName);

        try {
            if(Files.exists(configFile)) {
                configFile = configFile.toRealPath(LinkOption.NOFOLLOW_LINKS);
            }
        } catch(IOException e) {
            LOGGER.error("Failed to obtain a real configuration path", e);
        }

        return configFile;
    }

    @Override
    public void onExit() {
        storeConfiguration();
    }

    private void loadConfiguration() {
        LOGGER.debug("Loading configuration");

        try {
            loadFromFile(CONF_FILE);
            LOGGER.debug("Configuration successfully loaded");
        } catch (IOException e) {
            LOGGER.error("Unable to load configuration", e);
        }
    }

    private void storeConfiguration() {
        LOGGER.debug("Storing configuration");

        if (configChanged()) {
            try {
                saveToFile(CONF_FILE, "PhD application configuration");
                LOGGER.debug("Configuration successfully stored");
            } catch (IOException e) {
                LOGGER.error("Failed to store configuration", e);
            }
        }
    }
}
