/*
 * Copyright (c) 2014 Igor Botian
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
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.svm.analytics.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Модульные тесты для класса <code>ExponentialRandom</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.math.ExponentialRandom
 */
public class ExponentialRandomTest {

    private static final int[] NUMBERS_COUNTS = {10, 50, 100, 250, 500, 1000};

    @Test
    public void testRandomness() {
        int sampleCount = 10;
        int max = NUMBERS_COUNTS[NUMBERS_COUNTS.length - 1];

        for(int count : NUMBERS_COUNTS) {
            int[][] samples = new int[10][];

            for(int i = 0; i < sampleCount; i++) {
                samples[i] = ExponentialRandom.nextIntegers(count, 1, max);
                Arrays.sort(samples[i]);
            }

            checkArraysAreDifferent(samples);
        }
    }

    private void checkArraysAreDifferent(int[][] samples) {
        for(int i = 0; i < samples.length; i++) {
            for(int j = 0; j < samples.length; j++) {
                if(i == j) {
                    continue; // один и тот же массив
                }

                Assert.assertTrue(!Arrays.equals(samples[i], samples[j]));
            }
        }
    }

    @Test
    public void testExponentiality() {
        int min = 1;

        for(int i = 0; i < NUMBERS_COUNTS.length; i++) {
            int count = NUMBERS_COUNTS[i];
            int max = (i + 1 <= NUMBERS_COUNTS.length - 1) ? NUMBERS_COUNTS[i + 1] : NUMBERS_COUNTS[i];

            checkMoreNumbersInLessHalf(ExponentialRandom.nextIntegers(count, min, max), min, max);
        }
    }

    private void checkMoreNumbersInLessHalf(int[] randoms, int lowerBound, int upperBound) {
        int middle = lowerBound + (upperBound - lowerBound) / 2;
        int i = 0;

        Arrays.sort(randoms);

        while(i < randoms.length && randoms[i] < middle) {
            i++;
        }

        Assert.assertTrue(i > (randoms.length - i - 1 /* середину выкидываем */));
    }
}
