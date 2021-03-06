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

package ru.spbftu.igorbotian.phdapp.input;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.*;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Модульные тесты для класса <code>JsonInputDataManager</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.input.JsonInputDataManager
 */
public class JsonInputDataManagerTest {

    /**
     * Объект тестируемого класса
     */
    private JsonInputDataManager dataManager;

    /**
     * Тестовый набор исходных данных
     */
    private PointwiseInputData data;

    @Before
    public void setUp() throws DataException {
        Injector injector = Guice.createInjector(new DataModule(), new InputDataModule());
        DataFactory dataFactory = injector.getInstance(DataFactory.class);
        InputDataFactory inputDataFactory = injector.getInstance(InputDataFactory.class);
        dataManager = new JsonInputDataManager(mockConfigWithNoProperties(), dataFactory, inputDataFactory);

        Set<DataClass> classes = Stream.of(
                dataFactory.newClass("firstClass"),
                dataFactory.newClass("secondClass")
        ).collect(Collectors.toSet());
        Set<Parameter<?>> params = Stream.of(
                dataFactory.newParameter("firstParam", "firstValue", BasicDataTypes.STRING),
                dataFactory.newParameter("secondParam", "secondValue", BasicDataTypes.STRING)
        ).collect(Collectors.toSet());
        Set<UnclassifiedObject> testingSet = Stream.of(
                dataFactory.newUnclassifiedObject("firstObject", params),
                dataFactory.newUnclassifiedObject("secondObject", params)
        ).collect(Collectors.toSet());
        Set<PointwiseTrainingObject> trainingSet = Stream.of(
                dataFactory.newPointwiseTrainingObject("thirdObject", params, classes.iterator().next()),
                dataFactory.newPointwiseTrainingObject("fourthObject", params, classes.iterator().next())
        ).collect(Collectors.toSet());

        data = inputDataFactory.newPointwiseData(classes, trainingSet, testingSet);
    }

    private ApplicationConfiguration mockConfigWithNoProperties() {
        ApplicationConfiguration config = EasyMock.createNiceMock(ApplicationConfiguration.class);

        EasyMock.expect(config.hasParam(EasyMock.anyString())).andReturn(false);
        EasyMock.replay(config);

        return config;
    }

    @Test
    public void testSerializationMechanism() throws IOException, DataException {
        ByteArrayOutputStream json = new ByteArrayOutputStream();
        dataManager.serialize(data, json);
        Assert.assertEquals(data, dataManager.deserialize(new ByteArrayInputStream(json.toByteArray())));
    }
}