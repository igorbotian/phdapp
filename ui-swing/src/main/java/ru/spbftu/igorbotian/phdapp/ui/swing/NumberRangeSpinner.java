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

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Виджет для ввода диапазона чисел с заданными пределами, имеющего заданное описание
 */
public abstract class NumberRangeSpinner <T extends Number> extends JPanel {

    private String description;
    private JLabel label;
    private JSpinner minSpinner;
    private JSpinner maxSpinner;

    public NumberRangeSpinner(String description, SpinnerModel minSpinnerModel, SpinnerModel maxSpinnerModel) {
        this.description = Objects.requireNonNull(description);
        this.label = new JLabel(description + ":");
        this.minSpinner = new JSpinner(Objects.requireNonNull(minSpinnerModel));
        this.maxSpinner = new JSpinner(Objects.requireNonNull(maxSpinnerModel));

        layoutComponents();
        initListeners();
    }

    private void layoutComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(label);
        add(minSpinner);
        add(new JLabel("..."));
        add(maxSpinner);
    }

    private void initListeners() {
        // TODO check min/max
    }

    @SuppressWarnings("unchecked")
    public T getMinValue() {
        return (T) minSpinner.getValue();
    }

    @SuppressWarnings("unchecked")
    public T getMaxValue() {
        return (T) maxSpinner.getValue();
    }

    public void setMinValue(T value) {
        minSpinner.setValue(value);
    }

    public void setMaxValue(T value) {
        maxSpinner.setValue(value);
    }

    public String getDescription() {
        return description;
    }
}
