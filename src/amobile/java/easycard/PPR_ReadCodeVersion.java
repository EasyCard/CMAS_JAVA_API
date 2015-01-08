package amobile.java.easycard;

import amobile.java.util.Util;

public class PPR_ReadCodeVersion extends ReqResp {
	
	public static final String scDescription = "讀取程式碼及新舊SAM卡版本編號"; 
	
	private static PPR_ReadCodeVersion sThis = null;
	
	private static final int scReqDataLength = 0;
	private static final int scReqLength = scReqDataLength + scReqMinLength_NoData; 
	private static final int scRespDataLength = 10;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;

	private byte mSAMAppletVersion = 0;
	private byte mSAMType = 0;
	private byte mSAMVersionNumber = 0;
	private byte[] mReaderFWVersion = new byte[6];
	private byte mSpecVersionNumber;
	
	public static PPR_ReadCodeVersion sGetInstance() {
		if (sThis == null) {
			sThis = new PPR_ReadCodeVersion();
		}
		return sThis;
	}
	
	private PPR_ReadCodeVersion() {
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = 0x05;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x51;			
		Req_P1 = 0x01;
		Req_P2 = 0x00;
			
		Req_Lc = 0x00;
		Req_Le = scRespDataLength;
		
		mRequest[0] = Req_NAD;
		mRequest[1] = Req_PCB;
		mRequest[2] = Req_LEN;
		mRequest[3] = Req_CLA;
		mRequest[4] = Req_INS;
		mRequest[5] = Req_P1;
		mRequest[6] = Req_P2;
		mRequest[7] = Req_Le;
		mRequest[8] = Req_EDC = getEDC(mRequest, mRequest.length);
	}
	
	@Override
	public byte[] GetRequest() {
		return mRequest;
	}
	
	@Override
	public boolean SetRequestData(byte[] bytes) {
		// does nothing on this class...
		return false;
	}
	
	@Override
	public int GetReqRespLength() {
		return scRespLength;
	}
	
	@Override
	public byte[] GetRespond() {
		return mRespond;
	}
	
	@Override
	public boolean SetRespond(byte[] bytes, int length) {
		if (bytes == null || scRespLength != length) {
			// invalid respond format... 
			return false;
		}
		
		if (bytes[2] != scRespDataLength + 2) { // data + SW1 + SW2
			// invalid data format...
			return false;
		}
		
		byte sum = getEDC(bytes, length);
		if (sum != bytes[scRespLength - 1]) {
			// check sum error...
			return false;
		}
		
		// This is a good Respond!
		mRespond = bytes;
		int dataLength = mRespond[2];
		Resp_SW1 = mRespond[3 + dataLength - 2];
		Resp_SW2 = mRespond[3 + dataLength + 1 - 2];
		
		mSAMAppletVersion = mRespond[3];
		mSAMType = mRespond[3 + 1];
		mSAMVersionNumber = mRespond[3 + 2];
		for (int i = 0; i < 6; i ++) {
			mReaderFWVersion[i] = mRespond[3 + 3 + i];
		}
		mSpecVersionNumber = mRespond[3 + 9];
		
		return true;
	}

	public byte GetRespSAMAppletVersion() {
		return mSAMAppletVersion;
	}
	
	public byte GetRespSAMType() {
		return mSAMType;
	}
	
	public byte GetRespSAMVersionNumber() {
		return mSAMVersionNumber;
	}
	
	public byte[] GetRespReaderFWVersion() {
		return mReaderFWVersion;
	}
	
	public byte GetRespSpecVersionNumber() {
		return mSpecVersionNumber;
	}
	
	// for debug only...
	/*
	public void DumpRespData(TextView txt) {
		txt.append("PPR_ReadCodeVersion (" + scDescription + ") Respond Data: \n");
		txt.append("SW1 = " + String.format("%02X", GetRespStatus1()) + "\n");
		txt.append("SW2 = " + String.format("%02X", GetRespStatus2()) + "\n");
		txt.append("SAM Applet Version = " + String.format("%02X", GetRespSAMAppletVersion()) + "\n");
		txt.append("SAM Type = " + String.format("%02X", GetRespSAMType()) + "\n");
		txt.append("SAM Version Number = " + String.format("%02X", GetRespSAMVersionNumber()) + "\n");
		txt.append("Reader FW Version = " + Util.sGetHexString(GetRespReaderFWVersion(), GetRespReaderFWVersion().length) + "\n");
		txt.append("Spec. Version Number = " + String.format("%02X", GetRespSpecVersionNumber()) + "\n");
	}
	*/
	
	public void DumpRespData() {
		System.out.println("PPR_ReadCodeVersion (" + scDescription + ") Respond Data: \n");
		System.out.println("Resp Code = " + String.format("%02X", GetRespCode()) + "\n");
		System.out.println("Resp Desc = " + String.format("%s", GetRespDescription()) + "\n");
		System.out.println("SAM Applet Version = " + String.format("%02X", GetRespSAMAppletVersion()) + "\n");
		System.out.println("SAM Type = " + String.format("%02X", GetRespSAMType()) + "\n");
		System.out.println("SAM Version Number = " + String.format("%02X", GetRespSAMVersionNumber()) + "\n");
		System.out.println("Reader FW Version = " + Util.sGetHexString(GetRespReaderFWVersion(), GetRespReaderFWVersion().length) + "\n");
		System.out.println("Spec. Version Number = " + String.format("%02X", GetRespSpecVersionNumber()) + "\n");
	}
}
