package transferMessages.controller;

import java.util.HashMap;
import java.util.Map;

import static transferMessages.controller.Features.Baudrate.*;
import static transferMessages.controller.Features.CanStatus.*;
import static transferMessages.controller.Features.Channel.*;
import static transferMessages.controller.Features.Event.*;
import static transferMessages.controller.Features.FunctionReturnCode.*;
import static transferMessages.controller.Features.HandleState.*;
import static transferMessages.controller.Features.Reset.*;
import static transferMessages.controller.Features.UsbStatus.*;
import static transferMessages.controller.Features.VersionType.*;

public class Features {

    static {
        initCanStatuses();
        initUsbStatuses();
        initBaudsrate();
        initFunctionReturnCodes();
        initChannels();
        initResets();
        initVersionTypes();
        initEvents();
        initHandleStates();
    }

    public enum CanStatus {
        USBCAN_CANERR_OK,
        USBCAN_CANERR_XMTFULL,
        USBCAN_CANERR_OVERRUN,
        USBCAN_CANERR_BUSLIGHT,
        USBCAN_CANERR_BUSHEAVY,
        USBCAN_CANERR_BUSOFF,
        USBCAN_CANERR_QRCVEMPTY,
        USBCAN_CANERR_QOVERRUN,
        USBCAN_CANERR_QXMTFULL,
        USBCAN_CANERR_REGTEST,
        USBCAN_CANERR_MEMTEST,
        USBCAN_CANERR_TXMSGLOST
    }

    public enum UsbStatus {
        USBCAN_USBERR_OK,
        USBCAN_USBERR_STATUS_TIMEOUT,
        USBCAN_USBERR_WATCHDOG_TIMEOUT
    }

    public enum Baudrate {
        USBCAN_BAUD_1MBit,
        USBCAN_BAUD_800kBit,
        USBCAN_BAUD_500kBit,
        USBCAN_BAUD_250kBit,
        USBCAN_BAUD_125kBit,
        USBCAN_BAUD_100kBit,
        USBCAN_BAUD_50kBit,
        USBCAN_BAUD_20kBit,
        USBCAN_BAUD_10kBit
    }

    public enum FunctionReturnCode {
        USBCAN_SUCCESSFUL,
        USBCAN_ERR_RESOURCE,
        USBCAN_ERR_MAXMODULES,
        USBCAN_ERR_HWINUSE,
        USBCAN_ERR_ILLVERSION,
        USBCAN_ERR_ILLHW,
        USBCAN_ERR_ILLHANDLE,
        USBCAN_ERR_ILLPARAM,
        USBCAN_ERR_BUSY,
        USBCAN_ERR_TIMEOUT,
        USBCAN_ERR_IOFAILED,
        USBCAN_ERR_DLL_TXFULL,
        USBCAN_ERR_MAXINSTANCES,
        USBCAN_ERR_CANNOTINIT,
        USBCAN_ERR_DISCONNECT,
        USBCAN_ERR_NOHWCLASS,
        USBCAN_ERR_ILLCHANNEL,
        USBCAN_ERR_RESERVED1,
        USBCAN_ERR_ILLHWTYPE,
        USBCAN_ERR_SERVER_TIMEOUT,
        USBCAN_ERRCMD_NOTEQU,
        USBCAN_ERRCMD_REGTST,
        USBCAN_ERRCMD_ILLCMD,
        USBCAN_ERRCMD_EEPROM,
        USBCAN_ERRCMD_RESERVED1,
        USBCAN_ERRCMD_RESERVED2,
        USBCAN_ERRCMD_RESERVED3,
        USBCAN_ERRCMD_ILLBDR,
        USBCAN_ERRCMD_NOTINIT,
        USBCAN_ERRCMD_ALREADYINIT,
        USBCAN_ERRCMD_ILLSUBCMD,
        USBCAN_ERRCMD_ILLIDX,
        USBCAN_ERRCMD_RUNNING,
        USBCAN_WARN_NODATA,
        USBCAN_WARN_SYS_RXOVERRUN,
        USBCAN_WARN_DLL_RXOVERRUN,
        USBCAN_WARN_RESERVED1,
        USBCAN_WARN_RESERVED2,
        USBCAN_WARN_FW_TXOVERRUN,
        USBCAN_WARN_FW_RXOVERRUN,
        USBCAN_WARN_FW_TXMSGLOST,
        USBCAN_WARN_NULL_PTR,
        USBCAN_WARN_TXLIMIT,
        USBCAN_RESERVED
    }

