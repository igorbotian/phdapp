package ru.spbftu.igorbotian.phdapp.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>HausdorffDistance</code>
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see HausdorffDistance
 */
public class HausdorffDistanceTest {

    private static final double PRECISION = 0.00001;

    @Test
    public void testSamePoint() {
        List<Double> point = newPoint(1.0, 1.0);
        Assert.assertEquals(0.0, HausdorffDistance.compute(
                Collections.singleton(point),
                Collections.singleton(point)
        ), PRECISION);
    }

    @Test
    public void testPoints() {
        Assert.assertEquals(1.0, HausdorffDistance.compute(
                Collections.singleton(newPoint(0.0, 0.0)),
                Collections.singleton(newPoint(-1.0, 0.0))
        ), PRECISION);
        Assert.assertEquals(Math.sqrt(2.0), HausdorffDistance.compute(
                Collections.singleton(newPoint(0.0, 0.0)),
                Collections.singleton(newPoint(1.0, 1.0))
        ), PRECISION);
    }

    @Test
    public void testSquares() {
        Set<List<Double>> first = Stream.of(
                newPoint(0.0, 0.0),
                newPoint(0.0, 1.0),
                newPoint(1.0, 1.0),
                newPoint(1.0, 0.0)
        ).collect(Collectors.toSet());

        Set<List<Double>> second = Stream.of(
                newPoint(2.0, 0.0),
                newPoint(2.0, 1.0),
                newPoint(3.0, 1.0),
                newPoint(3.0, 0.0)
        ).collect(Collectors.toSet());

        Assert.assertEquals(2.0, HausdorffDistance.compute(first, second), PRECISION);
    }

    @Test
    public void testTriangles() {
        Set<List<Double>> first = Stream.of(
                newPoint(0.0, 0.0),
                newPoint(0.0, 1.0),
                newPoint(1.0, 0.0)
        ).collect(Collectors.toSet());

        Set<List<Double>> second = Stream.of(
                newPoint(2.0, 0.0),
                newPoint(2.0, 1.0),
                newPoint(3.0, 0.0)
        ).collect(Collectors.toSet());

        Assert.assertEquals(2.0, HausdorffDistance.compute(first, second), PRECISION);
    }

    @Test
    public void testPolygons() {
        Set<List<Double>> first = Stream.of(
                newPoint(-1.0, 0.0),
                newPoint(-2.0, 0.0),
                newPoint(-2.0, -2.0),
                newPoint(-1.0, -2.0)
        ).collect(Collectors.toSet());

        Set<List<Double>> second = Stream.of(
                newPoint(1.0, 0.0),
                newPoint(1.0, 2.0),
                newPoint(2.0, 2.0),
                newPoint(2.0, 0.0)
        ).collect(Collectors.toSet());

        Assert.assertEquals(Math.sqrt(13.0), HausdorffDistance.compute(first, second), PRECISION);
    }
    
    private List<Double> newPoint(Double... params) {
        return Arrays.asList(params);
    }
}
