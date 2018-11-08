package transferMessages;

import com.sun.jna.platform.win32.WinDef.*;
import org.junit.Before;
import org.junit.Test;
import transferMessages.callbacks.UcanConnectCallback;

import static org.junit.Assert.assertEquals;
import static transferMessages.controller.UсanController.Event.USBCAN_EVENT_CONNECT;
import static transferMessages.controller.UсanController.Event.USBCAN_EVENT_DISCONNECT;
import static transferMessages.controller.UсanController.Event.USBCAN_EVENT_FATALDISCON;
import static transferMessages.controller.UсanController.events;

public class UcanConnectCallbackTest {

    private Connect connect;

    @Before
    public void setUp() {
        connect = new Connect();
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_CONNECT(){
        connect.callback(new DWORD(events.get(USBCAN_EVENT_CONNECT)), null, null);
        assertEquals("onEventConnect", connect.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_DISCONNECT(){
        connect.callback(new DWORD(events.get(USBCAN_EVENT_DISCONNECT)), null, null);
        assertEquals("onEventDisconnect", connect.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_FATALDISCON(){
        connect.callback(new DWORD(events.get(USBCAN_EVENT_FATALDISCON)), null, null);
        assertEquals("onEventFatalDisconnect", connect.getNameCallbackMethod());
    }

    public static class Connect extends UcanConnectCallback
    {
        private String nameCallbackMethod;

        String getNameCallbackMethod() {
            return nameCallbackMethod;
        }

        @Override
        void onEventConnect() {
            nameCallbackMethod = "onEventConnect";
        }

        @Override
        void onEventDisconnect() {
            nameCallbackMethod = "onEventDisconnect";
        }

        @Override
        void onEventFatalDisconnect() {
            nameCallbackMethod = "onEventFatalDisconnect";
        }
    }
}
