package ru.spbftu.igorbotian.phdapp.svm.analytics;

import ru.spbftu.igorbotian.phdapp.svm.AbstractClassifierParameterFactory;
import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;

import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация параметра кросс-валидации классификатора по умолчанию
 */
class DefaultCrossValidatorParameterImpl<T> extends AbstractClassifierParameterFactory implements CrossValidatorParameter<T> {

    private static final String LOWER_BOUND_SUFFIX = "LowerBound";
    private static final String UPPER_BOUND_SUFFIX = "UpperBound";
    private static final String STEP_SIZE_SUFFIX = "StepSize";

    private final String name;
    private final ClassifierParameter<T> value;
    private final ClassifierParameter<T> lowerBound;
    private final ClassifierParameter<T> upperBound;
    private final ClassifierParameter<T> stepSize;

    public DefaultCrossValidatorParameterImpl(String name, Class<T> valueClass, T value, T lowerBound, T upperBound,
                                              T minValue, T maxValue, T stepSize, Comparator<T> comparator) {
        this.name = Objects.requireNonNull(name);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Cross-validator parameter name cannot be empty");
        }

        this.value = newParameter(name, valueClass, value, minValue, maxValue, comparator);
        this.lowerBound = newParameter(name + LOWER_BOUND_SUFFIX, valueClass, lowerBound, minValue, maxValue, comparator);
        this.upperBound = newParameter(name + UPPER_BOUND_SUFFIX, valueClass, upperBound, minValue, maxValue, comparator);
        this.stepSize = newParameter(name + STEP_SIZE_SUFFIX, valueClass, stepSize, minValue, maxValue, comparator);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<T> valueClass() {
        return lowerBound.valueClass();
    }

    @Override
    public ClassifierParameter<T> value() {
        return value;
    }

    @Override
    public ClassifierParameter<T> lowerBound() {
        return lowerBound;
    }

    @Override
    public ClassifierParameter<T> upperBound() {
        return upperBound;
    }

    @Override
    public ClassifierParameter<T> stepSize() {
        return stepSize;
    }

    @Override
    public String toString() {
        return String.format("%s(%s): %s [%s, %s]", name, valueClass().getSimpleName(),
                value.value().toString(), lowerBound.value().toString(), upperBound.value().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, lowerBound, upperBound, stepSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof DefaultCrossValidatorParameterImpl)) {
            return false;
        }

        DefaultCrossValidatorParameterImpl other = (DefaultCrossValidatorParameterImpl) obj;
        return (name.equals(other.name)
                && value.equals(other.value)
                && lowerBound.equals(other.lowerBound)
                && upperBound.equals(other.upperBound)
                && stepSize.equals(other.stepSize));
    }
}
