package detectionModules;

import transferMessages.transfer.Msg;
import transferMessages.controller.UcanLibrary.UcanMsg;
import transferMessages.transfer.TransferMsgs;

import java.nio.ByteBuffer;
import java.util.*;


import static detectionModules.Feature.*;
import static detectionModules.Feature.Command.*;
import static detectionModules.Feature.TypeMsg.DANGER;
import static detectionModules.Feature.TypeMsg.MANAGEMENT;
import static detectionModules.Feature.TypeMsg.REPLY;
import static halper.Ansi.ANSI_BLUE;
import static halper.Ansi.ANSI_RESET;

public abstract class BDcontroller {

    protected TransferMsgs transferMsg;

    private int idDanger = 0x0;
    private int idManagement = 0x100;
    private int idReply = 0x200;

    private int spectrCount = 0;
    MeasureData measureData;

    public BDcontroller(TransferMsgs transferMsg) {
        this.transferMsg = transferMsg;
        Parsing.setId(idDanger, idManagement, idReply);
    }

    public void setTransferCanMsg(TransferMsgs transferCanMsg) {
        this.transferMsg = transferCanMsg;
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
        if(spectrCount == 0) {
            Parsing parsing = new Parsing().parsingMsg(msg);
            byte commandCode = parsing.getCommandCode();
            TypeMsg typeMsg = parsing.getTypeMsg();

            if(commandCode == commandsCodes.get(SET_LOGIC_NUMBER)) {
                if(typeMsg == REPLY) {
                    byte logicNumber = parsing.getLogicNumber();
                }
            }
            else if(commandCode == commandsCodes.get(SET_STATE)) {
                if(typeMsg == DANGER) {
                    int serialNumber = parsing.getSerialNumber();
                    onNewBD(serialNumber);
                }
                else if(typeMsg == MANAGEMENT) {
                    byte stateCode = parsing.getStateCode();
                }
                else if(typeMsg == REPLY) {
                    byte operatingModeCode = parsing.getOperatingModeCode();
                }
            }
            else if(commandCode == commandsCodes.get(SET_PARAMETER)) {
                if(typeMsg == MANAGEMENT || typeMsg == REPLY) {
                    onNewParameter(parsing.getParameterCode(), parsing.getParameterValue());
                }
            }
            else if(commandCode == commandsCodes.get(GET_PARAMETER)) {
            }
            else if(commandCode == commandsCodes.get(CALIBRATION)) {
            }
            else if(commandCode == commandsCodes.get(MEASURE)) {
                measureData = new MeasureData();
                spectrCount = parsing.getSpectrLenght()/4;
            }
            showInfo(parsing.getLog());
        }
        else {
            measureData.add(new Parsing().parsingMeasureMsg(msg));
            if(--spectrCount ==0){
                onNewMeasureData(measureData);
                measureData.data.clear();
            }

        }
    }

    //-----------------------------------------------------------------------------------------------
    public void readMsgs() {
        List<? extends Msg> msgs = transferMsg.subFromReceiveMsgs();
        for (int i=0; i<msgs.size(); i++){
            parsingMsg((UcanMsg) msgs.get(i));
        }
    }

    private void writeMsgs(List<UcanMsg> msgs) {
        parsingMsg(msgs.get(0));
        transferMsg.addToTransmitMsgs(msgs);
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
        System.out.println("-----------------------measure");
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

    public class MeasureData {
        public MeasureData() {
        }

        public void add(List<Short> data) {
            this.data.addAll(data);
        }

        List<Short> data = new ArrayList<>();
    }

    interface CalibrationData {
    }

    private void showInfo(String msg) {
        System.out.println(ANSI_BLUE + msg + ANSI_RESET);
    }
}
