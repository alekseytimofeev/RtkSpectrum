package detectionModules;

import detectionModules.BDcontroller.*;

abstract class BDcommand {
    protected abstract void execute();
}

class GetParameter extends BDcommand {
    private BDcontroller handler;
    private byte logicNumber;
    private Parameter parameter;

    GetParameter(BDcontroller handler, byte logicNumber, Parameter parameter) {
        this.handler = handler;
        this.logicNumber = logicNumber;
        this.parameter = parameter;
    }

    @Override
    protected void execute() {
        handler.getParameter(logicNumber, parameter);
    }
}

class SetParameter extends BDcommand {
    private BDcontroller handler;
    private byte logicNumber;
    private Parameter parameter;
    private int value;

    SetParameter(BDcontroller handler, byte logicNumber, Parameter parameter, int value) {
        this.handler = handler;
        this.logicNumber = logicNumber;
        this.parameter = parameter;
        this.value = value;
    }

    @Override
    protected void execute() {
        handler.setParameter(logicNumber, parameter, value);
    }
}

class StartMeasure extends BDcommand {
    private BDcontroller handler;
    private byte logicNumber;

    public StartMeasure(BDcontroller handler, byte logicNumber) {
        this.handler = handler;
        this.logicNumber = logicNumber;
    }

    @Override
    protected void execute() {
        handler.measure(logicNumber, true);
    }
}

class StopMeasure extends BDcommand {
    private BDcontroller handler;
    private byte logicNumber;

    public StopMeasure(BDcontroller handler, byte logicNumber) {
        this.handler = handler;
        this.logicNumber = logicNumber;
    }

    @Override
    protected void execute() {
        handler.measure(logicNumber, false);
    }
}

class Calibration extends BDcommand {
    private BDcontroller handler;
    private byte logicNumber;

    public Calibration(BDcontroller handler, byte logicNumber) {
        this.handler = handler;
        this.logicNumber = logicNumber;
    }

    @Override
    protected void execute() {
        handler.calibration(logicNumber);
    }
}

class SetState extends BDcommand {
    private BDcontroller handler;
    private byte logicNumber;
    private State state;

    public SetState(BDcontroller handler, byte logicNumber, State state) {
        this.handler = handler;
        this.logicNumber = logicNumber;
        this.state = state;
    }

    @Override
    protected void execute() {
        handler.setState(logicNumber, state);
    }
}