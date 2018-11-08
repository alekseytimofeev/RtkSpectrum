package detectionModules;


import transferMessages.controller.UcanLibrary.UcanMsg;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static detectionModules.Feature.*;
import static detectionModules.Feature.Command.*;
import static detectionModules.Feature.TypeMsg.*;

public class Parsing {

    private static int idDanger = -1;
    private static int idManagement = -1;
    private static int idReply = -1;

    private TypeMsg typeMsg = UNKNOWN;
    private byte commandCode = -1;
    private byte logicNumber = -1;
    private int serialNumber = -1;
    private byte operatingModeCode = -1;
    private byte stateCode = -1;
    private byte parameterCode = -1;
    private int parameterValue = -1;
    private int spectrLenght = -1;
    private byte stabilization = -1;

    private StringBuilder log = new StringBuilder();

    private static final String ERROR_INIT_IDS = String.format("Не инициализированы idDanger == %d, idManagement == %d, idManagement == %d",
                                                                idDanger, idManagement, idReply);
    private static final String ERROR_PARSING_UNKNOWN_TYPE = "Ошибка парсинга can сообщения. Не известный тип сообщения. ";
    private static final String ERROR_PARSING_UNKNOWN_COMMAND_CODE = "Ошибка парсинга can сообщения. Не известная команда ";
    private static final String ERROR_PARSING_NOT_VALUE_PARAMETER = "Парсинг не выделил данный параметр";

    public static void setId(int idD, int idM, int idR) {
        idDanger = idD;
        idManagement = idM;
        idReply = idR;
    }

    public Parsing parsingMsg(UcanMsg msg) {

        if(idDanger == -1 || idManagement == -1 || idReply == -1)
            throw new RuntimeException(ERROR_INIT_IDS);

        typeMsg = getTypeMsgById(msg.getId());
        if(typeMsg == UNKNOWN) {
            //throw new RuntimeException(ERROR_PARSING_UNKNOWN_TYPE +  msg);
        }

        commandCode = getCommandCode(msg.getData());
        logicNumber = getLogicNumberFromDataMsg(msg.getData()); //-----
        if(commandCode == commandsCodes.get(SET_LOGIC_NUMBER)) {
            parsingMsgBySetLogicNumber(typeMsg, msg);
        }
        else if(commandCode == commandsCodes.get(SET_STATE)) {
            parsingMsgBySetState(typeMsg, msg);
        }
        else if(commandCode == commandsCodes.get(SET_PARAMETER)) {
            parsingMsgBySetParameter(typeMsg, msg);
        }
        else if(commandCode == commandsCodes.get(GET_PARAMETER)) {
            parsingMsgByGetParameter(typeMsg, msg);
        }
        else if(commandCode == commandsCodes.get(CALIBRATION)) {
            parsingMsgByCalibration(typeMsg, msg);
        }
        else if(commandCode == commandsCodes.get(MEASURE)) {
            parsingMsgByMeasure(typeMsg, msg);
        }
        else {
            parsingMsgByUnknown(typeMsg, msg);
            //throw new IllegalArgumentException(ERROR_PARSING_UNKNOWN_COMMAND_CODE + " " + msg);
        }

        log.append(msg.getId() + "\t");
        log.append(Arrays.toString(msg.getData()) + "\t");
        return this;
    }

    public List<Short> parsingMeasureMsg(UcanMsg msg) {
        byte[] dataCanMsg = msg.getData();
        List<Short> list = new ArrayList<>();
        for(int i=0; i<dataCanMsg.length; i+=2)
            list.add(ByteBuffer.wrap(new byte[] {dataCanMsg[i], dataCanMsg[i+1]}).getShort());
        return list;
    }

