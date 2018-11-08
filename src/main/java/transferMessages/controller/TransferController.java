package transferMessages.controller;

import transferMessages.transfer.Msg;

import java.util.List;

public interface Controller {
    void writeMsgs(List<? extends Msg> msgs);
    List<? extends Msg> readMsgs();
}
