package examples.producer_consumer.low_level_synchronization;

/**
 * Implementation of the producer-consumer example class, but rather
 * using elements from the {@code java.util.concurrent} package
 * the synchronization is done manually using {@code wait()} and
 * {@code notify()} methods.
 * <br>
 * Tutorial and minor comments are from
 * <a href="http://www.caveofprogramming.com">
 * <em>http://www.caveofprogramming.com</em>
 * </a>
 * <br>
 * also freely available at
 * <a href="https://www.udemy.com/java-multithreading/?couponCode=FREE">
 * <em>https://www.udemy.com/java-multithreading/?couponCode=FREE</em>
 * </a>
 */
public class App {

    public static void main(String[] args) throws InterruptedException {

        final Processor processor = new Processor();    // This instance uses low-level synchronization

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processor.producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processor.consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();

//        thread1.join();
//        thread2.join();
        // The two threads are in an infinite loop, so we need to force the application to shut down.
        Thread.sleep(15000);    // wait for 15 seconds while the two threads execute
        System.exit(0);         // ends the application
    }
}
