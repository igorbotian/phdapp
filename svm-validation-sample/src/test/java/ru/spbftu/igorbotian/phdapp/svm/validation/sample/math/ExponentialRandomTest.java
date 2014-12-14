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

package ru.spbftu.igorbotian.phdapp.svm.validation.sample.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Модульные тесты для класса <code>ExponentialRandom</code>
 *
 * @see ExponentialRandom
 */
public class ExponentialRandomTest {

    private static final int[] NUMBERS_COUNTS = {10, 50, 100, 250, 500, 1000};

    @Test
    public void testRandomness() {
        int sampleCount = 10;
        double min = 1.0;
        double max = NUMBERS_COUNTS[NUMBERS_COUNTS.length - 1];

        for(int count : NUMBERS_COUNTS) {
            double[][] samples = new double[sampleCount][];

            for(int i = 0; i < samples.length; i++) {
                samples[i] = new double[count];

                for(int j = 0; j < samples[i].length; j++) {
                    samples[i][j] = ExponentialRandom.nextDouble(min, max);
                }

                Arrays.sort(samples[i]);
            }

            checkArraysAreDifferent(samples);
        }
    }

    private void checkArraysAreDifferent(double[][] samples) {
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
        double min = 0.0;

        for(int i = 0; i < NUMBERS_COUNTS.length; i++) {
            int count = NUMBERS_COUNTS[i];
            int max = (i + 1 <= NUMBERS_COUNTS.length - 1) ? NUMBERS_COUNTS[i + 1] : NUMBERS_COUNTS[i];
            double[] values = new double[count];

            for(int j = 0; j < values.length; j++) {
                values[j] = ExponentialRandom.nextDouble(min, max);
            }

            checkMoreNumbersInTwoThirds(values, min, max);
        }
    }

    private void checkMoreNumbersInTwoThirds(double[] randoms, double lowerBound, double upperBound) {
        double middle = lowerBound + 2 * (upperBound - lowerBound) / 3;
        int i = 0;

        Arrays.sort(randoms);

        while(i < randoms.length && randoms[i] < middle) {
            i++;
        }

        Assert.assertTrue(i > (randoms.length - i));
    }
}
