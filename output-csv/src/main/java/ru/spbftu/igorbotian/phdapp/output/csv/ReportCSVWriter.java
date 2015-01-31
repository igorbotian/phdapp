package ru.spbftu.igorbotian.phdapp.output.csv;

import ru.spbftu.igorbotian.phdapp.svm.ClassifierParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.CrossValidatorParameter;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.Report;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Средство сохранения отчёта по работе классификатора с заданными параметрами в формате CSV
 *
 * @see CSVWriter
 */
public interface ReportCSVWriter<R extends Report> {

    /**
     * Получение типа отчёта по работе классификатора, с которым связано данное средство сохранения
     *
     * @return класс отчёта
     */
    Class<R> getTargetReportClass();

    /**
     * Сохрание заданного отчёта по работе классификатора в указанный символьный поток
     *
     * @param report        отчёт по работе классификатора
     * @param writer        выходной символьный поток
     * @param includeHeader если значение равно <code>true</code>, то первая строчка будет содержать локализованный заголовок
     *                      (название каждого столбца данных)
     * @throws java.io.IOException            в случае ошибки ввода/вывода при сохранении отчёта в поток
     * @throws java.lang.NullPointerException если хотя бы один из аргументов не задан
     */
    void writeTo(R report, PrintWriter writer, boolean includeHeader) throws IOException;
}
