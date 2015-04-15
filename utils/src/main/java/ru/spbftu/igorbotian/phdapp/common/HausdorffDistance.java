package ru.spbftu.igorbotian.phdapp.common;

import com.vividsolutions.jts.algorithm.distance.DiscreteHausdorffDistance;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;

import java.util.Objects;
import java.util.Set;

/**
 * Класс, служащий для вычисления расстояния Хаусдорфа между двумя заданными множествами точек
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="https://en.wikipedia.org/wiki/Hausdorff_distance">https://en.wikipedia.org/wiki/Hausdorff_distance</a>
 */
public final class HausdorffDistance {

    private HausdorffDistance() {
        //
    }

    /**
     * Вычисление расстояния Хаусдорфа между двумя заданными множествами точек
     *
     * @param first  первое множество точек
     * @param second второй множество точек
     * @return неотрицательное вещественное число
     * @throws NullPointerException     если хотя бы одно из множеств не задано
     * @throws IllegalArgumentException если хотя бы одно из множеств пустое
     */
    public static double compute(Set<Point> first, Set<Point> second) {
        if (first == null) {
            throw new NullPointerException("First set cannot be null");
        }

        if (first.isEmpty()) {
            throw new NullPointerException("First set cannot be empty");
        }

        if (second == null) {
            throw new NullPointerException("First set cannot be null");
        }

        if (second.isEmpty()) {
            throw new NullPointerException("First set cannot be empty");
        }

        GeometryFactory factory = new GeometryFactory();
        Geometry firstFigure = new MultiPoint(pointsOf(first, factory), factory);
        Geometry secondFigure = new MultiPoint(pointsOf(second, factory), factory);

        return DiscreteHausdorffDistance.distance(firstFigure, secondFigure);
    }

    private static com.vividsolutions.jts.geom.Point[] pointsOf(Set<Point> set, GeometryFactory factory) {
        assert set != null;
        assert !set.isEmpty();
        assert factory != null;

        com.vividsolutions.jts.geom.Point[] points = new com.vividsolutions.jts.geom.Point[set.size()];
        int i = 0;

        for (Point point : set) {
            points[i] = factory.createPoint(new Coordinate(point.x, point.y, point.z));
            i++;
        }

        return points;
    }

    /**
     * Точка
     */
    public static class Point {

        /**
         * Координата по оси X
         */
        public double x;

        /**
         * Координата по оси Y
         */
        public double y;

        /**
         * Координата по оси Z
         */
        public double z;

        public Point(double x, double y) {
            this(x, y, 0.0);
        }

        public Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null || !(obj instanceof Point)) {
                return false;
            }

            Point other = (Point) obj;
            return 0 == Double.compare(x, other.x)
                    && 0 == Double.compare(y, other.y)
                    && 0 == Double.compare(z, other.z);
        }

        @Override
        public String toString() {
            return String.format("[%.10f, %.10f, %.10f]", x, y, z);
        }
    }
}
