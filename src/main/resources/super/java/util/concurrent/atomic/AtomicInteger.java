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

import java.util.function.IntUnaryOperator;
import java.util.function.IntBinaryOperator;
import sun.misc.Unsafe;

/**
 * GWT emulated version of {@link AtomicInteger} wrapping simple int value.
 */
public class AtomicInteger extends Number implements java.io.Serializable {
    private static final long serialVersionUID = 6214790243416807050L;

    private int value;

    public AtomicInteger(int initialValue) {
        value = initialValue;
    }

    public AtomicInteger() {}

    public final int get() { return value; }

    public final void set(int newValue) { value = newValue; }

    public final void lazySet(int newValue) { set(newValue); }

    public final int getAndSet(int newValue) {
        int old = value;
        value = newValue;

        return old;
    }

    public final boolean compareAndSet(int expect, int update) {
        if (value == expect) {
            value = update;

            return true;
        }

        return false;
    }

    public final boolean weakCompareAndSet(int expect, int update) {
        return compareAndSet(expect, update);
    }

    public final int getAndIncrement() {
        return value++;
    }

    public final int getAndDecrement() {
        return value--;
    }

    public final int getAndAdd(int delta) {
        int old = value;
        value += delta;

        return old;
    }

    public final int incrementAndGet() {
        return ++value;
    }

    public final int decrementAndGet() {
        return --value;
    }

    public final int addAndGet(int delta) {
        value += delta;

        return value;
    }

    public final int getAndUpdate(IntUnaryOperator updateFunction) {
        int old = value;
        value = updateFunction.applyAsInt(value);

        return old;
    }

    public final int updateAndGet(IntUnaryOperator updateFunction) {
        value = updateFunction.applyAsInt(value);

        return value;
    }

    public final int getAndAccumulate(int x,
            IntBinaryOperator accumulatorFunction) {
        int old = value;
        value = accumulatorFunction.applyAsInt(value, x);

        return old;
    }

    public final int accumulateAndGet(int x,
            IntBinaryOperator accumulatorFunction) {
        value = accumulatorFunction.applyAsInt(value, x);

        return value;
    }

    public String toString() {
        return Integer.toString(get());
    }

    public int intValue() {
        return get();
    }

    public long longValue() {
        return (long) get();
    }

    public float floatValue() {
        return (float) get();
    }

    public double doubleValue() {
        return (double) get();
    }

}
