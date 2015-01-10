package ru.spbftu.igorbotian.phdapp.ui.swing;

import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationException;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidationProgressListener;
import ru.spbftu.igorbotian.phdapp.svm.validation.PairwiseClassifierCrossValidator;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Окно, отображающее прогресс выполнения кросс-валидации классификатора с заданными параметрами
 */
public class CrossValidationProgressWindow extends JDialog implements CrossValidationProgressListener {

    private static final String VALIDATION_PROCESS_LABEL = "validationProcess";
    private static final String CANCEL_LABEL = "cancel";

    private final SwingUIHelper uiHelper;

    /**
     * Графический индикатор выполнения процесса валидации.
     * Отображает процент выполнения валидации
     */
    private JProgressBar progressBar;

    /**
     * Кнопка для остановки процесса валидации
     */
    private JButton cancelButton;

    public CrossValidationProgressWindow(Window owner, SwingUIHelper uiHelper) {
        super(owner, uiHelper.getLabel(VALIDATION_PROCESS_LABEL) + "...", ModalityType.TOOLKIT_MODAL);

        this.uiHelper = Objects.requireNonNull(uiHelper);

        initComponents();
        layoutComponents();
        initListeners();

        uiHelper.crossValidationProgressWindowDirector().addProgressListener(this);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(350, 75));
        setResizable(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        cancelButton = new JButton(uiHelper.getLabel(CANCEL_LABEL));
    }

    private void layoutComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());

        contentPane.add(progressBar, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        contentPane.add(cancelButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

        pack();
        setLocationRelativeTo(getOwner());
    }

    private void initListeners() {
        cancelButton.addActionListener(e -> {
            try {
                uiHelper.crossValidationProgressWindowDirector().cancelValidation();
            } catch (CrossValidationException ex) {
                uiHelper.errorDialog().show(this, ex);
            }
        });
    }

    private void setPercentsCompleted(int percentsCompleted) {
        progressBar.setValue(percentsCompleted);
        progressBar.setString(percentsCompleted + "%");
    }

    public void goToPreviousWindow() {
        setVisible(false);
        getOwner().setVisible(true);
        dispose();
    }

    public void goToNextWindow(final Report report) {
        setVisible(false);

        SwingUtilities.invokeLater(() -> {
            CrossValidationResultsWindow window = new CrossValidationResultsWindow(getOwner(), uiHelper, report);
            window.setVisible(true);
        });

        dispose();
    }

    @Override
    public <R extends Report> void crossValidationStarted(PairwiseClassifierCrossValidator<R> validator) {
        setPercentsCompleted(0);
    }

    @Override
    public <R extends Report> void crossValidationContinued(PairwiseClassifierCrossValidator<R> validator,
                                                            int percentsCompleted) {
        setPercentsCompleted(percentsCompleted);
    }

    @Override
    public <R extends Report> void crossValidationInterrupted(PairwiseClassifierCrossValidator<R> validator) {
        uiHelper.crossValidationProgressWindowDirector().removeProgressListener(this);
        goToPreviousWindow();
    }

    @Override
    public <R extends Report> void crossValidationCompleted(PairwiseClassifierCrossValidator<R> validator,
                                                            Report report) {

        setPercentsCompleted(100);
        uiHelper.crossValidationProgressWindowDirector().removeProgressListener(this);
        goToNextWindow(report);
    }

    @Override
    public <R extends Report> void crossValidationFailed(PairwiseClassifierCrossValidator<R> validator,
                                                         Throwable reason) {

        uiHelper.crossValidationProgressWindowDirector().removeProgressListener(this);
        uiHelper.errorDialog().show(CrossValidationProgressWindow.this, reason);
        goToPreviousWindow();
    }
}
