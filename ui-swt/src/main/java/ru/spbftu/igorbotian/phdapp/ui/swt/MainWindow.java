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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

/**
 * Главное окно программы
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.swt.SwtUserInterface
 */
class MainWindow extends PhDAppWindow {

    static final String WINDOW_TITLE_LABEL = "appName";
    static final String WINDOW_ICON = "window_icon.png";

    private static final int WINDOW_HEIGHT = 320;
    private static final int WINDOW_WIDTH = 480;

    private final Shell shell;

    private Menu menuBar;

    private Menu fileMenu;
    private MenuItem quitMenuItem;

    private Menu helpMenu;
    private MenuItem aboutMenuItem;

    private AboutDialog aboutDialog;

    public MainWindow(Display display, Localization localization) {
        super(localization);

        this.shell = new Shell(display);

        initComponents();
        initListeners();
    }

    private void initComponents() {
        shell.setText(getLabel(WINDOW_TITLE_LABEL));
        shell.setImage(imageFromResource(shell.getDisplay(), WINDOW_ICON));
        shell.setMinimumSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        shell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        quitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
        quitMenuItem.setText("&Quit");

        MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        helpMenuHeader.setText("&Help");
        helpMenu = new Menu(shell, SWT.DROP_DOWN);
        helpMenuHeader.setMenu(helpMenu);
        aboutMenuItem = new MenuItem(helpMenu, SWT.PUSH);
        aboutMenuItem.setText("About");

        shell.setMenuBar(menuBar);

        aboutDialog = new AboutDialog(shell, localization);
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
                shell.dispose();
            }
        });
    }

    public void mainLoop() {
        Display display = shell.getDisplay();
        centerWindow(shell, display);
        shell.open();

        while (!shell.isDisposed()) {
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
