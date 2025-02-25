package examples.re_entrant_locks.dead_lock;

/**
 * This application runs two {@code Threads} instances, each executing
 * different process, while sharing the same locks Objects for synchronization.
 * <p>
 * A **Deadlock** can occur when two or more {@code Threads} instances attempt
 * to acquire multiple locks in different order. If one thread locks **{@code Object}
 * A** and then tries to lock **{@code Object} B**, while another thread locks
 * **{@code Object} B** first and then tries to lock **{@code Object} A**,
 * both threads may become stuck waiting for each other to release their
 * respective locks.
 * <p>
 * In this scenario, neither thread can proceed because each is waiting for a lock
 * that will never be releases, causing the application to freeze.
 * </p>
 */
public class App {
    public static void main(String[] args) throws InterruptedException {

        final Runner runner = new Runner();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runner.runsFirstProcess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runner.runsSecondProcess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        runner.notifiesFinish();
    }
}
