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
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.ui.common.CrossValidatorResultsFrameDirector;
import ru.spbftu.igorbotian.phdapp.ui.common.CrossValidatorParamsFrameDirector;
import ru.spbftu.igorbotian.phdapp.ui.common.SampleCanvasDirector;
import ru.spbftu.igorbotian.phdapp.ui.common.UIHelper;

import java.util.Objects;

/**
 * Реализация интерфейса <code>UIHelper</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.ui.common.UIHelper
 */
@Singleton
class SwingUIHelperImpl implements SwingUIHelper {

    private UIHelper uiHelper;
    private CrossValidatorParamsWidgets widgets;

    @Inject
    public SwingUIHelperImpl(UIHelper uiHelper, CrossValidatorParamsWidgets widgets) {
        this.uiHelper = Objects.requireNonNull(uiHelper);
        this.widgets = Objects.requireNonNull(widgets);
    }

    @Override
    public CrossValidatorParamsWidgets widgets() {
        return widgets;
    }

    @Override
    public String getLabel(String label) {
        return uiHelper.getLabel(label);
    }

    @Override
    public SampleCanvasDirector sampleCanvasDirector() {
        return uiHelper.sampleCanvasDirector();
    }

    @Override
    public CrossValidatorParamsFrameDirector crossValidatorParamsFrameDirector() {
        return uiHelper.crossValidatorParamsFrameDirector();
    }

    @Override
    public CrossValidatorResultsFrameDirector crossValidationResultsFrameDirector() {
        return uiHelper.crossValidationResultsFrameDirector();
    }
}
