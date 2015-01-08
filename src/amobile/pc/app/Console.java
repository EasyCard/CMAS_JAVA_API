package amobile.pc.app;

import java.net.InetAddress;
import java.net.UnknownHostException;

import amobile.java.cmas.CMASClient;
import amobile.java.cmas.EDCSignOn;
import amobile.java.easycard.PPR_ReadCardNumber;
import amobile.java.easycard.PPR_ReadCodeVersion;
import amobile.java.easycard.PPR_Reset;
import amobile.java.easycard.PPR_Reset.OneDayQuotaFlagForMicroPayment;
import amobile.java.easycard.PPR_Reset.OneDayQuotaWriteForMicroPayment;
import amobile.pc.io.RS232Port;

public class Console {
	
	private static final String scTimeZone = "Asia/Taipei";
	
	private static final String scCMASTestIP = "211.78.134.165";
    private static final int scCMASTestPortInternal = 7000;
    // private static final int scCMASTestPortOutside = 7100;
	
	private static int sUnixTimeStamp = 0;
	private static int sSerialNumber = 9302;
	
	private static CMASCallback sCallback = new CMASCallback();   
		
	public static void main(String[] args) {
		CMASClient cmasClient = new CMASClient(scCMASTestIP, scCMASTestPortInternal);
		cmasClient.start();
		
		String[] portNames = RS232Port.sGetPortList();
		if (portNames == null || portNames.length < 1) {
			System.exit(-1);
			return;
		}
		
		RS232Port port = new RS232Port();
		if(!port.OpenPort("COM6")){
		//if (!port.OpenPort(portNames[0])) {
			System.exit(-1);
			return;
		}
		
		// do_PPR_ReadCodeVersion(port);
		// do_PPR_ReadCardNumber(port);
		EDCSignOn edcSignOn = null;
		boolean bSigonOk = false;
		while (true) {
			edcSignOn = do_PPR_Reset(port, cmasClient);
			String respCode = edcSignOn.GetResp_ResponseCode();
			if (respCode.equals("00")) {
				bSigonOk = true;
				break;
			} else if (respCode.equals("19")) {
				sSerialNumber = Integer.valueOf(edcSignOn.GetResp_TMSerialNumber());
			}
		}
		if (bSigonOk && edcSignOn != null) {
			
		}
		
		port.ClosePort();

		System.exit(0);
		
		return; 
	}
	
	private static void do_PPR_ReadCodeVersion(RS232Port port) {
		PPR_ReadCodeVersion req = PPR_ReadCodeVersion.sGetInstance();
		if (!port.Write(req.GetRequest())) {
			port.ClosePort();
			return;
		}
		
		byte[] resp = port.Read(req.GetReqRespLength());
		if (resp != null && req.SetRespond(resp, resp.length)) {
			req.DumpRespData();
		}
	}
	
	private static void do_PPR_ReadCardNumber(RS232Port port) {
		PPR_ReadCardNumber req = PPR_ReadCardNumber.sGetInstance();
		req.SetReq_TXN_AMT(100);
		req.SetReq_LCDControlFlag(true);
		
		byte[] bytes = req.GetRequest();
		
		if (!port.Write(req.GetRequest())) {
			port.ClosePort();
			return;
		}
		
		int len = req.GetReqRespLength();
		
		byte[] resp = port.Read(9);
		if (resp != null && req.SetRespond(resp, resp.length)) {
			req.DumpRespData();
		}
	}
	
	private static EDCSignOn do_PPR_Reset(RS232Port port, CMASClient cmasClient) {
		PPR_Reset req = PPR_Reset.sGetInstance();
		req.SetOffline(false);

		req.SetReq_NewLocationID((short) 1);

		req.SetReq_TMLocationID("0000100011");

		req.SetReq_TMID("0");

		sUnixTimeStamp = (int) (System.currentTimeMillis() / 1000L);

		req.SetReq_TMTXNDateTime(sUnixTimeStamp);

		req.SetReq_TMSerialNumber(sSerialNumber);

		req.SetReq_TMAgentNumber("0");

		req.SetReq_TXNDateTime(sUnixTimeStamp, scTimeZone);

		req.SetReq_SAMSlotControlFlag(true, 1);

		req.SetReq_OneDayQuotaWriteForMicroPayment(OneDayQuotaWriteForMicroPayment.WNWC);

		req.SetReq_CheckEVFlagForMifareOnly(true);

		req.SetReq_MerchantLimitUse(true);

		req.SetReq_OneDayQuotaFlagForMicroPayment(OneDayQuotaFlagForMicroPayment.YCYA);

		req.SetReq_OnceQuotaFlagForMicroPayment(true);

		req.SetReq_PayOnBehalfFlag(true);

		req.SetReq_OneDayQuotaForMicroPayment((short) 2000);

		req.SetReq_OnceQuotaForMicroPayment((short) 500);
		
		if (!port.Write(req.GetRequest())) {
			port.ClosePort();
			return null;
		}
		
		byte[] resp = port.Read(req.GetReqRespLength());
		if (resp == null || !req.SetRespond(resp, resp.length)) {
			return null; 
		}
		
		String hostName = "Console_Class";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		EDCSignOn edcSignOn = null;
		try {
			edcSignOn = new EDCSignOn(req, sSerialNumber, hostName);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		
		String reqData = edcSignOn.GetRequestData();
		if (!cmasClient.SendRequest(sCallback, reqData)) {
			return null;
		}
		
		try {
			synchronized (sCallback) {
				sCallback.wait(10 * 1000); // 10 seconds
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	
		if (sCallback.IsSuccess()) {
			edcSignOn.SetResponse(sCallback.GetResponse());
			return edcSignOn; 
		} else {
			return null;
		}
	}
	
	private static void do_PPR_SignOn(RS232Port port, CMASClient cmasClient, EDCSignOn edcSignOn) {
		
	}
}
