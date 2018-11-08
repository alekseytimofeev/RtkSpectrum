package transferMessages;

import com.sun.jna.platform.win32.WinDef;
import org.junit.Before;
import org.junit.Test;
import transferMessages.callbacks.UcanCallback;

import static org.junit.Assert.assertEquals;
import static transferMessages.controller.Features.Event.USBCAN_EVENT_DEINITCAN;
import static transferMessages.controller.Features.Event.USBCAN_EVENT_INITHW;
import static transferMessages.controller.Features.Event.USBCAN_EVENT_RECEIVE;

public class UcanCallbackTest {

    private EventsUcan eventsUcan;

    @Before
    public void setUp() {
        eventsUcan = new EventsUcan();
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_RECEIVE(){
        eventsUcan.callback(null, new WinDef.DWORD(events.get(USBCAN_EVENT_RECEIVE)), null, null);
        assertEquals("onEventReceive", eventsUcan.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEvenUSBCAN_EVENT_INITHW(){
        eventsUcan.callback(null, new WinDef.DWORD(events.get(USBCAN_EVENT_INITHW)), null, null);
        assertEquals("onEventInitHw", eventsUcan.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_INITCAN(){
        eventsUcan.callback(null, new WinDef.DWORD(events.get(USBCAN_EVENT_INITCAN)), null, null);
        assertEquals("onEventInitCan", eventsUcan.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_STATUS(){
        eventsUcan.callback(null, new WinDef.DWORD(events.get(USBCAN_EVENT_STATUS)), null, null);
        assertEquals("onEventStatus", eventsUcan.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_DEINITCAN(){
        eventsUcan.callback(null, new WinDef.DWORD(events.get(USBCAN_EVENT_DEINITCAN)), null, null);
        assertEquals("onEventDeInitCan", eventsUcan.getNameCallbackMethod());
    }

    @Test
    public void getCallbackByEventUSBCAN_EVENT_DEINITHW(){
        eventsUcan.callback(null, new WinDef.DWORD(events.get(USBCAN_EVENT_DEINITHW)), null, null);
        assertEquals("onEventDeInitHw", eventsUcan.getNameCallbackMethod());
    }


    public static class EventsUcan extends UcanCallback
    {

        private String nameCallbackMethod;

        String getNameCallbackMethod() {
            return nameCallbackMethod;
        }

        @Override
        void onEventReceive() {
            nameCallbackMethod = "onEventReceive";
        }

        @Override
        void onEventInitHw() {
            nameCallbackMethod = "onEventInitHw";
        }

        @Override
        void onEventInitCan() {
            nameCallbackMethod = "onEventInitCan";
        }

        @Override
        void onEventStatus() {
            nameCallbackMethod = "onEventStatus";
        }

        @Override
        void onEventDeInitCan() {
            nameCallbackMethod = "onEventDeInitCan";
        }

        @Override
        void onEventDeInitHw() {
            nameCallbackMethod = "onEventDeInitHw";
        }
    }
}
