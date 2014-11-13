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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.*;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

/**
 * Диалоговое окно "О программе"
 */
public class AboutDialog extends PhDAppWindow {

    private static final String DIALOG_TITLE_LABEL = "about";
    private static final String APP_TITLE_LABEL = "appTitle";
    private static final String IGOR_BOTIAN_LABEL = "igorBotian";
    private static final String LEV_UTKIN_LABEL = "levUtkin";
    private static final String JULIA_ZHUK_LABEL = "juliaZhuk";
    private static final String OK_BUTTON_LABEL = "ok";

    private static final String IGOR_BOTIAN_EMAIL = "igor.botian@gmail.com";
    private static final String LEV_UTKIN_EMAIL = "lev.utkin@gmail.com";
    private static final String JULIA_ZHUK_EMAIL = "julia.zhuk@gmail.com";

    private static final String COPYRIGHT_TEXT = "(c) 2014-2015";

    private final Shell mainWindow;
    private final Shell aboutDialog;

    private Label appIconLabel;
    private Label appTitleLabel;
    private Label emptyLabel1;
    private Link igorBotianEmailLink;
    private Link levUtkinEmailLink;
    private Link juliaZhukEmailLink;
    private Label emptyLabel2;
    private Label copyrightLabel;
    private Label emptyLabel3;
    private Button okButton;

    public AboutDialog(Shell parent, Localization localization) {
        super(localization);

        this.mainWindow = parent;
        this.aboutDialog = new Shell(mainWindow, SWT.CLOSE | SWT.TITLE);

        initComponents();
        initListeners();
        layoutComponents();
    }

    private void initComponents() {
        aboutDialog.setText(getLabel(DIALOG_TITLE_LABEL));

        appIconLabel = new Label(aboutDialog, SWT.ICON);
        appIconLabel.setImage(imageFromResource(aboutDialog.getDisplay(), MainWindow.WINDOW_ICON));

        appTitleLabel = new Label(aboutDialog, SWT.WRAP | SWT.CENTER);
        appTitleLabel.setFont(makeTitle(aboutDialog.getDisplay(), appTitleLabel.getFont()));
        appTitleLabel.setText(getLabel(APP_TITLE_LABEL));

        emptyLabel1 = new Label(aboutDialog, SWT.NONE);

        igorBotianEmailLink = new Link(aboutDialog, SWT.NULL);
        igorBotianEmailLink.setText(linkTextOf(getLabel(IGOR_BOTIAN_LABEL), IGOR_BOTIAN_EMAIL));

        levUtkinEmailLink = new Link(aboutDialog, SWT.NULL);
        levUtkinEmailLink.setText(linkTextOf(getLabel(LEV_UTKIN_LABEL), LEV_UTKIN_EMAIL));

        juliaZhukEmailLink = new Link(aboutDialog, SWT.NULL);
        juliaZhukEmailLink.setText(linkTextOf(getLabel(JULIA_ZHUK_LABEL), JULIA_ZHUK_EMAIL));

        emptyLabel2 = new Label(aboutDialog, SWT.NONE);

        copyrightLabel = new Label(aboutDialog, SWT.NULL);
        copyrightLabel.setText(COPYRIGHT_TEXT);

        emptyLabel3 = new Label(aboutDialog, SWT.NONE);

        okButton = new Button(aboutDialog, SWT.PUSH);
        okButton.setText(getLabel(OK_BUTTON_LABEL));

        aboutDialog.setDefaultButton(okButton);
    }

    private String linkTextOf(String person, String email) {
        return String.format("%s <A>%s</A>", person, email);
    }

    private void initListeners() {
        okButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                hide();
            }
        });

        igorBotianEmailLink.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Program.launch("mailto:" + IGOR_BOTIAN_EMAIL);
            }
        });

        levUtkinEmailLink.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Program.launch("mailto:" + LEV_UTKIN_EMAIL);
            }
        });

        juliaZhukEmailLink.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Program.launch("mailto:" + JULIA_ZHUK_EMAIL);
            }
        });
    }

    private void layoutComponents() {
        aboutDialog.setLayout(new GridLayout(1, false));

        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        appIconLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        appTitleLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        emptyLabel1.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        igorBotianEmailLink.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        levUtkinEmailLink.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        juliaZhukEmailLink.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        emptyLabel2.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        copyrightLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        emptyLabel3.setLayoutData(gridData);

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        okButton.setLayoutData(gridData);

        aboutDialog.setTabList(new Control[] {okButton, igorBotianEmailLink, levUtkinEmailLink, juliaZhukEmailLink});
        aboutDialog.pack();
    }

    public void show() {
        centerDialog(aboutDialog, mainWindow);
        aboutDialog.setVisible(true);
        aboutDialog.setFocus();
    }

    private void hide() {
        aboutDialog.setVisible(false);
        mainWindow.setFocus();
    }
}
