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

package ru.spbftu.igorbotian.phdapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.Logger;
import ru.spbftu.igorbotian.phdapp.common.DataModule;
import ru.spbftu.igorbotian.phdapp.common.InputDataModule;
import ru.spbftu.igorbotian.phdapp.conf.ApplicationConfigurationModule;
import ru.spbftu.igorbotian.phdapp.input.JsonInputDataManagementModule;
import ru.spbftu.igorbotian.phdapp.ioc.PhDAppModule;
import ru.spbftu.igorbotian.phdapp.locale.java.JavaI18NLocalizationModule;
import ru.spbftu.igorbotian.phdapp.log.Log4j;
import ru.spbftu.igorbotian.phdapp.output.csv.CSVOutputDataManagementModule;
import ru.spbftu.igorbotian.phdapp.output.summary.SummaryOutputDataManagementModule;
import ru.spbftu.igorbotian.phdapp.quadprog.QuadraticProgrammingModule;
import ru.spbftu.igorbotian.phdapp.svm.IntervalPairwiseClassifierModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.SvmIntervalClassifierValidationModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.SvmValidationCommonsModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.report.SvmValidationReportManagementModule;
import ru.spbftu.igorbotian.phdapp.svm.validation.sample.SvmValidationSampleManagementModule;
import ru.spbftu.igorbotian.phdapp.ui.UserInterface;
import ru.spbftu.igorbotian.phdapp.ui.swing.SwingUserInterfaceModule;
import ru.spbftu.igorbotian.phdapp.utils.ShutdownHooks;
import ru.spbftu.igorbotian.phdapp.utils.UtilsModule;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Основной класс приложения.
 * Содержит точку входа в программу.
 */
public class PhDApp {

    /**
     * Название системного свойства, значение которого указывает на директорию для хранения конфигурационных файлов
     */
    private static final String CONFIG_FOLDER_SYSTEM_PROPERTY = "phdapp.conf.folder";
    /**
     * Директория для хранения конфигурационных файлов
     */
    private static final Path CONFIG_FOLDER = getConfigFolder();
    /**
     * Список из модулей приложения
     */
    public static final Set<PhDAppModule> INJECTION_MODULES = Stream.of(
            new DataModule(),
            new InputDataModule(),
            new ApplicationConfigurationModule(CONFIG_FOLDER),
            new JsonInputDataManagementModule(CONFIG_FOLDER),
            new JavaI18NLocalizationModule(),
            new QuadraticProgrammingModule(),
            new IntervalPairwiseClassifierModule(),
            new SvmValidationCommonsModule(),
            new SvmValidationReportManagementModule(),
            new SvmValidationSampleManagementModule(),
            new SvmIntervalClassifierValidationModule(),
            new CSVOutputDataManagementModule(),
            new SummaryOutputDataManagementModule(),
            new SwingUserInterfaceModule(),
            new UtilsModule()
    ).collect(Collectors.toSet());

    private static Path getConfigFolder() {
        String customConfFolderName = System.getProperty(CONFIG_FOLDER_SYSTEM_PROPERTY);

        if (customConfFolderName != null && !customConfFolderName.isEmpty()) {
            Path customConfigFolder = Paths.get(customConfFolderName);

            if (Files.exists(customConfigFolder)) {
                return customConfigFolder;
            }
        }

        return Paths.get(".");
    }

    private PhDApp() {
        //
    }

    /**
     * Точка входа в программу
     *
     * @param args аргументы приложения
     */
    public static void main(String[] args) {
        Log4j.init(CONFIG_FOLDER);
        Logger logger = Logger.getLogger(PhDApp.class);

        try {
            logger.debug("Initializing application modules");
            start();
            logger.info("Application modules successfully initialized");
        } catch (Throwable e) {
            String message = e.getMessage();

            if(e.getCause() != null) {
                message += "\r\nReason: " + e.getCause().getMessage();
            }

            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            logger.fatal("Unhandled exception caught. Exiting application", e);
        }
    }

    private static void start() {
        Injector injector = Guice.createInjector(INJECTION_MODULES);
        registerShutdownHooks(injector.getInstance(ShutdownHooks.class));
        injector.getInstance(UserInterface.class).showMainWindow();
    }

    private static void registerShutdownHooks(final ShutdownHooks hooks) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public synchronized void start() {
                hooks.triggerAll();
            }
        });
    }
}