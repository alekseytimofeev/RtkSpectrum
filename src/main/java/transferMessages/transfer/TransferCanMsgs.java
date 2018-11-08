package transferMessages.transfer;


import detectionModules.BlockDetectionCommands.ReadMessage;
import detectionModules.BlockDetectionController;
import detectionModules.ExecutorBlockDetectionCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transferMessages.controller.TransferController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TransferCanMsgs implements TransferMsgs {

    Logger logger = LoggerFactory.getLogger(TransferCanMsgs.class);

	private final TransferController transferController;
    private final BlockDetectionController blockDetectionController;

	private Queue<Msg> transmitMsgs = new LinkedList<>();
	private Queue<Msg> receiveMsgs = new LinkedList<>();
	private int countTrMsg;
	private int countRecMsg;

	public TransferCanMsgs(TransferController controller, BlockDetectionController bdController) {
        this.transferController = controller;
        this.blockDetectionController = bdController;
	}

    //------------------------------------receive
    @Override
    public void addToReceiveMsgs() {
        List<? extends Msg> msgs = transferController.readMsgs();
        for (int i = 0; i < msgs.size(); i++) {
            receiveMsgs.add(msgs.get(i));
            logger.info(String.format("%d) New receive msg: %s" , i, msgs.get(i)));
        }
        logger.info(String.format("Size receive msgs: %d", msgs.size()));
		countRecMsg += msgs.size();

        ExecutorBlockDetectionCommands.addCommand(new ReadMessage(blockDetectionController)); //Todo !!!!!!!!
	}

    @Override
	public List<? extends Msg> subFromReceiveMsgs() {
		List<Msg> msgs = new ArrayList<>(receiveMsgs.size());
        int size =  receiveMsgs.size();
        int i;
        for ( i = 0; i < size; i++) {
            msgs.add(receiveMsgs.poll());
            logger.info(String.format("%d) Read receive msg: %s", i, msgs.get(i)));
        }
        logger.info(String.format("Size list receive msgs: %d", i));
		return msgs;
	}

	//------------------------------------transmit
    @Override
    public void addToTransmitMsgs(List<? extends Msg> msgs) {
        for (Msg msg : msgs) {
            transmitMsgs.add(msg);
            logger.info(String.format("New transmit msg: %s", msg));
        }
        subFromTransmitMsgs(); //Todo !!!!!!!!!!!!!!!!!!!!!!
	}

    @Override
    public void subFromTransmitMsgs() {
		List<Msg> msgs = new ArrayList<>(transmitMsgs.size());
		int size = transmitMsgs.size();
		for (int i = 0; i < size; i++) {
			msgs.add(transmitMsgs.poll());
            logger.info(String.format("Write transmit msg: %s", msgs.get(i)));
		}
		transferController.writeMsgs(msgs);
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





