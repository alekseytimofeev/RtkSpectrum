package transferMessages;

public class UcanConnectHandler extends UcanConnectCallback {

    private TransferMsgs handler;

    public UcanConnectHandler() {
    }

    public UcanConnectHandler(TransferMsgs handler) {
        this.handler = handler;
    }

    public void setHandler(TransferMsgs handler) {
        this.handler = handler;
    }

    @Override
    void onEventConnect() {
    }

    @Override
    void onEventDisconnect() {
    }

    @Override
    void onEventFatalDisconnect() {
    }
}