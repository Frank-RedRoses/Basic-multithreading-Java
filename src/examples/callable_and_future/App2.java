package examples.callable_and_future;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

public class App2 {

    /**
     * Example implementation with an anonymous call of {@code Callable}.
     * <p>
     * {@code Callable} is a task that returns a result and may throw an exception.
     * The {@code Callable} interface is similar to {@code Runnable}, in that both
     * are designed for classes whose instances are potentially executed by another
     * thread. A Runnable, however, does not return a result and cannot throw a
     * checked exception.
     * <p>
     * When you submit a {@code Callable} to an {@code ExecutorService}, it returns
     * a {@code Future<V>} object that you can use to retrieve the result.
     * <p>
     * A {@code Future} represents the result of an asynchronous computation.
     * Methods are provided to check if the computation is complete, to wait for
     * its completion, and to retrieve the result of the computation.
     */
    public static void main(String[] args) {
        // Create a thread pool
        ExecutorService executor = Executors.newCachedThreadPool();

        // This is a simple example of retrieving value from a thread.
        // However, using Future<?> and Callable<Void> as the parameters
        // allows you to return a null value, and still being able to use
        // the methods of the Future interface.
        Future<?> future = executor.submit(
                // Executes a task.
                new Callable<Void>() {
                    // <parameter> datatype depends on what you want to return from the thread.

                    @Override
                    public Void call() throws Exception {
                        final Random random = new Random();
                        int duration = random.nextInt(4000);

                        // Example of how an exception is handled by Future<> class.
                        if (duration > 2000) throw new IOException("Sleeping for too long.");

                        System.out.println("Starting thread... ");
                        try {
                            Thread.sleep(duration); // simulates some work
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Finished thread.");

                        // The return type is determined by the type parameter of the Callable interface.
                        return null;
                    }
                });

        executor.shutdown();

        // Shows the result or the IOException
        try {
            System.out.println("Result is: " + future.get());
            // get() blocks the thread until the result is finished. If the task takes too long, it can cause delays.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // This catch handles the IOException from Callable.
            IOException ex = (IOException) e.getCause();
            System.out.println(ex.getMessage());
        }
    }
}