package ru.spbftu.igorbotian.phdapp.svm;

import ru.spbftu.igorbotian.phdapp.conf.AbstractConfiguration;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Контейнер, содержащий параметры конфигурации.
 * Значения параметров содержатся в памяти
 *
 * @see ru.spbftu.igorbotian.phdapp.conf.Configuration
 */
public class ClassifierParams extends AbstractConfiguration {

    @Override
    protected Map<String, String> load() throws IOException {
        return Collections.emptyMap();
    }

    @Override
    protected void store(Map<String, String> values) throws IOException {
        // значения параметров содержатся только в памяти
    }
}
