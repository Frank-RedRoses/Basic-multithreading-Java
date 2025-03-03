package examples.semaphore;

import java.util.concurrent.Semaphore;

/**
 * This is a simpleton connection class.
 */
public class Connection {

    private final Semaphore semaphore = new Semaphore(10, true);
    // There is another constructor with an additional boolean parameter
    // that enforces a FIFO order when granting permits.
    // This ensures that threats do not get stuck indefinitely waiting
    // for a permit that others threads continuously acquire first.

    private static final Connection instance = new Connection();

    private int nConnections = 0;   // Trace the current number of connections connected.

    private Connection() {
    }

    public static Connection getInstance() {
        return instance;
    }

    /**
     * Connects to the database and count the number of active connections.
     * <p>
     * This is a safer implementation of this code, rather than only using
     * a one single method to execute the connection. Here, the
     * {@code Semaphore} acquires a permit, then executes the logic
     * of the connection inside a {@code try}/{@code finally} block, where the
     * semaphore always execute the release of the permit inside {@code finally},
     * no matter if an exception is thrown by the {@code doConnect} method.
     * </p>
     */
    public void connect() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            doConnect();
        } finally {
            semaphore.release();
        }
    }

    // Connect to the database and count the number of active connections.
    private void doConnect() {

        synchronized (this) {
            nConnections++;
            System.out.println("Current connections: " + nConnections);
        }

        // Simulate some work in the middle of the task
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (this) {
            nConnections--;
        }
    }
}
