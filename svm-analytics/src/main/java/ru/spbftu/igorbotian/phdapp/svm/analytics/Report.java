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

/**
 * Отчёт, который содержит различные метрики работы заданного классификатора
 *
 * @see ru.spbftu.igorbotian.phdapp.svm.Classifier
 */
public interface Report {

    /**
     * Получение значения метрики <i>Accuracy</i>.
     * Метрика вычисляется по следующей формуле:<br/>
     * <i>((R - Kr) + Kw) / N</i>, где<br/>
     * <i>R</i> - общее количество документов, относящихся к искомой категории по мнению классификатора;<br/>
     * <i>Kr</i> - количество документов, которые классификатор правильно отметил как относящиеся к искомой категории;<br/>
     * <i>Kw</i> - количество документов, которые классификатор неправильно отметил как не относящиеся к искомой категории;<br/>
     * <i>N</i> - общее количество документов, относящихся к искомой категории.<br/>
     *
     * @return вещественное число в диапазоне [0.0; 1.0]
     */
    float accuracy();

    /**
     * Получение значения метрики <i>Precision</i>.
     * Метрика вычисляется по следующей формуле:<br/>
     * <i>Kr / N</i>, где<br/>
     * <i>Kr</i> - количество документов, которые классификатор правильно отметил как относящиеся к искомой категории;<br/>
     * <i>N</i> - общее количество документов, относящихся к искомой категории.<br/>
     *
     * @return вещественное число в диапазоне [0.0; 1.0]
     */
    float precision();

    /**
     * Получение значения метрики <i>Recall</i>.
     * Метрика вычисляется по следующей формуле:<br/>
     * <i>Kr / R</i>, где<br/>
     * <i>Kr</i> - количество документов, которые классификатор правильно отметил как относящиеся к искомой категории;<br/>
     * <i>R</i> - общее количество документов, относящихся к искомой категории по мнению классификатора.<br/>
     *
     * @return вещественное число в диапазоне [0.0; 1.0]
     */
    float recall();
}
