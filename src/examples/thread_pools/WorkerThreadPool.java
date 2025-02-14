package examples.thread_pools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the implementation of the
 * {@link examples.basic_synchronization.multiple_locks.Worker}
 * class with the {@code Runnable} interface.
 */
class Worker implements Runnable {

    private Random random = new Random();
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    public List<Integer> list1 = new ArrayList<Integer>();
    public List<Integer> list2 = new ArrayList<Integer>();

    @Override
    public void run() {
        process();
    }

    // Writes a random number between 0 and 99 in list1 after 1 millisecond.
    private void stageOne() {
        synchronized (lock1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list1.add(random.nextInt(100));
        }
    }

    // Writes a random number between 0 and 99 in list2 after 1 millisecond.
    private void stageTwo() {
        synchronized (lock2) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list2.add(random.nextInt(100));
        }
    }

    /**
     * Writes a random number to {@code list1}  and {@code list2}.
     */
    public void process() {
        // this loop should take approx. 2000 milliseconds
        for (int i = 0; i < 1000; i++) {
            stageOne();
            stageTwo();
        }
    }
}

/**
 * This is the implementation of the
 * {@link examples.basic_synchronization.multiple_locks.Worker} example
 * class with a thread pool to handle threads.
 */
public class WorkerThreadPool {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2); // Two threads

        System.out.println("Starting...");
        long start = System.currentTimeMillis();
        Worker worker = new Worker();
        for (int i = 0; i < 2; i++)
            executor.submit(worker); // worker instance must implement Runnable interface
        executor.shutdown();   // Prevent new tasks from being assigned

        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " milliseconds");
        System.out.println("List1: " + worker.list1.size() + "; List2: " + worker.list2.size());
    }
}
