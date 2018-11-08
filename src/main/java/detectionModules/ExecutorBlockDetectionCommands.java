package detectionModules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import detectionModules.BlockDetectionCommands.BlockDetectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorBlockDetectionCommands {

    private static Object sync = new Object();
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    private static Logger logger = LoggerFactory.getLogger(ExecutorBlockDetectionCommands.class);

    private ExecutorBlockDetectionCommands() {
    }

    public static void addCommand(BlockDetectionCommand command) {
        logger.info("Execution: " + command.toString());
        pool.submit(()-> command.execute());
    }

    public static void shutDown() {
        pool.shutdown();
    }


}
