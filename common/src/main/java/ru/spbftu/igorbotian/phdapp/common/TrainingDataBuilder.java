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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация шаблона проектирования <i>Builder</i>, направленная на поэтапное формирование набора исходных данных
 * для классификации или выполнения какого-либо другого действия над ними.
 *
 * @see ru.spbftu.igorbotian.phdapp.common.DataClass
 * @see ru.spbftu.igorbotian.phdapp.common.DataObject
 * @see ru.spbftu.igorbotian.phdapp.common.TrainingDataObject
 */
public class TrainingDataBuilder extends DataBuilder {

    /**
     * Обучающая выборка, объекты которой содержат информацию о реальных классах классификации, которым они соответствуют
     */
    private Set<TrainingDataObject> trainingSet;

    /**
     * Добавление нового элемента в обучающую выборку
     *
     * @param data новый элемент обучающей выборки
     * @throws java.lang.NullPointerException если объект не задан
     */
    public void addTrainingObject(TrainingDataObject data) {
        Objects.requireNonNull(data);

        if (trainingSet == null) {
            trainingSet = new LinkedHashSet<>();
        }

        trainingSet.add(data);
    }

    /**
     * Формирование набора исходных данных
     *
     * @return набор исходных данных
     * @throws ru.spbftu.igorbotian.phdapp.common.DataException если формирование набора в данный момент невозможно
     *                                                          или формируемый набор является некорректным
     */
    public TrainingData build() throws DataException {
        if (!isReady()) {
            throw new DataException("Can't build a training data. " +
                    "Classes and testing set should be initialized first");
        }

        Data data = super.build();
        return DataFactory.newTrainingData(data.classes(), data.objects(),
                (trainingSet != null) ? trainingSet : Collections.emptySet());
    }
}
