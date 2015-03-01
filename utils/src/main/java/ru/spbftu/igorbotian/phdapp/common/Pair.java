package ru.spbftu.igorbotian.phdapp.common;

import java.util.Objects;

/**
 * Пара объектов заданного типа (объекты не могут быть равно <code>null</code>)
 */
public class Pair<F, S> {

    /**
     * Первый объект в паре
     */
    public final F first;

    /**
     * Второй объектв в паре
     */
    public final S second;

    /**
     * Создание пары
     *
     * @param first  первый объект в паре
     * @param second второй объект в паре
     * @throws NullPointerException если хотя бы один из объектов не задан
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Возвращает пару, в которой первый и второй элементы исходной пары поменяны местами
     * @return объект типа <code>Pair</code>
     */
    public Pair<S, F> swap() {
        return new Pair<>(second, first);
    }

    @Override
    public String toString() {
        return String.format("<%s,%s>", first, second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Pair)) {
            return false;
        }

        Pair other = (Pair) obj;
        return first.equals(other.first)
                && second.equals(other.second);
    }
}
