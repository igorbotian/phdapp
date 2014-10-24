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

import org.apache.commons.lang3.StringUtils;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;
import ru.spbftu.igorbotian.phdapp.common.Parameter;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.UnclassifiedData
 * @see DataFactory
 */
class UnclassifiedObjectImpl implements UnclassifiedObject {

    /**
     * Идентификатор объекта (необходим для быстрого различения одного объекта от другого)
     */
    private final String id;

    /**
     * Набор параметров объекта, которыми он характеризуется
     */
    private final Set<Parameter<?>> parameters;

    public UnclassifiedObjectImpl(String id, Set<Parameter<?>> params) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(params);

        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("ID cannot be empty");
        }

        if (params.isEmpty()) {
            throw new IllegalArgumentException("At least one parameter should be presented");
        }

        this.id = id;
        this.parameters = Collections.unmodifiableSet(params);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Set<Parameter<?>> parameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof UnclassifiedObject)) {
            return false;
        }

        UnclassifiedObjectImpl other = (UnclassifiedObjectImpl) obj;
        return id.equals(other.id)
                && parameters.size() == other.parameters.size()
                && parameters.containsAll(other.parameters);
    }

    @Override
    public String toString() {
        return String.join(":", id, parameters.toString());
    }
}
