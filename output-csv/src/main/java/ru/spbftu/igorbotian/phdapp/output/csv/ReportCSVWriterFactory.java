package ru.spbftu.igorbotian.phdapp.output.csv;


import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

/**
 * Фабрика средств сохранения отчётов по работе классификатора с заданными параметрами в формате CSV
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.validation.report.Report
 * @see ReportCSVWriter
 */
public interface ReportCSVWriterFactory {

    /**
     * Регистрация средства сохранения отчётов по работе классификатора с заданными параметрами в формате CSV.
     * В дальнейшем, по типу отчёта можно будет получить связанное с ним средство сохранения в формате CSV
     *
     * @param csvWriter   средство вывода отчётов даного типа в формате CSV
     * @throws java.lang.NullPointerException если хотя бы один из параметров не задан
     */
    public <R extends Report> void register(ReportCSVWriter<R> csvWriter);

    /**
     * Получение для указанного типа отчётов по работе классификатора связанного с ним средства вывода в формате CSV
     *
     * @param reportClass целевой тип отчётов по работе классификатора
     * @return средство вывода в формате CSV для указанного типа отчётов; <code>null</code>, если средства вывода
     * для данного типа отчётов не задано
     */
    public <R extends Report> ReportCSVWriter<R> get(Class<R> reportClass);
}
