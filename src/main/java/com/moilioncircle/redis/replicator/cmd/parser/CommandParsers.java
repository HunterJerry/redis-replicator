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

package com.moilioncircle.redis.replicator.cmd.parser;

import static com.moilioncircle.redis.replicator.Constants.CHARSET;

/**
 * @author Leon Chen
 * @since 2.2.0
 */
class CommandParsers {

    public static String objToString(Object object) {
        if (object == null) return null;
        byte[] bytes = (byte[]) object;
        return new String(bytes, CHARSET);
    }

    public static byte[] objToBytes(Object object) {
        return (byte[]) object;
    }

}
