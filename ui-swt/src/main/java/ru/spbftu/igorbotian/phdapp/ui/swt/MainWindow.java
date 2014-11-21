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

    private static final String VALIDATION_ICON = "validation.png";
    private static final String COMPARISON_ICON = "comparison.png";

    private static final String VALIDATION_OPERATION_LABEL = "validationOperation";
    private static final String VALIDATION_DESCRIPTION_LABEL = "validationDescription";
    private static final String COMPARISON_OPERATION_LABEL = "comparisonOperation";
    private static final String COMPARISON_DESCRIPTION_LABEL = "comparisonDescription";

    private static final int WINDOW_HEIGHT = 320;
    private static final int WINDOW_WIDTH = 480;

    private final Shell mainWindow;

    private Menu menuBar;
    private Menu fileMenu;
    private MenuItem quitMenuItem;
    private Menu helpMenu;
    private MenuItem aboutMenuItem;

    private Label emptyLabel;

    private Label validationIconLabel;
    private Label validationDescriptionLabel;
    private Button validationButton;

    private Label comparisonIconLabel;
    private Label comparisonDescriptionLabel;
    private Button comparisonButton;

    private AboutDialog aboutDialog;

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
        mainWindow.setMinimumSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

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

        validationIconLabel = new Label(mainWindow, SWT.ICON);
        validationIconLabel.setImage(imageFromResource(mainWindow.getDisplay(), VALIDATION_ICON));

        validationButton = new Button(mainWindow, SWT.PUSH);
        validationButton.setText(localization.getLabel(VALIDATION_OPERATION_LABEL) + "...");

        validationDescriptionLabel = makeDescription(new Label(mainWindow, SWT.WRAP | SWT.CENTER));
        validationDescriptionLabel.setText(localization.getLabel(VALIDATION_DESCRIPTION_LABEL));
        validationDescriptionLabel.setEnabled(false);

        //-----------------------------

        emptyLabel = new Label(mainWindow, SWT.NULL);

        comparisonIconLabel = new Label(mainWindow, SWT.ICON);
        comparisonIconLabel.setImage(imageFromResource(mainWindow.getDisplay(), COMPARISON_ICON));

        comparisonButton = new Button(mainWindow, SWT.PUSH);
        comparisonButton.setText(localization.getLabel(COMPARISON_OPERATION_LABEL) + "...");
        comparisonButton.setEnabled(false);

        comparisonDescriptionLabel = makeDescription(new Label(mainWindow, SWT.WRAP | SWT.CENTER));
        comparisonDescriptionLabel.setText(localization.getLabel(COMPARISON_DESCRIPTION_LABEL));

        //-----------------------------

        mainWindow.setDefaultButton(validationButton);
        aboutDialog = new AboutDialog(mainWindow, localization);
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
        int margin = 20;
        GridLayout layout = new GridLayout(1, false);
        layout.marginBottom = margin;
        layout.marginTop = margin;
        layout.marginLeft = margin;
        layout.marginRight = margin;
        mainWindow.setLayout(layout);

        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        validationIconLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        validationButton.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gridData.grabExcessHorizontalSpace = true;
        validationDescriptionLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        emptyLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        comparisonIconLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        comparisonButton.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gridData.grabExcessHorizontalSpace = true;
        comparisonDescriptionLabel.setLayoutData(gridData);

        mainWindow.setTabList(new Control[]{validationButton, comparisonButton});
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
        aboutDialog.show();
    }
}
