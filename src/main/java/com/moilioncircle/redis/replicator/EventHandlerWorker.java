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

import com.moilioncircle.redis.replicator.cmd.Command;
import com.moilioncircle.redis.replicator.event.PostFullSyncEvent;
import com.moilioncircle.redis.replicator.event.PreFullSyncEvent;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyValuePair;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by leon on 8/25/16.
 */
/*package*/ class EventHandlerWorker extends Thread implements Closeable {

    private final AbstractReplicator replicator;
    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    public EventHandlerWorker(AbstractReplicator replicator) {
        this.replicator = replicator;
        setName("event-handler-worker");
    }

    @Override
    public void run() {
        while (!isClosed.get() || replicator.eventQueue.size() > 0) {
            Object event = null;
            try {
                event = replicator.eventQueue.poll(replicator.configuration.getPollTimeout(), TimeUnit.MILLISECONDS);
                if (event == null) {
                    continue;
                } else if (event instanceof KeyValuePair<?>) {
                    KeyValuePair<?> kv = (KeyValuePair<?>) event;
                    if (!replicator.doRdbFilter(kv)) continue;
                    replicator.doRdbHandler(kv);
                } else if (event instanceof Command) {
                    Command command = (Command) event;
                    if (!replicator.doCommandFilter(command)) continue;
                    replicator.doCommandHandler(command);
                } else if (event instanceof PreFullSyncEvent) {
                    replicator.doPreFullSync();
                } else if (event instanceof PostFullSyncEvent) {
                    replicator.doPostFullSync(((PostFullSyncEvent) event).checksum);
                } else {
                    throw new AssertionError(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Throwable throwable) {
                try {
                    replicator.doExceptionListener(throwable, event);
                } catch (Throwable e) {
                    replicator.close(e);
                    close();
                    while (replicator.eventQueue.size() > 0) {
                        replicator.eventQueue.clear(); //fail fast and discard event
                    }
                    break;
                }
            }
        }
        replicator.doCloseListener();
    }

    @Override
    public void close() {
        isClosed.compareAndSet(false, true);
    }

    public boolean isClosed() {
        return isClosed.get();
    }

}
