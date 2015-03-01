/*
 * Copyright (c) 2015 Igor Botian
 *
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
 */

package ru.spbftu.igorbotian.phdapp.common;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс для формирования сочетаний элементов заданных множеств
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public final class Combination {

    private Combination() {
        //
    }

    /**
     * Сочетание всех элементов из двух заданных множеств
     *
     * @param first  первое множество (непустое)
     * @param second второе множество (непустое)
     * @return множество, состоящее из пар сочетанияисходных множеств, где первый элемент взят из первого множества,
     * а второй - из второго
     * @throws NullPointerException     если хотя бы одно из множеств не задано
     * @throws IllegalArgumentException если хотя одно из множеств пустое
     */
    public static <F, S> Set<Pair<F, S>> of(Set<F> first, Set<S> second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);

        if (first.isEmpty() || second.isEmpty()) {
            throw new IllegalArgumentException("Both sets should not be empty");
        }

        Set<Pair<F, S>> result = new LinkedHashSet<>();
        first.forEach(f -> second.forEach(s -> result.add(new Pair<>(f, s))));
        return Collections.unmodifiableSet(result);
    }

    /**
     * Сочетание всех элементов заданного множества
     *
     * @param set                     множество, сочетание по элементам которого будет полученое (непустое)
     * @param includePairsOfSameItems <code>true</code>, если среди сочетаний для каждого элемента <code>A</code>
     *                                исходного множества будет включена пара <code>A-A</code>;
     *                                <code>false</code>, если такие пары не будут включены
     * @return сочетание всех элементов исходного множества
     * @throws NullPointerException если исходное множество не задано
     * @throws IllegalArgumentException если исходное множество пустое
     */
    public static <T> Set<Pair<T, T>> ofItems(Set<T> set, boolean includePairsOfSameItems) {
        Objects.requireNonNull(set);

        if(set.isEmpty()) {
            throw new IllegalArgumentException("Set cannot be empty");
        }

        Set<Pair<T, T>> result = new LinkedHashSet<>();

        for (T first : set) {
            for (T second : set) {
                if(!includePairsOfSameItems && first.equals(second)) {
                    continue;
                }

                Pair<T, T> pair = new Pair<>(first, second);

                if(!result.contains(pair) && !result.contains(pair.swap())) {
                    result.add(new Pair<>(first, second));
                }
            }
        }

        return Collections.unmodifiableSet(result);
    }
}
