package examples.thread_pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Represents a process that runs as a thread.
 */
class Processor implements Runnable{

    /**
     * Adding an {@code id} field is not necessary but can be useful when:
     * <ul>
     *     <li>You need to track specific tasks in logs or debugging.</li>
     *     <li>You want to monitor execution order or performance of different tasks.</li>
     *     <li>You are identifying failed tasks in a large-scale system.</li>
     * </ul>
     */
    private int id;

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Starting: " + id);

        try {
            System.out.println("Task " + id + " is running on " + Thread.currentThread().getName());
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
        System.out.println("Complete: " + id);
    }
}

/**
 * A Thread pool example implementation.
 *<p>
 * Thread pools are a simple way to manage lots of threads
 * at the same time.
 * </p>
 * An advantage of thread pools is that avoids the overhead of
 * starting new threads for each task by recycling the threads
 * in the pool.
 */
public class App {

    public static void main(String[] args) {
        // Create a pool with the assigned number of threads.
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            executor.submit(new Processor(i));  // The passed instance should implement the Runnable interface
            // This method assigns a task to an available thread in the pool.
            // If all threads are occupied, it will wait until a thread
            // finishes its task and becomes available again.
        }
        executor.shutdown();    // Tells the executor to stop accepting new tasks

        System.out.println("All tasks submitted.");

        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
            // Make this main thread to wait until all tasks are finished
            // before continuing with the rest of the code.
        } catch (InterruptedException ignored) {
        }

        System.out.println("All task completed.");
    }
}
