/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * GWT emulated implementation of {@link ExecutorService} which executes all tasks in the current thread.
 */
public class CurrentThreadExecutorService implements ExecutorService {

    @Override public Future<?> submit(Runnable task) {
        task.run();

        return new CurrentThreadFuture<>(null, null);
    }

    @Override public void execute(Runnable command) {
        command.run();
    }
}
