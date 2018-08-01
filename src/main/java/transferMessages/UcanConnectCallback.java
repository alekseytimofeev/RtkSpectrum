package transferMessages;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.*;

public abstract class UcanConnectCallback implements Callback {

    abstract void onEventConnect();
    abstract void onEventDisconnect();
    abstract void onEventFatalDisconnect();

    //void PUBLIC UcanConnectControlFktEx(DWORD dwEvent_p, DWORD dwParam_p, void* pArg_p)
    public  void callback(DWORD event, DWORD param, Pointer arg) {
        if(event.intValue() 		== UсanController.events.get(UсanController.Event.USBCAN_EVENT_CONNECT)) {
            onEventConnect();
        }
        else if(event.intValue()	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_DISCONNECT)) {
            onEventDisconnect();
        }
        else if(event.intValue() 	== UсanController.events.get(UсanController.Event.USBCAN_EVENT_FATALDISCON)) {
            onEventFatalDisconnect();
        }
    }
}