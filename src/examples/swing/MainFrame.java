package examples.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Represents the UI of the Swing application, what is shown in
 * the UI window and the main Swing thread.
 */
public class MainFrame extends JFrame {
    private final JLabel progressLabel;
    private final JLabel statusLabel;
    private final JButton startButton;

    public MainFrame(String title) {
        super(title);

        // Instantiate the window elements
        progressLabel = new JLabel("0");
        statusLabel = new JLabel("Task not completed");
        startButton = new JButton("Start");


        // Set the layout of the GUI window
        setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        layoutConstraints.fill = layoutConstraints.NONE;

        // add count1 label to GUI
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        add(progressLabel, layoutConstraints);

        // add status label to GUI
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 1;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        add(statusLabel, layoutConstraints);

        // add start button to GUI
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 2;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        add(startButton, layoutConstraints);
        // Action to execute when the button is clicked.
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent clickEvent) {
                startSwingThread();
            }
        });

        // GUI windows properties
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startSwingThread() {
        /*
          * If Void use as a <parameter> in SwingWorker, the return value of
          * doInBackground() method should be Void in the method signature and
          * null in the return statement. This is the simplest case for a
          * thread that just run code, returns no value, and no information
          * is updated in the UI.
          * The first <parameter> in SwingWorker represent the return value,
          * in this example is Boolean, but it can be any other datatype.
          * The second <parameter> in SwingWorker represent the value that is
          * used to update dynamically the UI.
         */
        SwingWorker<Boolean, Integer> worker = new SwingWorker<>() {

            /*
             * This method executes a thread by SwingWorker.
             * This thread should not be used to update the UI, only the main
             * Swing thread is able to update the UI in a safe manner, this
             * is done by calling the done() and process() method of the
             * SwingWorker class.
             * This method can be set to throw an Exception that will be caught by
             * the try/catch block inside the done() method as a ExecutionException.
             * Also, if this thread is interrupted, methods like Thread.sleep()
             * will throw an InterruptedException that will be caught by done() as well,
             * inside the catch block for InterruptedException.
             * Note: Search about if manually checking is needed for the
             * InterruptedException and Thread.sleep() inside of doInBackground().
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
             * {@code done()} is call when the thread {@code doInBackground()}
             * is finished. Inside this method you can safely update the UI.
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
             * This method receives the data from the publish() method and
             * can safely update the UI with like, for example, a progress bar.
             * The reason why the parameter of this method is a List<Integer>
             * is because there is no guarantee that the value returned by
             * publish(i) is in real time, it can be returned on pieces of
             * data or chunks.
             */
            @Override
            protected void process(List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1); // return the last value on the list.
                progressLabel.setText("Current value: " + value);
            }
        };

        worker.execute();
        // SwingWorker executes just one time, calling again execute()
        // will not run doInBackground() for a second time.
        // To running this code again you need a new instance of SwingWorker.

        // The method worker.cancel() can be call to interrupt the thread
        // executed by worker.execute()
    }
}
