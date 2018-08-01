package transferMessages;

import com.sun.jna.Pointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.*;
import static transferMessages.UсanController.*;
import static transferMessages.UсanController.Baudrate.*;
import static transferMessages.UсanController.CanStatus.*;
import static transferMessages.UсanController.Channel.*;
import static transferMessages.UсanController.Event.*;
import static transferMessages.UсanController.FunctionReturnCode.*;
import static transferMessages.UсanController.Reset.*;
import static transferMessages.UсanController.UsbStatus.*;
import static transferMessages.UсanController.VersionType.*;

import org.omg.CORBA.IntHolder;
import org.omg.CORBA.LongHolder;
import transferMessages.UcanLibrary.UcanInit;
import transferMessages.UcanLibrary.UcanStatus;
import transferMessages.UcanLibrary.UcanMsgCountInfo;

import com.sun.jna.platform.win32.WinDef.*;

public class UcanControllerTest {

    private UcanLibrary usbCanLibrary;
    private UcanCallback eventsUsbCan;
    private UcanConnectCallback connectUsbCan;
    private UсanController controller;

    @Before
    public void setUp() {
        initConnectUsbCanMock();
        initEventsUsbCanMock();
        initUsbCanLibraryMock();

        controller = new UсanController(usbCanLibrary, eventsUsbCan, connectUsbCan);
    }

