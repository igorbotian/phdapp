package ru.spbftu.igorbotian.phd.output.csv;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.locale.Localization;
import ru.spbftu.igorbotian.phdapp.svm.analytics.report.Report;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация фабрики средств сохранения отчётов по работе классификатора с заданными параметрами в формате CSV
 */
@Singleton
class ReportCSVWriterFactoryImpl implements ReportCSVWriterFactory {

    private final Map<Class<? extends Report>, ReportCSVWriter> csvWriters = new ConcurrentHashMap<>();

    @Inject
    public ReportCSVWriterFactoryImpl(Localization localization) {
        SingleClassificationReportCSVWriter singleReportWriter
                = new SingleClassificationReportCSVWriter(localization);
        register(singleReportWriter);
        register(new MultiClassificationReportCSVWriter(localization, singleReportWriter));
    }

    @Override
    public <R extends Report> void register(ReportCSVWriter<R> csvWriter) {
        Objects.requireNonNull(csvWriter);
        csvWriters.put(csvWriter.getTargetReportClass(), csvWriter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Report> ReportCSVWriter<R> get(Class<R> reportClass) {
        Objects.requireNonNull(reportClass);
        return csvWriters.get(reportClass);
    }
}
