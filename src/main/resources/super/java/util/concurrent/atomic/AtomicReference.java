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
package java.util.concurrent.atomic;

import java.util.function.UnaryOperator;
import java.util.function.BinaryOperator;

/**
 * GWT emulated version of {@link AtomicReference} wrapping reference.
 */
public class AtomicReference<V> implements java.io.Serializable {
    private static final long serialVersionUID = -1848883965231344442L;

    private V value;

    public AtomicReference(V initialValue) {
        value = initialValue;
    }

    public AtomicReference() {
    }

    public final V get() {
        return value;
    }

    public final void set(V newValue) {
        value = newValue;
    }

    public final void lazySet(V newValue) {
        set(newValue);
    }

    public final boolean compareAndSet(V expect, V update) {
        if (value == expect) {
            value = update;
            return true;
        }
        return false;
    }

    public final boolean weakCompareAndSet(V expect, V update) {
        return compareAndSet(expect, update);
    }

    public final V getAndSet(V newValue) {
        V prev = get();
        set(newValue);
        return prev;
    }

    public final V getAndUpdate(UnaryOperator<V> updateFunction) {
        V prev = get();
        set(updateFunction.apply(prev));
        return prev;
    }

    public final V updateAndGet(UnaryOperator<V> updateFunction) {
        V prev = get();
        V next = updateFunction.apply(prev);
        set(next);
        return next;
    }

    public final V getAndAccumulate(V x, BinaryOperator<V> accumulatorFunction) {
        V prev = get();
        set(accumulatorFunction.apply(prev, x));
        return prev;
    }

    public final V accumulateAndGet(V x, BinaryOperator<V> accumulatorFunction) {
        V prev = get();
        V next = accumulatorFunction.apply(prev, x);
        set(next);
        return next;
    }

    public String toString() {
        return String.valueOf(get());
    }

}
