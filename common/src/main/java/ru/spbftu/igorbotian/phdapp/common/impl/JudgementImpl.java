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

package ru.spbftu.igorbotian.phdapp.common.impl;

import ru.spbftu.igorbotian.phdapp.common.Judgement;
import ru.spbftu.igorbotian.phdapp.common.UnclassifiedObject;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @see ru.spbftu.igorbotian.phdapp.common.Judgement
 * @see ru.spbftu.igorbotian.phdapp.common.DataFactory
 */
class JudgementImpl implements Judgement {

    /**
     * Набор исходных объектов, который предпочтителен другого набора
     */
    private final Set<? extends UnclassifiedObject> preferable;

    /**
     * Набор исходных объектов, над которым другой набор имеет предпочтение
     */
    private final Set<? extends UnclassifiedObject> inferior;

    public JudgementImpl(Set<? extends UnclassifiedObject> preferable,
                         Set<? extends UnclassifiedObject> inferior) {
        this.preferable = Collections.unmodifiableSet(Objects.requireNonNull(preferable));
        this.inferior = Collections.unmodifiableSet(Objects.requireNonNull(inferior));

        checkSetsHaveDifferentItems();
    }

    private void checkSetsHaveDifferentItems() {
        for(UnclassifiedObject obj : preferable) {
            if(inferior.contains(obj)) {
                throw new IllegalArgumentException("Preferable and inferior sets should contain different objects");
            }
        }
    }

    @Override
    public Set<? extends UnclassifiedObject> preferable() {
        return preferable;
    }

    @Override
    public Set<? extends UnclassifiedObject> inferior() {
        return inferior;
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferable, inferior);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj == null || !(obj instanceof JudgementImpl)) {
            return false;
        }

        JudgementImpl other = (JudgementImpl) obj;
        return preferable.size() == other.preferable.size()
                && preferable.containsAll(other.preferable)
                && inferior.size() == other.inferior.size()
                && inferior.containsAll(other.inferior);
    }

    @Override
    public String toString() {
        return String.format("[%s;%s]", preferable, inferior);
    }
}
