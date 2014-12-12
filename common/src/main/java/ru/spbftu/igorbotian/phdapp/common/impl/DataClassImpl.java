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

package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.DataClass;

import java.util.Objects;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.DataClass
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class DataClassImpl implements DataClass {

    /**
     * Название класса (его строковое представление)
     */
    private final String name;

    public DataClassImpl(String name) {
        Objects.requireNonNull(name);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof DataClassImpl)) {
            return false;
        }

        return name.equals(((DataClassImpl) obj).name);
    }

    @Override
    public String toString() {
        return name();
    }
}
