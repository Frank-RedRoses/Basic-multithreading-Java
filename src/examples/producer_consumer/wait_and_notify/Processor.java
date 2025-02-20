package examples.producer_consumer.wait_and_notify;

import java.util.Scanner;

/**
 * If low-level synchronization is needed, the {@code wait} and {@code notify}
 * keywords can be used to coordinate thread execution.
 * {@code Processor} class implement both keywords to demonstrate their usage.
 */
public class Processor {

    /**
     * Exemplifies the use of the {@code wait()} method.
     * <p>
     * The {@code wait()} method is part of the {@code Object} class and
     * can only be called within a synchronized code block.
     * It releases the lock that the synchronized block is holding,
     * allowing other threads to acquire it.
     * <p>
     * The {@code wait()} method suspends execution without consuming
     * excessive system resources. To regain control of the lock,
     * another thread holding the same object's lock must call {@code notify()}.
     * <p>
     * If not handled properly, {@code wait()} can cause a thread
     * to wait indefinitely.
     */
    public void produce() throws InterruptedException {
        // This code needs to execute first.
        synchronized (this) {
            System.out.println("Producer thread running...");
            wait();
            System.out.println("Resumed");
        }
    }


    /**
     * Demonstrates the use of the {@code notify()} method.
     * <p>
     * Like {@code wait()}, the {@code notify()} method can only be
     * called within a synchronized block. It wakes up a thread that
     * previously called {@code wait()}, allowing it to regain control
     * of the same object's lock.
     * <p>
     * However, {@code notify()} does not immediately release the lock.
     * The notifying thread must first exit the synchronized block,
     * relinquishing control of the lock. Otherwise, the waiting thread
     * will remain blocked and never resume execution.
     */
    public void consume() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Thread.sleep(1000); // ensures that the other thread had time to start first
        synchronized (this) {
            System.out.println("Waiting for return key");
            scanner.nextLine();
            System.out.println("Return key pressed.");
            notify();
            Thread.sleep(5000); // waits 5 second before releasing "this" object lock.
            System.out.println("Consumption done.");
        }
    }
}
