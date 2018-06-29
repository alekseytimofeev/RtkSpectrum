import transferCanMessages.TransferCanMsgs;
import widget.Widget;

public class Main {
    public static void main(String[] args) {
        Widget.initialize(args);

        TransferCanMsgs transferCanMsgs = TransferCanMsgs.getInstance();
    }
}
