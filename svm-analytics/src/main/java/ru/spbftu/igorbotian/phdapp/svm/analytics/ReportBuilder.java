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

import java.util.Objects;

/**
 * Реализация шаблона проектирования <i>Builder</i> для создания объектов класса <code>Report</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.analytics.Report
 */
public final class ReportBuilder {

    private ReportImpl report = new ReportImpl();

    private ReportBuilder() {
        //
    }

    public static ReportBuilder newReport() {
        return new ReportBuilder();
    }

    public ReportBuilder setAccuracy(float accuracy) {
        report.accuracy = requireInRange(accuracy);
        return this;
    }

    public ReportBuilder setPrecision(float precision) {
        report.precision = requireInRange(precision);
        return this;
    }

    public ReportBuilder setRecall(float recall) {
        report.recall = requireInRange(recall);
        return this;
    }

    private float requireInRange(float metrics) {
        if (metrics < 0.0f || metrics > 1.0f) {
            throw new IllegalArgumentException("Metrics ");
        }

        return metrics;
    }

    public Report build() {
        if (report.accuracy == null
                || report.precision == null
                || report.recall == null) {
            throw new IllegalStateException("Cannot build a new report because not all metrics have been initialized");
        }

        return report;
    }

    private static class ReportImpl implements Report {

        public Float accuracy;
        public Float precision;
        public Float recall;

        @Override
        public float accuracy() {
            return accuracy;
        }

        @Override
        public float precision() {
            return precision;
        }

        @Override
        public float recall() {
            return recall;
        }

        @Override
        public int hashCode() {
            return Objects.hash(accuracy, precision, recall);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj == null || !(obj instanceof ReportImpl)) {
                return false;
            }

            ReportImpl other = (ReportImpl) obj;
            return Objects.equals(accuracy, other.accuracy)
                    && Objects.equals(precision, other.precision)
                    && Objects.equals(recall, other.recall);
        }

        @Override
        public String toString() {
            return String.format("[accuracy = %.2f, precision = %.2f, recall = %.2f]", accuracy, precision, recall);
        }
    }
}
