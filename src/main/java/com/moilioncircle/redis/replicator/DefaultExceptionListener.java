/*
 * Copyright 2016 leon chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moilioncircle.redis.replicator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by leon on 11/30/16.
 */
public class DefaultExceptionListener implements ExceptionListener {
    private static final Log logger = LogFactory.getLog(DefaultExceptionListener.class);

    @Override
    public void handle(Replicator replicator, Throwable throwable, Object event) {
        logger.error("error on event [" + event + "]", throwable);
    }
}
