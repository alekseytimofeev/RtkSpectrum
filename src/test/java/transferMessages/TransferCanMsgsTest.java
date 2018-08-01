package transferMessages;

import com.sun.jna.platform.win32.WinDef;
import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.sun.jna.platform.win32.WinDef.BYTE;
import com.sun.jna.platform.win32.WinDef.DWORD;

import org.junit.Test;
import transferMessages.UcanLibrary.UcanMsg;

public class TransferCanMsgsTest {

    private TransferCanMsgs transfer;
    private Controller controller;
    private List<Msg> list = initMsgs(10);

    @Before
    public void setUp() {
        initControllerMock(list);
        transfer = new TransferCanMsgs(controller);
    }

    private List<Msg> initMsgs(int count) {
        List<Msg> list = new ArrayList<>();
        Random rnd = new Random();
        for (int i=0; i<count; i++) {
            UcanMsg msg = new UcanMsg();
            msg.m_dwID = new DWORD( rnd.nextInt(100));
            msg.m_bData0 = new BYTE( 10*i + 1);
            msg.m_bData1 = new BYTE( 10*i + 2);
            msg.m_bData2 = new BYTE( 10*i + 3);
            msg.m_bData3 = new BYTE( 10*i + 4);
            msg.m_bData4 = new BYTE( 10*i + 5);
            msg.m_bData5 = new BYTE( 10*i + 6);
            msg.m_bData6 = new BYTE( 10*i + 6);
            msg.m_bData6 = new BYTE( 10*i + 7);
            list.add(msg);
        }
        return list;
    }

    private void initControllerMock(List<Msg> list) {
        controller = mock(Controller.class);
        doNothing().when(controller).writeMsgs(any(List.class));
        when(controller.readMsgs()).thenAnswer(answer->list);
    }

    @Test
    public void queueReceiveMsgsCheck() {
        transfer.addToReceiveMsgs();
        List<? extends Msg> msgs = transfer.subFromReceiveMsgs();
        for (int i=0; i<list.size(); i++)
            Assert.assertEquals(list.get(i), msgs.get(i));
    }

    @Test
    public void queueTransmitMsgsCheck() {
        transfer.addToTransmitMsgs(list);
        transfer.subFromTransmitMsgs();
        verify(controller, times(1)).writeMsgs(any(List.class));
    }
}
