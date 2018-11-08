package detectionModules;

import detectionModules.Feature.Parameter;
import detectionModules.Feature.State;

import java.io.IOException;

public class BlockDetectionCommands {

    private BlockDetectionCommands() {
    }

    public static abstract class BlockDetectionCommand {
        protected abstract void execute();
    }

    public static class SetLogicalNumber extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private int serialNumber;
        private byte logicNumber;

        SetLogicalNumber(BlockDetectionController handler, int serialNumber, byte logicNumber) {
            this.handler = handler;
            this.serialNumber = serialNumber;
            this.logicNumber = logicNumber;
        }

        @Override
        protected void execute() {
            handler.setLogicalNumber(serialNumber, logicNumber);
        }

        @Override
        public String toString() {
            return String.format("Command SetLogicalNumber, serialNumber: %d, logicNumber: %d", serialNumber, logicNumber);
        }
    }

    public static class GetParameter  extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;
        private Parameter parameter;

        GetParameter(BlockDetectionController handler, byte logicNumber, Parameter parameter) {
            this.handler = handler;
            this.logicNumber = logicNumber;
            this.parameter = parameter;
        }

        protected void execute() {
            handler.getParameter(logicNumber, parameter);
        }

        @Override
        public String toString() {
            return String.format("Command GetParameter, logicNumber: %d, parameter: %s", logicNumber, parameter);
        }
    }

    public static class SetParameter extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;
        private Parameter parameter;
        private int value;

        SetParameter(BlockDetectionController handler, byte logicNumber, Parameter parameter, int value) {
            this.handler = handler;
            this.logicNumber = logicNumber;
            this.parameter = parameter;
            this.value = value;
        }

        @Override
        protected void execute() {
            handler.setParameter(logicNumber, parameter, value);
        }

        @Override
        public String toString() {
            return String.format("Command StartMeasure, logicNumber: %d, parameter: %s", logicNumber, parameter);
        }
    }

    public static class StartMeasure extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;

        public StartMeasure(BlockDetectionController handler, byte logicNumber) {
            this.handler = handler;
            this.logicNumber = logicNumber;
        }

        @Override
        protected void execute() {
            handler.measure(logicNumber, true);
        }

        @Override
        public String toString() {
            return String.format("Command StartMeasure, logicNumber: %d", logicNumber);
        }
    }

    public static class StopMeasure extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;

        public StopMeasure(BlockDetectionController handler, byte logicNumber) {
            this.handler = handler;
            this.logicNumber = logicNumber;
        }

        @Override
        protected void execute() {
            handler.measure(logicNumber, false);
        }

        @Override
        public String toString() {
            return String.format("Command StopMeasure, logicNumber: %d", logicNumber);
        }
    }

    public static class Calibration extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;

        public Calibration(BlockDetectionController handler, byte logicNumber) {
            this.handler = handler;
            this.logicNumber = logicNumber;
        }

        @Override
        protected void execute() {
            handler.calibration(logicNumber);
        }

        @Override
        public String toString() {
            return String.format("Command SetState, logicNumber: %d", logicNumber);
        }
    }

    public static class SetState extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;
        private State state;

        public SetState(BlockDetectionController handler, byte logicNumber, State state) {
            this.handler = handler;
            this.logicNumber = logicNumber;
            this.state = state;
        }

        @Override
        protected void execute() {
            handler.setState(logicNumber, state);
        }

        @Override
        public String toString() {
            return String.format("Command SetState, logicNumber: %d, state: %s", logicNumber, state.toString());
        }
    }

    public static class ReadMessage extends BlockDetectionCommand {
        private BlockDetectionController handler;

        public ReadMessage(BlockDetectionController handler) {
            this.handler = handler;
        }

        @Override
        protected void execute() {
            handler.readMsgs();
        }

        @Override
        public String toString() {
            return String.format("Command ReadMessage");
        }
    }

    public static class ImportMeasureData extends BlockDetectionCommand {
        private BlockDetectionController handler;
        private byte logicNumber;

        public ImportMeasureData(BlockDetectionController handler, byte logicNumber) {
            this.handler = handler;
            this.logicNumber = logicNumber;
        }

        @Override
        protected void execute() {
            try
            {
                handler.importMeasureData(logicNumber);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return String.format("Command ImportMeasureData, logicNumber: %d", logicNumber);
        }
    }
}

