package detectionModules;

import javafx.application.Platform;
import transferMessages.transfer.TransferCanMsgs;
import widget.controllers.RootController;

import java.util.HashMap;
import java.util.Map;


public class BDMGcontroller extends BlockDetectionController {

    private byte countBds = 0;

    public BDMGcontroller(TransferCanMsgs transferCanMsg) {
        super(transferCanMsg);
    }

    @Override
    protected void onNewBD(int serialNumber) {
        System.out.println("New BD!");
        countBds++;
        bds.put(countBds, new Bdmg(countBds, serialNumber));

        RootController.addFoundedDevice(countBds, serialNumber);

        ExecutorBlockDetectionCommands.addCommand(new BlockDetectionCommands.SetLogicalNumber(this, serialNumber, countBds));
    }

    @Override
    protected void onNewParameter(byte logicNumber, byte parameter, int value) {
        System.out.println("New parameter!");
    }

    @Override
    protected void onNewParameter(byte logicNumber, byte parameter, float value) {
    }

    @Override
    protected void onNewMeasureData(byte logicNumber, MeasureData data) {
        System.out.println("New Measure Data!" + data.getData().size());

        ((Bdmg)bds.get(logicNumber)).add(data);

        RootController.showGraph(data);
    }

    @Override
    protected void onStartMeasureData(byte logicNumber) {
        System.out.println("onStartMeasureData");

        ((Bdmg)bds.get(logicNumber)).newMeasure();
    }
}
