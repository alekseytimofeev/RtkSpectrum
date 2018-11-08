package transferMessages;

public class UcanHandler extends UcanCallback {

    private TransferMsgs handler;

    public UcanHandler() {
    }

    public UcanHandler(TransferMsgs transfer) {
        this.handler = handler;
    }

    public void setHandler(TransferMsgs handler) {
        this.handler = handler;
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