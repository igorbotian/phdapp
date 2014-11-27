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
 * Виджет для ввода числа в заданных пределах, имеющего заданное описание
 */
public abstract class NumberSpinner<T extends Number> extends JPanel {

    private String description;
    private JLabel label;
    private JSpinner spinner;

    protected NumberSpinner(String description, SpinnerModel spinnerModel) {
        this.description = Objects.requireNonNull(description);
        this.spinner = new JSpinner(Objects.requireNonNull(spinnerModel));
        this.label = new JLabel(description + ":");

        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 0));
        add(label, BorderLayout.LINE_START);
        add(spinner, BorderLayout.LINE_END);
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) spinner.getValue();
    }

    public void setValue(T value) {
        spinner.setValue(value);
    }

    public String getDescription() {
        return description;
    }
}
