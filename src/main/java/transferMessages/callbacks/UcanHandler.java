package transferMessages.callbacks;

import transferMessages.transfer.TransferMsgs;

public class UcanHandler extends UcanCallback {

    private final TransferMsgs handler;

    public UcanHandler(TransferMsgs transfer) {
        this.handler = transfer;
    }

    @Override
    void onEventReceive() {
        if(handler != null)
            handler.addToReceiveMsgs();
    }

    @Override
    void onEventInitHw() {
    }

    @Override
    void onEventInitCan() {
    }

    @Override
    void onEventStatus() {
    }

    @Override
    void onEventDeInitCan() {
    }

    @Override
    void onEventDeInitHw() {
    }
}