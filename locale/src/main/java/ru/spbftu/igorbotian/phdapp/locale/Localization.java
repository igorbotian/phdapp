package ru.spbftu.igorbotian.phdapp.locale;

/**
 * Copyright (c) 2014 Igor Botian
 * <p>
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */
public interface Localization {

    /**
     * Получение локализованной строки по её идентификатору
     *
     * @param label идентификатор строки (непустая)
     * @return локализация заданной строки согласно текущей локали;
     * если строка не локализована для текущей локали, то возвращается её перевод по умолчанию;
     * если и перевода по умолчанию нет, то возвращается сам идентификатор строки
     * @throws java.lang.IllegalArgumentException если строка является пустой
     * @throws java.lang.NullPointerException     если строка не задана
     */
    public String getLabel(String label);
}
