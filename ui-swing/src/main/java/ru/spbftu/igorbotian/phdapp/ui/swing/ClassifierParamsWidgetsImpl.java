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
import ru.spbftu.igorbotian.phdapp.conf.Configuration;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.utils.ShutdownHook;

import javax.swing.*;
import java.util.Objects;

/**
 * Вспомогательный класс для формирования виджетов для задания параметров классификации.
 * Введённые пользователем значения сохраняются по завершению работы программы
 * и задаются по умолчанию при следующей работе приложения.
 */
@Singleton
class ClassifierParamsWidgetsImpl implements ClassifierParamsWidgets, ShutdownHook {

    /* Идентификаторы параметров (каждый их них используется для получения перевода и значения из конфигурации */

    private final String C_PARAM_ID = "cParameter";
    private final String C_PARAM_MIN_ID = "cParameterMin";
    private final String C_PARAM_MAX_ID = "cParameterMax";

    private final String SIGMA_PARAM_ID = "sigmaParameter";
    private final String SIGMA_PARAM_MIN_ID = "sigmaParameterMin";
    private final String SIGMA_PARAM_MAX_ID = "sigmaParameterMax";

    private final String NUMBER_OF_ITERATIONS_ID = "numberOfIterations";
    private final String NUMBER_OF_ITERATIONS_MIN_ID = "numberOfIterationsMin";
    private final String NUMBER_OF_ITERATIONS_MAX_ID = "numberOfIterationsMax";

    private final String SAMPLE_SIZE_ID = "sampleSize";
    private final String SAMPLE_SIZE_MIN_ID = "sampleSizeMin";
    private final String SAMPLE_SIZE_MAX_ID = "sampleSizeMax";

    private final String PERCENT_OF_JUDGED_SAMPLE_ITEMS_ID = "percentOfJudgedSampleItems";
    private final String PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN_ID = "percentOfJudgedSampleItemsMin";
    private final String PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX_ID = "percentOfJudgedSampleItemsMax";

    private final String PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_ID = "preciseIntervalJudgedSampleItemsRatio";
    private final String PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN_ID = "preciseIntervalJudgedSampleItemsRatioMin";
    private final String PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX_ID = "preciseIntervalJudgedSampleItemsRatioMax";

    private final String VIEW_SAMPLE_LABEL = "viewSample";

    /* Характеристика параметров */

    private final double DEFAULT_C_PARAM = 100.0;
    private final double DEFAULT_C_PARAM_MIN = 0.0;
    private final double DEFAULT_C_PARAM_MAX = 200.0;
    private final double C_PARAM_MAX = 1000000.0;
    private final double C_PARAM_MIN = -C_PARAM_MAX;
    private final double C_PARAM_STEP_SIZE = 1.0;

    private final double DEFAULT_SIGMA_PARAM = 0.1;
    private final double DEFAULT_SIGMA_PARAM_MIN = 0.001;
    private final double DEFAULT_SIGMA_PARAM_MAX = 0.1;
    private final double SIGMA_PARAM_MAX = 1000000.0;
    private final double SIGMA_PARAM_MIN = -SIGMA_PARAM_MAX;
    private final double SIGMA_PARAM_STEP_SIZE = 0.001;

    private final int DEFAULT_NUMBER_OF_ITERATIONS = 100;
    private final int DEFAULT_NUMBER_OF_ITERATIONS_MIN = 1;
    private final int DEFAULT_NUMBER_OF_ITERATIONS_MAX = 1000;
    private final int NUMBER_OF_ITERATIONS_MIN = 1;
    private final int NUMBER_OF_ITERATIONS_MAX = Short.MAX_VALUE;
    private final int NUMBER_OF_ITERATIONS_STEP_SIZE = 10;

    private final int DEFAULT_SAMPLE_SIZE = 1000;
    private final int DEFAULT_SAMPLE_SIZE_MIN = 100;
    private final int DEFAULT_SAMPLE_SIZE_MAX = 10000;
    private final int SAMPLE_SIZE_MIN = 100;
    private final int SAMPLE_SIZE_MAX = Short.MAX_VALUE;
    private final int SAMPLE_SIZE_STEP_SIZE = 100;

    private final double DEFAULT_PERCENT_OF_JUDGED_SAMPLE_ITEMS = 30.0;
    private final double DEFAULT_PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN = 5.0;
    private final double DEFAULT_PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX = 100.0;
    private final double PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN = 1.0;
    private final double PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX = 100.0;
    private final double PERCENT_OF_JUDGED_SAMPLE_ITEMS_STEP_SIZE = 5.0;

