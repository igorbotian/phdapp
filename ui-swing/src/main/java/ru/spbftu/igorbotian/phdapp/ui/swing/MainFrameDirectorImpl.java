package ru.spbftu.igorbotian.phdapp.ui.swing;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.ui.common.UserAction;

import javax.swing.*;
import java.util.Objects;

/**
 * Реализация модели главного окна приложения
 */
class MainFrameDirectorImpl implements SwingMainFrameDirector {

    private static final Logger LOGGER = Logger.getLogger(MainFrameDirectorImpl.class);

    private static final String VIEW_SAMPLE_LABEL = "viewSample";

    /**
     * Общие элементы пользовательского интерфейса
     */
    private final SwingUIHelper uiHelper;

    /**
     * Главное окно программы, с которым будет взаимодействовать данная модель
     */
    private MainFrame mainFrame;

    @Inject
    public MainFrameDirectorImpl(SwingUIHelper uiHelper) {
        this.uiHelper = uiHelper;
    }

    @Override
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = Objects.requireNonNull(mainFrame);
    }

    @Override
    public void performAction(UserAction action) {
        Objects.requireNonNull(action);

        if (mainFrame == null) {
            LOGGER.warn("No main frame instance was set for the main frame director!");
            return;
        }

        JComponent[] widgets = constructCrossValidationParamsFor(action, mainFrame);

        if (widgets != null) {
            performAction(mainFrame, widgets);
        }
    }

    private JComponent[] constructCrossValidationParamsFor(UserAction action, MainFrame mainFrame) {
        JComponent[] widgets = null;

        switch (action) {
            case CALCULATE_PRECISION:
                JButton viewSampleButton = new JButton(uiHelper.getLabel(VIEW_SAMPLE_LABEL) + "...");
                viewSampleButton.addActionListener(e -> new SampleDialog(mainFrame).setVisible(true));

                widgets = new JComponent[]{
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        viewSampleButton
                };
                break;
            case CALCULATE_AVERAGE_PRECISION:
                widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };
                break;
            case ANALYZE_PRECISION_ON_SAMPLE_SIZE_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().intervalSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };
                break;
            case ANALYZE_PRECISION_ON_TRAINING_TESTING_SETS_SIZE_RATIO_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().intervalTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };
                break;
            case ANALYZE_PRECISION_ON_CLASSIFIER_PARAMS_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().intervalConstantCostParamSpinner(),
                        uiHelper.widgets().intervalGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner()
                };
                break;
            case ANALYZE_PRECISION_ON_PRECISE_INTERVAL_SETS_SIZE_RATIO_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().preciseConstantCostParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().intervalPreciseIntervalJudgmentsCountRatioSpinner()
                };
                break;
            default:
                LOGGER.error("Unsupported user action detected: " + action);
        }

        return widgets;
    }

    private void performAction(MainFrame mainFrame, JComponent... widgets) {
        ClassifierParamsFrame nextPage = new ClassifierParamsFrame(uiHelper, mainFrame, widgets);
        mainFrame.setVisible(false);
        nextPage.setVisible(true);
    }
}
