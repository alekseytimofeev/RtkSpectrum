package detectionModules;

import transferMessages.Msg;
import transferMessages.TransferCanMsgs;
import transferMessages.UcanLibrary;
import transferMessages.UcanLibrary.UcanMsg;

import java.nio.ByteBuffer;
import java.util.*;


import static detectionModules.BDcontroller.OperatingMode.*;
import static detectionModules.BDcontroller.Parameter.*;
import static detectionModules.BDcontroller.State.*;
import static detectionModules.BDcontroller.Command.*;
import static detectionModules.BDcontroller.TypeMsg.*;


public abstract class BDcontroller {

    static {
        initParameters();
        initOperatingModes();
        initStates();
        initCommands();
        //TODO check initParameters, initOperatingsMode ...
    }

    TransferCanMsgs transferCanMsg;

    private int idDanger = 0x0;
    private int idManagement = 0x100;
    private int idReply = 0x200;

    Parsing parsing = new Parsing(idDanger, idManagement, idReply);

    public enum Parameter {
        ZERO_OFFSET, SENSITIVITY,
        AMPLITUDE_LIGHT_RAPPER,
        ACCUMULATION_INTERVAL,
        TEMPERATURE,
        LENGTH_SPECTRUM  //1-1024, 2-512, 3-256, 4-128
    }
    public enum OperatingMode {
        WAITING,
        BALANCING,
        CALIBRATION_ROUGHLY,
        CALIBRATION_EXACTLY,
        STABILIZATION_ROUGHLY,
        STABILIZATION_EXACTLY,
        SPECTRUM
    }
    public enum State {
        RESTART,
        RESET_CURRENT_COMMAND,
        GET_OPERATING_MODE
    }
    public enum Command {
        SET_LOGIC_NUMBER,
        SET_STATE,
        SET_PARAMETER,
        GET_PARAMETER,
        CALIBRATION,
        MEASURE
    }
    public enum TypeMsg {
        DANGER,
        MANAGEMENT,
        REPLY,
        UNKNOWN,
    }

    public static Map<Parameter, Byte>      parameterCodes;
    public static Map<Byte, String>         parameterNames;
    public static Map<OperatingMode, Byte>  operatingModesCodes;
    public static Map<Byte, String>         operatingModesNames;
    public static Map<State, Byte>          statesCodes;
    public static Map<Byte, String>         statesNames;
    public static Map<Command, Byte>        commandsCodes;
    public static Map<Byte, String>         commandsNames;

    private static void initParameters() {
        parameterCodes = new HashMap<>();
        parameterNames = new HashMap<>();

        byte code = 1;
        parameterCodes.put(ZERO_OFFSET, code);
        parameterNames.put(code, "Смещение нуля");
        code = 2;
        parameterCodes.put(SENSITIVITY, code);
        parameterNames.put(code, "Чувствительность");
        code = 3;
        parameterCodes.put(AMPLITUDE_LIGHT_RAPPER, code);
        parameterNames.put(code, "Амплитуда светового репера");
        code = 4;
        parameterCodes.put(ACCUMULATION_INTERVAL, code);
        parameterNames.put(code, "Интервал накопления");
        code = 5;
        parameterCodes.put(TEMPERATURE, code);
        parameterNames.put(code, "Температура");
        code = 6;
        parameterCodes.put(LENGTH_SPECTRUM, code);
        parameterNames.put(code, "Длина спектра");
    }
    private static void initOperatingModes() {
        operatingModesCodes = new HashMap<>();
        operatingModesNames = new HashMap<>();

        byte code = 1;
        operatingModesCodes.put(WAITING, code);
        operatingModesNames.put(code, "Ожидание команды от АРМ");
        code = 2;
        operatingModesCodes.put(BALANCING, code);
        operatingModesNames.put(code, "Балансировка нуля АЦП формирователя МДГ-С");
        code = 3;
        operatingModesCodes.put(CALIBRATION_ROUGHLY, code);
        operatingModesNames.put(code, "Регулировка чувствительности МДГ-С по алгоритму с двумя наложенными окнами");
        code = 4;
        operatingModesCodes.put(CALIBRATION_EXACTLY, code);
        operatingModesNames.put(code, "Регулировка чувствительности МДГ-С по алгоритму с двумя дифференциальными окнами");
        code = 5;
        operatingModesCodes.put(STABILIZATION_ROUGHLY, code);
        operatingModesNames.put(code, "Регулировка тока светодиода МДГ-С по алгоритму с заданным порогом");
        code = 6;
        operatingModesCodes.put(STABILIZATION_EXACTLY, code);
        operatingModesNames.put(code, "Регулировка тока светодиода МДГ-С по алгоритму с двумя дифференциальными окнами");
        code = 7;
        operatingModesCodes.put(SPECTRUM, code);
        operatingModesNames.put(code, "Автоматическое накопление и передача спектра");
    }
    private static void initStates() {
        statesCodes = new HashMap<>();
        statesNames = new HashMap<>();

        byte code = 1;
        statesCodes.put(RESTART, code);
        statesNames.put(code, "Перезапуск МДГ-С");
        code = 2;
        statesCodes.put(RESET_CURRENT_COMMAND, code);
        statesNames.put(code,"Сброс текущей команды МГД-С");
        code = 3;
        statesCodes.put(GET_OPERATING_MODE, code);
        statesNames.put(code, "Запрос состояния");
    }
    private static void initCommands() {
        commandsCodes = new HashMap<>();
        commandsNames = new HashMap<>();

        byte code = 0;
        commandsCodes.put(SET_LOGIC_NUMBER, code);
        commandsNames.put(code, "Задание логического номера");
        code = 1;
        commandsCodes.put(SET_STATE, code);
        commandsNames.put(code, "Задание состояния");
        code = 2;
        commandsCodes.put(SET_PARAMETER, code);
        commandsNames.put(code, "Задание параметра");
        code = 4;
        commandsCodes.put(GET_PARAMETER, code);
        commandsNames.put(code , "Запрос параметра");
        code = 6;
        commandsCodes.put(CALIBRATION, code);
        commandsNames.put(code, "Калибровка");
        code = 7;
        commandsCodes.put(MEASURE, code);
        commandsNames.put(code, "Измерение спетра");
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

    private void parsingMsg(UcanLibrary.UcanMsg msg) {
        parsing.parsingMsg(msg);
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



    //------------------------

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

    public void readMsgs() {
        List<? extends Msg> msgs = transferCanMsg.subFromReceiveMsgs();
        for (Msg msg: msgs)
            parsingMsg((UcanMsg) msg);
    }

    private void writeMsgs(List<UcanLibrary.UcanMsg> msgs) {
        parsingMsg(msgs.get(0));
        transferCanMsg.addToTransmitMsgs(msgs);
    }

    protected void setLogicalNumber(int serialNumber, byte logicNumber) {
        byte[] bytesSerialNumber = ByteBuffer.allocate(4).putInt(serialNumber).array();
        List<UcanLibrary.UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanLibrary.UcanMsg(idManagement,
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
        List<UcanLibrary.UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanLibrary.UcanMsg(idManagement + logicNumber,
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
        List<UcanLibrary.UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanLibrary.UcanMsg(idManagement + logicNumber,
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
        List<UcanLibrary.UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanLibrary.UcanMsg(idManagement + logicNumber,
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
        msgs.add(new UcanLibrary.UcanMsg(idManagement + logicNumber,
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
        List<UcanLibrary.UcanMsg> msgs = new ArrayList<>();
        msgs.add(new UcanLibrary.UcanMsg(idManagement + logicNumber,
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