    private final double DEFAULT_PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO = 40.0;
    private final double DEFAULT_PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN = 0.0;
    private final double DEFAULT_PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX = 100.0;
    private final double PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN = 0.0;
    private final double PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX = 100.0;
    private final double PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_STEP_SIZE = 5.0;

    /* Внешние ресурсы */

    private final Localization localization;
    private final Configuration config;

    /* Виджеты */

    private DoubleSpinner preciseCParamSpinner;
    private DoubleRangeSpinner intervalCParamSpinner;
    private DoubleSpinner preciseSigmaParamSpinner;
    private DoubleRangeSpinner intervalSigmaParamSpinner;
    private IntegerSpinner preciseNumberOfIterationsSpinner;
    private IntegerRangeSpinner intervalNumberOfIterationsSpinner;
    private IntegerSpinner preciseSampleSizeSpinner;
    private IntegerRangeSpinner intervalSampleSizeSpinner;
    private DoubleSpinner precisePercentOfJudgedSampleItemsSpinner;
    private DoubleRangeSpinner intervalPercentOfJudgedSampleItemsSpinner;
    private DoubleSpinner precisePreciseIntervalJudgedSampleItemsRatioSpinner;
    private DoubleRangeSpinner intervalPreciseIntervalJudgedSampleItemsRatioSpinner;
    private JButton viewSampleButton;

    @Inject
    public ClassifierParamsWidgetsImpl(Localization localization, Configuration config) {
        this.localization = Objects.requireNonNull(localization);
        this.config = Objects.requireNonNull(config);

        preciseCParamSpinner = doubleSpinner(C_PARAM_ID, DEFAULT_C_PARAM, C_PARAM_MIN, C_PARAM_MAX, C_PARAM_STEP_SIZE);
        intervalCParamSpinner = doubleRangeSpinner(C_PARAM_ID, DEFAULT_C_PARAM_MIN, DEFAULT_C_PARAM_MAX, C_PARAM_MIN,
                C_PARAM_MAX, C_PARAM_STEP_SIZE);

        preciseSigmaParamSpinner = doubleSpinner(SIGMA_PARAM_ID, DEFAULT_SIGMA_PARAM, SIGMA_PARAM_MIN, SIGMA_PARAM_MAX,
                SIGMA_PARAM_STEP_SIZE);
        intervalSigmaParamSpinner = doubleRangeSpinner(SIGMA_PARAM_ID, DEFAULT_SIGMA_PARAM_MIN, DEFAULT_SIGMA_PARAM_MAX,
                SIGMA_PARAM_MIN, SIGMA_PARAM_MAX, SIGMA_PARAM_STEP_SIZE);

        preciseNumberOfIterationsSpinner = integerSpinner(NUMBER_OF_ITERATIONS_ID, DEFAULT_NUMBER_OF_ITERATIONS,
                NUMBER_OF_ITERATIONS_MIN, NUMBER_OF_ITERATIONS_MAX, NUMBER_OF_ITERATIONS_STEP_SIZE);
        intervalNumberOfIterationsSpinner = integerRangeSpinner(NUMBER_OF_ITERATIONS_ID, DEFAULT_NUMBER_OF_ITERATIONS_MIN,
                DEFAULT_NUMBER_OF_ITERATIONS_MAX, NUMBER_OF_ITERATIONS_MIN, NUMBER_OF_ITERATIONS_MAX,
                NUMBER_OF_ITERATIONS_STEP_SIZE);

        preciseSampleSizeSpinner = integerSpinner(SAMPLE_SIZE_ID, DEFAULT_SAMPLE_SIZE, SAMPLE_SIZE_MIN, SAMPLE_SIZE_MAX,
                SAMPLE_SIZE_STEP_SIZE);
        intervalSampleSizeSpinner = integerRangeSpinner(SAMPLE_SIZE_ID, DEFAULT_SAMPLE_SIZE_MIN, DEFAULT_SAMPLE_SIZE_MAX,
                SAMPLE_SIZE_MIN, SAMPLE_SIZE_MAX, SAMPLE_SIZE_STEP_SIZE);

        precisePercentOfJudgedSampleItemsSpinner = doubleSpinner(PERCENT_OF_JUDGED_SAMPLE_ITEMS_ID,
                DEFAULT_PERCENT_OF_JUDGED_SAMPLE_ITEMS, PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN,
                PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX, PERCENT_OF_JUDGED_SAMPLE_ITEMS_STEP_SIZE);
        intervalPercentOfJudgedSampleItemsSpinner = doubleRangeSpinner(PERCENT_OF_JUDGED_SAMPLE_ITEMS_ID,
                DEFAULT_PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN, DEFAULT_PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX,
                PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN, PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX,
                PERCENT_OF_JUDGED_SAMPLE_ITEMS_STEP_SIZE);

        precisePreciseIntervalJudgedSampleItemsRatioSpinner = doubleSpinner(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_ID,
                DEFAULT_PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO, PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN,
                PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX, PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_STEP_SIZE);
        intervalPreciseIntervalJudgedSampleItemsRatioSpinner = doubleRangeSpinner(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_ID,
                DEFAULT_PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN, DEFAULT_PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX,
                PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN, PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX,
                PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_STEP_SIZE);

        viewSampleButton = new JButton(localization.getLabel(VIEW_SAMPLE_LABEL) + "...");
        viewSampleButton.addActionListener(e -> new SampleDialog(localization).setVisible(true));

        loadConfigValues();
    }

