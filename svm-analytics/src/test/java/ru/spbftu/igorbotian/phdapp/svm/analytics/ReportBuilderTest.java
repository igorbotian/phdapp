/**
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

package ru.spbftu.igorbotian.phdapp.svm.analytics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Модульные тесты для класса <code>ReportBuilder</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.ReportBuilder
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.Report
 */
public class ReportBuilderTest {

    private final float delta = 0.0001f;

    private final float accuracy = 0.1f;
    private final float precision = 0.2f;
    private final float recall = 0.3f;

    private final float lessThanMinimumValue = -0.5f;
    private final float minimumValue = 0.0f;
    private final float maximumValue = 1.0f;
    private final float greaterThanMaximumValue = 1.5f;

    private ReportBuilder reportBuilder;

    @Before
    public void setUp() {
        reportBuilder = ReportBuilder.newReport()
                .setAccuracy(accuracy)
                .setPrecision(precision)
                .setRecall(recall);
    }

    @Test
    public void testBuild() {
        Assert.assertEquals(accuracy, reportBuilder.build().accuracy(), delta);
        Assert.assertEquals(precision, reportBuilder.build().precision(), delta);
        Assert.assertEquals(recall, reportBuilder.build().recall(), delta);
    }

    //-------------------------------------------------------------------------

    @Test(expected = IllegalStateException.class)
    public void testBuildWithoutMetrics() {
        ReportBuilder.newReport().build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWithoutAccuracy() {
        ReportBuilder.newReport().setPrecision(precision).setRecall(recall).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWithoutPrecision() {
        ReportBuilder.newReport().setAccuracy(accuracy).setRecall(recall).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWithoutRecall() {
        ReportBuilder.newReport().setAccuracy(accuracy).setPrecision(precision).build();
    }

    //-------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanMinimumAccuracyValue() {
        ReportBuilder.newReport().setAccuracy(lessThanMinimumValue);
    }

    @Test
    public void testMinimumAccuracyValue() {
        reportBuilder.setAccuracy(minimumValue);
        Assert.assertEquals(minimumValue, reportBuilder.build().accuracy(), delta);
    }

    @Test
    public void testMaximumAccuracyValue() {
        reportBuilder.setAccuracy(maximumValue);
        Assert.assertEquals(maximumValue, reportBuilder.build().accuracy(), delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanMaximumAccuracyValue() {
        ReportBuilder.newReport().setAccuracy(greaterThanMaximumValue);
    }

    //-------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanMinimumPrecisionValue() {
        ReportBuilder.newReport().setPrecision(lessThanMinimumValue);
    }

    @Test
    public void testMinimumPrecisionValue() {
        reportBuilder.setPrecision(minimumValue);
        Assert.assertEquals(minimumValue, reportBuilder.build().precision(), delta);
    }

    @Test
    public void testMaximumPrecisionValue() {
        reportBuilder.setPrecision(maximumValue);
        Assert.assertEquals(maximumValue, reportBuilder.build().precision(), delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanMaximumPrecisionValue() {
        ReportBuilder.newReport().setPrecision(greaterThanMaximumValue);
    }

    //-------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanMinimumRecallValue() {
        ReportBuilder.newReport().setRecall(lessThanMinimumValue);
    }

    @Test
    public void testMinimumRecallValue() {
        reportBuilder.setRecall(minimumValue);
        Assert.assertEquals(minimumValue, reportBuilder.build().recall(), delta);
    }

    @Test
    public void testMaximumRecallValue() {
        reportBuilder.setRecall(maximumValue);
        Assert.assertEquals(maximumValue, reportBuilder.build().recall(), delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanMaximumRecallValue() {
        ReportBuilder.newReport().setRecall(greaterThanMaximumValue);
    }
}
