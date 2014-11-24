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

package ru.spbftu.igorbotian.phdapp.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

/**
 * Главное окно программы
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.swt.SwtUserInterface
 */
class MainWindow extends PhDAppWindow {

    static final String WINDOW_TITLE_LABEL = "appName";
    static final String WINDOW_ICON = "window_icon.png";

    private static final String ACTIONS_LABEL = "actions";
    private static final String PRECISION_ACTION_LABEL = "calculatePrecision";
    private static final String AVERAGE_PRECISION_ACTION_LABEL = "calculateAveragePrecision";
    private static final String SAMPLE_SIZE_ACTION_LABEL = "determinePrecisionDependenceOnSampleSize";
    private static final String JUDGEMENTS_COUNT_ACTION_LABEL = "determinePrecisionDependenceOnJudgementsCount";
    private static final String PARAMETERS_ACTION_LABEL = "determinePrecisionDependenceOnParameters";
    private static final String INTERVAL_JUDGEMENTS_RATIO_ACTION_LABEL = "determinePrecisionDependenceOnIntervalJudgementsRatio";
    private static final String NEXT_LABEL = "next";

    private final Shell mainWindow;

    private Menu menuBar;
    private Menu fileMenu;
    private MenuItem quitMenuItem;
    private Menu helpMenu;
    private MenuItem aboutMenuItem;

    private Group actionGroup;
    private Button precisionButton;
    private Button averagePrecision;
    private Button sampleSizeButton;
    private Button judgementsCountButton;
    private Button parametersButton;
    private Button intervalJudgementsRatioButton;

    private Button nextButton;

    public MainWindow(Display display, Localization localization) {
        super(localization);

        this.mainWindow = new Shell(display);

        initComponents();
        initListeners();
        layoutComponents();
    }

    private void initComponents() {
        mainWindow.setText(getLabel(WINDOW_TITLE_LABEL));
        mainWindow.setImage(imageFromResource(mainWindow.getDisplay(), WINDOW_ICON));

        //-----------------------------

        menuBar = new Menu(mainWindow, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        fileMenu = new Menu(mainWindow, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        quitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
        quitMenuItem.setText("&Quit");

        MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        helpMenuHeader.setText("&Help");
        helpMenu = new Menu(mainWindow, SWT.DROP_DOWN);
        helpMenuHeader.setMenu(helpMenu);
        aboutMenuItem = new MenuItem(helpMenu, SWT.PUSH);
        aboutMenuItem.setText("About");

        mainWindow.setMenuBar(menuBar);

        //-----------------------------

        actionGroup = new Group(mainWindow, SWT.SHADOW_IN);
        actionGroup.setText(getLabel(ACTIONS_LABEL));
        actionGroup.setLayout(new RowLayout(SWT.VERTICAL));

        precisionButton = new Button(actionGroup, SWT.RADIO);
        precisionButton.setText(getLabel(PRECISION_ACTION_LABEL));
        precisionButton.setSelection(true);

        averagePrecision = new Button(actionGroup, SWT.RADIO);
        averagePrecision.setText(getLabel(AVERAGE_PRECISION_ACTION_LABEL));

        sampleSizeButton = new Button(actionGroup, SWT.RADIO);
        sampleSizeButton.setText(getLabel(SAMPLE_SIZE_ACTION_LABEL));

        judgementsCountButton = new Button(actionGroup, SWT.RADIO);
        judgementsCountButton.setText(getLabel(JUDGEMENTS_COUNT_ACTION_LABEL));

        parametersButton = new Button(actionGroup, SWT.RADIO);
        parametersButton.setText(getLabel(PARAMETERS_ACTION_LABEL));

        intervalJudgementsRatioButton = new Button(actionGroup, SWT.RADIO);
        intervalJudgementsRatioButton.setText(getLabel(INTERVAL_JUDGEMENTS_RATIO_ACTION_LABEL));

        nextButton = new Button(mainWindow, SWT.PUSH);
        nextButton.setText(getLabel(NEXT_LABEL));

        //-----------------------------

        mainWindow.setDefaultButton(nextButton);
    }

    private void initListeners() {
        aboutMenuItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                showAboutDialog();
            }
        });

        quitMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mainWindow.dispose();
            }
        });
    }

    private void layoutComponents() {
        int margin = 5;
        GridLayout layout = new GridLayout(1, false);
        layout.marginBottom = margin;
        layout.marginTop = margin;
        layout.marginLeft = margin;
        layout.marginRight = margin;
        mainWindow.setLayout(layout);

        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        actionGroup.setLayoutData(gridData);

        gridData = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false);
        nextButton.setLayoutData(gridData);

        mainWindow.setTabList(new Control[]{actionGroup, nextButton});
        mainWindow.pack();

        mainWindow.setMinimumSize(mainWindow.getSize().x, mainWindow.getSize().y);
    }

    public void mainLoop() {
        Display display = mainWindow.getDisplay();
        centerWindow(mainWindow, display);
        mainWindow.open();

        while (!mainWindow.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }

    private void showAboutDialog() {
        AboutDialog dialog = new AboutDialog(mainWindow, localization);
        dialog.show();
    }
}
