package examples.swing;

import javax.swing.SwingUtilities;

/**
 * Demonstrates how to use the {@code SwingWorker} class to manage threads in
 * Swing applications while keeping the UI responsive.
 * <p>
 * In Swing, regular threads should not be used to update the UI directly. Instead,
 * the {@code SwingWorker} abstract class provide a safe way to perform background tasks
 * while ensuring UI updates occur on the Event Dispatch Thread (EDT). In this
 * example, UI updates are handled within the {@code MainFrame} class.
 * <p>
 * There are two common scenarios where UI updates are needed:
 * <ul>
 *     <li>Displaying a status message after a task completes or when a
 *     major change occurs in the application.</li>
 *     <li>Updating a progress bar or a label dynamically while a background
 *     is running.</li>
 * </ul>
 */
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame("SwingWorker Demo");
            }
        });
    }
}
