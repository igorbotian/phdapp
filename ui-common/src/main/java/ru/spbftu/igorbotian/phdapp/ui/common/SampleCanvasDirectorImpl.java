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

package ru.spbftu.igorbotian.phdapp.ui.common;

import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Line;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Range;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.SampleGenerator;

import java.util.Objects;
import java.util.Set;

/**
 * Реализация интерфейса <code>SampleCanvasDirector</code>.
 * Все данные берутся из <code>SampleGenerator</code>
 *
 * @see SampleCanvasDirector
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.sample.SampleGenerator
 */
class SampleCanvasDirectorImpl implements SampleCanvasDirector {

    private final SampleGenerator sampleGenerator;

    public SampleCanvasDirectorImpl(SampleGenerator sampleGenerator) {
        this.sampleGenerator = Objects.requireNonNull(sampleGenerator);
    }

    @Override
    public int numberOfPoints() {
        return sampleGenerator.numberOfPoints();
    }

    @Override
    public Line separatingLine() {
        return sampleGenerator.separatingLine();
    }

    @Override
    public Range<Double> xCoordinateRange() {
        return sampleGenerator.xCoordinateRange();
    }

    @Override
    public Range<Double> yCoordinateRange() {
        return sampleGenerator.yCoordinateRange();
    }

    @Override
    public Point firstSupportingPoint() {
        return sampleGenerator.firstSupportingPoint();
    }

    @Override
    public Point secondSupportingPoint() {
        return sampleGenerator.secondSupportingPoint();
    }

    @Override
    public Set<Point> firstSetOfPoints() {
        return sampleGenerator.firstSetOfPoints();
    }

    @Override
    public Set<Point> secondSetOfPoints() {
        return sampleGenerator.secondSetOfPoints();
    }

    @Override
    public void regeneratePoints(int count) {
        sampleGenerator.regeneratePoints(count);
    }
}
