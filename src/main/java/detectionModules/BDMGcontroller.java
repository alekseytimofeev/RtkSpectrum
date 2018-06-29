package detectionModules;

public class BDMGcontroller extends BDcontroller {


    @Override
    protected void onNewBD(int serialNumber) {
        System.out.println("New BD!");
        setLogicalNumber(serialNumber, (byte)5);
    }

    @Override
    protected void onNewParameter(byte parameter, int value) {
        System.out.println("New parameter!");
    }

    @Override
    protected void onNewParameter(byte parameter, float value) {

    }

    @Override
    protected void onNewMeasureData(MeasureData data) {

    }

    @Override
    protected void onNewMeasureData(CalibrationData data) {

    }
}
