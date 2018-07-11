package executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import detectionModules.BDcommand;

public class ExecutorBDcommands  {

    private ExecutorService pool = Executors.newSingleThreadExecutor();

    private ExecutorBDcommands() {
    }

    public void addComand(BDcommand command) {
        Future[] tasks = new Future[10];
        pool.submit(()->);
    }

    public void shutDown() {
        pool.shutdown();
    }

}
