package context;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import detectionModules.BDMGcontroller;
import detectionModules.BlockDetectionController;
import transferMessages.callbacks.UcanCallback;
import transferMessages.callbacks.UcanConnectCallback;
import transferMessages.callbacks.UcanConnectHandler;
import transferMessages.callbacks.UcanHandler;
import transferMessages.controller.TransferController;
import transferMessages.controller.UcanLibrary;
import transferMessages.controller.UсanTransferController;
import transferMessages.transfer.TransferCanMsgs;
import transferMessages.transfer.TransferMsgs;
import widget.Widget;
import widget.controllers.RootController;

import java.lang.reflect.Field;

public class Context {
    UcanCallback ucanHandler = new UcanHandler(null);
    UcanConnectCallback ucanConnectHandler = new UcanConnectHandler(null);
    private final TransferController transferController = new UсanTransferController(
            Native.loadLibrary("Usbcan64", UcanLibrary.class), ucanHandler, ucanConnectHandler);

    private final TransferMsgs transferMsgs = new TransferCanMsgs(null, null);
    private final BlockDetectionController blockDetectionController = new BDMGcontroller(null);


    public Context(String[] args) {
        System.out.println("main " + Thread.currentThread().getName());

        try {
            injectionTransferControllerToTransferMsgs();
            injectionBlockDetectionControllerToTransferMsgs();
            injectionTransferMsgsToBlockDetectionController();
            injectionTransferMsgsToUcanHandler();
            injectionTransferMsgsToUcanConnectHandler();
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Widget.initialize(args, blockDetectionController);
        System.out.println("main " + Thread.currentThread().getName());
    }

    private void injectionTransferControllerToTransferMsgs() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends TransferMsgs> clTransferMsgs = transferMsgs.getClass();
        Field filedTransferController = clTransferMsgs.getDeclaredField("transferController");
        filedTransferController.setAccessible(true);
        filedTransferController.set(transferMsgs, transferController);
    }

    private void injectionBlockDetectionControllerToTransferMsgs() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends TransferMsgs> clTransferMsgs = transferMsgs.getClass();
        Field filedBockDetectionController = clTransferMsgs.getDeclaredField("blockDetectionController");
        filedBockDetectionController.setAccessible(true);
        filedBockDetectionController.set(transferMsgs, blockDetectionController);
    }

    private void injectionTransferMsgsToBlockDetectionController() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clBlockDetectionController = blockDetectionController.getClass().getSuperclass();
        Field filedTransferMsgs = clBlockDetectionController.getDeclaredField("transferMsg");
        filedTransferMsgs.setAccessible(true);
        filedTransferMsgs.set(blockDetectionController, transferMsgs);
    }

    private void injectionTransferMsgsToUcanHandler() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clUcanHandler = ucanHandler.getClass();
        Field filedHandler = clUcanHandler.getDeclaredField("handler");
        filedHandler.setAccessible(true);
        filedHandler.set(ucanHandler, transferMsgs);
    }

    private void injectionTransferMsgsToUcanConnectHandler() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clUcanHandler = ucanConnectHandler.getClass();
        Field filedHandler = clUcanHandler.getDeclaredField("handler");
        filedHandler.setAccessible(true);
        filedHandler.set(ucanConnectHandler, transferMsgs);
    }

}
