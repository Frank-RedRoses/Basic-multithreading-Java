package examples.callable_and_future;

import java.util.concurrent.*;

class CallableImplemented implements Callable<Integer> {

    private int myName;

    CallableImplemented(int i) {
        myName = i;
    }

    public int getMyName() {
        return myName;
    }

    public void setMyName(int name) {
        this.myName = name;
    }

    @Override
    public Integer call() throws Exception {

        for (int i = 0; i < 10; i++) {
            System.out.println("Thread name: " + getMyName() + " value is :" + i);
        }
        return getMyName();
    }
}


public class CallableTester {

    public static void main(String[] args) throws InterruptedException {
        Callable<Integer> callable = new CallableImplemented(2);
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Integer> future = executor.submit(callable);

        try {
            System.out.println("Future value: " + future.get());
        } catch (Exception ignored) { }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }
}