    private void initUsbCanLibraryMock(){
        usbCanLibrary = mock(UcanLibrary.class);
        when(usbCanLibrary.UcanInitHwConnectControlEx(eq(connectUsbCan), eq(null)))
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));
        when(usbCanLibrary.UcanInitHardwareEx(any(Pointer.class), any(BYTE.class), eq(eventsUsbCan), eq(null)))
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));
        when(usbCanLibrary.UcanInitCanEx2(any(BYTE.class), any(BYTE.class), any(UcanInit.ByRef.class)))
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));
        when(usbCanLibrary.UcanDeinitCanEx(any(BYTE.class), any(BYTE.class)))
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));
        when(usbCanLibrary.UcanDeinitHardware(any(BYTE.class)))
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));
        when(usbCanLibrary.UcanDeinitHwConnectControl())
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));

        when(usbCanLibrary.UcanGetMsgCountInfoEx(any(BYTE.class), any(BYTE.class), any(UcanMsgCountInfo.ByRef.class)))
                .thenAnswer(answer->{
                    UcanMsgCountInfo.ByRef info = (UcanMsgCountInfo.ByRef)(answer.getArguments())[2];
                    info.m_wRecvdMsgCount = new WORD(1);
                    info.m_wSentMsgCount = new WORD(2);
                    return new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL));
                });

        when(usbCanLibrary.UcanResetCanEx(any(BYTE.class),any(BYTE.class), eq(new DWORD(resets.get(USBCAN_RESET_ALL)))))
                .thenReturn(new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL)));

        when(usbCanLibrary.UcanGetStatusEx(any(BYTE.class), any(BYTE.class), any(UcanStatus.ByRef.class)))
                .thenAnswer(answer->{
                    UcanStatus.ByRef status = (UcanStatus.ByRef)(answer.getArguments())[2];
                    status.m_wCanStatus = new WORD(1);
                    status.m_wUsbStatus = new WORD(2);
                    return new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL));
                });

        when(usbCanLibrary.UcanGetCanErrorCounter(any(BYTE.class), any(BYTE.class), any(DWORDByReference.class), any(DWORDByReference.class)))
                .thenAnswer(answer->{
                    DWORDByReference refTrErrorCounter = (DWORDByReference)(answer.getArguments())[2];
                    DWORDByReference refRecErrorCounter = (DWORDByReference)(answer.getArguments())[3];
                    refTrErrorCounter.setValue(new DWORD(1));
                    refRecErrorCounter.setValue(new DWORD(2));
                    return new BYTE(functionReturnCodes.get(USBCAN_SUCCESSFUL));
                });

        when(usbCanLibrary.UcanGetVersionEx(any(DWORD.class)))
                .thenReturn(new DWORD(0x1122FF44));
    }
    private void initConnectUsbCanMock() {
        connectUsbCan = mock(UcanConnectCallback.class);
        doNothing().when(connectUsbCan).callback(any(DWORD.class), any(DWORD.class), any(Pointer.class));
    }
    private void initEventsUsbCanMock() {
        eventsUsbCan = mock(UcanCallback.class);
        doNothing().when(eventsUsbCan).callback(any(BYTE.class), any(DWORD.class), any(BYTE.class), any(Pointer.class));
    }

    @Test
    public void verifyAtLeastOnceCallMethodUsbCanLibraryAfterInitUsbCanController() {
        verify(usbCanLibrary, times(1)).UcanInitHwConnectControlEx(eq(connectUsbCan), eq(null));
        verify(usbCanLibrary, times(1)).UcanInitHardwareEx(any(Pointer.class), any(BYTE.class), eq(eventsUsbCan), eq(null));
        verify(usbCanLibrary, times(1)).UcanInitCanEx2(any(BYTE.class), any(BYTE.class), any(UcanInit.ByRef.class));

        verify(eventsUsbCan,never()).callback(any(BYTE.class), any(DWORD.class), any(BYTE.class), any(Pointer.class));
        verify(connectUsbCan,never()).callback(any(DWORD.class), any(DWORD.class), any(Pointer.class));
    }

    @Test
    public void verifyAtLeastOnceCallMethodUsbCanLibraryAfterCloseUsbCanController() {
        controller.close();
        verify(usbCanLibrary, times(1)).UcanDeinitCanEx(any(BYTE.class), any(BYTE.class));
        verify(usbCanLibrary, times(1)).UcanDeinitHardware(any(BYTE.class));
        verify(usbCanLibrary, times(1)).UcanDeinitHwConnectControl();
    }

    @Test
    public void getMsgCountTest( ) {
        IntHolder trMsgCount = new IntHolder();
        IntHolder recMsgCount  = new IntHolder();
        controller.getMsgCount(trMsgCount, recMsgCount);
        verify(usbCanLibrary, times(1)).UcanGetMsgCountInfoEx(any(BYTE.class), any(BYTE.class), any(UcanMsgCountInfo.ByRef.class));
        Assert.assertEquals(1, recMsgCount.value);
        Assert.assertEquals(2, trMsgCount.value);
    }

    @Test
    public void resetTest() {
        controller.reset();
        verify(usbCanLibrary, times(1)).UcanResetCanEx(any(BYTE.class),any(BYTE.class), eq(new DWORD(resets.get(USBCAN_RESET_ALL))));
    }

    @Test
    public void getStatusTest() {
        IntHolder statusCan = new IntHolder();
        IntHolder statusUsb = new IntHolder();
        controller.getStatus(statusCan, statusUsb);
        verify(usbCanLibrary, times(1)).UcanGetStatusEx(any(BYTE.class), any(BYTE.class), any(UcanStatus.ByRef.class));
        Assert.assertEquals(1, statusCan.value);
        Assert.assertEquals(2, statusUsb.value);
    }

    @Test
    public void getErrorCounterTest() {
        LongHolder trErrorCounter = new LongHolder();
        LongHolder recErrorCounter = new LongHolder();
        controller.getErrorCounter(trErrorCounter, recErrorCounter);
        verify(usbCanLibrary, times(1)).UcanGetCanErrorCounter(any(BYTE.class), any(BYTE.class), any(DWORDByReference.class), any(DWORDByReference.class));
        Assert.assertEquals(1, trErrorCounter.value);
        Assert.assertEquals(2, recErrorCounter.value);
    }

    @Test
    public void getVersionTest() {
        IntHolder verMajor = new IntHolder();
        IntHolder verMinor = new IntHolder();
        IntHolder verRelease = new IntHolder();
        VersionType type =K_VER_TYPE_USER_DLL;

        controller.getVersion(verMajor, verMinor, verRelease, type);
        verify(usbCanLibrary, times(1)).UcanGetVersionEx(any(DWORD.class));
        Assert.assertEquals(68, verMajor.value);
        Assert.assertEquals(255, verMinor.value);
        Assert.assertEquals(4386, verRelease.value);
    }

    @Test
    public void canStatusesCheck() {
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_OK).intValue(),           0x0000);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_XMTFULL).intValue(),      0x0001);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_OVERRUN).intValue(),      0x0002);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_BUSLIGHT).intValue(), 	0x0004);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_BUSHEAVY).intValue(), 	0x0008);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_BUSOFF).intValue(), 		0x0010);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_QRCVEMPTY).intValue(),	0x0020);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_QOVERRUN).intValue(), 	0x0040);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_QXMTFULL).intValue(), 	0x0080);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_REGTEST).intValue(), 		0x0100);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_MEMTEST).intValue(), 		0x0200);
        Assert.assertEquals(canStatuses.get(USBCAN_CANERR_TXMSGLOST).intValue(), 	0x0400);
    }

    @Test
    public void usbStatusesCheck() {
        Assert.assertEquals(usbStatuses.get(USBCAN_USBERR_OK).intValue(),               0x0000);
        Assert.assertEquals(usbStatuses.get(USBCAN_USBERR_STATUS_TIMEOUT).intValue(),   0x2000);
        Assert.assertEquals(usbStatuses.get(USBCAN_USBERR_WATCHDOG_TIMEOUT).intValue(),	0x4000);
    }

    @Test
    public void baudsrateCheck() {
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_1MBit).intValue(),    0x0014);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_800kBit).intValue(),  0x0016);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_500kBit).intValue(),  0x001c);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_250kBit).intValue(),  0x011c);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_125kBit).intValue(),  0x031c);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_125kBit).intValue(),  0x031c);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_100kBit).intValue(),  0x432f);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_50kBit).intValue(),   0x472f);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_50kBit).intValue(),   0x472f);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_20kBit).intValue(),   0x532f);
        Assert.assertEquals(baudsrate.get(USBCAN_BAUD_10kBit).intValue(),   0x672f);
    }

    @Test
    public void initFunctionReturnCodesCheck() {
        Assert.assertEquals(functionReturnCodes.get(USBCAN_SUCCESSFUL).intValue(),			0x00);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_RESOURCE).intValue(), 		0x01);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_MAXMODULES).intValue(), 		0x02);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_HWINUSE).intValue(), 		0x03);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_ILLVERSION).intValue(),		0x04);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_ILLHW).intValue(),			0x05);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_ILLHANDLE).intValue(), 		0x06);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_ILLPARAM).intValue(), 		0x07);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_BUSY).intValue(), 			0x08);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_TIMEOUT).intValue(),		    0x09);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_IOFAILED).intValue(), 		0x0a);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_DLL_TXFULL).intValue(), 		0x0b);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_MAXINSTANCES).intValue(), 	0x0c);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_CANNOTINIT).intValue(), 		0x0d);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_DISCONNECT).intValue(), 		0x0e);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_NOHWCLASS).intValue(), 		0x0f);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_ILLCHANNEL).intValue(), 		0x10);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_RESERVED1).intValue(), 		0x11);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_ILLHWTYPE).intValue(), 		0x12);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERR_SERVER_TIMEOUT).intValue(), 	0x13);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_NOTEQU).intValue(), 		0x40);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_REGTST).intValue(), 		0x41);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_ILLCMD).intValue(),		0x42);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_EEPROM).intValue(), 		0x43);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_RESERVED1).intValue(), 	0x44);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_RESERVED2).intValue(), 	0x45);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_RESERVED3).intValue(), 	0x46);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_ILLBDR).intValue(), 		0x47);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_NOTINIT).intValue(), 		0x48);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_ALREADYINIT).intValue(), 	0x49);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_ILLSUBCMD).intValue(), 	0x4A);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_ILLIDX).intValue(), 		0x4B);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_ERRCMD_RUNNING).intValue(), 		0x4C);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_NODATA).intValue(), 		0x80);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_SYS_RXOVERRUN).intValue(), 	0x81);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_DLL_RXOVERRUN).intValue(), 	0x82);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_RESERVED1).intValue(), 		0x83);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_RESERVED2).intValue(),		0x84);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_FW_TXOVERRUN).intValue(),	0x85);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_FW_RXOVERRUN).intValue(), 	0x86);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_FW_TXMSGLOST).intValue(), 	0x87);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_NULL_PTR).intValue(), 		0x90);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_WARN_TXLIMIT).intValue(),		0x91);
        Assert.assertEquals(functionReturnCodes.get(USBCAN_RESERVED).intValue(), 			0xc0);
    }

    @Test
    public void channelsCheck() {
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_CH0).intValue(), 	0);
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_CH1).intValue(), 	1);
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_ANY).intValue(), 	255);
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_ALL).intValue(),  	254);
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_CAN1).intValue(), 	0);
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_CAN2).intValue(),  	1);
        Assert.assertEquals(channels.get(USBCAN_CHANNEL_LIN).intValue(),  	1);
    }

    @Test
    public void resetsCheck() {
        Assert.assertEquals(resets.get(USBCAN_RESET_ALL).intValue(),	        	0x00000000);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_STATUS).intValue(),  		0x00000001);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_CANCTRL).intValue(),  		0x00000002);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_TXCOUNTER).intValue(), 		0x00000004);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_RXCOUNTER).intValue(), 		0x00000008);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_TXBUFFER_CH).intValue(),  	0x00000010);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_TXBUFFER_DLL).intValue(),	0x00000020);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_TXBUFFER_FW).intValue(),  	0x00000080);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_RXBUFFER_CH).intValue(), 	0x00000100);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_RXBUFFER_DLL).intValue(),  	0x00000200);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_RXBUFFER_SYS).intValue(),  	0x00000400);
        Assert.assertEquals(resets.get(USBCAN_RESET_NO_RXBUFFER_FW).intValue(),  	0x00000800);
        Assert.assertEquals(resets.get(USBCAN_RESET_FIRMWARE).intValue(),			0xFFFFFFFF);
    }

    @Test
    public void versionTypesCheck() {
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_USER_LIB).intValue(), 	    0x01);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_USER_DLL).intValue(), 	    0x01);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_DRV).intValue(), 	    0x02);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_FIRMWARE).intValue(), 	    0x03);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_NET_DRV).intValue(), 	    0x04);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_LD).intValue(), 		0x05);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_L2).intValue(), 	 	0x06);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_L3).intValue(), 	 	0x07);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_L4).intValue(), 	 	0x08);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_L5).intValue(), 	 	0x09);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_CPL).intValue(), 	 		0x0A);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_L21).intValue(), 	 	0x0B);
        Assert.assertEquals(versionTypes.get(K_VER_TYPE_SYS_L22).intValue(), 	 	0x0B);
    }

    @Test
    public void eventsCheck() {
        Assert.assertEquals(events.get(USBCAN_EVENT_INITHW).intValue(),     0);
        Assert.assertEquals(events.get(USBCAN_EVENT_INITCAN).intValue(),    1);
        Assert.assertEquals(events.get(USBCAN_EVENT_RECEIVE).intValue(),    2);
        Assert.assertEquals(events.get(USBCAN_EVENT_STATUS).intValue(),     3);
        Assert.assertEquals(events.get(USBCAN_EVENT_DEINITCAN).intValue(),  4);
        Assert.assertEquals(events.get(USBCAN_EVENT_DEINITHW).intValue(),   5);
        Assert.assertEquals(events.get(USBCAN_EVENT_CONNECT).intValue(),    6);
        Assert.assertEquals(events.get(USBCAN_EVENT_DISCONNECT).intValue(), 7);
        Assert.assertEquals(events.get(USBCAN_EVENT_FATALDISCON).intValue(),8);
    }

}
