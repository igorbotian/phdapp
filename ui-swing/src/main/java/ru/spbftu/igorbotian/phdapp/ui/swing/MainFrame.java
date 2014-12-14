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

import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Enumeration;
import java.util.function.Supplier;

/**
 * Главное окно программы
 */
class MainFrame extends PhDAppFrame {

    private static final String WINDOW_ICON_RESOURCE = "window_icon.png";
    private static final String WINDOW_TITLE_LABEL = "appName";
    private static final String FILE_LABEL = "file";
    private static final String QUIT_LABEL = "quit";
    private static final String HELP_LABEL = "help";
    private static final String ABOUT_LABEL = "about";
    private static final String PRECISION_ACTION_LABEL = "calculatePrecisionAction";
    private static final String PRECISION_LABEL = "calculatePrecision";
    private static final String AVERAGE_PRECISION_ACTION_LABEL = "calculateAveragePrecisionAction";
    private static final String AVERAGE_PRECISION_LABEL = "calculateAveragePrecision";
    private static final String SAMPLE_SIZE_LABEL = "determinePrecisionDependenceOnSampleSize";
    private static final String SAMPLE_SIZE_ACTION_LABEL = "determinePrecisionDependenceOnSampleSizeAction";
    private static final String JUDGEMENTS_COUNT_LABEL = "determinePrecisionDependenceOnJudgementsCount";
    private static final String JUDGEMENTS_COUNT_ACTION_LABEL = "determinePrecisionDependenceOnJudgementsCountAction";
    private static final String PARAMETERS_LABEL = "determinePrecisionDependenceOnParameters";
    private static final String PARAMETERS_ACTION_LABEL = "determinePrecisionDependenceOnParametersAction";
    private static final String INTERVAL_JUDGEMENTS_RATIO_LABEL = "determinePrecisionDependenceOnIntervalJudgementsRatio";
    private static final String INTERVAL_JUDGEMENTS_RATIO_ACTION_LABEL = "determinePrecisionDependenceOnIntervalJudgementsRatioAction";
    private static final String NEXT_LABEL = "next";
    private static final String VIEW_SAMPLE_LABEL = "viewSample";

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem exitMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;

    private ButtonGroup actionsGroup;
    private JRadioButton precisionActionRadioButton;
    private JRadioButton averagePrecisionActionRadioButton;
    private JRadioButton sampleSizeActionRadioButton;
    private JRadioButton judgementsCountActionRadioButton;
    private JRadioButton parametersActionRadioButton;
    private JRadioButton intervalJudgementsActionRadioButton;

    private JButton nextButton;

    public MainFrame(SwingUIHelper uiHelper) {
        super(uiHelper);

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        setIconImage(new ImageIcon(this.getClass().getResource(WINDOW_ICON_RESOURCE)).getImage());
        setTitle(uiHelper.getLabel(WINDOW_TITLE_LABEL));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        menuBar = new JMenuBar();
        fileMenu = new JMenu(uiHelper.getLabel(FILE_LABEL));
        exitMenuItem = new JMenuItem(uiHelper.getLabel(QUIT_LABEL));
        helpMenu = new JMenu(uiHelper.getLabel(HELP_LABEL));
        aboutMenuItem = new JMenuItem(uiHelper.getLabel(ABOUT_LABEL));

        actionsGroup = new ButtonGroup();
        precisionActionRadioButton = new JRadioButton(uiHelper.getLabel(PRECISION_ACTION_LABEL));
        actionsGroup.add(precisionActionRadioButton);
        precisionActionRadioButton.setSelected(true);

        averagePrecisionActionRadioButton = new JRadioButton(uiHelper.getLabel(AVERAGE_PRECISION_ACTION_LABEL));
        actionsGroup.add(averagePrecisionActionRadioButton);

        sampleSizeActionRadioButton = new JRadioButton(uiHelper.getLabel(SAMPLE_SIZE_ACTION_LABEL));
        actionsGroup.add(sampleSizeActionRadioButton);

        judgementsCountActionRadioButton = new JRadioButton(uiHelper.getLabel(JUDGEMENTS_COUNT_ACTION_LABEL));
        actionsGroup.add(judgementsCountActionRadioButton);

        parametersActionRadioButton = new JRadioButton(uiHelper.getLabel(PARAMETERS_ACTION_LABEL));
        actionsGroup.add(parametersActionRadioButton);

        intervalJudgementsActionRadioButton = new JRadioButton(uiHelper.getLabel(INTERVAL_JUDGEMENTS_RATIO_ACTION_LABEL));
        actionsGroup.add(intervalJudgementsActionRadioButton);

        nextButton = new JButton(uiHelper.getLabel(NEXT_LABEL) + " >");

        setJMenuBar(menuBar);
    }

