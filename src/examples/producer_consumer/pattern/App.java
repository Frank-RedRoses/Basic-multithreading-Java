package examples.producer_consumer.pattern;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The Producer-Consumer pattern is based on the idea that one
 * or more threads (producers) generate items and add them to a shared
 * data structure (a {@code queue}), while other threads (consumers)
 * retrieve and process those items.
 *
 *
 */
public class App {
    /**
     * {@code BlockingQueue} and {@code ArrayBlockingQueue} are part
     * of the {@code java.util.concurrent} package, which provides
     * thread-safe classes for concurrency programming.
     * Since this classes handle synchronization internally,
     * there is no need to use the {@code synchronized} keyword.
     * <p>
     * It is generally better to use thread-safe classes and methods
     * from the  {@code java.util.concurrent} package rather than relying
     * on low-level synchronization with the {@code synchronized} keyword.
     * However, in some cases, manual synchronization may still be necessary
     * depending on the specific requirements of the application.
     * </p>
     */
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);


    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    producer();
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException ignored) {
                }
            }
        });

        t1.start();
        t2.start();

        /*  Original code from the tutorial.
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */

        // Pause for 30 seconds and force quitting the app (because we are
        // looping infinitely inside producer() and consumer() methods)
        Thread.sleep(30000);
        System.exit(0);
    }


    /**
     * The Producer task.
     * <p>
     * For examnple, this could be a thread that listens for incoming
     * text messages and adds them to the {@code queue} for processing.
     * </p>
     *
     * @throws InterruptedException if the thread is interrupted while
     * processing items
     */
    private static void producer() throws InterruptedException {
        Random random = new Random();
        while (true) {
            queue.put(random.nextInt(100));
            /*
             * The {@code put()} method waits patiently until space becomes
             * available in the queue before adding more items.
             * While waiting, it does not consume excessive resources.
             */
        }
    }

    /**
     * Simulates a real-world scenario where a separated thread
     * retrieves messages from the {@code queue}, processes them,
     * and finally sends them to their destination.
     *
     * @throws InterruptedException if the thread is interrupted while
     * processing items
     */
    private static void consumer() throws InterruptedException {
        Random random = new Random();
         /*
         * Uses randomness and sleep to periodically retrieve integers from the queue.
         * In a real-world scenario, processing each item takes time.
         * Here, randomness simulates that behavior by introducing a delay
         * before taking an item in each loop iteration.
         */
        while (true) {
            Thread.sleep(100);

            if (random.nextInt(10) == 0) {
                Integer value = queue.take();
                /*
                 * The {@code take()} method waits patiently until an item becomes
                 * available in the queue if it is empty.
                 * While waiting, it does not consume excessive resources.
                 */
                System.out.println("Taken value: " + value
                        + "; Queue size is: " + queue.size());
            }
        }
    }

}

