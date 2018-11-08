package transferMessages.controller;

import org.omg.CORBA.IntHolder;
import org.omg.CORBA.LongHolder;

import com.sun.jna.ptr.ByteByReference;

import com.sun.jna.platform.win32.WinDef.BYTE;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinDef.WORDByReference;

import transferMessages.transfer.Msg;
import transferMessages.controller.UcanLibrary.UcanMsg;
import transferMessages.controller.UcanLibrary.UcanInit;
import transferMessages.controller.UcanLibrary.UcanStatus;
import transferMessages.controller.UcanLibrary.UcanMsgCountInfo;
import transferMessages.callbacks.UcanCallback;
import transferMessages.callbacks.UcanConnectCallback;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static transferMessages.controller.Features.*;
import static transferMessages.controller.Features.Baudrate.*;
import static transferMessages.controller.Features.Channel.*;
import static transferMessages.controller.Features.FunctionReturnCode.*;
import static transferMessages.controller.Features.HandleState.*;
import static transferMessages.controller.Features.Reset.*;

public class UсanController implements Controller {

	private final UcanLibrary usbCanLibrary;
	private final UcanConnectCallback connectUsbCan;
	private final UcanCallback eventsUsbCan;
	private final ByteByReference usbCanHandle  = new ByteByReference();
	private final BYTE usbCanChannel			= new BYTE(channels.get(USBCAN_CHANNEL_ANY));
	private final short	baudrate 				= baudsrate.get(USBCAN_BAUD_500kBit).shortValue();

	public UсanController(UcanLibrary usbCanLibrary,
                          UcanCallback eventsUsbCan,
                          UcanConnectCallback connectUsbCan) {
		this.usbCanLibrary = usbCanLibrary;
		this.connectUsbCan = connectUsbCan;
		this.eventsUsbCan = eventsUsbCan;
		usbCanHandle.setValue(handleStates.get(USBCAN_INVALID_HANDLE).byteValue());
        initHardwareControl();
		initialize();
	}

