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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import ru.spbftu.igorbotian.phdapp.common.DataException;
import ru.spbftu.igorbotian.phdapp.common.TrainingData;
import ru.spbftu.igorbotian.phdapp.conf.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Модульные тесты для класса <code>FileBasedInputDataManager</code>
 *
 * @see FileBasedInputDataManager
 */
public class FileBasedInputDataManagerTest {

    @Test
    public void testDefaultInputDataFolder() {
        File anyFolder = new File(FileBasedInputDataManager.DATA_FOLDER_NAME);
        FileBasedInputDataManager instance = new FileBasedInputDataManagerImpl(mockConfigWithNoProperties(),
                "anyPathToConfigFolder");

        Assert.assertEquals(anyFolder.getAbsolutePath(), instance.defaultInputDataFolder().getAbsolutePath());
    }

    @Test
    public void testSetDefaultInputDataFolder() {
        File expectedDataFolder = new File("dataFolder");
        FileBasedInputDataManager instance = new FileBasedInputDataManagerImpl(mockConfigWithNoProperties(),
                "anyPathToConfigFolder");

        Assert.assertNotEquals(expectedDataFolder, instance.defaultInputDataFolder());
        instance.setDefaultInputDataFolder(expectedDataFolder);
        Assert.assertEquals(expectedDataFolder.getAbsolutePath(), instance.defaultInputDataFolder().getAbsolutePath());
    }

    @Test
    public void testDefaultInputDataFolderSetByConfig() {
        File expectedDataFolder = new File("pathToDataFolder");
        testDefaultInputDataFolder(expectedDataFolder, mockConfigWithDataFolderProperty(
                expectedDataFolder.getAbsolutePath()), "anyConfigFolder");
    }

    @Test
    public void testInputDataFolderIsNearConfigFolderByDefault() {
        File parentFolder = new File("parentFolder");
        File configFolder = new File(parentFolder, "configFolder");
        File expectedDataFolder = new File(parentFolder, FileBasedInputDataManager.DATA_FOLDER_NAME);

        configFolder.mkdirs();

        try {
            testDefaultInputDataFolder(expectedDataFolder, mockConfigWithNoProperties(), configFolder.getAbsolutePath());
        } finally {
            FileUtils.deleteQuietly(configFolder);
        }
    }

    @Test
    public void testInputDataFolderIsInCurrentFolderByDefault() {
        File configFolder = new File("anyNonExistingFolder");
        File expectedDataFolder = new File(FileBasedInputDataManager.DATA_FOLDER_NAME);

        testDefaultInputDataFolder(expectedDataFolder, mockConfigWithNoProperties(), configFolder.getAbsolutePath());
    }

    private void testDefaultInputDataFolder(File expectedDataFolder, Configuration config, String pathToConfigFolder) {
        FileBasedInputDataManager instance = new FileBasedInputDataManagerImpl(config, pathToConfigFolder);
        File defaultDataFolder = instance.defaultInputDataFolder();

        try {
            Assert.assertEquals(expectedDataFolder.getAbsolutePath(), defaultDataFolder.getAbsolutePath());
        } finally {
            FileUtils.deleteQuietly(expectedDataFolder);
            FileUtils.deleteQuietly(defaultDataFolder);
        }
    }

    private Configuration mockConfigWithNoProperties() {
        Configuration config = EasyMock.createNiceMock(Configuration.class);

        EasyMock.expect(config.hasSetting(EasyMock.anyString())).andReturn(false);
        EasyMock.replay(config);

        return config;
    }

    private Configuration mockConfigWithDataFolderProperty(String pathToDataFolder) {
        assert (StringUtils.isNotEmpty(pathToDataFolder));

        Configuration config = EasyMock.createNiceMock(Configuration.class);

        EasyMock.expect(config.hasSetting(FileBasedInputDataManager.DATA_FOLDER_CONFIG_SETTING)).andReturn(true);
        EasyMock.expect(config.getString(FileBasedInputDataManager.DATA_FOLDER_CONFIG_SETTING)).andReturn(pathToDataFolder);
        EasyMock.replay(config);

        return config;
    }

    private class FileBasedInputDataManagerImpl extends FileBasedInputDataManager {

        private FileBasedInputDataManagerImpl(Configuration config, String pathToConfigFolder) {
            super(config, pathToConfigFolder);
        }

        @Override
        protected TrainingData deserialize(InputStream stream) throws IOException, DataException {
            return null;
        }

        @Override
        protected void serialize(TrainingData data, OutputStream stream) throws IOException, DataException {
            // nothing
        }

        @Override
        protected String supportedFileExtension() {
            return "any";
        }
    }
}
