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

import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationProgressListener;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Диалог задания значений указанных параметров классификации
 */
public class ClassifierParamsFrame extends PhDAppFrame {

    private static final String BACK_LABEL = "back";
    private static final String NEXT_LABEL = "next";
    private static final String ERROR_LABEL = "error";

    private final PhDAppFrame mainFrame;
    private JButton backButton;
    private JButton nextButton;

    public ClassifierParamsFrame(SwingUIHelper uiHelper, PhDAppFrame mainFrame, JComponent... paramWidgets) {
        super(uiHelper);

        this.mainFrame = Objects.requireNonNull(mainFrame);

        initComponents();
        layoutComponents(Objects.requireNonNull(paramWidgets));
        initListeners();
    }

    private void initComponents() {
        setTitle(mainFrame.getTitle());
        setIconImage(mainFrame.getIconImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        backButton = new JButton("< " + uiHelper.getLabel(BACK_LABEL));
        nextButton = new JButton(uiHelper.getLabel(NEXT_LABEL) + " >");
    }

    private void layoutComponents(JComponent... classifierParamWidgets) {
        JPanel paramsPanel = new JPanel();
        paramsPanel.setLayout(new GridBagLayout());

        int gridy = 1;
        int inset = 0;
        for (JComponent widget : classifierParamWidgets) {
            GridBagConstraints gbConstraints = new GridBagConstraints(1, gridy, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(inset, inset, inset, inset), 1, 1);
            paramsPanel.add(widget, gbConstraints);
            gridy++;
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        inset = 20;
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(new Insets(inset, inset, inset, inset)));
        contentPane.add(paramsPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                goToPreviousPage();
            }
        });

        backButton.addActionListener(e -> goToPreviousPage());
        nextButton.addActionListener(e -> goToNextPage());
    }

    public void goToPreviousPage() {
        setVisible(false);
        mainFrame.setVisible(true);
        dispose();
    }

    public void goToNextPage() {
        uiHelper.crossValidationProgressWindowDirector().addProgressListener(new CrossValidationProgressListener() {

            @Override
            public void crossValidationContinued(int percentsCompleted) {
                //
            }

            @Override
            public void crossValidationCompleted(Report report) {
                showResultsWindow(report);
            }

            @Override
            public void crossValidationFailed(Exception reason) {
                JOptionPane.showMessageDialog(ClassifierParamsFrame.this, reason.getMessage(),
                        uiHelper.getLabel(ERROR_LABEL), JOptionPane.ERROR_MESSAGE);
            }
        });
        uiHelper.crossValidationProgressWindowDirector().validate();
    }

    private void showResultsWindow(final Report report) {
        SwingUtilities.invokeLater(() -> {
            CrossValidationResultWindow window
                    = new CrossValidationResultWindow(ClassifierParamsFrame.this, uiHelper, report);
            setVisible(false);
            window.setVisible(true);
        });
    }
}
