package examples.semaphore;

import java.util.concurrent.*;

public class App {

    /**
     * Exemplifies the use of {@code Semaphore} in multithreading.
     * <p>
     * A {@code Semaphore} is an object that maintains a permit count,
     * this count represents the number of available resources. They
     * are commonly used to control access to shared resources, such as
     * database connections or thread pools.
     * <p>
     * No other threads are allowed to acquire the resource when
     * the semaphore count reaches 0. The current number of permits
     * can be check using the {@code availablePermits()} method.
     * <p>
     * The {@code release()} and {@code acquire()} methods are used to return or
     * acquire a permit from the semaphore, increasing or reducing the number of
     * available permits by 1, respectively.
     * <p>
     * They act similar to locks, but unlike locks, permits in a {@code Semaphore}
     * do not have to be released by the same thread that acquired them; Any
     * thread can release a permit.
     * <p>
     * Mutex (or a semaphore initialized to 1; meaning there's only one resource)
     * is basically a mutual exclusion; Only one thread can acquire the resource
     * at once, and all other threads trying to acquire the resource are blocked
     * until the thread owning the resource releases.
     * </p>
     */
    public static void main(String[] args) throws Exception{
        /*
         * Semaphore semaphore = new Semaphore(1); // Initial or default count of 1
         *
         * semaphore.release(); // Increments the number of permits
         * semaphore.acquire(); // Decrements the number of permits.
         * // If no permits are available acquire() will make the tread to wait until one is release.
         *
         * System.out.println("Available permits: " +  semaphore.availablePermits());
         */

        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 200; i++) {
            // This code will start 200 new threads in the pool, it will not reuse any of them.
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    Connection.getInstance().connect();
                }
            });
        }

        executor.shutdown();

        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
