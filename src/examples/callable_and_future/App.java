package examples.callable_and_future;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

public class App {

    /**
     * Example implementation with an anonymous call of {@code Callable}.
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        // This is a simple example of how to get a simple value from a thread.
        // But, using a collection as <parameter> can give you
        // the possibility to store result from multiple threads.
        Future<Integer> future = executor.submit(
                new Callable<Integer>() { // Executes a new task
                    // <parameter> Datatype depends on what you want to return from the thread.

                    @Override
                    public Integer call() throws Exception {
                        final Random random = new Random();
                        int duration = random.nextInt(4000);
                        // Example of how an exception is handled by Future<> class.
                        if (duration > 2000) throw new IOException("Sleeping for too long.");

                        System.out.println("Starting thread... ");

                        try {
                            Thread.sleep(duration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Finished thread.");

                        // The return type depends on the definition of the Callable class parameter.
                        return duration;
                    }
                });

        executor.shutdown();

        try {
            System.out.println("Result is: " + future.get());
            // get() method gets block and wait for all threads associated with future to finish.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // This catch handles the IOException from Callable.
            IOException ex = (IOException) e.getCause();
            System.out.println(ex.getMessage());
        }
    }
}