    private void initHardwareControl() {
		BYTE res = usbCanLibrary.UcanInitHwConnectControlEx(connectUsbCan, null);
		if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			//TODO Logger!
			throw new RuntimeException("UcanInitHwConnectControlEx error " + res.intValue());
		}
	}

	private void initialize() {
		initHardware();
		initCan();
	}

	private void initHardware() {
		BYTE res = usbCanLibrary.UcanInitHardwareEx(usbCanHandle.getPointer(),
													usbCanChannel,
													eventsUsbCan,
													null);

		if(res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanInitHardwareEx error " + res.intValue());
		}
	}

	private void initCan() {
		byte[] bytesValue = ByteBuffer.allocate(Short.BYTES).putShort(baudrate).array();
		UcanInit.ByRef param = new UcanInit.ByRef();
		param.m_dwSize               = new DWORD(0x18);
		param.m_bMode                = new BYTE(0x00);
		param.m_bBTR0                = new BYTE(bytesValue[0]);
		param.m_bBTR1                = new BYTE(bytesValue[1]);
		param.m_bOCR                 = new BYTE(0x1a);
		param.m_dwAMR                = new DWORD(0xffffffff);
		param.m_dwACR                = new DWORD(0x00);
		param.m_dwBaudrate           = new DWORD(0x00);
		param.m_wNrOfRxBufferEntries = new WORD(4096);
		param.m_wNrOfTxBufferEntries = new WORD(4096);

		BYTE res =  usbCanLibrary.UcanInitCanEx2(	new BYTE(usbCanHandle.getValue()),
													usbCanChannel,
													param);

		if(res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanInitCanEx2 error " + res.intValue());
		}
	}

	public void shutDown() {
		deInitCan();
		deInitHardware();
	    usbCanHandle.setValue( handleStates.get(USBCAN_INVALID_HANDLE).byteValue());
	}

	private void deInitCan() {
		BYTE res = usbCanLibrary.UcanDeinitCanEx( 	new BYTE(usbCanHandle.getValue()),
													usbCanChannel);

		if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanDeinitCanEx error " + res.intValue());
		}
	}

	private void deInitHardware() {
		BYTE res = usbCanLibrary.UcanDeinitHardware(new BYTE(usbCanHandle.getValue()));

		if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanDeinitHardware error " + res.intValue());
		}
	}

	private void deInitHardwareControl() {
		BYTE res =  usbCanLibrary.UcanDeinitHwConnectControl();
		if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanDeinitHwConnectControl error " + res.intValue());
		}
	}

	public void close() {
		shutDown();
		deInitHardwareControl();
	}

    @Override
	public void writeMsgs(List<? extends Msg> msgs) {
		UcanMsg.ByRef canMsgs = new UcanMsg.ByRef(msgs.size());
		UcanMsg[] arrayCamMsgs = (UcanMsg[])canMsgs.toArray(msgs.size());
		for(int i=0; i< msgs.size(); i++)
            ((UcanMsg)msgs.get(i)).copyFrom(arrayCamMsgs[i]);

      	BYTE res =  usbCanLibrary.UcanWriteCanMsgEx (	new BYTE(usbCanHandle.getValue()),
									      				new BYTE(channels.get(USBCAN_CHANNEL_CH0)),
														canMsgs,
														new DWORDByReference(new DWORD(msgs.size())));

      	if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
      		throw new RuntimeException("UcanWriteCanMsgEx error " + res.intValue());
        }
	}

	@Override
	public List<? extends Msg> readMsgs() {
		final int MAX = 128;
		DWORDByReference countMsg = new DWORDByReference(new DWORD(MAX));
		UcanMsg.ByRef canMsgs = new UcanMsg.ByRef(MAX);

		BYTE res =  usbCanLibrary.UcanReadCanMsgEx(	new BYTE(usbCanHandle.getValue()),
													new WORDByReference(new WORD( usbCanChannel.longValue())),
													canMsgs,
													countMsg);

		if(res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL) && res.intValue() != functionReturnCodes.get(USBCAN_WARN_NODATA)) {
			throw new RuntimeException("UcanReadCanMsgEx error " + res.intValue());
		}

		UcanMsg[] arrayCanMsg =(UcanMsg[])canMsgs.toArray(MAX);
		int size = countMsg.getValue().intValue();

		List<UcanMsg> listCanMsg = new ArrayList<>(size);
		for (int i = 0; i < size; i++)
			listCanMsg.add(arrayCanMsg[i]);

		return listCanMsg;
	}

	public void getMsgCount(IntHolder trMsgCount, IntHolder recMsgCount) {
		UcanMsgCountInfo.ByRef msgCount = new UcanMsgCountInfo.ByRef();

		BYTE res = usbCanLibrary.UcanGetMsgCountInfoEx(	new BYTE(usbCanHandle.getValue()),
														usbCanChannel,
														msgCount);
		
        if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL) ) {
			throw new RuntimeException("UcanGetMsgCountInfoEx error " + res.intValue());
        }
        else {
        	trMsgCount.value	= msgCount.m_wSentMsgCount .intValue(); 
        	recMsgCount.value	= msgCount.m_wRecvdMsgCount.intValue();
        }
	}

	public void reset() {
		BYTE res =  usbCanLibrary.UcanResetCanEx(	new BYTE(usbCanHandle.getValue()),
													usbCanChannel,
													new DWORD(resets.get(USBCAN_RESET_ALL)));
        
        if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanResetCanEx error " + res.intValue());
        }
	 }

	public void getStatus(IntHolder statusCan, IntHolder statusUsb)  {
		UcanStatus.ByRef status = new UcanStatus.ByRef();

		BYTE res =  usbCanLibrary.UcanGetStatusEx(	new BYTE(usbCanHandle.getValue()),
													usbCanChannel,
													status);

		if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
			throw new RuntimeException("UcanGetStatusEx error " + res.intValue());
		}
		else {
			statusCan.value = status.m_wCanStatus.intValue();
			statusUsb.value = status.m_wUsbStatus.intValue();
		}
	}

	public void getErrorCounter(LongHolder trErrorCounter, LongHolder recErrorCounter)  {
		DWORDByReference refTrErrorCounter = new DWORDByReference();
		DWORDByReference refRecErrorCounter = new DWORDByReference();

		BYTE res = usbCanLibrary.UcanGetCanErrorCounter(	new BYTE(usbCanHandle.getValue()),
															usbCanChannel,
															refTrErrorCounter,
															refRecErrorCounter);
		
		if (res.intValue() != functionReturnCodes.get(USBCAN_SUCCESSFUL)) {
        	throw new RuntimeException("UcanResetCanEx error " + res.intValue());
        }
		else {
			trErrorCounter.value	= refTrErrorCounter.getValue().longValue();
			recErrorCounter.value	= refRecErrorCounter.getValue().longValue();
		}
	}

	public void getVersion(IntHolder verMajor, IntHolder verMinor, IntHolder verRelease, VersionType type) {

		DWORD tp = new DWORD();
		tp.setValue(versionTypes.get(type));

		DWORD version = usbCanLibrary.UcanGetVersionEx(tp);
		verMajor.value 		= version.intValue() & 0x000000FF;
		verMinor.value 		= (version.intValue() & 0x0000FF00) >> 8;
		verRelease.value 	= (version.intValue() & 0xFFFF0000) >> 16;
	}

}