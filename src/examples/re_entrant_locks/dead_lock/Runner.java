package examples.re_entrant_locks.dead_lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@code Runner} class provide a solution for the **DeadLock** problem
 * by ensuring that both locks are acquired safely before proceeding.
 * <p>
 * This is achieved by using a method that checks whether both lock are
 * available. If one of the locks is already held by another {@code Thread},
 * it releases the acquired lock and retries later. To avoid excessive contention,
 * the {@code Thread} sleeps for a short period before attempting to acquire both
 * locks again.
 * <p>
 * Another approach to prevent deadlocks is to enforce a consistent lock acquisition
 * order across all threads, ensuring that locks are always requested in the
 * same sequence.
 * <p>
 * A **Deadlock** can occur not only when using **Re-entrant locks**, but also
 * with nested {@code synchronized} blocks if the locks are acquired in an
 * inconsistent order.
 * </p>
 */
public class Runner {
    private final Account userAccount1 = new Account();
    private final Account userAccount2 = new Account();

    private final Lock lockAccount1 = new ReentrantLock();
    private final Lock lockAccount2 = new ReentrantLock();

    /**
     * Method to handles the Thread acquisition of the locks.
     *
     * @param firstLock one lock
     * @param secondLock another lock
     * @throws InterruptedException Thrown when a thread is waiting,
     * sleeping, or otherwise occupied, and the thread is interrupted
     */
    private void adquiereLocks(Lock firstLock, Lock secondLock) throws InterruptedException {
        while (true) {
            // try to acquire the locks
            boolean gotFirstLock = false;
            boolean gotSecondLock = false;
            try {
                gotFirstLock = firstLock.tryLock();
                gotSecondLock = secondLock.tryLock();
            } finally {
                if (gotFirstLock && gotSecondLock) return;
                if (gotFirstLock) firstLock.unlock();
                if (gotSecondLock) secondLock.unlock();
            }
            // Locks not acquire
            Thread.sleep(1);
        }
    }

    /**
     * The process makes a transfer for the {@code userAccount1} to the
     * {@code userAccount2} and gets the locks in inverse order of the
     * {@code runsSecondProcess()} method.
     *
     * @throws InterruptedException Thrown when a thread is waiting,
     * sleeping, or otherwise occupied, and the thread is interrupted
     */
    public void runsFirstProcess() throws InterruptedException {
        Random randomAmount = new Random();

        for (int i = 0; i < 10000; i++) {
            adquiereLocks(lockAccount1, lockAccount2);
            try {
                Account.transfer(userAccount1, userAccount2, randomAmount.nextInt(100));
            } finally {
                lockAccount1.unlock();
                lockAccount2.unlock();
            }
        }
    }

    /**
     * The process makes a transfer for the {@code userAccount2} to the
     * {@code userAccount1} and gets the locks in inverse order of the
     * {@code runsFirstProcess()} method.
     *
     * @throws InterruptedException Thrown when a thread is waiting,
     * sleeping, or otherwise occupied, and the thread is interrupted
     */
    public void runsSecondProcess() throws InterruptedException {
        Random randomAmount = new Random();

        for (int i = 0; i < 10000; i++) {
            adquiereLocks(lockAccount2, lockAccount1);
            try {
                Account.transfer(userAccount2, userAccount1, randomAmount.nextInt(100));
            } finally {
                lockAccount1.unlock();
                lockAccount2.unlock();
            }
        }
    }

    /**
     * Notifies the end of the Thread's running.
     */
    public void notifiesFinish() {
        System.out.println("Account user1 balance: " + userAccount1.getBalance());
        System.out.println("Account user2 balance: " + userAccount2.getBalance());
        System.out.println("Overall balance of both user: " +
                (userAccount1.getBalance() + userAccount2.getBalance()));
    }
}
