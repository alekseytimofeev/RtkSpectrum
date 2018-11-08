package transferMessages.callbacks;

import transferMessages.transfer.TransferMsgs;

public class UcanConnectHandler extends UcanConnectCallback
{

    private final TransferMsgs handler;

    public UcanConnectHandler(TransferMsgs handler) {
        this.handler = handler;
    }

    @Override
    void onEventConnect() {
        System.out.println("onEventConnect " + Thread.currentThread().getName());
    }

    @Override
    void onEventDisconnect() {
        System.out.println("onEventDisconnect");
    }

    @Override
    void onEventFatalDisconnect() {
        System.out.println("onEventFatalDisconnect");
    }
}