    public enum Channel {
        USBCAN_CHANNEL_CH0,
        USBCAN_CHANNEL_CH1,
        USBCAN_CHANNEL_ANY,
        USBCAN_CHANNEL_ALL,
        USBCAN_CHANNEL_CAN1,
        USBCAN_CHANNEL_CAN2,
        USBCAN_CHANNEL_LIN
    }

    public enum Reset {
        USBCAN_RESET_ALL,
        USBCAN_RESET_NO_STATUS,
        USBCAN_RESET_NO_CANCTRL,
        USBCAN_RESET_NO_TXCOUNTER,
        USBCAN_RESET_NO_RXCOUNTER,
        USBCAN_RESET_NO_TXBUFFER_CH,
        USBCAN_RESET_NO_TXBUFFER_DLL,
        USBCAN_RESET_NO_TXBUFFER_FW,
        USBCAN_RESET_NO_RXBUFFER_CH,
        USBCAN_RESET_NO_RXBUFFER_DLL,
        USBCAN_RESET_NO_RXBUFFER_SYS,
        USBCAN_RESET_NO_RXBUFFER_FW,
        USBCAN_RESET_FIRMWARE,
    }

    public enum VersionType {
        K_VER_TYPE_USER_LIB,
        K_VER_TYPE_USER_DLL,
        K_VER_TYPE_SYS_DRV ,
        K_VER_TYPE_FIRMWARE,
        K_VER_TYPE_NET_DRV,
        K_VER_TYPE_SYS_LD,
        K_VER_TYPE_SYS_L2,
        K_VER_TYPE_SYS_L3,
        K_VER_TYPE_SYS_L4,
        K_VER_TYPE_SYS_L5,
        K_VER_TYPE_CPL,
        K_VER_TYPE_SYS_L21,
        K_VER_TYPE_SYS_L22
    }

    public enum Event {
        USBCAN_EVENT_INITHW,
        USBCAN_EVENT_INITCAN,
        USBCAN_EVENT_RECEIVE,
        USBCAN_EVENT_STATUS,
        USBCAN_EVENT_DEINITCAN,
        USBCAN_EVENT_DEINITHW,
        USBCAN_EVENT_CONNECT,
        USBCAN_EVENT_DISCONNECT,
        USBCAN_EVENT_FATALDISCON
    }

    public enum HandleState {
        USBCAN_INVALID_HANDLE
    }

    public static Map<CanStatus, Integer>           canStatuses;
    public static Map<UsbStatus, Integer> 			usbStatuses;
    public static Map<Baudrate, Integer>  			baudsrate;
    public static Map<FunctionReturnCode, Integer>	functionReturnCodes;
    public static Map<Channel, Integer>				channels;
    public static Map<Reset, Integer>				resets;
    public static Map<VersionType, Integer>			versionTypes;
    public static Map<Event, Integer>				events;
    public static Map<HandleState, Integer>			handleStates;

    private static void initCanStatuses() {
        canStatuses = new HashMap<>();
        canStatuses.put(USBCAN_CANERR_OK, 			0x0000);
        canStatuses.put(USBCAN_CANERR_XMTFULL, 		0x0001);
        canStatuses.put(USBCAN_CANERR_OVERRUN, 		0x0002);
        canStatuses.put(USBCAN_CANERR_BUSLIGHT, 	0x0004);
        canStatuses.put(USBCAN_CANERR_BUSHEAVY, 	0x0008);
        canStatuses.put(USBCAN_CANERR_BUSOFF, 		0x0010);
        canStatuses.put(USBCAN_CANERR_QRCVEMPTY,	0x0020);
        canStatuses.put(USBCAN_CANERR_QOVERRUN, 	0x0040);
        canStatuses.put(USBCAN_CANERR_QXMTFULL, 	0x0080);
        canStatuses.put(USBCAN_CANERR_REGTEST, 		0x0100);
        canStatuses.put(USBCAN_CANERR_MEMTEST, 		0x0200);
        canStatuses.put(USBCAN_CANERR_TXMSGLOST, 	0x0400);
    }

    private static void initUsbStatuses() {
        usbStatuses = new HashMap<>();
        usbStatuses.put(USBCAN_USBERR_OK, 					0x0000);
        usbStatuses.put(USBCAN_USBERR_STATUS_TIMEOUT, 		0x2000);
        usbStatuses.put(USBCAN_USBERR_WATCHDOG_TIMEOUT, 	0x4000);
    }

