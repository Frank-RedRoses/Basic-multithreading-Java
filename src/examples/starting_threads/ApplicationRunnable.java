package examples.starting_threads;

/**
 * Example class that shows how to implement a Thread using the
 * interface {@code Thread}  and implementing method {@code run()}.
 * <p>
 * Threads are lightweight units of execution within a process that
 * run concurrently within the same application and share the same
 * memory space.
 * </p><
 * Code explanation available at:
 * <a href="https://www.udemy.com/java-multithreading/?couponCode=FREE">
 * <em>https://www.udemy.com/java-multithreading/?couponCode=FREE</em>
 * </a>
 */
class RunnerRunnable implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Hello " + i + " Thread: " + Thread.currentThread().getName());

            try {
                Thread.sleep(100);  // simulates the thread doing some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ApplicationRunnable {

    /**
     * To run a Thread, we need to pass the class implementing the {@code Runnable}
     * interface to the declaration of a new {@code Thread} and invoke the
     * {@code start()} method. This internally calls the implementation of the
     * {@code run()} method.
     */
    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnerRunnable());
        Thread thread2 = new Thread(new RunnerRunnable());
        thread1.start();
        thread2.start();
    }
}
