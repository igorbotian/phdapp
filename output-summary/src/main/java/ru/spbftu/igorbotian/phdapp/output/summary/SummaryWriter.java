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

package ru.spbftu.igorbotian.phdapp.output.summary;

import java.io.*;
import java.util.Objects;

/**
 * Средство записи значений с соответствующими описаниями в виде сводки результатов по какому-либо действию.
 * Каждая строка такой сводки имеет следующий формат: <code>описание: значение</code>
 *
 * @see java.io.PrintWriter
 */
public class SummaryWriter extends PrintWriter {

    private static final String LINE_BREAK = "\r\n"; // cross-platform

    public SummaryWriter(Writer out) {
        super(out);
    }

    public SummaryWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public SummaryWriter(OutputStream out) {
        super(out);
    }

    public SummaryWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public SummaryWriter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public SummaryWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public SummaryWriter(File file) throws FileNotFoundException {
        super(file);
    }

    public SummaryWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    /**
     * Запись заголовка сводных данных
     * @param header заголовок
     * @throws IOException в случае ошибки ввода/вывода
     * @throws NullPointerException если параметр не задан
     */
    public void writeHeader(String header) throws IOException {
        Objects.requireNonNull(header);

        write(header);
        writeEmptyLine();
    }

    /**
     * Запись указанного значения вместе с соответствующим описанием как элемент сводных данных
     *
     * @param key   описание записываемого значения
     * @param value значение, которое будет содержать результирующая сводка
     * @throws IOException в случае ошибки ввода/вывода
     * @throws NullPointerException если хотя бы один из параметров не задан
     */
    public void writeItem(String key, Object value) throws IOException {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        write(key + ": " + value.toString());
        writeEmptyLine();
    }

    /**
     * Запись пустой строки
     * @throws IOException в случае ошибки ввода/вывода
     */
    public void writeEmptyLine() throws IOException {
        write(LINE_BREAK);
    }
}
