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
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Objects;

/**
 * Виджет для ввода числа в заданных пределах, имеющего заданное описание
 */
public abstract class NumberSpinner<T extends Number> extends JPanel {

    private JLabel label;
    private JSpinner spinner;

    protected NumberSpinner(String description, SpinnerModel spinnerModel) {
        this.spinner = new JSpinner(Objects.requireNonNull(spinnerModel));
        this.label = new JLabel(description + ":");

        adjustSpinnerTextFieldColumns();
        this.spinner.addChangeListener(e -> adjustSpinnerTextFieldColumns());

        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0);
        add(label, gbConstraints);

        gbConstraints.gridx++;
        add(spinner, gbConstraints);

        gbConstraints.gridx++;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.weightx = 1.0;
        add(new JPanel(), gbConstraints);
    }

    private void adjustSpinnerTextFieldColumns() {
        JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        textField.setColumns(getValue().toString().length());
        revalidate();
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) spinner.getValue();
    }

    public void setValue(T value) {
        spinner.setValue(value);
    }

    public void addChangeListener(ChangeListener listener) {
        spinner.addChangeListener(listener);
    }
}
