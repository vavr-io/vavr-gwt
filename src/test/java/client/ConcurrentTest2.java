/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package client;

import com.google.gwt.junit.client.GWTTestCase;
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
public class ConcurrentTest2 extends GWTTestCase {

    @Override public String getModuleName()  {
        return "TestModule";
    }

    public void testFutureFailure() {
        boolean[] onFailureCalled = new boolean[] { false };
        Promise<String> promise = Promise.make();
        promise.future().onFailure(e -> {
            onFailureCalled[0] = true;
            assertEquals("message", e.getMessage());
        });
        promise.failure(new Exception("message"));
        assertTrue("onFailure handler should have been called", onFailureCalled[0]);
    }

    public void testFutureSequence() {
        boolean[] onCompleteCalled = new boolean[] { false };
        Promise<String> promise1 = Promise.make();
        Promise<String> promise2 = Promise.make();
        Future.sequence(List.of(promise1.future(), promise2.future()))
              .onComplete(results -> {
                  onCompleteCalled[0] = true;
                  assertEquals(2, results.get().size());
              });
        promise1.success("success1");
        promise2.success("success2");
        assertTrue("onComplete handler should have been called", onCompleteCalled[0]);
    }
}
