package examples.callable_and_future;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Understanding the {@code Callable} interface
 */
class MyCallable implements Callable<Integer> {
    int value;

    public MyCallable(int i) {
        this.value = i;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < value; i++) {
            sum += i;
        }
        System.out.println("Sum in Callable.call() is : " + sum);
        return sum;
    }
}

/**
 * Detail implementation of a task thread using {@code Future} interface
 */
public class App3 {

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> list = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> future;

        for (int i = 0; i < 10; i++) {
            future = executor.submit(new MyCallable(i));
            try {
                list.add(future.get());
            } catch (ExecutionException ex) {
                System.out.println(ex.getMessage());
            }
        }

        // Accept no more tasks
        executor.shutdown();
        //this is ont necessary in this case, but good practice :)
        executor.awaitTermination(1, TimeUnit.DAYS);

        for (int i = 0; i < list.size(); i++) {
            // get returned values from call()
            System.out.println("List " + i + "th value; Value: " + list.get(i));
        }
    }
}