    private TypeMsg getTypeMsgById(int id) { //TODO алгоритм!
        if(id >= idDanger && id < idDanger + 0x100) {
            log.append("BD --->\t");
            return DANGER;
        }
        else if(id >= idManagement && id < idManagement + 0x100) {
            log.append("BD <---\t");
            return MANAGEMENT;
        }
        else if(id >= idReply && id < idReply + 0x100) {
            log.append("BD --->\t");
            return REPLY;
        }
        else {
            log.append("?\t");
            return UNKNOWN;
        }
    }

    private byte getCommandCode(byte[] dataCanMsg) {
        byte commandCode = dataCanMsg[0];
        log.append(commandsNames.get(commandCode) + "\t");
        return commandCode;
    }

    private void parsingMsgBySetLogicNumber(TypeMsg typeMsg, UcanMsg msg) {
        byte[] dataCanMsg = msg.getData();
        if(typeMsg == REPLY) {
            logicNumber = getLogicNumberFromDataMsg(dataCanMsg);
        }
        addLogBySetLogicNumber(typeMsg, dataCanMsg);
    }

    private void parsingMsgBySetState(TypeMsg typeMsg, UcanMsg msg) {
        byte[] dataCanMsg = msg.getData();
        if(typeMsg == REPLY) {
            operatingModeCode = getOperatingModeCodeFromDataMsg(dataCanMsg);
        }
        else if(typeMsg == DANGER) {
            serialNumber = getSerialNumberFromDataMsg(dataCanMsg);
        }
        addLogBySetState(typeMsg, dataCanMsg);
    }

    private void parsingMsgBySetParameter(TypeMsg typeMsg, UcanMsg msg) {
        byte[] dataCanMsg = msg.getData();
        if( typeMsg == REPLY) {
            parameterCode = getParameterCodeFromDataMsg(dataCanMsg);
            parameterValue = getParameterValueFromDataMsg(dataCanMsg);
        }
        else if(typeMsg == DANGER) {
            stabilization = getStabilizationFromDataMsg(dataCanMsg);
        }
        addLogBySetParameter(typeMsg, dataCanMsg);
    }

    private void parsingMsgByGetParameter(TypeMsg typeMsg, UcanMsg msg) {
        byte[] dataCanMsg = msg.getData();
        addLogByGetParameter(typeMsg, dataCanMsg);
    }

    private void parsingMsgByCalibration(TypeMsg typeMsg, UcanMsg msg) {
        if(typeMsg == MANAGEMENT) {
        }
        else if(typeMsg == REPLY) {
        }
    }

    private void parsingMsgByMeasure(TypeMsg typeMsg, UcanMsg msg) {
        byte[] dataCanMsg = msg.getData();
        if(typeMsg == REPLY) {
            spectrLenght = 1024; //Todo какая то херь в протоколе!
        }
        addLogByMeasure(typeMsg, dataCanMsg);
    }

    private byte getLogicNumberFromDataMsg(byte[] dataCanMsg) {
        return dataCanMsg[1];
    }
    private int getSerialNumberFromDataMsg(byte[] dataCanMsg) {
        return ByteBuffer.wrap(new byte[] {dataCanMsg[4], dataCanMsg[5], dataCanMsg[6], dataCanMsg[7]}).getInt();
    }
    private void addLogBySetLogicNumber(TypeMsg typeMsg, byte[] dataCanMsg) {
        byte logicNumber = getLogicNumberFromDataMsg(dataCanMsg);
        int serialNumber = getSerialNumberFromDataMsg(dataCanMsg);
        if(typeMsg == MANAGEMENT) {
            log.append("Логический номер: " + logicNumber);
        }
        else if(typeMsg == REPLY) {
            log.append("Логический номер: " + logicNumber + " задан");
        }
        log.append(" (серийный номер: " + serialNumber +")\t");
    }

