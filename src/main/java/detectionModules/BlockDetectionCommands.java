package detectionModules;

import detectionModules.Feature.Parameter;
import detectionModules.Feature.State;

public class BDcommands {

    private BDcommands() {
    }

    public static abstract class BDcommand {
        protected abstract void execute();
    }

    public static class SetLogicalNumber extends BDcommand{
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

    public static class GetParameter  extends BDcommand{
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

    public static class SetParameter extends BDcommand {
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

    public static class StartMeasure extends BDcommand {
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

    public static class StopMeasure extends BDcommand {
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

    public static class Calibration extends BDcommand {
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

    public static class SetState extends BDcommand {
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

    public static class ReadMessage extends BDcommand {
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
}