    private void layoutComponents() {
        fileMenu.add(exitMenuItem);
        helpMenu.add(aboutMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        JPanel actionsGroupPanel = new JPanel();
        actionsGroupPanel.setLayout(new BoxLayout(actionsGroupPanel, BoxLayout.Y_AXIS));
        actionsGroupPanel.add(describe(precisionActionRadioButton, uiHelper.getLabel(PRECISION_LABEL)));
        actionsGroupPanel.add(describe(averagePrecisionActionRadioButton, uiHelper.getLabel(AVERAGE_PRECISION_LABEL)));
        actionsGroupPanel.add(describe(sampleSizeActionRadioButton, uiHelper.getLabel(SAMPLE_SIZE_LABEL)));
        actionsGroupPanel.add(describe(judgementsCountActionRadioButton, uiHelper.getLabel(JUDGEMENTS_COUNT_LABEL)));
        actionsGroupPanel.add(describe(parametersActionRadioButton, uiHelper.getLabel(PARAMETERS_LABEL)));
        actionsGroupPanel.add(describe(intervalJudgementsActionRadioButton, uiHelper.getLabel(INTERVAL_JUDGEMENTS_RATIO_LABEL)));

        JPanel actionsPane = new JPanel();
        actionsPane.setLayout(new BoxLayout(actionsPane, BoxLayout.PAGE_AXIS));
        actionsPane.add(actionsGroupPanel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(nextButton);

        int inset = 20;
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.insets = new Insets(inset, inset, inset, inset);
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1.0;
        gbConstraints.weighty = 1.0;
        gbConstraints.gridx = 1;
        gbConstraints.gridy = 1;
        contentPane.add(actionsPane, gbConstraints);

        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.insets = new Insets(0, inset, inset, inset);
        gbConstraints.weighty = 0.0;
        gbConstraints.gridy++;
        gbConstraints.anchor = GridBagConstraints.EAST;
        contentPane.add(buttonPane, gbConstraints);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel describe(JRadioButton button, String description) {
        JLabel descriptionLabel = new JLabel(
                "<html><p style=\"text-align: center;\">" + description.replaceAll("\\n", "<br/>") + "</p></html>",
                JLabel.CENTER);
        descriptionLabel.setEnabled(false);
        descriptionLabel.setFont(button.getFont().deriveFont(Font.PLAIN, button.getFont().getSize() - 1));

        JPanel pane = new JPanel();
        pane.setBorder(new EmptyBorder(0, 0, 5, 0));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(button);
        pane.add(descriptionLabel);

        return pane;
    }

    private void initListeners() {
        exitMenuItem.addActionListener(e -> System.exit(0));
        aboutMenuItem.addActionListener(e -> new AboutDialog(MainFrame.this).setVisible(true));
        nextButton.addActionListener(e -> goToNextPage());
    }

    private void goToNextPage() {
        Enumeration<AbstractButton> buttons = actionsGroup.getElements();

        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();

            if (!button.isSelected()) {
                continue;
            }

            if (precisionActionRadioButton == button) {
                JButton viewSampleButton = new JButton(uiHelper.getLabel(VIEW_SAMPLE_LABEL) + "...");
                viewSampleButton.addActionListener(e -> {
                    new SampleDialog(MainFrame.this).setVisible(true);
                });

                JComponent[] widgets = new JComponent[]{
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        viewSampleButton
                };

                doAction(this::calculatePrecisionOnGivenParams, widgets);
            } else if (averagePrecisionActionRadioButton == button) {
                JComponent[] widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };

                doAction(this::calculateAveragePrecisionOnMultipleIterations, widgets);
            } else if (sampleSizeActionRadioButton == button) {
                JComponent[] widgets = new JComponent[]{
                        uiHelper.widgets().intervalSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };

                doAction(this::calculatePrecisionOnDifferentSampleSizes, widgets);
            } else if (judgementsCountActionRadioButton == button) {
                JComponent[] widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().intervalTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };

                doAction(this::calculatePrecisionOnDifferentNumberOfJudgedSampleItems, widgets);
            } else if (parametersActionRadioButton == button) {
                JComponent[] widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().intervalConstantCostParamSpinner(),
                        uiHelper.widgets().intervalGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };

                doAction(this::calculatePrecisionOnDifferentParams, widgets);
            } else if (intervalJudgementsActionRadioButton == button) {
                JComponent[] widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().intervalPreciseIntervalJudgmentsCountRatioSpinner()
                };

                doAction(this::calculatePrecisionOnDifferentPreciseIntervalSampleItemsRatio, widgets);
            }
        }
    }

    private void doAction(Supplier<Report> reportSupplier, JComponent... widgets) {
        ClassifierParamsFrame nextPage = new ClassifierParamsFrame(uiHelper, this, reportSupplier, widgets);
        setVisible(false);
        nextPage.setVisible(true);
    }

    private Report calculatePrecisionOnGivenParams() {
        return null; // TODO
    }

    private Report calculateAveragePrecisionOnMultipleIterations() {
        return null; // TODO
    }

    private Report calculatePrecisionOnDifferentSampleSizes() {
        return null; // TODO
    }

    private Report calculatePrecisionOnDifferentNumberOfJudgedSampleItems() {
        return null; // TODO
    }

    private Report calculatePrecisionOnDifferentParams() {
        return null; // TODO
    }

    private Report calculatePrecisionOnDifferentPreciseIntervalSampleItemsRatio() {
        return null; // TODO
    }
}
