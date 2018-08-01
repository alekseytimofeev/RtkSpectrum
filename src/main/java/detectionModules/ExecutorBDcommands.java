package detectionModules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorBDcommands  {

    private static Object sync = new Object();
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    private ExecutorBDcommands() {
    }

    public static void addComand(BDcommand command) {
        synchronized(sync) {
            pool.submit(()->command.execute());
        }
    }

    public static void shutDown() {
        pool.shutdown();
    }
}