    private static void initBaudsrate() {
        baudsrate = new HashMap<>();
        baudsrate.put(USBCAN_BAUD_1MBit, 	0x0014);
        baudsrate.put(USBCAN_BAUD_800kBit, 	0x0016);
        baudsrate.put(USBCAN_BAUD_500kBit, 	0x001c);
        baudsrate.put(USBCAN_BAUD_250kBit,  0x011c);
        baudsrate.put(USBCAN_BAUD_125kBit,  0x031c);
        baudsrate.put(USBCAN_BAUD_125kBit,  0x031c);
        baudsrate.put(USBCAN_BAUD_100kBit,  0x432f);
        baudsrate.put(USBCAN_BAUD_50kBit,   0x472f);
        baudsrate.put(USBCAN_BAUD_50kBit,   0x472f);
        baudsrate.put(USBCAN_BAUD_20kBit,   0x532f);
        baudsrate.put(USBCAN_BAUD_10kBit,   0x672f);
    }

    private static void initFunctionReturnCodes() {
        functionReturnCodes = new HashMap<>();
        functionReturnCodes.put(USBCAN_SUCCESSFUL, 			0x00);
        functionReturnCodes.put(USBCAN_ERR_RESOURCE, 		0x01);
        functionReturnCodes.put(USBCAN_ERR_MAXMODULES, 		0x02);
        functionReturnCodes.put(USBCAN_ERR_HWINUSE, 		0x03);
        functionReturnCodes.put(USBCAN_ERR_ILLVERSION, 		0x04);
        functionReturnCodes.put(USBCAN_ERR_ILLHW, 			0x05);
        functionReturnCodes.put(USBCAN_ERR_ILLHANDLE, 		0x06);
        functionReturnCodes.put(USBCAN_ERR_ILLPARAM, 		0x07);
        functionReturnCodes.put(USBCAN_ERR_BUSY, 			0x08);
        functionReturnCodes.put(USBCAN_ERR_TIMEOUT, 		0x09);
        functionReturnCodes.put(USBCAN_ERR_IOFAILED, 		0x0a);
        functionReturnCodes.put(USBCAN_ERR_DLL_TXFULL, 		0x0b);
        functionReturnCodes.put(USBCAN_ERR_MAXINSTANCES, 	0x0c);
        functionReturnCodes.put(USBCAN_ERR_CANNOTINIT, 		0x0d);
        functionReturnCodes.put(USBCAN_ERR_DISCONNECT, 		0x0e);
        functionReturnCodes.put(USBCAN_ERR_NOHWCLASS, 		0x0f);
        functionReturnCodes.put(USBCAN_ERR_ILLCHANNEL, 		0x10);
        functionReturnCodes.put(USBCAN_ERR_RESERVED1, 		0x11);
        functionReturnCodes.put(USBCAN_ERR_ILLHWTYPE, 		0x12);
        functionReturnCodes.put(USBCAN_ERR_SERVER_TIMEOUT, 	0x13);
        functionReturnCodes.put(USBCAN_ERRCMD_NOTEQU, 		0x40);
        functionReturnCodes.put(USBCAN_ERRCMD_REGTST, 		0x41);
        functionReturnCodes.put(USBCAN_ERRCMD_ILLCMD, 		0x42);
        functionReturnCodes.put(USBCAN_ERRCMD_EEPROM, 		0x43);
        functionReturnCodes.put(USBCAN_ERRCMD_RESERVED1, 	0x44);
        functionReturnCodes.put(USBCAN_ERRCMD_RESERVED2, 	0x45);
        functionReturnCodes.put(USBCAN_ERRCMD_RESERVED3, 	0x46);
        functionReturnCodes.put(USBCAN_ERRCMD_ILLBDR, 		0x47);
        functionReturnCodes.put(USBCAN_ERRCMD_NOTINIT, 		0x48);
        functionReturnCodes.put(USBCAN_ERRCMD_ALREADYINIT, 	0x49);
        functionReturnCodes.put(USBCAN_ERRCMD_ILLSUBCMD, 	0x4A);
        functionReturnCodes.put(USBCAN_ERRCMD_ILLIDX, 		0x4B);
        functionReturnCodes.put(USBCAN_ERRCMD_RUNNING, 		0x4C);
        functionReturnCodes.put(USBCAN_WARN_NODATA, 		0x80);
        functionReturnCodes.put(USBCAN_WARN_SYS_RXOVERRUN, 	0x81);
        functionReturnCodes.put(USBCAN_WARN_DLL_RXOVERRUN, 	0x82);
        functionReturnCodes.put(USBCAN_WARN_RESERVED1, 		0x83);
        functionReturnCodes.put(USBCAN_WARN_RESERVED2, 		0x84);
        functionReturnCodes.put(USBCAN_WARN_FW_TXOVERRUN, 	0x85);
        functionReturnCodes.put(USBCAN_WARN_FW_RXOVERRUN, 	0x86);
        functionReturnCodes.put(USBCAN_WARN_FW_TXMSGLOST, 	0x87);
        functionReturnCodes.put(USBCAN_WARN_NULL_PTR, 		0x90);
        functionReturnCodes.put(USBCAN_WARN_TXLIMIT, 		0x91);
        functionReturnCodes.put(USBCAN_RESERVED, 			0xc0);
    }

