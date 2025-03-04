package examples.interrupting_threads;

import java.util.concurrent.*;

/**
 * Demonstrates how to interrupt a running thread in Java using
 * the built-in thread interruption mechanism.
 * <p>
 * It is important NOT to confuse thread interruption with:
 * </p>
 * <ul>
 *     <li><b>Software interrupts:</b> These occur when the CPU periodically
 *     interrupts the current instruction flow to execute a registered piece of
 *     code, such as the thread scheduler.</li>
 *     <li><b>Hardware interrupts:</b> These happen when the CPU responds to an
 *     external hardware signal by interrupting the current process to handle
 *     the event.</li>
 * </ul>
 */
public class App2 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting.");

        ExecutorService executor = Executors.newCachedThreadPool();

        Future<?> future = executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = 0; i < 1E8; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.printf("Interrupted at %d !!!", i);
                        break;
                    }
                }
                return null;
            }
        });

        executor.shutdown();
        Thread.sleep(500);

        //JavaDoc: http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html#cancel-boolean-
//        fu.cancel(true);

        //JavaDoc: http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html#shutdownNow--
        executor.shutdownNow();

        executor.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("Finished.");
    }
}
