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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import ru.spbftu.igorbotian.phdapp.locale.Localization;

import java.util.Objects;

/**
 * Общий класс для окон и диалогов пользовательского интерфейса приложения
 */
abstract class PhDAppWindow {

    protected final Localization localization;

    public PhDAppWindow(Localization localization) {
        this.localization = Objects.requireNonNull(localization);
    }

    /**
     * Получение локализованного сообщения по заданному идентификатору
     *
     * @param label идентификатор сообщения
     * @return локализованное сообщение или сам идентификатор, если локализация для него отсутствует
     */
    protected String getLabel(String label) {
        Objects.requireNonNull(label);

        return localization.getLabel(label);
    }

    /**
     * Задание для заданного текстового виджета роли пояснения к чему-либо
     */
    protected Label makeDescription(Label label) {
        label.setForeground(new Color(label.getDisplay(), 128, 128, 128));
        return label;
    }

    /**
     * Получение заголовочного шрифта на базе заданного
     */
    protected Font makeTitle(Display display, Font font) {
        Objects.requireNonNull(display);
        Objects.requireNonNull(font);

        FontData fontData = font.getFontData()[0];
        return new Font(display, new FontData(fontData.getName(), fontData.getHeight() + 5, SWT.BOLD));
    }

    /**
     * Создание изображения из файла, хранящегося в ресурах приложения
     */
    protected Image imageFromResource(Display display, String resourceName) {
        Objects.requireNonNull(display);
        Objects.requireNonNull(resourceName);

        return new Image(display, MainWindow.class.getResourceAsStream(resourceName));
    }

    /**
     * Изменение размеров заданного изображения
     */
    protected Image resize(Image image, int width, int height) {
        Objects.requireNonNull(image);

        if (width < 0) {
            throw new IllegalArgumentException("Image width cannot have a negative value");
        }

        if (height < 0) {
            throw new IllegalArgumentException("Image height cannot have a negative value");
        }

        ImageData imgData = image.getImageData();
        imgData = imgData.scaledTo(width, height);
        return new Image(image.getDevice(), imgData);
    }

    /**
     * Центрирование заданного окна
     */
    protected void centerWindow(Shell window, Display display) {
        Objects.requireNonNull(window);
        Objects.requireNonNull(display);

        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = window.getBounds();

        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;

        window.setLocation(x, y);
    }

    /**
     * Центрирование заданного диалога на заданном родительском окне
     */
    protected void centerDialog(Shell dialog, Shell parent) {
        Objects.requireNonNull(dialog);
        Objects.requireNonNull(parent);

        Rectangle parentSize = parent.getBounds();
        Rectangle childSize = dialog.getBounds();

        int x = (parentSize.width - childSize.width) / 2 + parentSize.x;
        int y = (parentSize.height - childSize.height) / 2 + parentSize.y;

        dialog.setLocation(new Point(x, y));
    }
}
