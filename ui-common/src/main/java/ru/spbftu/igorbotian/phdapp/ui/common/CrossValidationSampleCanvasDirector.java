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
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.MathDataFactory;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Point;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.math.Range;

import java.util.Set;

/**
 * Модель компонента, отвечающего за отображение выборки для классификатора
 */
public interface CrossValidationSampleCanvasDirector {

    MathDataFactory mathDataFactory();

    int numberOfPoints();

    Line separatingLine();

    Range<Double> xCoordinateRange();

    Range<Double> yCoordinateRange();

    Point firstSupportingPoint();

    Point secondSupportingPoint();

    Set<Point> firstSetOfPoints();

    Set<Point> secondSetOfPoints();

    void regeneratePoints(int count);
}
