package ru.spbftu.igorbotian.phdapp.svm;

import java.util.Comparator;
import java.util.Objects;

/**
 * Реализация параметра классификатора по умолчанию
 */
class DefaultClassifierParameterImpl<T> implements MutableClassifierParameter<T> {

    private final String name;
    private T value;
    private final T minValue;
    private final T maxValue;
    private final Class<T> valueClass;

    public DefaultClassifierParameterImpl(String name, Class<T> valueClass, T value, T minValue, T maxValue,
                                          Comparator<T> comparator) {

        this.name = Objects.requireNonNull(name);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Classifier parameter name cannot be empty");
        }

        if (Objects.requireNonNull(comparator).compare(minValue, maxValue) == 1) {
            throw new IllegalArgumentException("Minimum value of the classifier parameter cannot be greater than the maximum one");
        }

        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueClass = Objects.requireNonNull(valueClass);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public void setValue(T newValue) {
        this.value = newValue;
    }

    @Override
    public T minValue() {
        return minValue;
    }

    @Override
    public T maxValue() {
        return maxValue;
    }

    @Override
    public Class<T> valueClass() {
        return valueClass;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)=%s", name, valueClass.getSimpleName(), value.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, valueClass, value);
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
        return name.equals(other.name())
                && valueClass.equals(other.valueClass())
                && value.equals(other.value());
    }
}
