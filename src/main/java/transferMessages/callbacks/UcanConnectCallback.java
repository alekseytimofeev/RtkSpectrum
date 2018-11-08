package transferMessages.callbacks;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.*;

import static transferMessages.controller.Features.Event.*;
import static transferMessages.controller.Features.events;

public abstract class UcanConnectCallback implements Callback {

    abstract void onEventConnect();
    abstract void onEventDisconnect();
    abstract void onEventFatalDisconnect();

    //void PUBLIC UcanConnectControlFktEx(DWORD dwEvent_p, DWORD dwParam_p, void* pArg_p)
    public  void callback(DWORD event, DWORD param, Pointer arg) {

        if(event.intValue() 		== events.get(USBCAN_EVENT_CONNECT)) {
            onEventConnect();
        }
        else if(event.intValue()	== events.get(USBCAN_EVENT_DISCONNECT)) {
            onEventDisconnect();
        }
        else if(event.intValue() 	== events.get(USBCAN_EVENT_FATALDISCON)) {
            onEventFatalDisconnect();
        }
    }
}