    private IntegerSpinner integerSpinner(String paramId, int defaultValue, int min, int max, int stepSize) {
        return new IntegerSpinner(localization.getLabel(paramId), defaultValue, min, max, stepSize);
    }

    private IntegerRangeSpinner integerRangeSpinner(String paramId, int lowerValue, int upperValue,
                                                    int min, int max, int stepSize) {
        return new IntegerRangeSpinner(localization.getLabel(paramId), lowerValue, min, max, upperValue, min, max,
                stepSize);
    }

    private DoubleSpinner doubleSpinner(String paramId, double defaultValue, double min, double max, double stepSize) {
        return new DoubleSpinner(localization.getLabel(paramId), defaultValue, min, max, stepSize);
    }

    private DoubleRangeSpinner doubleRangeSpinner(String paramId, double lowerValue, double upperValue,
                                                  double min, double max, double stepSize) {
        return new DoubleRangeSpinner(localization.getLabel(paramId), lowerValue, min, max, upperValue, min, max,
                stepSize);
    }

    private void loadConfigValues() {
        loadConfigDoubleValue(C_PARAM_ID, preciseCParamSpinner);
        loadConfigDoubleRangeValue(C_PARAM_MIN_ID, C_PARAM_MAX_ID, intervalCParamSpinner);

        loadConfigDoubleValue(SIGMA_PARAM_ID, preciseSigmaParamSpinner);
        loadConfigDoubleRangeValue(SIGMA_PARAM_MIN_ID, SIGMA_PARAM_MAX_ID, intervalSigmaParamSpinner);

        loadConfigIntegerValue(NUMBER_OF_ITERATIONS_ID, preciseNumberOfIterationsSpinner);
        loadConfigIntegerRangeValue(NUMBER_OF_ITERATIONS_MIN_ID, NUMBER_OF_ITERATIONS_MAX_ID,
                intervalNumberOfIterationsSpinner);

        loadConfigIntegerValue(SAMPLE_SIZE_ID, preciseSampleSizeSpinner);
        loadConfigIntegerRangeValue(SAMPLE_SIZE_MIN_ID, SAMPLE_SIZE_MAX_ID, intervalSampleSizeSpinner);

        loadConfigDoubleValue(PERCENT_OF_JUDGED_SAMPLE_ITEMS_ID, precisePercentOfJudgedSampleItemsSpinner);
        loadConfigDoubleRangeValue(PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN_ID, PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX_ID,
                intervalPercentOfJudgedSampleItemsSpinner);

        loadConfigDoubleValue(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_ID,
                precisePreciseIntervalJudgedSampleItemsRatioSpinner);
        loadConfigDoubleRangeValue(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN_ID,
                PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX_ID, intervalPreciseIntervalJudgedSampleItemsRatioSpinner);
    }

    private void loadConfigIntegerValue(String param, IntegerSpinner spinner) {
        if (config.hasSetting(param)) {
            spinner.setValue(config.getInt(param));
        }
    }

    private void loadConfigIntegerRangeValue(String paramMin, String paramMax, IntegerRangeSpinner spinner) {
        if (config.hasSetting(paramMin)) {
            spinner.setMinValue(config.getInt(paramMin));
        }

        if (config.hasSetting(paramMax)) {
            spinner.setMaxValue(config.getInt(paramMax));
        }
    }

    private void loadConfigDoubleValue(String param, DoubleSpinner spinner) {
        if (config.hasSetting(param)) {
            spinner.setValue(config.getDouble(param));
        }
    }

