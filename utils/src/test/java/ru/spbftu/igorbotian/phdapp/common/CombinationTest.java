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

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>Combination</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public class CombinationTest {

    @Test
    public void testMultipleMultiple() {
        String x1 = "x1";
        String x2 = "x2";
        String y1 = "y1";
        String y2 = "y2";

        Set<Pair<String, String>> expected = Stream.of(
                new Pair<>(x1, y1),
                new Pair<>(x1, y2),
                new Pair<>(x2, y1),
                new Pair<>(x2, y2)
        ).collect(Collectors.toSet());

        Set<Pair<String, String>> actual = Combination.of(
                Stream.of(x1, x2).collect(Collectors.toSet()),
                Stream.of(y1, y2).collect(Collectors.toSet())
        );

        Assert.assertTrue(sameSets(expected, actual));
    }

    @Test
    public void testMultipleSingle() {
        String x1 = "x1";
        String x2 = "x2";
        String y1 = "y1";

        Set<Pair<String, String>> expected = Stream.of(
                new Pair<>(x1, y1),
                new Pair<>(x2, y1)
        ).collect(Collectors.toSet());

        Set<Pair<String, String>> actual = Combination.of(
                Stream.of(x1, x2).collect(Collectors.toSet()),
                Collections.singleton(y1)
        );

        Assert.assertTrue(sameSets(expected, actual));
    }

    @Test
    public void testSingleSingle() {
        String x1 = "x1";
        String y1 = "y1";
        Set<Pair<String, String>> expected = Collections.singleton(new Pair<>(x1, y1));
        Set<Pair<String, String>> actual = Combination.of(
                Collections.singleton(x1),
                Collections.singleton(y1)
        );

        Assert.assertTrue(sameSets(expected, actual));
    }

    @Test
    public void testItemsCombinationIncludingPairsOfSameItems() {
        Set<String> set = Stream.of("a", "b", "c").collect(Collectors.toSet());
        Set<Pair<String, String>> expected = Stream.of(
                new Pair<>("a", "a"),
                new Pair<>("a", "b"),
                new Pair<>("a", "c"),
                new Pair<>("b", "b"),
                new Pair<>("b", "c"),
                new Pair<>("c", "c")
        ).collect(Collectors.toSet());
        Set<Pair<String, String>> actual = Combination.ofItems(set, true);

        Assert.assertTrue(sameSets(expected, actual));
    }

    @Test
    public void testItemsCombinationNotIncludingPairsOfSameItems() {
        Set<String> set = Stream.of("a", "b", "c").collect(Collectors.toSet());
        Set<Pair<String, String>> expected = Stream.of(
                new Pair<>("a", "b"),
                new Pair<>("a", "c"),
                new Pair<>("b", "c")
        ).collect(Collectors.toSet());
        Set<Pair<String, String>> actual = Combination.ofItems(set, false);

        Assert.assertTrue(sameSets(expected, actual));
    }

    private <T> boolean sameSets(Set<T> first, Set<T> second) {
        return (first.size() == second.size()) && first.containsAll(second);
    }
}
