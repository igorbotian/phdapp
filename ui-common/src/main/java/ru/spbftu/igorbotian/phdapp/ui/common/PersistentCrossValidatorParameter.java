package ru.spbftu.igorbotian.phdapp.ui.common;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;
import ru.spbftu.igorbotian.phdapp.svm.MutableClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.MutableCrossValidatorParameter;

import java.util.Objects;
import java.util.function.Function;

/**
 * Параметр средства кросс-валидации классификатора, значение которого сохраняется в конфигурации приложения
 */
class PersistentCrossValidatorParameter<T> implements MutableCrossValidatorParameter<T> {

    private final PersistentClassifierParameter<T> value;
    private final PersistentClassifierParameter<T> lowerBound;
    private final PersistentClassifierParameter<T> upperBound;
    private final PersistentClassifierParameter<T> stepSize;

    @Inject
    public PersistentCrossValidatorParameter(ApplicationConfiguration appConfig, CrossValidatorParameter<T> parameter,
                                             Function<String, T> parser) {
        value = new PersistentClassifierParameter<>(appConfig, parameter.value(), parser);
        lowerBound = new PersistentClassifierParameter<>(appConfig, parameter.lowerBound(), parser);
        upperBound = new PersistentClassifierParameter<>(appConfig, parameter.upperBound(), parser);
        stepSize = new PersistentClassifierParameter<>(appConfig, parameter.stepSize(), parser);
    }

    @Override
    public String name() {
        return value.name();
    }

    @Override
    public Class<T> valueClass() {
        return value.valueClass();
    }

    @Override
    public MutableClassifierParameter<T> value() {
        return value;
    }

    @Override
    public MutableClassifierParameter<T> lowerBound() {
        return lowerBound;
    }

    @Override
    public MutableClassifierParameter<T> upperBound() {
        return upperBound;
    }

    @Override
    public MutableClassifierParameter<T> stepSize() {
        return stepSize;
    }

    @Override
    public String toString() {
        return String.format("%s(%s): %s [%s, %s]", name(), valueClass().getSimpleName(),
                value.value().toString(), lowerBound.value().toString(), upperBound.value().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), value, lowerBound, upperBound, stepSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof CrossValidatorParameter)) {
            return false;
        }

        CrossValidatorParameter other = (CrossValidatorParameter) obj;
        return (name().equals(other.name())
                && value().equals(other.value())
                && lowerBound().equals(other.lowerBound())
                && upperBound().equals(other.upperBound())
                && stepSize().equals(other.stepSize()));
    }
}
