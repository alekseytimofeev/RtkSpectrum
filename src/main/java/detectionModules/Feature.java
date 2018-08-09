package detectionModules;

import java.util.HashMap;
import java.util.Map;

import static detectionModules.Feature.Command.*;
import static detectionModules.Feature.OperatingMode.*;
import static detectionModules.Feature.Parameter.*;
import static detectionModules.Feature.State.GET_OPERATING_MODE;
import static detectionModules.Feature.State.RESET_CURRENT_COMMAND;
import static detectionModules.Feature.State.RESTART;

public class Feature {

    static {
        initParameters();
        initOperatingModes();
        initStates();
        initCommands();
    }

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

    static void initParameters() {
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

    static void initOperatingModes() {
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

    static void initStates() {
        statesCodes = new HashMap<>();
        statesNames = new HashMap<>();

        byte code = 1;
        statesCodes.put(RESTART, code);
        statesNames.put(code, "Перезапуск");
        code = 2;
        statesCodes.put(RESET_CURRENT_COMMAND, code);
        statesNames.put(code,"Сброс текущей команды");
        code = 4;
        statesCodes.put(GET_OPERATING_MODE, code);
        statesNames.put(code, "Запрос состояния");
    }

    static void initCommands() {
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
}
