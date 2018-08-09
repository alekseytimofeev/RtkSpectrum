package detectionModules;


import org.junit.Before;
import org.junit.Test;
import transferMessages.UcanLibrary.UcanMsg;


import java.nio.ByteBuffer;
import java.util.Arrays;


import static detectionModules.Feature.*;
import static detectionModules.Feature.Command.SET_LOGIC_NUMBER;
import static detectionModules.Feature.Command.SET_STATE;
import static detectionModules.Feature.OperatingMode.*;
import static detectionModules.Feature.State.GET_OPERATING_MODE;
import static detectionModules.Feature.State.RESET_CURRENT_COMMAND;
import static detectionModules.Feature.State.RESTART;
import static detectionModules.Feature.TypeMsg.DANGER;
import static detectionModules.Feature.TypeMsg.MANAGEMENT;
import static detectionModules.Feature.TypeMsg.REPLY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParsingTest {

    private static int idDanger = 0x0;
    private static int idManagement = 0x100;
    private static int idReply = 0x200;

    @Before
    public void setUI() {
        Parsing.setId(idDanger, idManagement, idReply);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkUnknownTypeMsg() {
        short id = 0x301; //todo и больше
        byte[] data = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);

        String log = parsing.getLog();
        System.out.println(log);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkUnknownCodeCommandMsg() {
        short id = 0x100; //todo и больше
        byte[] data = new byte[]{0x20, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);

        String log = parsing.getLog();
        System.out.println(log);
    }

    @Test
    public void checkDanderMsgBySET_STATE() {
        short id = 0x0;
        byte[] data = new byte[]{0x1, 0x1, 0x0, 0x0, 0x1, 0x4, 0x9, 0x78};
        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);

        String log = parsing.getLog();
        System.out.println(log);
        int expectedSerialNumber = ByteBuffer.wrap(new byte[] {data[4], data[5], data[6], data[7]}).getInt();
        assertTrue(log.contains(String.valueOf(id)));
        assertTrue(log.contains(Arrays.toString(data)));
        assertTrue(log.contains("Задание состояния"));
        assertTrue(log.contains("Включение в сеть БД (серийный номер: " + expectedSerialNumber + ")"));

        assertEquals(commandsCodes.get(SET_STATE).byteValue(), parsing.getCommandCode());

        assertEquals(DANGER, parsing.getTypeMsg());

        assertEquals(expectedSerialNumber,  parsing.getSerialNumber());
    }

    @Test
    public void checkManagementMsgBySET_STATE_restart() {
        checkManagementMsgBySET_STATE(RESTART);
    }

    @Test
    public void checkManagementMsgBySET_STATE_reset_command() {
        checkManagementMsgBySET_STATE(RESET_CURRENT_COMMAND);
    }

    @Test
    public void checkManagementMsgBySET_STATE_get_operating_mode() {
        checkManagementMsgBySET_STATE(GET_OPERATING_MODE);
    }

    private void checkManagementMsgBySET_STATE(State state) {
        byte logicNumber = 7;
        short id = (short) (0x100 + logicNumber);
        byte[] data = new byte[] { 0x1, statesCodes.get(state), 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};

        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);
        String log = parsing.getLog();
        System.out.println(log);

        assertTrue(log.contains(String.valueOf(id)));
        assertTrue(log.contains(Arrays.toString(data)));
        assertTrue(log.contains("Задание состояния"));
        assertTrue(log.contains("Состояние: " + statesNames.get(statesCodes.get(state))));

        assertEquals( commandsCodes.get(SET_STATE).byteValue(), parsing.getCommandCode());

        assertEquals(MANAGEMENT, parsing.getTypeMsg());
    }

    @Test
    public void checkReplyMsgBySET_STATE_waiting() {
        checkReplyMsgBySET_STATE(WAITING);
    }

    @Test
    public void checkReplyMsgBySET_STATE_balancing() {
        checkReplyMsgBySET_STATE(BALANCING);
    }

    @Test
    public void checkReplyMsgBySET_STATE_calibration_roughly() {
        checkReplyMsgBySET_STATE(CALIBRATION_ROUGHLY);
    }

    @Test
    public void checkReplyMsgBySET_STATE_calibration_exactly() {
        checkReplyMsgBySET_STATE(CALIBRATION_EXACTLY);
    }

    @Test
    public void checkReplyMsgBySET_STATE_stabilization_roughly() {
        checkReplyMsgBySET_STATE(STABILIZATION_ROUGHLY);
    }

    @Test
    public void checkReplyMsgBySET_STATE_stabilization_exactly() {
        checkReplyMsgBySET_STATE(STABILIZATION_EXACTLY);
    }

    @Test
    public void checkReplyMsgBySET_STATE_stabilization_spectrum() {
        checkReplyMsgBySET_STATE(SPECTRUM);
    }

    private void checkReplyMsgBySET_STATE(OperatingMode operatingMode) {
        byte logicNumber = 7;
        short id = (short) (0x200 + logicNumber);
        byte[] data = new byte[] { 0x1, 0x0, operatingModesCodes.get(operatingMode), 0x0, 0x0, 0x0, 0x0, 0x0};

        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);
        String log = parsing.getLog();
        System.out.println(log);

        assertTrue(log.contains(String.valueOf(id)));
        assertTrue(log.contains(Arrays.toString(data)));
        assertTrue(log.contains("Задание состояния"));
        assertTrue(log.contains("Режим: " + operatingModesNames.get(operatingModesCodes.get(operatingMode))));

        assertEquals( commandsCodes.get(SET_STATE).byteValue(), parsing.getCommandCode());

        assertEquals(REPLY, parsing.getTypeMsg());
    }

    @Test
    public void checkManagerMsgSET_LOGIC_NUMBER() {
        byte logicNumber = 7;
        short id = 0x100;
        byte[] data = new byte[] {0x0, logicNumber, 0x0, 0x0, 0x1, 0x4, 0x9, 0x78};
        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);

        String log = parsing.getLog();
        System.out.println(log);
        int expectedSerialNumber = ByteBuffer.wrap(new byte[] {data[4], data[5], data[6], data[7]}).getInt();

        assertTrue(log.contains(String.valueOf(id)));
        assertTrue(log.contains(Arrays.toString(data)));
        assertTrue(log.contains("Задание логического номера"));
        assertTrue(log.contains("Логический номер: " + logicNumber + " (серийный номер: " + expectedSerialNumber + ")"));

        assertEquals(commandsCodes.get(SET_LOGIC_NUMBER).byteValue(), parsing.getCommandCode());

        assertEquals(MANAGEMENT, parsing.getTypeMsg());
    }

    @Test
    public void checkDangerMsgSET_LOGIC_NUMBER() {
        byte logicNumber = 7;
        short id = (short)(0x200 + logicNumber);
        byte[] data = new byte[] {0x0, logicNumber, 0x0, 0x0, 0x1, 0x4, 0x9, 0x78};
        UcanMsg msg = new UcanMsg( id, data);
        Parsing parsing = new Parsing().parsingMsg(msg);
        String log = parsing.getLog();
        System.out.println(log);
        int expectedSerialNumber = ByteBuffer.wrap(new byte[] {data[4], data[5], data[6], data[7]}).getInt();

        assertTrue(log.contains(String.valueOf(id)));
        assertTrue(log.contains(Arrays.toString(data)));
        assertTrue(log.contains("Задание логического номера"));
        assertTrue(log.contains("Логический номер: " + logicNumber + " задан (серийный номер: " + expectedSerialNumber + ")"));

        assertEquals(commandsCodes.get(SET_LOGIC_NUMBER).byteValue(), parsing.getCommandCode());

        assertEquals(REPLY, parsing.getTypeMsg());
    }

}
