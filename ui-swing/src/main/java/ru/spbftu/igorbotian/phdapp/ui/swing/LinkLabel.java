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

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

/**
 * Виджет, содержащий ссылку на веб-ресурс
 *
 * @see javax.swing.JLabel
 */
public class LinkLabel extends JLabel {

    private static final Logger LOGGER = Logger.getLogger(LinkLabel.class);

    public LinkLabel(String email) {
        this(email, "mailto:" + email);
    }

    public LinkLabel(String text, final String href) {
        super(text);
        setForeground(Color.BLUE);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL))) {
                    return;
                }

                try {
                    URI uri = new URI(href);

                    if (href.startsWith("mailto:")) {
                        Desktop.getDesktop().mail(uri);
                    } else {
                        Desktop.getDesktop().browse(uri);
                    }
                } catch (Exception ex) {
                    LOGGER.error("Failed to process link label address", ex);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                LinkLabel.this.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                LinkLabel.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }
}
