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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.PointwiseInputData;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Модульные тесты для класса <code>FileBasedInputDataManager</code>
 *
 * @see ru.spbftu.igorbotian.phdapp.input.FileBasedInputDataManager
 */
public class FileBasedInputDataManagerTest {

    @Test
    public void testDefaultInputDataFolder() {
        Path dataFolder = Paths.get(FileBasedInputDataManager.DATA_FOLDER_NAME);
        FileBasedInputDataManager instance = new InputDataManagerImpl(mockConfigWithNoProperties());

        Assert.assertEquals(dataFolder.toAbsolutePath(), instance.defaultInputDataFolder().toAbsolutePath());
    }

    @Test
    public void testSetDefaultInputDataFolder() {
        Path dataFolder = Paths.get("dataFolder");
        FileBasedInputDataManager instance = new InputDataManagerImpl(mockConfigWithNoProperties());

        Assert.assertNotEquals(dataFolder, instance.defaultInputDataFolder());
        instance.setDefaultInputDataFolder(dataFolder);
        Assert.assertEquals(dataFolder.toAbsolutePath(), instance.defaultInputDataFolder().toAbsolutePath());
    }

    @Test
    public void testDefaultInputDataFolderSetByConfig() throws IOException {
        Path dataFolder = Paths.get("pathToDataFolder");
        testDefaultInputDataFolder(mockConfigWithDataFolderProperty(dataFolder), dataFolder);
    }

    @Test
    public void testInputDataFolderIsInCurrentFolderByDefault() throws IOException {
        Path dataFolder = Paths.get(FileBasedInputDataManager.DATA_FOLDER_NAME);
        testDefaultInputDataFolder(mockConfigWithNoProperties(), dataFolder);
    }

    private void testDefaultInputDataFolder(ApplicationConfiguration config, Path expectedDataFolder)
            throws IOException {

        FileBasedInputDataManager instance = new InputDataManagerImpl(config);
        Path defaultDataFolder = instance.defaultInputDataFolder();

        try {
            Assert.assertEquals(expectedDataFolder.toAbsolutePath(), defaultDataFolder.toAbsolutePath());
        } finally {
            Files.deleteIfExists(expectedDataFolder);
            Files.deleteIfExists(defaultDataFolder);
        }
    }

    private ApplicationConfiguration mockConfigWithNoProperties() {
        ApplicationConfiguration config = EasyMock.createNiceMock(ApplicationConfiguration.class);

        EasyMock.expect(config.hasParam(EasyMock.anyString())).andReturn(false);
        EasyMock.replay(config);

        return config;
    }

    private ApplicationConfiguration mockConfigWithDataFolderProperty(Path dataFolder) {
        ApplicationConfiguration config = EasyMock.createNiceMock(ApplicationConfiguration.class);

        EasyMock.expect(config.hasParam(FileBasedInputDataManager.DATA_FOLDER_CONFIG_SETTING)).andReturn(true);
        EasyMock.expect(config.getString(FileBasedInputDataManager.DATA_FOLDER_CONFIG_SETTING)).andReturn(
                dataFolder.toString());
        EasyMock.replay(config);

        return config;
    }

    private class InputDataManagerImpl extends FileBasedInputDataManager {

        private InputDataManagerImpl(ApplicationConfiguration config) {
            super(config, "any");
        }

        @Override
        protected PointwiseInputData deserialize(InputStream stream) throws IOException, DataException {
            return null;
        }

        @Override
        protected void serialize(PointwiseInputData data, OutputStream stream) throws IOException, DataException {
            // nothing
        }
    }
}
