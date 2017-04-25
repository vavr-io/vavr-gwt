/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
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
