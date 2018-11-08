package transferMessages;

import detectionModules.BDcommands.ReadMessage;
import detectionModules.BDcontroller;
import detectionModules.ExecutorBDcommands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class TransferCanMsgs implements TransferMsgs {

	private final Controller controller;

    private final BDcontroller bdController;

	private Queue<Msg> transmitMsgs = new LinkedList<>();
	private Queue<Msg> receiveMsgs = new LinkedList<>();
	private int countTrMsg;
	private int countRecMsg;

	public TransferCanMsgs(Controller controller, BDcontroller bdController) {
        this.controller = controller;
        this.bdController = bdController;
	}

	//------------------------------------receive

    @Override
    public void addToReceiveMsgs() {
        List<? extends Msg> msgs = controller.readMsgs();
        for (Msg msg : msgs) {
            receiveMsgs.add(msg);
            System.out.println("New receive msg:" + msg);
        }
		countRecMsg += msgs.size();

        ExecutorBDcommands.addComand(new ReadMessage(bdController));
	}

    @Override
	public List<? extends Msg> subFromReceiveMsgs() {
		List<Msg> msgs = new ArrayList<>(receiveMsgs.size());
        System.out.println( "size = " +receiveMsgs.size());
        int size =  receiveMsgs.size();
		for (int i = 0; i < size; i++) {
            msgs.add(receiveMsgs.poll());
            System.out.println("Read receive msg:" + msgs.get(i));
        }
		return msgs;
	}

	//------------------------------------transmit
    @Override
    public void addToTransmitMsgs(List<? extends Msg> msgs) {
        for (Msg msg : msgs) {
            transmitMsgs.add(msg);
            System.out.println("New transmit msg:" + msg);
        }
	}

    @Override
    public void subFromTransmitMsgs() {
		List<Msg> msgs = new ArrayList<>(transmitMsgs.size());
		int size = transmitMsgs.size();
		for (int i = 0; i < size; i++) {
			msgs.add(transmitMsgs.poll());
			System.out.println("New transmit msg:" + msgs.get(i));
		}
		controller.writeMsgs(msgs);
		countTrMsg += msgs.size();
	}

	//------------------------------------
	public int getCountTrMsg() {
		return countTrMsg;
	}

	public int getCountRecMsg() {
		return countRecMsg;
	}

}





