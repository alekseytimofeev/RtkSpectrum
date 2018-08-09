package detectionModules;

import transferMessages.Msg;
import transferMessages.TransferCanMsgs;
import transferMessages.UcanLibrary;
import transferMessages.UcanLibrary.UcanMsg;

import java.nio.ByteBuffer;
import java.util.*;


import static detectionModules.Feature.*;
import static detectionModules.Feature.Command.*;
import static detectionModules.Feature.TypeMsg.DANGER;
import static detectionModules.Feature.TypeMsg.MANAGEMENT;
import static detectionModules.Feature.TypeMsg.REPLY;

public abstract class BDcontroller {

    TransferCanMsgs transferCanMsg;

    private int idDanger = 0x0;
    private int idManagement = 0x100;
    private int idReply = 0x200;


    public BDcontroller() {
        Parsing.setId(idDanger, idManagement, idReply);
    }

    private byte getCodeLengthByParameter(Parameter parameter) {
        switch(parameter) {
            case ZERO_OFFSET:
                return 1;
            case SENSITIVITY:
                return 1;
            case AMPLITUDE_LIGHT_RAPPER:
                return 1;
            case ACCUMULATION_INTERVAL:
                return 1;
            case TEMPERATURE:
                return 0;
            case LENGTH_SPECTRUM:
                return 0;
        }
        return -1;
    }

    private void parsingMsg(UcanMsg msg) {
        Parsing parsing = new Parsing().parsingMsg(msg);
        byte commandCode = parsing.getCommandCode();
        TypeMsg typeMsg = parsing.getTypeMsg();

        if(commandCode == commandsCodes.get(SET_LOGIC_NUMBER)) {
            if(typeMsg == REPLY) {
                byte logicNumber = parsing.getLogicNumber();
            }
            System.out.println(parsing.getLog());
        }
        else if(commandCode == commandsCodes.get(SET_STATE)) {
            if(typeMsg == DANGER) {
                int serialNumber = parsing.getSerialNumber();
                System.out.println(parsing.getLog());
                onNewBD(serialNumber);
            }
            else if(typeMsg == MANAGEMENT) {
                byte stateCode = parsing.getStateCode();
                System.out.println(parsing.getLog());
            }
            else if(typeMsg == REPLY) {
                byte operatingModeCode = parsing.getOperatingModeCode();
                System.out.println(parsing.getLog());
            }
        }
        else if(commandCode == commandsCodes.get(SET_PARAMETER)) {
            if(typeMsg == MANAGEMENT || typeMsg == REPLY) {
                System.out.println(parsing.getLog());
                onNewParameter(parsing.getParameterCode(), parsing.getParameterValue());
            }
        }
        else if(commandCode == commandsCodes.get(GET_PARAMETER)) {
            System.out.println(parsing.getLog());
        }
        else if(commandCode == commandsCodes.get(CALIBRATION)) {
            System.out.println(parsing.getLog());
        }
        else if(commandCode == commandsCodes.get(MEASURE)) {
            System.out.println(parsing.getLog());
        }
    }



    //-----------------------------------------------------------------------------------------------------------

    public BDcommand getCommandGetParameter(byte logicNumber, Parameter parameter) {
        return new GetParameter(this, logicNumber, parameter);
    }

    public BDcommand getCommandSetParameter(byte logicNumber, Parameter parameter, int value) {
        return new SetParameter(this, logicNumber, parameter, value);
    }

    public BDcommand getCommandSetState(byte logicNumber, State state) {
        return new SetState(this, logicNumber, state);
    }

    public BDcommand setCommandStartMeasure(byte logicNumber) {
        return new StartMeasure(this, logicNumber);
    }

    public BDcommand setCommandStopMeasure(byte logicNumber) {
        return new StopMeasure(this, logicNumber);
    }


    //-----------------------------------------------------------------------------------------------
    public void readMsgs() {
        List<? extends Msg> msgs = transferCanMsg.subFromReceiveMsgs();
        for (Msg msg: msgs)
            parsingMsg((UcanMsg) msg);
    }

    private void writeMsgs(List<UcanMsg> msgs) {
        parsingMsg(msgs.get(0));
        transferCanMsg.addToTransmitMsgs(msgs);
    }

    protected void setLogicalNumber(int serialNumber, byte logicNumber) {
        byte[] bytesSerialNumber = ByteBuffer.allocate(4).putInt(serialNumber).array();
        List<UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanMsg(idManagement,
                    new byte[] {
                        commandsCodes.get(SET_LOGIC_NUMBER),
                        logicNumber,
                        0,
                        0,
                        bytesSerialNumber[0],
                        bytesSerialNumber[1],
                        bytesSerialNumber[2],
                        bytesSerialNumber[3]}));
        writeMsgs(msgs);
    }

    protected void setState(byte logicNumber, State state) {
        List<UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanMsg(idManagement + logicNumber,
                    new byte[] {
                        commandsCodes.get(SET_STATE),
                        statesCodes.get(state),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0}));
        writeMsgs(msgs);
    }

    protected void setParameter(byte logicNumber, Parameter parameter, int value) {
        byte[] bytesValue = ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
        List<UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanMsg(idManagement + logicNumber,
                    new byte[] {
                        commandsCodes.get(SET_PARAMETER),
                        parameterCodes.get(parameter),
                        getCodeLengthByParameter(parameter),
                        bytesValue[0],
                        bytesValue[1],
                        bytesValue[2],
                        bytesValue[3]}));
        writeMsgs(msgs);
    }

    protected void getParameter( byte logicNumber, Parameter parameter) {
        List<UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanMsg(idManagement + logicNumber,
                    new byte[] {
                        commandsCodes.get(GET_PARAMETER),
                        parameterCodes.get(parameter),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0}));
        writeMsgs(msgs);
    }

    protected void calibration(byte logicNumber){
        List<UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanMsg(idManagement + logicNumber,
                    new byte[] {
                        commandsCodes.get(CALIBRATION),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0}));
        writeMsgs(msgs);
    }

    protected void measure(byte logicNumber, boolean start) {
        List<UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanMsg(idManagement + logicNumber,
                    new byte[] {
                        commandsCodes.get(MEASURE),
                        (byte) (start ? 1 : 0),
                        0,
                        0,
                        0,
                        0,
                        0,
                        0}));
        writeMsgs(msgs);
    }

    protected abstract void onNewBD(int serialNumber);
    protected abstract void onNewParameter(byte parameter, int value);
    protected abstract void onNewParameter(byte parameter, float value);
    protected abstract void onNewMeasureData(MeasureData data);
    protected abstract void onNewMeasureData(CalibrationData data);

    interface MeasureData {
    }

    interface CalibrationData {
    }
}