    private static void initChannels() {
        channels = new HashMap<>();
        channels.put(USBCAN_CHANNEL_CH0, 	0);
        channels.put(USBCAN_CHANNEL_CH1, 	1);
        channels.put(USBCAN_CHANNEL_ANY, 	255);
        channels.put(USBCAN_CHANNEL_ALL, 	254);
        channels.put(USBCAN_CHANNEL_CAN1,	0);
        channels.put(USBCAN_CHANNEL_CAN2, 	1);
        channels.put(USBCAN_CHANNEL_LIN, 	1);
    }

    private static void initResets() {
        resets = new HashMap<>();
        resets.put(USBCAN_RESET_ALL,  				0x00000000);
        resets.put(USBCAN_RESET_NO_STATUS,  		0x00000001);
        resets.put(USBCAN_RESET_NO_CANCTRL,  		0x00000002);
        resets.put(USBCAN_RESET_NO_TXCOUNTER,  		0x00000004);
        resets.put(USBCAN_RESET_NO_RXCOUNTER,  		0x00000008);
        resets.put(USBCAN_RESET_NO_TXBUFFER_CH,  	0x00000010);
        resets.put(USBCAN_RESET_NO_TXBUFFER_DLL,	0x00000020);
        resets.put(USBCAN_RESET_NO_TXBUFFER_FW,  	0x00000080);
        resets.put(USBCAN_RESET_NO_RXBUFFER_CH,  	0x00000100);
        resets.put(USBCAN_RESET_NO_RXBUFFER_DLL,  	0x00000200);
        resets.put(USBCAN_RESET_NO_RXBUFFER_SYS,  	0x00000400);
        resets.put(USBCAN_RESET_NO_RXBUFFER_FW,  	0x00000800);
        resets.put(USBCAN_RESET_FIRMWARE,  			0xFFFFFFFF);
    }

    private static void initVersionTypes() {
        versionTypes = new HashMap<>();
        versionTypes.put(K_VER_TYPE_USER_LIB, 	0x01);
        versionTypes.put(K_VER_TYPE_USER_DLL, 	0x01);
        versionTypes.put(K_VER_TYPE_SYS_DRV, 	0x02);
        versionTypes.put(K_VER_TYPE_FIRMWARE, 	0x03);
        versionTypes.put(K_VER_TYPE_NET_DRV, 	0x04);
        versionTypes.put(K_VER_TYPE_SYS_LD, 	0x05);
        versionTypes.put(K_VER_TYPE_SYS_L2, 	0x06);
        versionTypes.put(K_VER_TYPE_SYS_L3, 	0x07);
        versionTypes.put(K_VER_TYPE_SYS_L4, 	0x08);
        versionTypes.put(K_VER_TYPE_SYS_L5, 	0x09);
        versionTypes.put(K_VER_TYPE_CPL, 		0x0A);
        versionTypes.put(K_VER_TYPE_SYS_L21, 	0x0B);
        versionTypes.put(K_VER_TYPE_SYS_L22, 	0x0B);
    }

    private static void initEvents() {
        events = new HashMap<>();
        events.put(USBCAN_EVENT_INITHW, 0);
        events.put(USBCAN_EVENT_INITCAN, 1);
        events.put(USBCAN_EVENT_RECEIVE, 2);
        events.put(USBCAN_EVENT_STATUS, 3);
        events.put(USBCAN_EVENT_DEINITCAN,4);
        events.put(USBCAN_EVENT_DEINITHW, 5);
        events.put(USBCAN_EVENT_CONNECT, 6);
        events.put(USBCAN_EVENT_DISCONNECT, 7);
        events.put(USBCAN_EVENT_FATALDISCON, 8);
    }

    private static void initHandleStates() {
        handleStates = new HashMap<>();
        handleStates.put(USBCAN_INVALID_HANDLE, 0xff);
    }
}
