package examples.starting_threads;

/**
 * Example class that shows how to implement a Thread using
 * inheritance through {@code Thread} class and overriding
 * the {@code run()} method.
 * <p>
 * Threads are lightweight units of execution within a process that
 * run concurrently within the same application and share the same
 * memory space.
 * </p>
 * Code explanation available at:
 * <a href="https://www.udemy.com/java-multithreading/?couponCode=FREE">
 * <em>https://www.udemy.com/java-multithreading/?couponCode=FREE</em>
 * </a>
 */
class Runner extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Hello " + i + "Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(100);  // pauses the current execution of the Thread.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ApplicationExtends {
    /**
     * To run a {@code Thread}, we need to declare a {@code Thread} instance and
     * invoke the {@code start()} method. This internally calls the overridden
     * {@code run()} method.
     */
    public static void main(String[] args) {
        Runner runner1 = new Runner();
        runner1.start();

        Runner runner2 = new Runner();
        runner2.start();
    }
}
