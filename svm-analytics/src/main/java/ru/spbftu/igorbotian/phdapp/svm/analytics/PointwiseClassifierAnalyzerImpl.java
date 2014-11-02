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

package ru.spbftu.igorbotian.phdapp.svm.analytics;

import com.google.inject.Singleton;
import ru.spbftu.igorbotian.phdapp.common.*;

import java.util.*;

/**
 * Реализация анализитора корректности работы классификатора, основывающийся на результатах его работы
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.PointwiseClassifier
 */
@Singleton
final class PointwiseClassifierAnalyzerImpl implements PointwiseClassifierAnalyzer {

    PointwiseClassifierAnalyzerImpl() {
        //
    }

    @Override
    public Report analyze(ClassifiedData classifiedData, PointwiseTrainingSet realData) {
        Objects.requireNonNull(classifiedData);
        Objects.requireNonNull(realData);

        if (classifiedData.objects().size() != realData.objects().size() ||
                classifiedData.objects().containsAll(realData.objects())) {
            throw new IllegalArgumentException("Real values for all classified objects are required");
        }

        Set<? extends ClassifiedObject> givenClasses = classifiedData.objects();
        Map<DataClass, DataClassStatistics> statistics = new HashMap<>();

        for (DataClass clazz : realData.classes()) {
            statistics.put(clazz, new DataClassStatistics());
        }

        for (PointwiseTrainingObject obj : realData.objects()) {
            DataClass realClass = obj.realClass();
            DataClass givenClass = getGivenClassFor(obj, givenClasses);

            DataClassStatistics realClassStatistics = statistics.get(realClass);
            realClassStatistics.realNumberOfObjects++;

            if (realClass.equals(givenClass)) {
                realClassStatistics.correctlyClassifiedObjects++;
            } else {
                realClassStatistics.incorrectlyClassifiedObjects++;
            }

            statistics.get(givenClass).classifiedNumberOfObjects++;
        }

        return composeReport(classifiedData.classes().size(), statistics.values());
    }

    private static DataClass getGivenClassFor(PointwiseTrainingObject realObj, Set<? extends ClassifiedObject> givenClasses) {
        assert realObj != null;
        assert givenClasses != null;

        for (ClassifiedObject obj : givenClasses) {
            if (Objects.equals(realObj.id(), obj.id())) {
                return obj.dataClass();
            }
        }

        throw new IllegalStateException("Can't find a given training object in the set of classified objects");
    }

    private static Report composeReport(int numberOfClasses, Collection<DataClassStatistics> statistics) {
        assert numberOfClasses >= 0;
        assert statistics != null;

        float accuracy = 0.0f;
        float precision = 0.0f;
        float recall = 0.0f;

        for (DataClassStatistics classStatistics : statistics) {
            accuracy += calculateAccuracy(classStatistics);
            precision += calculatePrecision(classStatistics);
            recall += calculateRecall(classStatistics);
        }

        return ReportBuilder.newReport()
                .setAccuracy(accuracy / numberOfClasses)
                .setPrecision(precision / numberOfClasses)
                .setRecall(recall / numberOfClasses)
                .build();
    }

    private static float calculateAccuracy(DataClassStatistics classStatistics) {
        return (classStatistics.classifiedNumberOfObjects - classStatistics.correctlyClassifiedObjects
                + classStatistics.incorrectlyClassifiedObjects)
                / classStatistics.realNumberOfObjects;
    }

    private static float calculatePrecision(DataClassStatistics classStatistics) {
        return classStatistics.correctlyClassifiedObjects / classStatistics.realNumberOfObjects;
    }

    private static float calculateRecall(DataClassStatistics classStatistics) {
        return classStatistics.correctlyClassifiedObjects / classStatistics.classifiedNumberOfObjects;
    }

    /**
     * Результаты классификации для отдельно взятого класса
     */
    private final static class DataClassStatistics {

        /**
         * Количество объектов, которое классификатор отнёс к данному классу
         */
        public int classifiedNumberOfObjects = 0;

        /**
         * Количество корректно классифицированных объектов, относящихся к данному классу
         */
        public int correctlyClassifiedObjects = 0;

        /**
         * Количество некорректно классифицированных объектов, относящихся к данному классу
         */
        public int incorrectlyClassifiedObjects = 0;

        /**
         * Реальное количество объектов, которое относится к данному классу
         */
        public int realNumberOfObjects = 0;
    }
}
