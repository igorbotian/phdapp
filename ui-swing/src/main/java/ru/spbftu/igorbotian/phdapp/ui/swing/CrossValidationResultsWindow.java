package ru.spbftu.igorbotian.phdapp.ui.swing;

import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Окно для отображения результатов кросс-валидации
 */
public class CrossValidationResultsWindow extends JFrame {

    private static final String CROSS_VALIDATION_RESULTS_LABEL = "crossValidationResults";
    private static final String EXPORT_TO_CSV_LABEL = "exportToCSV";
    private static final String BACK_LABEL = "back";
    private static final String EXIT_LABEL = "exit";

    /**
     * Окно, из которого пользователь перешёл на данное для возможности возвращения назад
     */
    private final Window previousWindow;

    /**
     * Общие элементы пользовательского интерфейса
     */
    private final SwingUIHelper uiHelper;

    /**
     * Отчёт о результатах кросс-валидации, которые будет отображён в данном окне
     */
    private final Report report;

    /**
     * Текстовая область для отображения сводки по отчёта о результатах кросс-валидации
     */
    private JTextArea reportSummaryTextArea;

    /**
     * Кнопка для сохранения отчёта о результатах классификации в файле в формате CSV
     */
    private JButton exportToCSVButton;

    /**
     * Кнопка для закрытия данного окна и перехода к предыдущему
     */
    private JButton backButton;

    /**
     * Кнопка для выхода из приложения
     */
    private JButton exitButton;

    public CrossValidationResultsWindow(Window previousWindow, SwingUIHelper uiHelper, Report report) {
        this.previousWindow = Objects.requireNonNull(previousWindow);
        this.uiHelper = Objects.requireNonNull(uiHelper);
        this.report = Objects.requireNonNull(report);

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        setTitle(uiHelper.getLabel(CROSS_VALIDATION_RESULTS_LABEL));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        reportSummaryTextArea = new JTextArea();
        reportSummaryTextArea.setEditable(false);
        uiHelper.crossValidationResultWindowDirector().getReportSummary(report).forEach(line -> {
            reportSummaryTextArea.append(line);
            reportSummaryTextArea.append("\n");
        });

        exportToCSVButton = new JButton(uiHelper.getLabel(EXPORT_TO_CSV_LABEL) + "...");
        backButton = new JButton("< " + uiHelper.getLabel(BACK_LABEL));
        exitButton = new JButton(uiHelper.getLabel(EXIT_LABEL));
    }

    private void layoutComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(exportToCSVButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);

        int inset = 20;
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(new Insets(inset, inset, inset, inset)));
        contentPane.add(new JScrollPane(reportSummaryTextArea), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);

        setContentPane(contentPane);
        setMinimumSize(new Dimension(640, 480));
        setMaximumSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                goToPreviousWindow();
            }
        });

        backButton.addActionListener(e -> goToPreviousWindow());
        exitButton.addActionListener(e -> System.exit(0));
    }

    public void goToPreviousWindow() {
        setVisible(false);
        previousWindow.setVisible(true);
        dispose();
    }
}
