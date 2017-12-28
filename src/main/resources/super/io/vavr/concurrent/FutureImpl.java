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

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.collection.Queue;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * GWT emulated version of {@link FutureImpl} with removed uses of Object's wait and notify methods.
 */
final class FutureImpl<T> implements Future<T> {

    /**
     * Used to start new threads.
     */
    private final Executor executor;

    /**
     * Used to synchronize state changes.
     */
    private final Object lock = new Object();

    /**
     * Indicates if this Future is cancelled
     */
    @GuardedBy("lock")
    private volatile boolean cancelled;

    /**
     * Once the Future is completed, the value is defined.
     */
    @GuardedBy("lock")
    private volatile Option<Try<T>> value;

    /**
     * The queue of actions is filled when calling onComplete() before the Future is completed or cancelled.
     * Otherwise actions = null.
     */
    @GuardedBy("lock")
    private Queue<Consumer<Try<T>>> actions;

    // single constructor
    private FutureImpl(Executor executor, Option<Try<T>> value, Queue<Consumer<Try<T>>> actions, Computation<T> computation) {
        this.executor = executor;
        synchronized (lock) {
            this.cancelled = false;
            this.value = value;
            this.actions = actions;
            try {
                computation.execute(this::tryComplete, this::updateThread);
            } catch(Throwable x) {
                tryComplete(Try.failure(x));
            }
        }
    }

    /**
     * Creates a {@code FutureImpl} that is immediately completed with the given value. No task will be started.
     *
     * @param executor An {@link Executor} to run and control the computation and to perform the actions.
     * @param value the result of this Future
     */
    @SuppressWarnings("unchecked")
    static <T> FutureImpl<T> of(Executor executor, Try<? extends T> value) {
        return new FutureImpl<>(executor, Option.some(Try.narrow(value)), null, (tryComplete, updateThread) -> {});
    }

    /**
     * Creates a {@code FutureImpl} that is eventually completed.
     * The given {@code computation} is <em>synchronously</em> executed, no thread is started.
     *
     * @param executor  An {@link Executor} to run and control the computation and to perform the actions.
     * @param computation A non-blocking computation
     * @param <T> value type of the Future
     * @return a new {@code FutureImpl} instance
     */
    static <T> FutureImpl<T> sync(Executor executor, CheckedConsumer<Predicate<Try<? extends T>>> computation) {
        return new FutureImpl<>(executor, Option.none(), Queue.empty(), (tryComplete, updateThread) -> {
            computation.accept(tryComplete);
        });
    }

    /**
     * Creates a {@code FutureImpl} that is eventually completed.
     * The given {@code computation} is <em>asynchronously</em> executed, a new thread is started.
     *
     * @param executor  An {@link Executor} to run and control the computation and to perform the actions.
     * @param computation A (possibly blocking) computation
     * @param <T> value type of the Future
     * @return a new {@code FutureImpl} instance
     */
    static <T> FutureImpl<T> async(Executor executor, CheckedConsumer<Predicate<Try<? extends T>>> computation) {
        return new FutureImpl<>(executor, Option.none(), Queue.empty(), (tryComplete, updateThread) -> executor.execute(() -> {
            try {
                computation.accept(tryComplete);
            } catch (Throwable x) {
                tryComplete.test(Try.failure(x));
            }
        }));
    }

    @Override
    public Future<T> await() {
        return this;
    }

    @Override
    public Future<T> await(long timeout, TimeUnit unit) {
        return this;
    }

    @Override
    public Future<T> cancel(boolean mayInterruptIfRunning) {
        if (!isCompleted()) {
            this.cancelled = tryComplete(Try.failure(new CancellationException()));
        }
        return this;
    }

    private void updateThread() {
    }

    @Override
    public Executor executor() {
        return executor;
    }

    @Override
    public Option<Try<T>> getValue() {
        return value;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isCompleted() {
        return value.isDefined();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Future<T> onComplete(Consumer<? super Try<T>> action) {
        Objects.requireNonNull(action, "action is null");
        if (isCompleted()) {
            perform(action);
        } else {
            synchronized (lock) {
                if (isCompleted()) {
                    perform(action);
                } else {
                    actions = actions.enqueue((Consumer<Try<T>>) action);
                }
            }
        }
        return this;
    }

    // This class is MUTABLE and therefore CANNOT CHANGE DEFAULT equals() and hashCode() behavior.
    // See http://stackoverflow.com/questions/4718009/mutable-objects-and-hashcode

    @Override
    public String toString() {
        final Option<Try<T>> value = this.value;
        final String s = (value == null || value.isEmpty()) ? "?" : value.get().toString();
        return stringPrefix() + "(" + s + ")";
    }

    /**
     * INTERNAL METHOD, SHOULD BE USED BY THE CONSTRUCTOR, ONLY.
     * <p>
     * Completes this Future with a value and performs all actions.
     * <p>
     * This method is idempotent. I.e. it does nothing, if this Future is already completed.
     *
     * @param value A Success containing a result or a Failure containing an Exception.
     * @throws IllegalStateException if the Future is already completed or cancelled.
     * @throws NullPointerException  if the given {@code value} is null.
     */
    private boolean tryComplete(Try<? extends T> value) {
        Objects.requireNonNull(value, "value is null");
        if (isCompleted()) {
            return false;
        } else {
            final Queue<Consumer<Try<T>>> actions;
            // it is essential to make the completed state public *before* performing the actions
            synchronized (lock) {
                if (isCompleted()) {
                    actions = null;
                } else {
                    // the job isn't set to null, see isCancelled()
                    actions = this.actions;
                    this.value = Option.some(Try.narrow(value));
                    this.actions = null;
                }
            }
            if (actions != null) {
                actions.forEach(this::perform);
                return true;
            } else {
                return false;
            }
        }
    }

    private void perform(Consumer<? super Try<T>> action) {
        try {
            executor.execute(() -> action.accept(value.get()));
        } catch(Throwable x) {
            // ignored // TODO: tell UncaughtExceptionHandler?
        }
    }

    private interface Computation<T> {
        void execute(Predicate<Try<? extends T>> tryComplete, Runnable updateThread) throws Throwable;
    }
}
