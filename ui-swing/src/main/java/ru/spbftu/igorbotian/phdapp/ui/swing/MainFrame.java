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

import ru.spbftu.igorbotian.phdapp.ui.common.UserAction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Главное окно программы
 */
class MainFrame extends JFrame {

    private static final String WINDOW_ICON_RESOURCE = "window_icon.png";
    private static final String WINDOW_TITLE_LABEL = "appName";
    private static final String FILE_LABEL = "file";
    private static final String QUIT_LABEL = "quit";
    private static final String HELP_LABEL = "help";
    private static final String ABOUT_LABEL = "about";
    private static final String ACCURACY_ACTION_LABEL = "calculateAccuracyAction";
    private static final String ACCURACY_LABEL = "calculateAccuracy";
    private static final String AVERAGE_ACCURACY_ACTION_LABEL = "calculateAverageAccuracyAction";
    private static final String AVERAGE_ACCURACY_LABEL = "calculateAverageAccuracy";
    private static final String SAMPLE_SIZE_LABEL = "determineAccuracyDependenceOnSampleSize";
    private static final String SAMPLE_SIZE_ACTION_LABEL = "determineAccuracyDependenceOnSampleSizeAction";
    private static final String JUDGEMENTS_COUNT_LABEL = "determineAccuracyDependenceOnJudgementsCount";
    private static final String JUDGEMENTS_COUNT_ACTION_LABEL = "determineAccuracyDependenceOnJudgementsCountAction";
    private static final String PARAMETERS_LABEL = "determineAccuracyDependenceOnParameters";
    private static final String PARAMETERS_ACTION_LABEL = "determineAccuracyDependenceOnParametersAction";
    private static final String INTERVAL_JUDGEMENTS_RATIO_LABEL = "determineAccuracyDependenceOnIntervalJudgementsRatio";
    private static final String INTERVAL_JUDGEMENTS_RATIO_ACTION_LABEL = "determineAccuracyDependenceOnIntervalJudgementsRatioAction";
    private static final String NEXT_LABEL = "next";

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem exitMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;

    private ButtonGroup actionsGroup;
    private JRadioButton accuracyActionRadioButton;
    private JRadioButton averageAccuracyActionRadioButton;
    private JRadioButton sampleSizeActionRadioButton;
    private JRadioButton judgementsCountActionRadioButton;
    private JRadioButton parametersActionRadioButton;
    private JRadioButton intervalJudgementsActionRadioButton;

    private JButton nextButton;

    /**
     * Общие элементы пользовательского интерфейса
     */
    private final SwingUIHelper uiHelper;

    public MainFrame(SwingUIHelper uiHelper) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
        SwingUtilities.invokeLater(() -> uiHelper.mainFrameDirector().setMainFrame(MainFrame.this));

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
        accuracyActionRadioButton = new JRadioButton(uiHelper.getLabel(ACCURACY_ACTION_LABEL));
        actionsGroup.add(accuracyActionRadioButton);
        accuracyActionRadioButton.setSelected(true);

        averageAccuracyActionRadioButton = new JRadioButton(uiHelper.getLabel(AVERAGE_ACCURACY_ACTION_LABEL));
        actionsGroup.add(averageAccuracyActionRadioButton);

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
        actionsGroupPanel.add(describe(accuracyActionRadioButton, uiHelper.getLabel(ACCURACY_LABEL)));
        actionsGroupPanel.add(describe(averageAccuracyActionRadioButton, uiHelper.getLabel(AVERAGE_ACCURACY_LABEL)));
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

    /**
     * Добавление к заданному переключателю для выбора пользовательского действия описания ниже него
     */
    private JPanel describe(JRadioButton button, String description) {
        JLabel descriptionLabel = new JLabel(
                "<html><p style=\"text-align: left;\">" + description.replaceAll("\\n", "<br/>") + "</p></html>",
                JLabel.CENTER);
        descriptionLabel.setEnabled(false);
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
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
        aboutMenuItem.addActionListener(e -> new AboutDialog(MainFrame.this, uiHelper).setVisible(true));
        nextButton.addActionListener(e -> goToNextWindow());
    }

    private void goToNextWindow() {
        Enumeration<AbstractButton> buttons = actionsGroup.getElements();

        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();

            if (!button.isSelected()) {
                continue;
            }

            UserAction action = null;

            if (accuracyActionRadioButton == button) {
                action = UserAction.CALCULATE_ACCURACY;
            } else if (averageAccuracyActionRadioButton == button) {
                action = UserAction.CALCULATE_AVERAGE_ACCURACY;
            } else if (sampleSizeActionRadioButton == button) {
                action = UserAction.ANALYZE_ACCURACY_ON_SAMPLE_SIZE_DEPENDENCE;
            } else if (judgementsCountActionRadioButton == button) {
                action = UserAction.ANALYZE_ACCURACY_ON_TRAINING_TESTING_SETS_SIZE_RATIO_DEPENDENCE;
            } else if (parametersActionRadioButton == button) {
                action = UserAction.ANALYZE_ACCURACY_ON_CLASSIFIER_PARAMS_DEPENDENCE;
            } else if (intervalJudgementsActionRadioButton == button) {
                action = UserAction.ANALYZE_ACCURACY_ON_PRECISE_INTERVAL_SETS_SIZE_RATIO_DEPENDENCE;
            }

            if (action != null) {
                final UserAction userAction = action;
                SwingUtilities.invokeLater(() -> uiHelper.mainFrameDirector().performAction(userAction));
            }
        }
    }
}
