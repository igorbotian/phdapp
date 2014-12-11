package ru.spbftu.igorbotian.phd.output.summary;

import ru.spbftu.igorbotian.phdapp.svm.analytics.report.Report;

/**
 * Фабрика средств сохранения сводки по отчёту по работе классификатора с заданными параметрами
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.report.Report
 * @see ru.spbftu.igorbotian.phd.output.summary.ReportSummaryWriter
 */
public interface ReportSummaryWriterFactory {

    /**
     * Регистрация средства сохранения сводки по отчёту по работе классификатора с заданными параметрами
     * В дальнейшем, по типу отчёта можно будет получить связанное с ним средство сохранения сводки
     *
     * @param summaryWriter средство вывода отчётов даного типа в формате CSV
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     */
    public <R extends Report> void register(ReportSummaryWriter<R> summaryWriter);

    /**
     * Получение для указанного типа отчётов по работе классификатора связанного с ним средства сохранения сводки
     *
     * @param reportClass целевой тип отчётов по работе классификатора
     * @return средство сохранения сводки для указанного типа отчётов; <code>null</code>, если средства вывода
     * для данного типа отчётов не задано
     */
    public <R extends Report> ReportSummaryWriter<R> get(Class<R> reportClass);
}