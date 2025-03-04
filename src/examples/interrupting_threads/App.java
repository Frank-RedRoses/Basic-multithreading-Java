package examples.interrupting_threads;

import java.util.Random;

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
public class App {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting.");

        System.out.println("Running...");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < 1E8; i++) {
                    /*
                    // if-check to see if the thread is interrupted
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Thread is interrupted!");
                        break;
                    }
                    */
                    try {
                        Thread.sleep(1);
                        // sleep() will throw a InterruptedException if any thread has
                        // interrupted the current thread.
                        // The interrupted status of the current thread is cleared
                        // when this exception is thrown.
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException caught, t1 is interrupted");
                        break;
                    }
                    Math.sin(random.nextDouble());  // This line just simulates some work
                }
            }
        }
        );

        t1.start();

        Thread.sleep(500); // This sleeps gives plenty of time to t1 to start

        t1.interrupt(); // Does not actually stop the thread.
        /* The interrupt() method does not immediately terminate the thread.
         * Instead, it sets an internal flag indicating that the thread has been interrupted.
         * To properly handle an interruption, the thread must check its interrupted status
         * using Thread.currentThread().isInterrupted() or handled InterruptionException
         * if it is blocking on sleep(), wait(), or join().
         * See the commented code inside the t1 implementation for an example.
         */

        t1.join();

        System.out.println("Finished!");
    }
}
