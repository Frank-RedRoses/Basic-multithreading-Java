package examples.basic_synchronization.volatile_keyword;

import java.util.Scanner;

/**
 * In basic thread synchronization, there are two common problems that
 * need to be avoided:
 * <ul>
 *  <li>Data caching</li>
 *  <li>Thread interleaving</li>
 * </ul>
 * <p>
 * In this example, the first problem is addressed by using the
 * {@code volatile} keyword. This keyword ensures that threads do not cache
 * variables when they are not accessed or modified within their own thread.
 */
class Processor extends Thread {
    /**
     * Declaring a variable as {@code volatile} tells the JVM and CPU that the
     * variable may be modified by multiple threads. As a result, every
     * read or write to a {@code volatile} variable is directly done from
     * the main memory. This creates a sort of “memory barrier”
     * that prevents caching optimizations for that variable.
     * <p>
     * With {@code volatile}, once one thread updates the variable, all other
     * threads see the updated value immediately because they are forced
     * to bypass any local caches.
     * </p>
     */
    private volatile boolean running = true;     //

    /**
     * Simulates that the thread does some work.
     */
    @Override
    public void run() {
        while (running) {
            System.out.println("Running");

            try {
                Thread.sleep(100);   // Simulates thread doing some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Change the value of {@code running} to {@code false}, this stops
     * the thread's running state.
     */
    public void shutdown() {
        running = false;
    }
}

/**
 * This code illustrates how the problem of data caching
 * can occur in a thread.
 */
public class App {

    /**
     * The {@code main} method is invoked by the main thread and runs
     * independently of a {@code Processor} thread.
     * <p>
     * Normally, one thread does not expect the other to modify its internal data.
     * However, if this happens, a threat may not detect changes to a variable's value,
     * even if another thead has modified it. This might happen because the JVM sometimes
     * optimizes code in threats that do not modify their internal data —such as the
     * {@code running} variable in {@code Processor}— by caching the variable's value.
     * As a result, even if another thread updates the variable, the cached value
     * remains unchanged in the optimized thread.
     * </p>
     * <p>
     * By using the {@code volatile} keyword in the variable declaration, we can prevent
     * this issue from occurring, as it ensures that the variable's value is never cached.
     * </p>
     */
    public static void main(String[] args) {
        Processor processor1 = new Processor();
        processor1.start();

        System.out.println("Press Enter to stop...\n" +
                "Volatile variable running will always be read from the main memory");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        processor1.shutdown();  
    }
}
