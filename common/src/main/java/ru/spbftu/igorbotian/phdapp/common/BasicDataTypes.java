/**
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

package ru.spbftu.igorbotian.phdapp.common;

/**
 * Набор базовых типов данных (значения параметра объекта набора исходных данных)
 *
 * @see DataType
 */
public final class BasicDataTypes {

    private BasicDataTypes() {
        //
    }

    /**
     * Целочисленный тип данных
     */
    public static final DataType<java.lang.Integer> INTEGER = new Integer();

    /**
     * Вещественный тип данных
     */
    public static final DataType<Double> REAL = new Real();

    /**
     * Строковый тип данных
     */
    public static final DataType<java.lang.String> STRING = new String();

    /**
     * Булев тип данных
     */
    public static final DataType<java.lang.Boolean> BOOL = new Bool();

    /**
     * Класс целочисленного типа данных
     */
    private static class Integer extends AbstractDataType<java.lang.Integer> {

        public Integer() {
            super("integer", java.lang.Integer.class);
        }
    }

    /**
     * Класс строкового типа данных
     */
    private static class String extends AbstractDataType<java.lang.String> {

        public String() {
            super("string", java.lang.String.class);
        }
    }

    /**
     * Класс вещественного типа данных
     */
    private static class Real extends AbstractDataType<Double> {

        private Real() {
            super("real", java.lang.Double.class);
        }
    }

    /**
     * Класс булева типа данных
     */
    private static class Bool extends AbstractDataType<Boolean> {

        public Bool() {
            super("bool", java.lang.Boolean.class);
        }
    }
}
