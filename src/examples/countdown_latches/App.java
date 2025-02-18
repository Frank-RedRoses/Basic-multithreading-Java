package examples.countdown_latches;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exemplifies the use of {@code CountDownLatch} class.
 * {@code CountDownLatch} is a synchronization aid designed to
 * make threads wait until certain conditions are met.
 * <p>
 * {@code CountDownLatch} is used to block threads until a certain
 * number of signals (countdowns) occur. It acts as a counter that
 * threads can wait on until it reaches zero.
 * </p>
 * <p>
 * {@code CountDownLatch} cannot be reset; once it reaches zero, it
 * cannot be reused.
 * </p>
 * Steps on {@code CountDownLatch} execution:
 * <ul>
 * <li>One or more threads wait for the counter to reach zero (await() method).</li>
 * <li>Other threads decrement the counter (countDown() method).</li>
 * <li>Once the counter reaches zero, waiting threads are released.</li>
 * </ul>
 */
class Processor implements Runnable {
    private final int id;
    private final CountDownLatch latch;

    public Processor(int id, CountDownLatch latch) {
        this.id = id;
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Started...");
        try {
            System.out.println("Process " + id + " does some work");
            Thread.sleep(3000); // simulates some Process work
            System.out.println("Process " + id + " finish work");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();  // this thread decrease latch count after the work is done.
        }
    }
}

public class App {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++)
            executor.submit(new Processor(i, latch));
        executor.shutdown();
        /*
        A CountDownLatch initialized to N can be used to make
        one thread wait until N threads have completed some
        action, or some action has been completed N times.
         */
        try {
            // The Application's main thread waits until countdown reaches zero
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Tasks completed. Main thread continues.");
    }
}
