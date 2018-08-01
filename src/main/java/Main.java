import com.sun.jna.Native;
import transferMessages.*;

//@Configuration
//@ComponentScan(basePackages = "config")
public class Main {
    public static void main(String[] args) throws InterruptedException
    {
        //System.out.println("main " + Thread.currentThread().getName());
        //Widget.initialize(args);



        //System.out.println("---------------------");
        //System.out.println("main " + Thread.currentThread().getName());

        UcanHandler ucanHandler = new UcanHandler();
        UcanConnectHandler ucanConnectHandler = new UcanConnectHandler();
        Controller controller = new UсanController( Native.loadLibrary("Usbcan64", UcanLibrary.class),
                                                    ucanHandler,
                                                    ucanConnectHandler);

        TransferCanMsgs transferCanMsgs = new TransferCanMsgs(controller);
        ucanHandler.setHandler(transferCanMsgs);
        ucanConnectHandler.setHandler(transferCanMsgs);
    }
}
