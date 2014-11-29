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

import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.SampleGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

/**
 * Диалоговое окно, в котором отображается расположение элементов выборки для классификатора
 */
public class SampleDialog extends JDialog {

    private static final String SAMPLE_LABEL = "sample";
    private static final String REGENERATE_LABEL = "regenerate";
    private static final String CLOSE_LABEL = "close";

    private final Localization localization;
    private final SampleGenerator sampleGenerator;
    private final ClassifierParamsWidgets paramsWidgets;

    private IntegerSpinner sampleSizeSpinner;
    private SampleCanvas sampleCanvas;
    private JButton regenerateButton;
    private JButton closeButton;

    public SampleDialog(Localization localization, SampleGenerator sampleGenerator, ClassifierParamsWidgets paramsWidgets) {
        this.localization = Objects.requireNonNull(localization);
        this.sampleGenerator = Objects.requireNonNull(sampleGenerator);
        this.paramsWidgets = Objects.requireNonNull(paramsWidgets);

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        setModal(true);
        setTitle(localization.getLabel(SAMPLE_LABEL));

        sampleSizeSpinner = paramsWidgets.preciseSampleSizeSpinner();
        sampleCanvas = new SampleCanvas(sampleGenerator);
        sampleCanvas.setPreferredSize(new Dimension(480, 480));
        sampleCanvas.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        regenerateButton = new JButton(localization.getLabel(REGENERATE_LABEL));
        closeButton = new JButton(localization.getLabel(CLOSE_LABEL));
    }

    private void layoutComponents() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(regenerateButton);
        buttonPane.add(closeButton);

        int margin = 20;
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(margin, margin, margin, margin));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0);
        contentPane.add(sampleSizeSpinner, gbConstraints);

        gbConstraints.gridy++;
        gbConstraints.weightx = 1.0;
        gbConstraints.weighty = 1.0;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.insets = new Insets(0, 0, 0, 0);
        contentPane.add(sampleCanvas, gbConstraints);

        gbConstraints.gridy++;
        gbConstraints.weighty = 0.0;
        gbConstraints.weightx = 0.0;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.insets = new Insets(10, 0, 0, 0);
        gbConstraints.anchor = GridBagConstraints.EAST;
        contentPane.add(buttonPane, gbConstraints);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    private void initListeners() {
        regenerateButton.addActionListener(e -> {
            sampleGenerator.regeneratePoints(sampleGenerator.numberOfPoints());
            sampleCanvas.repaint();
        });

        sampleSizeSpinner.addChangeListener(e -> {
            sampleGenerator.regeneratePoints(sampleSizeSpinner.getValue());
            sampleCanvas.repaint();
        });

        closeButton.addActionListener(e -> SampleDialog.this.dispose());
    }
}
