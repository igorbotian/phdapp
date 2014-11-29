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
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Диалоговое окно, в котором отображается расположение элементов выборки для классификатора
 */
public class SampleDialog extends JDialog {

    private static final String ZOOM_IN_ICON_RESOURCE = "zoom_in.png";
    private static final String ZOOM_OUT_ICON_RESOURCE = "zoom_out.png";

    private static final String SAMPLE_LABEL = "sample";
    private static final String ZOOM_IN_LABEL = "zoomIn";
    private static final String ZOOM_OUT_LABEL = "zoomOut";
    private static final String CLOSE_LABEL = "close";

    private static final float NO_SCALE = 1.0f;
    private static final float SCALE_DELTA = 0.1f;

    private final Localization localization;

    private JScrollPane sampleCanvasScrollPane;
    private SampleCanvas sampleCanvas;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton closeButton;

    private float scale = NO_SCALE;

    public SampleDialog(Localization localization) {
        this.localization = Objects.requireNonNull(localization);

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        setModal(true);
        setTitle(localization.getLabel(SAMPLE_LABEL));

        sampleCanvas = new SampleCanvas();
        sampleCanvasScrollPane = new JScrollPane(sampleCanvas);
        sampleCanvasScrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        sampleCanvasScrollPane.setPreferredSize(new Dimension(480, 480));

        zoomInButton = new JButton(localization.getLabel(ZOOM_IN_LABEL),
                new ImageIcon(SampleDialog.class.getResource(ZOOM_IN_ICON_RESOURCE)));
        zoomOutButton = new JButton(localization.getLabel(ZOOM_OUT_LABEL),
                new ImageIcon(SampleDialog.class.getResource(ZOOM_OUT_ICON_RESOURCE)));
        zoomOutButton.setEnabled(false);
        closeButton = new JButton(localization.getLabel(CLOSE_LABEL));
    }

    private void layoutComponents() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(zoomInButton);
        buttonPane.add(zoomOutButton);
        buttonPane.add(closeButton);

        int margin = 20;
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(margin, margin, margin, margin));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbConstraints = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        contentPane.add(sampleCanvasScrollPane, gbConstraints);

        gbConstraints.gridy++;
        gbConstraints.weighty = 0.0;
        gbConstraints.weightx = 0.0;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.insets = new Insets(10, 0, 0, 0);
        contentPane.add(buttonPane, gbConstraints);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    private void initListeners() {
        zoomInButton.addActionListener(e -> SampleDialog.this.zoomIn());
        zoomOutButton.addActionListener(e -> SampleDialog.this.zoomOut());
        closeButton.addActionListener(e -> SampleDialog.this.dispose());
    }

    private void zoomIn() {
        scale += SCALE_DELTA;
        fireScaleChanged();
    }

    private void zoomOut() {
        if(isNoScale(scale)) {
            return;
        }

        scale -= SCALE_DELTA;
        fireScaleChanged();
    }

    private void fireScaleChanged() {
        zoomOutButton.setEnabled(!isNoScale(scale));
        Dimension sampleCanvasSize = sampleCanvas.getSize();
        sampleCanvas.setPreferredSize(new Dimension(
                (int) scale * sampleCanvasSize.width, (int) scale * sampleCanvasSize.height));
    }

    private boolean isNoScale(double scale) {
        return round(scale - NO_SCALE) == 0.0;
    }

    private double round(double value) {
        return Double.parseDouble(new DecimalFormat("#.###").format(value));
    }
}
