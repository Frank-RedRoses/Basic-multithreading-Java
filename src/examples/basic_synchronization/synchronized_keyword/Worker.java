package examples.basic_synchronization.synchronized_keyword;

/**
 * This class shows how the {@code synchronized} keyword and
 * the {@code Thread.join()} method works.
 * <p>
 * When two {@code Thread} instances run inside the {@code main}
 * method, they are independent. This means that any code in
 * {@code main} after the threads have started will execute
 * regardless of whether the threads have finished or not.
 * To be avoid this, we need to synchronize the threads
 * with the {@code main} thread. This can be achieved by using
 * the {@code Thread.join()} method and the {@code synchronized}
 * keyword.
 * </p>
 */
public class Worker {
    /*
     A volatile variable does not help in this case because
     the main problem is not related to caching but to
     interleaving.
     */
    private int count = 0;


    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.doWork();
    }

    /**
     * This method adds one to the value of count.
     * <p>
     * The {@code synchronized} keyword ensure that when a thread
     * calls a {@code synchronized} method, no other thread can
     * access it simultaneously.
     * </p>
     * <p>
     * {@code synchronized} achieved this by using the intrinsic
     * lock that every {@code Object} in Java possesses. When a
     * thread calls a {@code synchronized} method, it acquires
     * the object's lock, allowing only that thread to execute
     * the method. Other threads must wait until the method execution
     * finishes and the lock is release before they can access the
     * {@code synchronized} method. Each {@code Object} has a single
     * intrinsic lock.
     * </p>
     * <p>
     * {@code synchronized} can also be used with code blocks.
     * Variables inside a {@code synchronized} block are guaranteed
     * to have a consistent state across all threads, eliminating
     * the need for the {@code volatile} keyword.
     * </p>
     */
    public synchronized void increment() {
        count++;  // count++ is a non-atomic statement
        // count = count + 1; Is executed in three steps.
        // 1. fetch the value of count.
        // 2. increase the value by 1.
        // 3. assign new value to count.
    }

    public void doWork() {
        // first thread
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    increment();
                }
            }
        });

        // second thread
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    increment();
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            /*
              The join() method makes the thread in which it is called (e.g., the main thread)
              pause and wait until the thread that called joint() has finished execution.

              Be careful when using a non-atomic operation (such as ++ operator) on the same
              variable in multiple threads. This can lead to race conditions, where the
              variable's value is modified in the middle of the computation process.
              As a result, values may be overwritten, and the outcome of the non-atomic
              operation can be incorrect. (Note: the volatile keyword does not fix this
              problem)

              Race condition: If two threads execute count++ at the same time, both
              could read the same value before either writes the updated value.
              This can cause one increment to be lost, leading to incorrect results.

              To prevent race conditions, you need to synchronize access to the
              shared resource using:
              - synchronized keyword
              - AtomicInteger (which ensures atomic updates)
              - Lock mechanisms like ReentrantLock
             */
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Count is: " + count);
    }

}
