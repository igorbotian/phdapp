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

import ru.spbftu.igorbotian.phdapp.ui.common.CrossValidationSampleCanvasDirector;
import ru.spbftu.igorbotian.phdapp.ui.swing.widget.IntegerSpinner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * Диалоговое окно, в котором отображается расположение элементов выборки для классификатора
 */
public class SampleDialog extends JDialog {

    private static final String LOAD_LABEL = "load";
    private static final String SAVE_LABEL = "save";
    private static final String SAMPLE_LABEL = "sample";
    private static final String CLOSE_LABEL = "close";

    private static final String SAMPLE_FILE_EXTENSION = "json";

    private final CrossValidationSampleCanvasDirector canvasDirector;

    private IntegerSpinner sampleSizeSpinner;
    private CrossValidationSampleCanvas sampleCanvas;
    private JButton loadButton;
    private JButton saveButton;
    private JButton closeButton;

    /**
     * Общие элементы пользовательского интерфейса
     */
    private final SwingUIHelper uiHelper;

    public SampleDialog(JFrame owner, SwingUIHelper uiHelper) {
        super(owner);

        this.uiHelper = Objects.requireNonNull(uiHelper);
        this.canvasDirector = this.uiHelper.sampleCanvasDirector();

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        setModal(true);
        setTitle(uiHelper.getLabel(SAMPLE_LABEL));

        sampleSizeSpinner = uiHelper.widgets().preciseSampleSizeSpinner();
        sampleCanvas = new CrossValidationSampleCanvas(canvasDirector);
        sampleCanvas.setPreferredSize(new Dimension(480, 480));
        sampleCanvas.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        loadButton = new JButton(uiHelper.getLabel(LOAD_LABEL) + "...");
        saveButton = new JButton(uiHelper.getLabel(SAVE_LABEL) + "...");
        closeButton = new JButton(uiHelper.getLabel(CLOSE_LABEL));
    }

    private void layoutComponents() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(loadButton);
        buttonPane.add(saveButton);
        buttonPane.add(Box.createHorizontalGlue());
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
        setLocationRelativeTo(getOwner());
    }

    private void initListeners() {
        sampleSizeSpinner.addChangeListener(e -> {
            canvasDirector.regeneratePoints(sampleSizeSpinner.getValue());
            sampleCanvas.repaint();
        });

        loadButton.addActionListener(e -> loadSampleFromFile());
        saveButton.addActionListener(e -> saveSampleToFile());
        closeButton.addActionListener(e -> SampleDialog.this.dispose());
    }

    private void loadSampleFromFile() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON", SAMPLE_FILE_EXTENSION));
        fileChooser.setMultiSelectionEnabled(false);

        int result = fileChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION) {
            loadSampleFromFile(fileChooser.getSelectedFile());
        }
    }

    private void loadSampleFromFile(File file) {
        // TODO
    }

    private void saveSampleToFile() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON", SAMPLE_FILE_EXTENSION));
        fileChooser.setMultiSelectionEnabled(false);

        int result = fileChooser.showSaveDialog(this);

        if(result == JFileChooser.APPROVE_OPTION) {
            saveSampleToFile(fileChooser.getSelectedFile());
        }
    }

    private void saveSampleToFile(File file) {
        // TODO
    }
}
