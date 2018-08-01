package transferMessages;

import java.util.List;

public interface Controller {
    void writeMsgs(List<? extends Msg> msgs);
    List<? extends Msg> readMsgs();
}
