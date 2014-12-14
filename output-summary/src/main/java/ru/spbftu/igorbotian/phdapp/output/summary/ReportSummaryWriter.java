package ru.spbftu.igorbotian.phdapp.output.summary;

import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Средство сохранения сводки по отчёту по работе классификатора с заданными параметрами в формате сводки
 *
 * @see SummaryWriter
 */
public interface ReportSummaryWriter<R extends Report> {

    /**
     * Получение типа отчёта по работе классификатора, с которым связано данное средство сохранения
     *
     * @return класс отчёта
     */
    Class<R> getTargetReportClass();

    /**
     * Сохрание сводки по заданному отчёту по работе классификатора в указанный символьный поток
     *
     * @param report отчёт по работе классификатора
     * @param writer выходной символьный поток
     * @throws java.io.IOException            в случае ошибки ввода/вывода при сохранении отчёта в поток
     * @throws java.lang.NullPointerException если хотя бы один из аргументов не задан
     */
    void writeTo(R report, PrintWriter writer) throws IOException;
}
