package examples.re_entrant_locks.lock;

public class App {

    public static void main(String[] args) throws InterruptedException {
        final Runner runner = new Runner();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runner.firstProcess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runner.secondProcess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        runner.finishedNotification();
    }


}
