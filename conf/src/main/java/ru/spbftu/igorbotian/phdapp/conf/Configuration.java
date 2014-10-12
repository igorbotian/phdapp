/**
 * Copyright (c) 2014 Igor Botian
 * <p/>
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author Igor Botian <igor.botian@gmail.com>
 */

package ru.spbftu.igorbotian.phdapp.conf;

/**
 * Средство управления конфигурацией приложения на основе чтения и задания настроек.
 */
public interface Configuration {

    /**
     * Получение настройки типа <code>Boolean</code>
     *
     * @param param название настройки (непустое)
     * @return значение типа <code>Boolean</code> или <code>null</code>, если такой настройки не было задано ранее
     */
    public Boolean getBoolean(String param);

    /**
     * Задание настройки с заданными именем и значением типа <code>Boolean</code>
     *
     * @param param имя настройки (непустое)
     * @param value значение настройки типа <code>Boolean</code>
     * @throws java.lang.NullPointerException если название настройки равно <code>null</code>
     */
    public void setBoolean(String param, boolean value);

    /**
     * Получение настройки типа <code>Integer</code>
     *
     * @param param название настройки (непустое)
     * @return значение типа <code>Integer</code> или <code>null</code>, если такой настройки не было задано ранее
     */
    public Integer getInt(String param);

    /**
     * Задание настройки с заданными именем и значением типа <code>Integer</code>
     *
     * @param param имя настройки (непустое)
     * @param value значение настройки типа <code>Integer</code>
     * @throws java.lang.NullPointerException если название настройки равно <code>null</code>
     */
    public void setInt(String param, int value);

    /**
     * Получение настройки типа <code>Long</code>
     *
     * @param param название настройки (непустое)
     * @return значение типа <code>Long</code> или <code>null</code>, если такой настройки не было задано ранее
     */
    public Long getLong(String param);

    /**
     * Задание настройки с заданными именем и значением типа <code>Long</code>
     *
     * @param param имя настройки (непустое)
     * @param value значение настройки типа <code>Long</code>
     * @throws java.lang.NullPointerException если название настройки равно <code>null</code>
     */
    public void setLong(String param, long value);

    /**
     * Получение настройки типа <code>Float</code>
     *
     * @param param название настройки (непустое)
     * @return значение типа <code>Float</code> или <code>null</code>, если такой настройки не было задано ранее
     */
    public Float getFloat(String param);

    /**
     * Задание настройки с заданными именем и значением типа <code>Float</code>
     *
     * @param param имя настройки (непустое)
     * @param value значение настройки типа <code>Float</code>
     * @throws java.lang.NullPointerException если название настройки равно <code>null</code>
     */
    public void setFloat(String param, float value);

    /**
     * Получение настройки типа <code>Double</code>
     *
     * @param param название настройки (непустое)
     * @return значение типа <code>Double</code> или <code>null</code>, если такой настройки не было задано ранее
     */
    public Double getDouble(String param);

    /**
     * Задание настройки с заданными именем и значением типа <code>Double</code>
     *
     * @param param имя настройки (непустое)
     * @param value значение настройки типа <code>Double</code>
     * @throws java.lang.NullPointerException если название настройки равно <code>null</code>
     */
    public void setDouble(String param, double value);

    /**
     * Получение настройки типа <code>String</code>
     *
     * @param param название настройки (непустое)
     * @return значение типа <code>String</code> или <code>null</code>, если такой настройки не было задано ранее
     */
    public String getString(String param);

    /**
     * Задание настройки с заданными именем и значением типа <code>String</code>
     *
     * @param param имя настройки (непустое)
     * @param value значение настройки типа <code>String</code> (не равно <code>null</code>)
     * @throws java.lang.NullPointerException если название или значение настройки равно <code>null</code>
     */
    public void setString(String param, String value);
}
