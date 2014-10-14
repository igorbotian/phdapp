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

import org.junit.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Тест, базовый для тестов объектов предметной области.
 * Предоставляет проверки, общие для поведения каждого объекта предметной области
 *
 * @param <T> объект предметной области, который тестируется
 */
public abstract class BaseDataTest<T> {

    /**
     * Тестирование метода hashCode
     *
     * @param obj          первый объект
     * @param differentObj второй объект, который отличается своими параметрами от первого
     * @param similarObj   третий объект, который идентичен своими параметрами первому
     */
    protected void testHashCode(T obj, T differentObj, T similarObj) {
        Set<T> container = new HashSet<>(); // hash code based collection

        container.add(obj);
        container.add(differentObj);

        Assert.assertTrue(obj.hashCode() == similarObj.hashCode());
        Assert.assertFalse(obj.hashCode() == differentObj.hashCode());
        Assert.assertEquals(2, container.size());
        Assert.assertTrue(container.contains(obj));
        Assert.assertTrue(container.contains(similarObj));
        Assert.assertTrue(container.contains(differentObj));
    }

    /**
     * Тестирование метода equals
     *
     * @param obj          первый объект
     * @param differentObj второй объект, который отличается своими параметрами от первого
     * @param similarObj   третий объект, который идентичен своими параметрами первому
     */
    protected void testEquals(T obj, T differentObj, T similarObj) {
        Assert.assertFalse(obj.equals(BaseDataTest.this /* или любой другой объект, имеющий другой класс */));
        Assert.assertTrue(obj.equals(obj));
        Assert.assertFalse(obj.equals(this));
        Assert.assertTrue(obj.equals(similarObj));
        Assert.assertFalse(obj.equals(differentObj));
    }
}
