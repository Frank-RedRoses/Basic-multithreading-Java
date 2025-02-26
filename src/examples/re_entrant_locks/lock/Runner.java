package examples.re_entrant_locks.lock;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code Runner} class shows how the {@code ReentrantLock} is used.
 * <p>
 * A {@code ReentrantLock} is an alternative to using the {@code synchronized}
 * keyword for thread synchronization.
 * <p>
 * The key feature of a reentrant lock is that it keeps track of how many times
 * it has been locked by the same thread. To fully release the lock, the thread
 * must call {@code unlock()} the same number of times it called {@code lock()}.
 * <p>
 * To prevent a situation where an exception occurs after acquiring the lock
 * —potentially causing the lock to never be released— it is essential to use
 * a {@code try/finally} block. The {@code unlock()} method should always be placed
 * inside the {@code finally} block to ensure it executes, even if an exception
 * is thrown.
 */
public class Runner {

    private int counter = 0;
    private Lock lock = new ReentrantLock();
    /**
     * With {@code ReentrantLock}, it is possible to achieve functionality similar
     * to {@code wait()} and {@code notify()} using the {@code Condition} class.
     * <p>
     * The {@code Lock} interface provides a method called {@code newCondition()},
     * which returns an instance of {@code Condition}. This instance allows the use
     * of:
     * <ul>
     *     <li>{@code await()} - equivalent to {@code wait()}</li>
     *     <li>{@code signal()} - equivalent to {@code notify()}</li>
     *     <li>{@code signalAll()} - equivalent to {@code notifyAll()}</li>
     * </ul>
     */
    private Condition condition = lock.newCondition();

    private void increment() {
        for (int i = 0; i < 10000; i++) {
            counter++;
        }
    }

    /**
     * Increment the value of our counter by 1 using synchronization
     * with a {@code ReentrantLock}, to avoid thread interleaving.
     * <p>
     * To use the methods {@code await()} and {@code notify}, it required to first
     * adquire the lock by calling {@code lock()}, just as you would enter a
     * {@code synchronized} block. However, unlike {@code wait()}, which automatically
     * releases the intrinsic lock, {@code await()} must be explicitly used within a
     * {@code try} block and will release the lock only while waiting.
     * <p>
     * Similarly, after calling {@code signal()}, you must release the lock using
     * {@code unlock()}. This ensures that other waiting threads can proceed once
     * the lock becomes available, just like {@code notify} work when a
     * {@code synchronized} block ends.
     *
     * @throws InterruptedException Thrown when a thread is interrupted.
     */
    public void firstProcess() throws InterruptedException {
        lock.lock();
        System.out.println("Waiting...");
        condition.await();
        System.out.println("Woken up...");

        try {
            increment();
        } finally {
            lock.unlock();
        }
    }

    public void secondProcess() throws InterruptedException {
        Thread.sleep(1000);
        lock.lock();
        System.out.println("Press the return key");
        new Scanner(System.in).nextLine();
        System.out.println("Got return key!");

        condition.signal();

        try {
            increment();
        } finally {
            lock.unlock();
        }
    }

    public void finishedNotification() {
        System.out.println("Counter has count: " + counter + " times.");
    }


}
