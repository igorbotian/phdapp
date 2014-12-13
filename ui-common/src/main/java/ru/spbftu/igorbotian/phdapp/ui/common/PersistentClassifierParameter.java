package ru.spbftu.igorbotian.phdapp.ui.common;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.MutableClassifierParameter;

import java.util.Objects;
import java.util.function.Function;

/**
 * Параметр конфигурации классификатора, значение которого сохраняется в конфигурации приложения
 */
class PersistentClassifierParameter<T> implements MutableClassifierParameter<T> {

    /**
     * Конфигурация приложения
     */
    private final ApplicationConfiguration appConfig;

    /**
     * Параметр конфигурации классиифкатора, значение которого будет сохраняться в конфигурации
     */
    private final ClassifierParameter<T> parameter;

    /**
     * Средство десериализации значения параметра из строкового представления
     */
    private final Function<String, T> parser;

    @Inject
    public PersistentClassifierParameter(ApplicationConfiguration appConfig,
                                         ClassifierParameter<T> parameter,
                                         Function<String, T> parser) {
        this.appConfig = Objects.requireNonNull(appConfig);
        this.parameter = Objects.requireNonNull(parameter);
        this.parser = Objects.requireNonNull(parser);

        if (!appConfig.hasParam(parameter.name())) {
            setValue(parameter.value());
        }
    }

    @Override
    public String name() {
        return parameter.name();
    }

    @Override
    public T value() {
        return parser.apply(appConfig.getString(parameter.name()));
    }

    @Override
    public void setValue(T newValue) throws IllegalArgumentException {
        appConfig.setString(parameter.name(), Objects.requireNonNull(newValue).toString());
    }

    @Override
    public T minValue() {
        return parameter.minValue();
    }

    @Override
    public T maxValue() {
        return parameter.maxValue();
    }

    @Override
    public Class<T> valueClass() {
        return parameter.valueClass();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)=%s", name(), valueClass().getSimpleName(), value().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), valueClass(), value());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof ClassifierParameter)) {
            return false;
        }

        ClassifierParameter other = (ClassifierParameter) obj;
        return (name().equals(other.name())
                && valueClass().equals(other.valueClass())
                && value().equals(other.value()));
    }
}
