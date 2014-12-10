package ru.spbftu.igorbotian.phdapp.conf;

import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.utils.ShutdownHook;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Модуль конфигурации приложения
 * @see ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration
 */
public class ApplicationConfigurationModule extends PhDAppModule {

    /**
     * Директория для хранения конфигурационных файлов
     */
    private final Path configFolder;

    /**
     * Конструктор объекта
     *
     * @param configFolder директория для хранения конфигурационных файлов
     * @throws java.lang.NullPointerException если директория не задана
     */
    public ApplicationConfigurationModule(Path configFolder) {
        Objects.requireNonNull(configFolder);
        this.configFolder = configFolder;
    }


    @Override
    protected void configure() {
        bindConstant().annotatedWith(ConfigFolderPath.class).to(configFolder.toAbsolutePath().toString());
        bind(ApplicationConfiguration.class, ApplicationConfigurationImpl.class);
        multiBind(ShutdownHook.class, ApplicationConfigurationImpl.class);
    }
}
