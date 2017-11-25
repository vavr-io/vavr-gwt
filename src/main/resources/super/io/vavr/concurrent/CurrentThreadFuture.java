/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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
package io.vavr.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * GWT emulated implementation of {@link java.util.concurrent.Future}.
 */
class CurrentThreadFuture<T> implements java.util.concurrent.Future<T> {

    private T result;

    private Exception exception;

    public CurrentThreadFuture(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    @Override public boolean cancel(boolean mayInterruptIfRunning) { return false; }

    @Override public boolean isCancelled() { return false; }

    @Override public boolean isDone() {
        return true;
    }

    @Override public T get() throws InterruptedException, ExecutionException {
        if (exception != null) {
            throw new ExecutionException(exception);
        }

        return result;
    }

    @Override public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
                                                               TimeoutException {
        return get();
    }
}
