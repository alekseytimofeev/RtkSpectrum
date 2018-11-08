package detectionModules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import detectionModules.BlockDetectionCommands.BlockDetectionCommand;

import static halper.Ansi.ANSI_CYAN;
import static halper.Ansi.ANSI_RESET;

public class ExecutorBDcommands  {

    private static Object sync = new Object();
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    private ExecutorBDcommands() {
    }

    public static void addComand(BlockDetectionCommand command) {
        showInfo("Execution: " + command.toString());
        pool.submit(()->command.execute());
    }

    public static void shutDown() {
        pool.shutdown();
    }

    private static void showInfo(String msg) {
        System.out.println(ANSI_CYAN + msg + ANSI_RESET);
    }
}
