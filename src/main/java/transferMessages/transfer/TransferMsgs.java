package transferMessages;

import java.util.List;

public interface TransferMsgs {
    void addToReceiveMsgs();
    List<? extends Msg> subFromReceiveMsgs();
    void addToTransmitMsgs(List<? extends Msg> msg);
    void subFromTransmitMsgs();
}