    private byte getStateCodeFromDataCanMsg(byte[] dataCanMsg) {
        return dataCanMsg[1];
    }
    private byte getOperatingModeCodeFromDataMsg(byte[] dataCanMsg) {
        return dataCanMsg[2];
    }
    private void addLogBySetState(TypeMsg typeMsg, byte[] dataCanMsg) {
        if(typeMsg == MANAGEMENT) {
            log.append("Состояние: " + statesNames.get(getStateCodeFromDataCanMsg(dataCanMsg)) + "\t");
        }
        else if(typeMsg == REPLY) {
            log.append("Режим: " + operatingModesNames.get(getOperatingModeCodeFromDataMsg(dataCanMsg)) + "\t");
        }
        else if(typeMsg == DANGER) {
            log .append("Включение в сеть БД (серийный номер: ")
                .append(getSerialNumberFromDataMsg(dataCanMsg) + ")\t");
        }
    }

    private byte getParameterCodeFromDataMsg(byte[] dataCanMsg) {
        return dataCanMsg[1];
    }
    private int getParameterValueFromDataMsg(byte[] dataCanMsg) {
        return ByteBuffer.wrap(new byte[] {dataCanMsg[4], dataCanMsg[5]}).getInt();
    }
    private byte getStabilizationFromDataMsg(byte[] dataCanMsg) {
        return dataCanMsg[1];
    }
    private void addLogBySetParameter(TypeMsg typeMsg, byte[] dataCanMsg) {
        if(typeMsg == MANAGEMENT) {
            log.append("Параметр: " + parameterNames.get(getParameterCodeFromDataMsg(dataCanMsg)) + " ");
            log.append("Значение: " + getParameterValueFromDataMsg(dataCanMsg));
        }
        else if(typeMsg == REPLY) {
            log.append("Параметр: " + parameterNames.get(getParameterCodeFromDataMsg(dataCanMsg)) + " задан. ");
            log.append("Значение: " + getParameterValueFromDataMsg(dataCanMsg));
        }
        else if(typeMsg == DANGER) {
            if(getStabilizationFromDataMsg(dataCanMsg) == 1)
                log.append("Стабилизирован!");
            else
                log.append("Не стабилизации!");
        }
    }
    private void addLogByGetParameter(TypeMsg typeMsg, byte[] dataCanMsg) {
        if(typeMsg == MANAGEMENT) {
            log.append("Параметр: " + parameterNames.get(getParameterCodeFromDataMsg(dataCanMsg)) + " ");
            log.append("Значение: " + getParameterValueFromDataMsg(dataCanMsg));
        }
    }

    private boolean isStartMeasure(byte[] dataCanMsg) {
        return dataCanMsg[1] == 1;
    }
    private void addLogByMeasure(TypeMsg typeMsg, byte[] dataCanMsg) {
        if(isStartMeasure(dataCanMsg)) {
            log.append("Старт измерения спекта ");
        }
        else {
            log.append("Стоп измерения спекта ");
        }
    }

    private void parsingMsgByUnknown(TypeMsg typeMsg, UcanMsg msg) {
        log.append("Неизвестная команда ");
    }




    public String getLog() {
        String str = log.toString();
        log.setLength(0);
        return str;
    }

    public byte getCommandCode() {
        if(commandCode == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return commandCode;
    }

    public TypeMsg getTypeMsg() {
        return typeMsg;
    }

    public byte getLogicNumber() {
        if(logicNumber == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return logicNumber;
    }

    public int getSerialNumber() {
        if(serialNumber == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return serialNumber;
    }

    public byte getOperatingModeCode() {
        if(operatingModeCode == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return operatingModeCode;
    }

    public byte getStateCode() {
        if(stateCode == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return stateCode;
    }

    public byte getParameterCode() {
        if(parameterCode == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return parameterCode;
    }

    public int getParameterValue() {
        if(parameterValue == -1)
            throw new IllegalStateException(ERROR_PARSING_NOT_VALUE_PARAMETER);
        return parameterValue;
    }

    public int getSpectrLenght() {
        return spectrLenght;
    }

    public byte getStabilization() {

        return stabilization;
    }
}
