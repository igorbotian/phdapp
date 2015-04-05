/*
 * Copyright (c) 2014 Igor Botian
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.output.csv;

import java.io.*;
import java.util.Objects;

/**
 * Средство записи строковых данных в формате CSV
 *
 * @see java.io.PrintWriter
 * @see <a href="https://en.wikipedia.org/wiki/Comma-separated_values">https://en.wikipedia.org/wiki/Comma-separated_values</a>
 */
public class CSVWriter extends PrintWriter {

    private static final String LINE_BREAK = "\r\n"; // cross-platform

    public CSVWriter(Writer out) {
        super(out);
    }

    public CSVWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public CSVWriter(OutputStream out) {
        super(out);
    }

    public CSVWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public CSVWriter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public CSVWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public CSVWriter(File file) throws FileNotFoundException {
        super(file);
    }

    public CSVWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    /**
     * Запись указанного набора строковых данных в виде CSV-строки в выходной поток, указанный в конструкторе
     *
     * @param elements набор строковых данных
     * @throws IOException                    в случае ошибки ввода/вывода
     * @throws java.lang.NullPointerException если набора строковых данных не задан
     */
    public void writeLine(String... elements) throws IOException {
        write(String.join(";", Objects.requireNonNull(elements)));
        write(LINE_BREAK);
        flush();
    }
}
