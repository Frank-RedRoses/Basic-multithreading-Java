package examples.producer_consumer.low_level_synchronization;

import java.util.LinkedList;
import java.util.Random;

/**
 * {@code Processor} demonstrates low-level synchronization by implementing {@code producer()}
 * and {@code consumer()} methods using {@code wait()} and {@code notify()}. It utilizes a
 * shared list where two threads will simultaneously add and retrieve values.
 *<p>
 * A dedicated lock object is used to manage synchronization, ensuring that both threads operate
 * the same lock. This is crucial because {@code wait()} temporarily releases the lock and pauses
 * execution, and {@code notify()} or {@code notifyAll()} must be called on the same lock to resume
 * execution. If different locks are used in the synchronized blocks, the waiting thread will not
 * be notified and remain blocked.
 *<p>
 * Key considerations:
 *  <ul>
 *      <li>The {@code wait()} and {@code notify()} methods must be called on the same lock object
 *      used by the synchronized block.</li>
 *      <li>When multiple threads are waiting for the lock, {@code notifyAll()} can be used to wake
 *      them up instead of {@code notify()}, which only signals one thread.</li>
 *  </ul>
 *
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
public class Processor {

    private LinkedList<Integer> sharedList = new LinkedList<Integer>();
    private final int listLimitSize = 10;
    private final Object sharedExplicitLock = new Object();

    public  void producer() throws InterruptedException {
        int valueToAddOnList = 0;
        // infinite loop
        while (true) {
            synchronized (sharedExplicitLock) {

                while (sharedList.size() == listLimitSize)
                    /* Typically, the {@code wait()} method is placed inside a while loop to ensure that
                     * the condition requiring the thread to wait is no longer true, so it can safely proceed
                     * with the rest of the code after being notified.
                     */
                    sharedExplicitLock.wait();  // wait until the shared list has space to add a new value

                sharedList.add(valueToAddOnList);
                System.out.println("Producer added: " + valueToAddOnList + "; list size is " + sharedList.size());
                valueToAddOnList++;
                sharedExplicitLock.notify();
            }
        }
    }

    public void consumer() throws InterruptedException {
        Random randomTimeInMilliseconds = new Random();
        // infinite loop
        while(true){
            synchronized (sharedExplicitLock) {
                while (sharedList.size() == 0)
                    // again wait() inside a while loop
                    sharedExplicitLock.wait();  // wait until the shared list contain some value in it

                int valueRetrieved = sharedList.removeFirst();
                System.out.println("Removed value by consumer is: " + valueRetrieved +
                        "; Now list size is: " + sharedList.size());
                sharedExplicitLock.notify();
            }
            // This sleep time gives the producer time to fill out the shared list
            Thread.sleep(randomTimeInMilliseconds.nextInt(1000));
        }
    }
}
