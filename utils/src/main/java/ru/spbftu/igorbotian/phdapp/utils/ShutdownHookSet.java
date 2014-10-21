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

package ru.spbftu.igorbotian.phdapp.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Контейнер для всех действий, которые необходимо выполнить перед завершением работы приложения
 *
 * @see ru.spbftu.igorbotian.phdapp.utils.ShutdownHooks
 * @see ru.spbftu.igorbotian.phdapp.utils.ShutdownHook
 */
@Singleton
class ShutdownHookSet implements ShutdownHooks {

    private static final Logger LOGGER = Logger.getLogger(ShutdownHookSet.class);

    private final Set<ShutdownHook> hooks;

    @Inject
    public ShutdownHookSet(Set<ShutdownHook> hooks) {
        this.hooks = Collections.unmodifiableSet(Objects.requireNonNull(hooks));
    }

    @Override
    public void triggerAll() {
        LOGGER.info("Executing pre-exit actions");
        hooks.forEach(ShutdownHook::onExit);
    }
}
