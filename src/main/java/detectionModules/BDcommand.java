package detectionModules;

import detectionModules.BDcontroller.Parameter;

abstract class BDcommand
{
    protected abstract void execute();
}

class GetParameter extends BDcommand
{
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

class SetParameter extends BDcommand
{
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