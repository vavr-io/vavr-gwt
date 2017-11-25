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
import io.vavr.control.Try;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

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
        final AtomicBoolean onFailureCalled = new AtomicBoolean(false);
        final Ref<Predicate<Try<? extends String>>> comp = new Ref<>();
        final Future<String> future = Future.join(comp::setV);
        future.onFailure(e -> {
            onFailureCalled.set(true);
            assertEquals("message", e.getMessage());
        });
        comp.getV().test(Try.failure(new Exception("message")));
        assertTrue("onFailure handler should have been called", onFailureCalled.get());
    }

    public void testFutureSequence() {
        final AtomicBoolean onCompleteCalled = new AtomicBoolean(false);
        final Ref<Predicate<Try<? extends String>>> comp1 = new Ref<>();
        final Future<String> future1 = Future.join(comp1::setV);
        final Ref<Predicate<Try<? extends String>>> comp2 = new Ref<>();
        final Future<String> future2 = Future.join(comp2::setV);
        Future.sequence(List.of(future1, future2))
              .onComplete(results -> {
                  onCompleteCalled.set(true);
                  assertEquals(2, results.get().size());
              });
        comp1.getV().test(Try.success("success1"));
        comp2.getV().test(Try.success("success2"));
        assertTrue("onComplete handler should have been called", onCompleteCalled.get());
    }
}
