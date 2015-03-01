package ru.spbftu.igorbotian.phdapp.common;

import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.GaussianKernel;

/**
 * Модульный тест для класса <code>GaussianKernel</code>
 */
public class GaussianKernelTest {

    @Test
    public void testCompute() {
        testCompute(1.0, new double[] {1.0}, new double[] {1.0}, 1.0);
        testCompute(1.0 / Math.E, new double[] {0.0}, new double[] {1.0}, 1 / Math.sqrt(2));
    }

    private void testCompute(double expected, double[] x, double[] y, double sigma) {
        Assert.assertEquals(expected, GaussianKernel.compute(x, y, sigma), 0.001);
    }
}
