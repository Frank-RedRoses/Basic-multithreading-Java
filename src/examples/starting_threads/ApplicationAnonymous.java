package examples.starting_threads;
/**
 * A third way to implement a {@code Thread} is by using an
 * anonymous class when instantiating the {@code Thread} class.
 * This requires implementing the {@code Runnable} interface
 * inside the instance declaration.
 */
public class ApplicationAnonymous {

    public static void main(String[] args) {
        /*
         Sometimes when we want to run a method in its own
         thread, it may seem too much work to create a separate
         class that extends "Thread" or implements "Runnable".
         In such cases, the following implementation can be use
         instead.
        */
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Hello " + i + " Thread: " + Thread.currentThread().getName());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
