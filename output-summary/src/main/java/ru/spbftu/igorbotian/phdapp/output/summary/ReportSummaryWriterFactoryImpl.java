package ru.spbftu.igorbotian.phdapp.output.summary;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SingleClassificationReport;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация фабрики средств сохранения сводки по отчёту по работе классификатора с заданными параметрами
 *
 * @see ReportSummaryWriterFactory
 */
class ReportSummaryWriterFactoryImpl implements ReportSummaryWriterFactory {

    private final Map<Class<? extends Report>, ReportSummaryWriter> summaryWriters = new ConcurrentHashMap<>();

    @Inject
    public ReportSummaryWriterFactoryImpl(Localization localization) {
        ReportSummaryWriter<SingleClassificationReport> singleReportWriter
                = new SingleClassificationReportSummaryWriterImpl(localization);
        register(singleReportWriter);
        register(new MultiClassificationReportSummaryWriterImpl(localization, singleReportWriter));
    }

    @Override
    public <R extends Report> void register(ReportSummaryWriter<R> summaryWriter) {
        Objects.requireNonNull(summaryWriter);
        summaryWriters.put(summaryWriter.getTargetReportClass(), summaryWriter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Report> ReportSummaryWriter<R> get(Class<R> reportClass) {
        return summaryWriters.get(Objects.requireNonNull(reportClass));
    }
}