    private void loadConfigDoubleRangeValue(String paramMin, String paramMax, DoubleRangeSpinner spinner) {
        if (config.hasSetting(paramMin)) {
            spinner.setMinValue(config.getDouble(paramMin));
        }

        if (config.hasSetting(paramMax)) {
            spinner.setMaxValue(config.getDouble(paramMax));
        }
    }

    private void saveConfigValues() {
        config.setDouble(C_PARAM_ID, preciseCParamSpinner.getValue());
        config.setDouble(C_PARAM_MIN_ID, intervalCParamSpinner.getMinValue());
        config.setDouble(C_PARAM_MAX_ID, intervalCParamSpinner.getMaxValue());

        config.setDouble(SIGMA_PARAM_ID, preciseSigmaParamSpinner.getValue());
        config.setDouble(SIGMA_PARAM_MIN_ID, intervalSigmaParamSpinner.getMinValue());
        config.setDouble(SIGMA_PARAM_MAX_ID, intervalSigmaParamSpinner.getMaxValue());

        config.setInt(NUMBER_OF_ITERATIONS_ID, preciseNumberOfIterationsSpinner.getValue());
        config.setInt(NUMBER_OF_ITERATIONS_MIN_ID, intervalNumberOfIterationsSpinner.getMinValue());
        config.setInt(NUMBER_OF_ITERATIONS_MAX_ID, intervalNumberOfIterationsSpinner.getMaxValue());

        config.setInt(SAMPLE_SIZE_ID, preciseSampleSizeSpinner.getValue());
        config.setInt(SAMPLE_SIZE_MIN_ID, intervalSampleSizeSpinner.getMinValue());
        config.setInt(SAMPLE_SIZE_MAX_ID, intervalSampleSizeSpinner.getMaxValue());

        config.setDouble(PERCENT_OF_JUDGED_SAMPLE_ITEMS_ID, precisePercentOfJudgedSampleItemsSpinner.getValue());
        config.setDouble(PERCENT_OF_JUDGED_SAMPLE_ITEMS_MIN_ID, intervalPercentOfJudgedSampleItemsSpinner.getMinValue());
        config.setDouble(PERCENT_OF_JUDGED_SAMPLE_ITEMS_MAX_ID, intervalPercentOfJudgedSampleItemsSpinner.getMaxValue());

        config.setDouble(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_ID,
                precisePreciseIntervalJudgedSampleItemsRatioSpinner.getValue());
        config.setDouble(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MIN_ID,
                intervalPreciseIntervalJudgedSampleItemsRatioSpinner.getMinValue());
        config.setDouble(PRECISE_INTERVAL_JUDGED_SAMPLE_ITEMS_RATIO_MAX_ID,
                intervalPreciseIntervalJudgedSampleItemsRatioSpinner.getMaxValue());
    }

    @Override
    public void onExit() {
        saveConfigValues();
    }

    public DoubleSpinner preciseCParamSpinner() {
        return preciseCParamSpinner;
    }

    public DoubleRangeSpinner intervalCParamSpinner() {
        return intervalCParamSpinner;
    }

    public DoubleSpinner preciseSigmaParamSpinner() {
        return preciseSigmaParamSpinner;
    }

    public DoubleRangeSpinner intervalSigmaParamSpinner() {
        return intervalSigmaParamSpinner;
    }

    public IntegerSpinner preciseNumberOfIterationsSpinner() {
        return preciseNumberOfIterationsSpinner;
    }

    public IntegerRangeSpinner intervalNumberOfIterationsSpinner() {
        return intervalNumberOfIterationsSpinner;
    }

    public IntegerSpinner preciseSampleSizeSpinner() {
        return preciseSampleSizeSpinner;
    }

    public IntegerRangeSpinner intervalSampleSizeSpinner() {
        return intervalSampleSizeSpinner;
    }

    public DoubleSpinner precisePercentOfJudgedSampleItemsSpinner() {
        return precisePercentOfJudgedSampleItemsSpinner;
    }

    public DoubleRangeSpinner intervalPercentOfJudgedSampleItemsSpinner() {
        return intervalPercentOfJudgedSampleItemsSpinner;
    }

    public DoubleSpinner precisePreciseIntervalJudgedSampleItemsRatioSpinner() {
        return precisePreciseIntervalJudgedSampleItemsRatioSpinner;
    }

    public DoubleRangeSpinner intervalPreciseIntervalJudgedSampleItemsRatioSpinner() {
        return intervalPreciseIntervalJudgedSampleItemsRatioSpinner;
    }

    @Override
    public JButton sampleViewButton() {
        return viewSampleButton;
    }
}
