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
import ru.spbftu.igorbotian.phdapp.common.TrainingData;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

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

    private static final Path anyConfigFolder = Paths.get("anyPath");

    @Test
    public void testDefaultInputDataFolder() {
        Path dataFolder = Paths.get(FileBasedInputDataManager.DATA_FOLDER_NAME);
        FileBasedInputDataManager instance = new InputDataManagerImpl(mockConfigWithNoProperties(), anyConfigFolder);

        Assert.assertEquals(dataFolder.toAbsolutePath(), instance.defaultInputDataFolder().toAbsolutePath());
    }

    @Test
    public void testSetDefaultInputDataFolder() {
        Path dataFolder = Paths.get("dataFolder");
        FileBasedInputDataManager instance = new InputDataManagerImpl(mockConfigWithNoProperties(), anyConfigFolder);

        Assert.assertNotEquals(dataFolder, instance.defaultInputDataFolder());
        instance.setDefaultInputDataFolder(dataFolder);
        Assert.assertEquals(dataFolder.toAbsolutePath(), instance.defaultInputDataFolder().toAbsolutePath());
    }

    @Test
    public void testDefaultInputDataFolderSetByConfig() throws IOException {
        Path dataFolder = Paths.get("pathToDataFolder");
        testDefaultInputDataFolder(mockConfigWithDataFolderProperty(dataFolder), anyConfigFolder, dataFolder);
    }

    @Test
    public void testInputDataFolderIsNearConfigFolderByDefault() throws IOException {
        Path parentFolder = Paths.get("parentFolder");
        Path configFolder = parentFolder.resolve("configFolder");
        Path dataFolder = parentFolder.resolve(FileBasedInputDataManager.DATA_FOLDER_NAME);

        Files.createDirectories(configFolder);

        try {
            testDefaultInputDataFolder(mockConfigWithNoProperties(), configFolder, dataFolder);
        } finally {
            Files.deleteIfExists(configFolder);
            Files.deleteIfExists(parentFolder);
        }
    }

    @Test
    public void testInputDataFolderIsInCurrentFolderByDefault() throws IOException {
        Path configFolder = Paths.get("anyNonExistingFolder");
        Path dataFolder = Paths.get(FileBasedInputDataManager.DATA_FOLDER_NAME);

        testDefaultInputDataFolder(mockConfigWithNoProperties(), configFolder, dataFolder);
    }

    private void testDefaultInputDataFolder(Configuration config, Path configFolder, Path expectedDataFolder)
            throws IOException {

        FileBasedInputDataManager instance = new InputDataManagerImpl(config, configFolder);
        Path defaultDataFolder = instance.defaultInputDataFolder();

        try {
            Assert.assertEquals(expectedDataFolder.toAbsolutePath(), defaultDataFolder.toAbsolutePath());
        } finally {
            Files.deleteIfExists(expectedDataFolder);
            Files.deleteIfExists(defaultDataFolder);
        }
    }

    private Configuration mockConfigWithNoProperties() {
        Configuration config = EasyMock.createNiceMock(Configuration.class);

        EasyMock.expect(config.hasSetting(EasyMock.anyString())).andReturn(false);
        EasyMock.replay(config);

        return config;
    }

    private Configuration mockConfigWithDataFolderProperty(Path dataFolder) {
        Configuration config = EasyMock.createNiceMock(Configuration.class);

        EasyMock.expect(config.hasSetting(FileBasedInputDataManager.DATA_FOLDER_CONFIG_SETTING)).andReturn(true);
        EasyMock.expect(config.getString(FileBasedInputDataManager.DATA_FOLDER_CONFIG_SETTING)).andReturn(
                dataFolder.toString());
        EasyMock.replay(config);

        return config;
    }

    private class InputDataManagerImpl extends FileBasedInputDataManager {

        private InputDataManagerImpl(Configuration config, Path configFolder) {
            super(config, configFolder.toString(), "any");
        }

        @Override
        protected TrainingData deserialize(InputStream stream) throws IOException, DataException {
            return null;
        }

        @Override
        protected void serialize(TrainingData data, OutputStream stream) throws IOException, DataException {
            // nothing
        }
    }
}
