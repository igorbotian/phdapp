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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Диалог "О программе"
 */
class AboutDialog extends JDialog {

    private static final String APP_ICON_RESOURCE = "window_icon.png";
    private static final String ABOUT_LABEL = "about";
    private static final String APP_TITLE_LABEL = "appTitle";
    private static final String IGOR_BOTIAN_LABEL = "igorBotian";
    private static final String LEV_UTKIN_LABEL = "levUtkin";
    private static final String JULIA_ZHUK_LABEL = "juliaZhuk";
    private static final String OK_LABEL = "ok";

    private static final String IGOR_BOTIAN_EMAIL = "igor.botian@gmail.com";
    private static final String LEV_UTKIN_EMAIL = "lev.utkin@gmail.com";
    private static final String JULIA_ZHUK_EMAIL = "zhuk_yua@mail.ru";

    private static final String COPYRIGHT_TEXT;

    static {
        int yearOfInitialRelease = 2014;
        int currentYear = LocalDate.now().getYear();
        StringBuilder copyright = new StringBuilder("(c) " + yearOfInitialRelease);

        if (currentYear > yearOfInitialRelease) {
            copyright.append("-");
            copyright.append(currentYear);
        }

        COPYRIGHT_TEXT = copyright.toString();
    }

    private final Localization localization;

    private JLabel appIconLabel;
    private JLabel appTitleLabel;
    private JPanel igorBotianEmailLabel;
    private JPanel levUtkinEmailLabel;
    private JPanel juliaZhukEmailLabel;
    private JLabel copyrightLabel;
    private JButton okButton;

    public AboutDialog(Frame owner, Localization localization) {
        super(owner, localization.getLabel(ABOUT_LABEL), true);

        this.localization = Objects.requireNonNull(localization);

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        appIconLabel = new JLabel(new ImageIcon(this.getClass().getResource(APP_ICON_RESOURCE)));
        appTitleLabel = titleLabel(localization.getLabel(APP_TITLE_LABEL));
        igorBotianEmailLabel = personPanel(localization.getLabel(IGOR_BOTIAN_LABEL), IGOR_BOTIAN_EMAIL);
        levUtkinEmailLabel = personPanel(localization.getLabel(LEV_UTKIN_LABEL), LEV_UTKIN_EMAIL);
        juliaZhukEmailLabel = personPanel(localization.getLabel(JULIA_ZHUK_LABEL), JULIA_ZHUK_EMAIL);
        copyrightLabel = new JLabel(COPYRIGHT_TEXT);
        okButton = new JButton(localization.getLabel(OK_LABEL));
    }

    private JLabel titleLabel(String text) {
        JLabel label = new JLabel("<html><p style=\"text-align: center;\">" + text.replaceAll("\\n", "<br/>") + "</p></html>");
        label.setFont(label.getFont().deriveFont(Font.BOLD, label.getFont().getSize() + 3));
        return label;
    }

    private JPanel personPanel(String person, String email) {
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(new JLabel(person));
        panel.add(new LinkLabel(email));

        return panel;
    }

    private void layoutComponents() {
        int inset = 10;
        Insets insetsNowhere = new Insets(0, 0, 0, 0);
        Insets insetsEverywhere = new Insets(inset, inset, inset, inset);
        Insets insetsOnTopOnly = new Insets(inset, 0, 0, 0);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(insetsEverywhere));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.gridx = 1;
        gbConstraints.gridy = 1;
        contentPane.add(appIconLabel, gbConstraints);

        gbConstraints.gridy++;
        gbConstraints.insets = insetsOnTopOnly;
        contentPane.add(appTitleLabel, gbConstraints);

        gbConstraints.gridy++;
        contentPane.add(igorBotianEmailLabel, gbConstraints);

        gbConstraints.gridy++;
        gbConstraints.insets = insetsNowhere;
        contentPane.add(levUtkinEmailLabel, gbConstraints);

        gbConstraints.gridy++;
        contentPane.add(juliaZhukEmailLabel, gbConstraints);

        gbConstraints.gridy++;
        gbConstraints.insets = insetsOnTopOnly;
        contentPane.add(copyrightLabel, gbConstraints);

        gbConstraints.gridy++;
        contentPane.add(okButton, gbConstraints);

        setContentPane(contentPane);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    private void initListeners() {
        okButton.addActionListener(e -> AboutDialog.this.dispose());
    }
}
