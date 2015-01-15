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

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.ui.UserInterface;

import javax.swing.*;
import java.util.Objects;

/**
 * Swing-реализация пользовательского интерфейса программа
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.UserInterface
 */
class SwingUserInterface implements UserInterface {

    private static Logger LOGGER = Logger.getLogger(SwingUserInterface.class);

    private final SwingUIHelper uiHelper;

    @Inject
    public SwingUserInterface(SwingUIHelper uiHelper) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
    }

    @Override
    public void showMainWindow() {
        setLookAndFeel();
        SwingUtilities.invokeLater(() -> new MainFrame(uiHelper).setVisible(true));
    }

    private void setLookAndFeel() {
        try {
            if (System.getProperty("os.name").contains("OS X")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            }

            LOGGER.debug("Setting system UI look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.warn("Unable to set system UI look and feel", e);
        }
    }
}
