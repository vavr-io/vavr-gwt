/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package client;

import com.google.gwt.junit.client.GWTTestCase;
import io.vavr.CheckedFunction0;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.concurrent.Promise;

import java.util.concurrent.Executors;

import static io.vavr.API.Future;

/**
 * We should split tests into small parts because of this compiler message
 *
 * [INFO]    Compiling...
 * [INFO]       [WARN] JDT threw an exception: file:/home/ruslan/proj/javaslang-gwt/src/test/java/client/ConcurrentTestGwt.java: java.lang.ArrayIndexOutOfBoundsException: 1600
 */
public class ConcurrentTest1 extends GWTTestCase {

    @Override public String getModuleName()  {
        return "TestModule";
    }

    public void testCreateFailFuture() {
        final Future<Void> failed = Future.failed(new RuntimeException("ooops"));
        assertTrue(failed.isFailure());
        Throwable t = failed.getValue().get().getCause();
        assertEquals(t.getClass(), RuntimeException.class);
        assertEquals(t.getMessage(), "ooops");
    }

    public void testCreateSuccessFuture() {
        final Future<String> success = Future(Executors.newCachedThreadPool(), () -> "hehehe");
        assertTrue(success.isSuccess());
        assertEquals(success.get(), "hehehe");
    }

    public void testFutureSuccess() {
        boolean[] onCompleteCalled = new boolean[] { false };
        Promise<String> promise = Promise.make();
        promise.future().onComplete(value -> {
            onCompleteCalled[0] = true;
            assertEquals("value", value.get());
        });
        promise.success("value");
        assertTrue("onComplete handler should have been called", onCompleteCalled[0]);
    }
}
