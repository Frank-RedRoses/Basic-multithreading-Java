package examples.basic_synchronization.multiple_locks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker {

    private Random random = new Random();

    /**
     * The limitation with synchronized methods is that they used
     * the single intrinsic lock of the {@code Worker} class.
     * This can become a bottleneck when multiple {@code synchronized}
     * methods of the same class are called, as only one thread
     * can acquire the intrinsic lock at a time.
     * <p>
     * To mitigate this issue, we can instantiate a separate
     * {@code Object} and use a {@code synchronized} block inside
     * the method instead of synchronizing the entire method.
     * By passing the {@code Object} to the synchronized block,
     * we allow finer control over synchronization.
     * </p>
     * While this is a simple way to address the problem, the
     * {@code ReentrantLock} class provides a more robust and
     * flexible solution for handling such cases.
     */
    Object lock1 = new Object();
    Object lock2 = new Object();

    private List<Integer> list1 = new ArrayList<Integer>();
    private List<Integer> list2 = new ArrayList<Integer>();

    /**
     * Writes a random number to the {@code list1}.
     */
    public void stageOne() {
        synchronized (lock1) {
            try {
                Thread.sleep(1);    // simulates some kind of calculation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list1.add(random.nextInt(100));
        }
    }

    /**
     * Writes a random number to the {@code list2}.
     */
    public synchronized void stageTwo() {
        synchronized (lock2) {
            try {
                Thread.sleep(1);    // simulates some kind of calculation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list2.add(random.nextInt(100));
        }
    }

    /**
     * Writes to {@code list1} and {@code list2}.
     * If two threads call this method, they will try to write
     * to both lists concurrently, and this will create the
     * same problem we had with the ++ operator, because writing
     * to a list is not an atomic operation.
     * <p>
     * To avoid this issue, the {@code synchronized} keyword should
     * be use in the methods {@code stageOne()} and {@code stageTwo()}.
     * </p>
     */
    public void process() {
        for (int i = 0; i < 1000; i++) {
            // each stage takes 1 millisecond
            stageOne();
            stageTwo();
        }
        // 2 milliseconds * 1000 times
        // At the end the for-loop should take approximately 2000 milliseconds
    }

    public void main() {
        System.out.println("Starting...");

        long start = System.currentTimeMillis();
/*
//      This is the minimal amount of code to run a thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        }).start();
*/
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();  // writes to both lists
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                process();  // writes to both lists
            }
        });

        // writes to both lists concurrently
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println("Time take: " + (end - start));
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());
        /*
         * In the end, this implementation will take the same amount of
         * execution time as running both threads concurrently, while
         * avoiding the issues caused by thread interleaving.
         */
    }
}
