package transferMessages.controller;

import transferMessages.transfer.Msg;

import java.util.List;

public interface TransferController {
    void writeMsgs(List<? extends Msg> msgs);
    List<? extends Msg> readMsgs();
}
