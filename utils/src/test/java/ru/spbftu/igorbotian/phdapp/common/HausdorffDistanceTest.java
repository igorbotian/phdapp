package ru.spbftu.igorbotian.phdapp.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
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
        HausdorffDistance.Point point = new HausdorffDistance.Point(1.0, 1.0);
        Assert.assertEquals(0.0, HausdorffDistance.compute(
                Collections.singleton(point),
                Collections.singleton(point)
        ), PRECISION);
    }

    @Test
    public void testPoints() {
        Assert.assertEquals(1.0, HausdorffDistance.compute(
                Collections.singleton(new HausdorffDistance.Point(0.0, 0.0)),
                Collections.singleton(new HausdorffDistance.Point(-1.0, 0.0))
        ), PRECISION);
        Assert.assertEquals(Math.sqrt(2.0), HausdorffDistance.compute(
                Collections.singleton(new HausdorffDistance.Point(0.0, 0.0)),
                Collections.singleton(new HausdorffDistance.Point(1.0, 1.0))
        ), PRECISION);
    }

    @Test
    public void testSquares() {
        Set<HausdorffDistance.Point> first = Stream.of(
                new HausdorffDistance.Point(0.0, 0.0),
                new HausdorffDistance.Point(0.0, 1.0),
                new HausdorffDistance.Point(1.0, 1.0),
                new HausdorffDistance.Point(1.0, 0.0)
        ).collect(Collectors.toSet());

        Set<HausdorffDistance.Point> second = Stream.of(
                new HausdorffDistance.Point(2.0, 0.0),
                new HausdorffDistance.Point(2.0, 1.0),
                new HausdorffDistance.Point(3.0, 1.0),
                new HausdorffDistance.Point(3.0, 0.0)
        ).collect(Collectors.toSet());

        Assert.assertEquals(2.0, HausdorffDistance.compute(first, second), PRECISION);
    }

    @Test
    public void testTriangles() {
        Set<HausdorffDistance.Point> first = Stream.of(
                new HausdorffDistance.Point(0.0, 0.0),
                new HausdorffDistance.Point(0.0, 1.0),
                new HausdorffDistance.Point(1.0, 0.0)
        ).collect(Collectors.toSet());

        Set<HausdorffDistance.Point> second = Stream.of(
                new HausdorffDistance.Point(2.0, 0.0),
                new HausdorffDistance.Point(2.0, 1.0),
                new HausdorffDistance.Point(3.0, 0.0)
        ).collect(Collectors.toSet());

        Assert.assertEquals(2.0, HausdorffDistance.compute(first, second), PRECISION);
    }

    @Test
    public void testPolygons() {
        Set<HausdorffDistance.Point> first = Stream.of(
                new HausdorffDistance.Point(-1.0, 0.0),
                new HausdorffDistance.Point(-2.0, 0.0),
                new HausdorffDistance.Point(-2.0, -2.0),
                new HausdorffDistance.Point(-1.0, -2.0)
        ).collect(Collectors.toSet());

        Set<HausdorffDistance.Point> second = Stream.of(
                new HausdorffDistance.Point(1.0, 0.0),
                new HausdorffDistance.Point(1.0, 2.0),
                new HausdorffDistance.Point(2.0, 2.0),
                new HausdorffDistance.Point(2.0, 0.0)
        ).collect(Collectors.toSet());

        Assert.assertEquals(Math.sqrt(13.0), HausdorffDistance.compute(first, second), PRECISION);
    }
}
