package transferMessages;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.*;

public abstract class UcanCallback implements Callback {

    abstract void onEventReceive();
    abstract void onEventInitHw();
    abstract void onEventInitCan();
    abstract void onEventStatus();
    abstract void onEventDeInitCan();
    abstract void onEventDeInitHw();

    //void PUBLIC UcanCallbackFktEx (BYTE UcanHandle_p, DWORD dwEvent_p, BYTE bChannel_p, void* pArg_p)
    public void callback(BYTE ucanHandle, DWORD event, BYTE channel, Pointer ptr) {
        if(event.intValue() 		== UсanController.events.get(UсanController.Event.USBCAN_EVENT_RECEIVE)) {
            onEventReceive();
        }
        else if (event.intValue() 	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_INITHW)) {
            onEventInitHw();
        }
        else if(event.intValue() 	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_INITCAN)) {
            onEventInitCan();
        }
        else if(event.intValue() 	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_STATUS)) {
            onEventStatus();
        }
        else if(event.intValue() 	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_DEINITCAN)) {
            onEventDeInitCan();
        }
        else if(event.intValue()	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_DEINITHW)) {
            onEventDeInitHw();
        }
    }
}