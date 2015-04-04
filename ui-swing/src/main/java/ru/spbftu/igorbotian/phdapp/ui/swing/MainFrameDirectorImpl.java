package ru.spbftu.igorbotian.phdapp.ui.swing;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.svm.validation.AsyncRankingPairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.IntervalPairwiseClassifierCrossValidators;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;
import ru.spbftu.igorbotian.phdapp.ui.common.UserAction;

import javax.swing.*;
import java.util.Objects;

/**
 * Реализация модели главного окна приложения
 */
@Singleton
class MainFrameDirectorImpl implements SwingMainFrameDirector {

    private static final Logger LOGGER = Logger.getLogger(MainFrameDirectorImpl.class);

    private static final String VIEW_SAMPLE_LABEL = "viewSample";

    /**
     * Общие элементы пользовательского интерфейса
     */
    private final SwingUIHelper uiHelper;

    /**
     * Фабрика средств кросс-валидации попарного классификатора
     */
    private final IntervalPairwiseClassifierCrossValidators crossValidators;

    /**
     * Главное окно программы, с которым будет взаимодействовать данная модель
     */
    private MainFrame mainFrame;

    /**
     * Средство кросс-валидации, выбранное пользователем
     */
    private AsyncRankingPairwiseClassifierCrossValidator<? extends Report> selectedCrossValidator;

    public MainFrameDirectorImpl(SwingUIHelper uiHelper, IntervalPairwiseClassifierCrossValidators crossValidators) {
        this.uiHelper = uiHelper;
        this.crossValidators = Objects.requireNonNull(crossValidators);
    }

    @Override
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = Objects.requireNonNull(mainFrame);
    }

    @Override
    public AsyncRankingPairwiseClassifierCrossValidator<? extends Report> selectedCrossValidator() {
        return selectedCrossValidator;
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
                viewSampleButton.addActionListener(e -> new SampleDialog(mainFrame, uiHelper).setVisible(true));

                widgets = new JComponent[]{
                        uiHelper.widgets().maxJudgementGroupSizeParamSpinner(),
                        uiHelper.widgets().precisePenaltyParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        viewSampleButton
                };

                LOGGER.debug("Performing action: " + action);
                selectedCrossValidator = crossValidators.asyncPrecisionValidator();
                break;
            case CALCULATE_AVERAGE_PRECISION:
                widgets = new JComponent[]{
                        uiHelper.widgets().maxJudgementGroupSizeParamSpinner(),
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().preciseSamplesToGenerateCountSpinner(),
                        uiHelper.widgets().precisePenaltyParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        uiHelper.widgets().stopCrossValidationOnErrorCheckBox()
                };

                selectedCrossValidator = crossValidators.asyncAveragePrecisionValidator();
                LOGGER.debug("Performing action: " + action);
                break;
            case ANALYZE_PRECISION_ON_SAMPLE_SIZE_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().maxJudgementGroupSizeParamSpinner(),
                        uiHelper.widgets().intervalSampleSizeSpinner(),
                        uiHelper.widgets().precisePenaltyParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        uiHelper.widgets().stopCrossValidationOnErrorCheckBox()
                };

                selectedCrossValidator = crossValidators.asyncPrecisionDependenceOnSampleSizeAnalyzer();
                LOGGER.debug("Performing action: " + action);
                break;
            case ANALYZE_PRECISION_ON_TRAINING_TESTING_SETS_SIZE_RATIO_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().maxJudgementGroupSizeParamSpinner(),
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().precisePenaltyParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().intervalTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        uiHelper.widgets().stopCrossValidationOnErrorCheckBox()
                };

                selectedCrossValidator = crossValidators.asyncPrecisionDependenceOnTrainingSetSizeAnalyzer();
                LOGGER.debug("Performing action: " + action);
                break;
            case ANALYZE_PRECISION_ON_CLASSIFIER_PARAMS_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().maxJudgementGroupSizeParamSpinner(),
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().intervalPenaltyParamSpinner(),
                        uiHelper.widgets().intervalGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().precisePreciseIntervalJudgmentsCountRatioSpinner(),
                        uiHelper.widgets().stopCrossValidationOnErrorCheckBox()
                };

                selectedCrossValidator = crossValidators.asyncPrecisionDependenceOnClassifierParametersAnalyzer();
                LOGGER.debug("Performing action: " + action);
                break;
            case ANALYZE_PRECISION_ON_PRECISE_INTERVAL_SETS_SIZE_RATIO_DEPENDENCE:
                widgets = new JComponent[]{
                        uiHelper.widgets().maxJudgementGroupSizeParamSpinner(),
                        uiHelper.widgets().preciseSampleSizeSpinner(),
                        uiHelper.widgets().precisePenaltyParamSpinner(),
                        uiHelper.widgets().preciseGaussianKernelParamSpinner(),
                        uiHelper.widgets().preciseTrainingTestingSetsSizeRatioSpinner(),
                        uiHelper.widgets().intervalPreciseIntervalJudgmentsCountRatioSpinner(),
                        uiHelper.widgets().stopCrossValidationOnErrorCheckBox()
                };

                selectedCrossValidator = crossValidators.asyncPrecisionDependenceOnPreciseIntervalJudgementsRatioAnalyzer();
                LOGGER.debug("Performing action: " + action);
                break;
            default:
                LOGGER.error("Unsupported user action detected: " + action);
        }

        return widgets;
    }

    private void performAction(MainFrame mainFrame, JComponent... widgets) {
        CrossValidationParamsWindow nextPage = new CrossValidationParamsWindow(mainFrame, uiHelper, widgets);
        mainFrame.setVisible(false);
        nextPage.setVisible(true);
    }
}
