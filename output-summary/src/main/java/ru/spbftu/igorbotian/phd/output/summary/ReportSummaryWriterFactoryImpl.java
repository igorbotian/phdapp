package ru.spbftu.igorbotian.phd.output.summary;

import com.google.inject.Inject;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.Report;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация фабрики средств сохранения сводки по отчёту по работе классификатора с заданными параметрами
 *
 * @see ru.spbftu.igorbotian.phd.output.summary.ReportSummaryWriterFactory
 */
class ReportSummaryWriterFactoryImpl implements ReportSummaryWriterFactory {

    private final Map<Class<? extends Report>, ReportSummaryWriter> summaryWriters = new ConcurrentHashMap<>();

    @Inject
    public ReportSummaryWriterFactoryImpl(Localization localization) {
        register(new SingleClassificationReportSummaryWriterImpl(localization));
        register(new MultiClassificationReportSummaryWriterImpl(localization));
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
