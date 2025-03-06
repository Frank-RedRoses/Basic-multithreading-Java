package examples.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Represents the main UI windows of the Swing application.
 * <br>
 * This class extends {@code JFrame} and runs on the Event Dispatch Thread (EDT)
 * ensuring proper handling of UI components.
 */
public class MainFrame extends JFrame {
    private final JLabel progressLabel;
    private final JLabel statusLabel;
    private final JButton startButton;

    public MainFrame(String title) {
        super(title);

        // Initialize the UI components
        progressLabel = new JLabel("0");
        statusLabel = new JLabel("Task not completed");
        startButton = new JButton("Start");


        // Configure the layout of the main window
        setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        layoutConstraints.fill = layoutConstraints.NONE;

        // add count1 label to main window
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        add(progressLabel, layoutConstraints);

        // add status label to main window
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        add(statusLabel, layoutConstraints);

        // add start button to main window
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 2;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        add(startButton, layoutConstraints);
        // Defines the action to execute when the button is clicked.
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent clickEvent) {
                startSwingThreadExample();
            }
        });

        // GUI windows properties
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startSwingThreadExample() {
        /*
         * When using `Void` as the first type parameter in `SwingWorker`,
         * the `doInBackground()` method must return `Void` in its signature
         * and `null` in the return statement.
         * This is the simplest case for a background thread that executes code
         * without returning a value or updating the UI.
         *
         * The first type parameter in `SwingWorker` defines the return type
         * of `doInBackground()`. In this example, it is `Boolean`, but in can
         * be any data type.
         *
         * The second parameter defines the type of the intermediate values
         * used to update the UI dynamically. In this example, it is `Integer`.
         */
        SwingWorker<Boolean, Integer> worker = new SwingWorker<>() {

            /*
             * Executes a background thread using `SwingWorker`.
             *
             * This thread should **not** update the UI directly; only the main Swing thread
             * should handle UI updates to ensure thread safety. UI updates are managed
             * through the `done()` and `process()` methods of the SwingWorker class.
             *
             * This method may throw an `Exception`, which will be caught as an
             * `ExecutionException` inside the `done()` method. Additionally, if this
             * thread is interrupted (e.g., while calling `Thread.sleep()`), it will
             * throw an `InterruptedException`, which will also be handled in `done()`.
             *
             * Note: Search about if manually checking is needed for the
             * InterruptedException and Thread.sleep() inside of `doInBackground()`.
             *
             * Answer: In SwingWorker `doInBackground`, manual interruptions are not
             * strictly required because calling `Thread.sleep()` inside `doInBackground()`
             * will automatically throw an `InterruptedException` if the thread is interrupted.
             * However, manually checking for interruption can still be useful if the task
             * involves long-running loops or computations that don't call methods
             * like Thread.sleep().
             */
            @Override
            protected Boolean doInBackground() throws Exception {
                // Note: the UI should not be updated inside this thread!
                for (int i = 0; i < 30; i++) {
                    Thread.sleep(100);
                    System.out.println("Cycle: " + i);  // simulates some work
                    publish(i); // This method sends the data to be process by the process() method.
                }
                return true;
            }

            /*
             * The `done()` method is called when `doInBackground()` has finished executing.
             * This method runs on the Event Dispatch Thread (EDT), so it is safe to update
             * the UI here.
             * In this implementation, it updates the text of the `JLabel` to display
             * "Task completed! Status: " followed by the result obtain using `get()`.
             */
            @Override
            protected void done() {
                try {
                    Boolean status = get();
                    statusLabel.setText("Task completed! Status: " + status);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            /*
             * This method receives data from the `publish()` method and is used
             * to safely update the UI, such as updating a progress bar or a label.
             *
             * The parameter `chunks` is a `List<Integer>` because `publish(i)` can
             * send multiple pieces of data over time, not necessarily all at once.
             * The method process data in chunks, but in this case, only the last
             * value in the list is used to update the `JLabel` with the message
             * "Current value: " followed by the value.
             */
            @Override
            protected void process(List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1); // return the last value on the list.
                progressLabel.setText("Current value: " + value);
            }
        };

        worker.execute();
        /* `SwingWorker` can only be executed once. Calling `execute()`
         * again will not run `doInBackground` a second time. To run the
         * code again, a new instance of `SwingWorker` must be created.
         */

        //`worker.cancel()` can be called to interrupt the thread executed by `worker.execute()`.
    }
}
