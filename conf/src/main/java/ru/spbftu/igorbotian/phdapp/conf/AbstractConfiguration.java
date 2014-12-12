package ru.spbftu.igorbotian.phdapp.conf;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Реализация основной логики управления конфигурацией приложения
 *
 * @see ru.spbftu.igorbotian.phdapp.conf.Configuration
 */
public abstract class AbstractConfiguration implements Configuration {

    private final Logger LOGGER = Logger.getLogger(AbstractConfiguration.class);

    /**
     * Контейнер для хранения конфигурационных настроек
     */
    private final Map<String, String> config = new ConcurrentHashMap<>();

    /**
     * Флаг того, что текущая конфигурация изменилась.
     * В этом случае файл конфигурация физически будет перезаписан.
     */
    private boolean configChanged;

    /**
     * Удаление всех настроек конфигурации
     */
    protected void clear() {
        config.clear();
    }

    /**
     * Получение информации о том, была ли изменена конфигурация приложения
     *
     * @return <code>true</code>, если конфигуриция была изменена; иначе <code>false</code>
     */
    protected boolean configChanged() {
        return configChanged;
    }

    /**
     * Указание того, что будет проведена физическое сохранение конфигурации по причине изменения её значений
     */
    protected void fireConfigChanged() {
        if (!configChanged) {
            configChanged = true;
            LOGGER.debug("Configuration changed");
        }
    }

    private void setValue(String param, Object value) {
        Objects.requireNonNull(param);
        Objects.requireNonNull(value);

        if (param.isEmpty()) {
            throw new IllegalArgumentException("Parameter cannot be empty");
        }

        Object existingValue = config.get(param);

        if (existingValue == null || !existingValue.equals(value)) {
            fireConfigChanged();
        }

        LOGGER.debug(String.format("Setting new configuration setting: name = '%s', value = '%s'",
                param, value.toString()));
        config.put(param, value.toString());
    }

    private <T> T getValue(String param, Function<String, T> parser) {
        assert (parser != null);

        Objects.requireNonNull(param);

        if (param.isEmpty()) {
            throw new IllegalArgumentException("Parameter cannot be empty");
        }

        LOGGER.debug(String.format("Requesting value of configuration setting '%s'", param));
        String value = config.get(param);

        if (value == null) {
            LOGGER.warn("Trying to read non-initialized configuration setting: " + param);
        }

        return (value == null) ? null : parser.apply(value);
    }

    @Override
    public boolean hasParam(String param) {
        Objects.requireNonNull(param);

        if (param.isEmpty()) {
            throw new IllegalArgumentException("Param cannot be empty");
        }

        return config.get(param) != null;
    }

    @Override
    public Set<String> params() {
        return config.keySet();
    }

    @Override
    public Boolean getBoolean(String param) {
        return getValue(param, Boolean::parseBoolean);
    }

    @Override
    public void setBoolean(String param, boolean value) {
        setValue(param, value);
    }

    @Override
    public Integer getInt(String param) {
        return getValue(param, Integer::parseInt);
    }

    @Override
    public void setInt(String param, int value) {
        setValue(param, value);
    }

    @Override
    public Long getLong(String param) {
        return getValue(param, Long::parseLong);
    }

    @Override
    public void setLong(String param, long value) {
        setValue(param, value);
    }

    @Override
    public Float getFloat(String param) {
        return getValue(param, Float::parseFloat);
    }

    @Override
    public void setFloat(String param, float value) {
        setValue(param, value);
    }

    @Override
    public Double getDouble(String param) {
        return getValue(param, Double::parseDouble);
    }

    @Override
    public void setDouble(String param, double value) {
        setValue(param, value);
    }

    @Override
    public String getString(String param) {
        return config.get(param);
    }

    @Override
    public void setString(String param, String value) {
        setValue(param, value);
    }
}
