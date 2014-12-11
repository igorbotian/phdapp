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

package ru.spbftu.igorbotian.phdapp.ui.swing.widget;

import javax.swing.*;

/**
 * Виджет для ввода диапазона целых чисел с заданными пределами, имеющего заданное описание
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.swing.widget.NumberRangeSpinner
 */
public class IntegerRangeSpinner extends NumberRangeSpinner<Integer> {

    public IntegerRangeSpinner(String description, int lowerValue, int lowerMin, int lowerMax,
                               int upperValue, int upperMin, int upperMax, int stepSize) {
        super(description, new SpinnerNumberModel(lowerValue, lowerMin, lowerMax, stepSize),
                new SpinnerNumberModel(upperValue, upperMin, upperMax, stepSize));
    }
}