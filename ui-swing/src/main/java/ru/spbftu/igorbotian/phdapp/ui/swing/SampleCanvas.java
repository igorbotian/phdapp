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

package ru.spbftu.igorbotian.phdapp.ui.swing;

import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.*;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;
import ru.spbftu.igorbotian.phdapp.ui.common.SampleCanvasDirector;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Set;

/**
 * Компонент, отображающий расположение элементов выборки для классификатора
 */
public class SampleCanvas extends JPanel {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color SEPARATING_LINE_COLOR = Color.GRAY;
    private static final Color FIRST_SET_OF_POINTS_COLOR = Color.BLUE;
    private static final Color SECOND_SET_OF_POINTS_COLOR = Color.RED;
    private static final Color SUPPORTING_POINT_COLOR = Color.GREEN;

    private static final int SET_POINT_RADIUS = 5;
    private static final int SUPPORTING_POINT_RADIUS = 7;

    private SampleCanvasDirector director;

    public SampleCanvas(SampleCanvasDirector director) {
        this.director = Objects.requireNonNull(director);
        setMinimumSize(new Dimension(320, 320));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        paintSeparatingLine(g);
        paintFirstSetOfPoints(g);
        paintSecondSetOfPoints(g);
    }

    private void paintBackground(Graphics g) {
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintSeparatingLine(Graphics g) {
        Line line = director.separatingLine();
        double beginX = line.x(director.yCoordinateRange().lowerBound());
        double beginY = line.y(beginX);
        double endX = line.x(director.yCoordinateRange().upperBound());
        double endY = line.y(endX);

        g.setColor(SEPARATING_LINE_COLOR);
        g.drawLine(toCanvasX(beginX), toCanvasY(beginY), toCanvasX(endX), toCanvasY(endY));
    }

    private void paintFirstSetOfPoints(Graphics g) {
        paintSetOfPoints(g, director.firstSetOfPoints(), director.firstSupportingPoint(),
                FIRST_SET_OF_POINTS_COLOR);
    }

    private void paintSecondSetOfPoints(Graphics g) {
        paintSetOfPoints(g, director.secondSetOfPoints(), director.secondSupportingPoint(),
                SECOND_SET_OF_POINTS_COLOR);
    }

    private void paintSetOfPoints(Graphics g, Set<ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point> points,
                                  Point supportingPoint, Color color) {
        for (Point point : points) {
            paintPoint(g, point, color, SET_POINT_RADIUS);
        }

        paintPoint(g, supportingPoint, SUPPORTING_POINT_COLOR, SUPPORTING_POINT_RADIUS);
    }

    private void paintPoint(Graphics g, Point point, Color color, int radius) {
        int x = toCanvasX(point.x());
        int y = toCanvasY(point.y());

        g.setColor(color);
        g.fillOval(x, y, radius, radius);
    }

    private int toCanvasX(double sampleX) {
        return (int) translate(sampleX, director.xCoordinateRange().lowerBound(),
                director.xCoordinateRange().upperBound(), 0.0, getWidth());
    }

    private int toCanvasY(double sampleY) {
        return (int) translate(sampleY, director.yCoordinateRange().lowerBound(),
                director.yCoordinateRange().upperBound(), 0.0, getHeight());
    }

    private double translate(double from, double fromMin, double fromMax, double toMin, double toMax) {
        double fromLength = Math.abs(fromMax - fromMin);
        double distanceBetweenFromXAndFromMin = from - fromMin;
        assert (distanceBetweenFromXAndFromMin >= 0.0);

        double c = distanceBetweenFromXAndFromMin / fromLength;
        return (c * (toMax - toMin));
    }
}
