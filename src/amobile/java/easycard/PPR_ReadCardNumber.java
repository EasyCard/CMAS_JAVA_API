package amobile.java.easycard;

import java.util.Arrays;

public class PPR_ReadCardNumber extends ReqResp {
	public static final String scDescription = "讀取可卡號";
	
	private static PPR_ReadCardNumber sThis = null;
	
	private static final int scReqDataLength = 4;
	private static final int scReqLength = scReqDataLength + scReqMinLength; 
	private static final int scRespDataLength = 9;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	private byte[] mRequest = new byte[scReqLength]; 
	private byte[] mRespond = null;
	
	public static PPR_ReadCardNumber sGetInstance() {
		if (sThis == null) {
			sThis = new PPR_ReadCardNumber();
		}
		return sThis;
	}
	
	private PPR_ReadCardNumber() {
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = 0x05;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x51;			
		Req_P1 = 0x02;
		Req_P2 = 0x02;
			
		Req_Lc = 0x04;
		// Req_Data = null;
		Req_Le = scRespDataLength;
		
		mRequest[0] = Req_NAD;
		mRequest[1] = Req_PCB;
		mRequest[2] = Req_LEN;
		mRequest[3] = Req_CLA;
		mRequest[4] = Req_INS;
		mRequest[5] = Req_P1;
		mRequest[6] = Req_P2;
		mRequest[7] = Req_Lc;
		
		mRequest[scReqLength - 2] = Req_Le;
		mRequest[scReqLength - 1] = 0; // EDC
	}
	
	/* TXN AMT, 3 bytes, TM, 交易金額 */
	private static final int scReqData_TXN_AMT = scReqDataOffset;
	private static final int scReqData_TXN_AMT_Len = 3;
	public boolean SetReq_TXN_AMT(int amount) {
		if (amount > 0x00FFFFFF) {
			return false;
		}
		
		mRequest[scReqData_TXN_AMT + 2] = (byte) ((amount & 0x00FF0000) >> 16);
		mRequest[scReqData_TXN_AMT + 1] = (byte) ((amount & 0x0000FF00) >> 8);
		mRequest[scReqData_TXN_AMT] = (byte) (amount & 0x000000FF);
		
		mReqDirty = true;
		return true;
	}
	
	/* LCD Control Flag, 1 bytes, TM, 控制交易完成後之LCD顯示 */
	private static final int scReqData_LCDControlFlag = scReqData_TXN_AMT + scReqData_TXN_AMT_Len;
	private static final int scReqData_LCDControlFlag_Len = 1;
	public boolean SetReq_LCDControlFlag(boolean bDisplayAmount) {
		if (bDisplayAmount) {
			mRequest[scReqData_LCDControlFlag] = 0x01; // 顯示交易金額
		} else {
			mRequest[scReqData_LCDControlFlag] = 0x00; // 維持LCD顯示
		}
		
		mReqDirty = true;
		return true;
	}
	
	@Override
	public byte[] GetRequest() {
		if (mReqDirty) {
			mReqDirty = false;
			mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, mRequest.length);
		}
		return mRequest;
	}

	@Override
	public boolean SetRequestData(byte[] bytes) {
		if (bytes == null || bytes.length != scReqDataLength) {
			return false;
		}
		
		System.arraycopy(bytes, 0, mRequest, scReqDataOffset, scReqDataLength);
		
		mReqDirty = true;
		return true;
	}
	
	@Override
	public int GetReqRespLength() {
		return scRespLength;
	}
	
	/* Card Physical ID, 7 bytes, Card, Mifare卡號, 卡號長度為4 bytes時, 左靠右捕0 */
	private static final int scRespData_CardPhysicalID = scRespDataOffset;
	private static final int scRespData_CardPhysicalID_Len = 7;
	public byte[] GetResp_CardPhysicalID() {
		if (mRespond == null) {
			return null;
		}
		
		return Arrays.copyOfRange(mRespond, scRespData_CardPhysicalID, 
					scRespData_CardPhysicalID + scRespData_CardPhysicalID_Len);
	}
	
	/* Card Physical ID Length, 1 byte, Reader, Mifare卡號長度, 只可以是0x04或0x07 */
	private static final int scRespData_CardPhysicalIDLength = scRespData_CardPhysicalID + scRespData_CardPhysicalID_Len;
	private static final int scRespData_CardPhysicalIDLength_Len = 1;
	public int GetResp_CardPhysicalIDLength() {
		if (mRespond == null) {
			return 0;
		}
		
		return mRespond[scRespData_CardPhysicalIDLength];
	}
	
	/* Card Class, 1 byte, 票卡分類, 0x00: default(應該是Mifare卡), 0x01: 一代卡(含聯名卡), 0x02: 二代卡(CPU卡) */
	private static final int scRespData_CardClass = scRespData_CardPhysicalIDLength + scRespData_CardPhysicalIDLength_Len;
	private static final int scRespData_CardClass_Len = 1;
	public enum CardClass {
		mifare,		// Mifare Card
		gen1,		// 第一代卡
		gen2,		// 第二代卡
		invalid		// 非法卡別
	};
	public CardClass GetResp_CardClass() {
		if (mRespond == null) {
			return CardClass.invalid;
		}
		switch (mRespond[scRespData_CardClass]) {
		case 0x00:
			return CardClass.mifare;
		case 0x01:
			return CardClass.gen1;
		case 0x02:
			return CardClass.gen2;
		}
		
		return CardClass.invalid;
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
		
		if (bytes[2] != (byte) (scRespDataLength + 2)) { // Data + SW1 + SW2
			// invalid data format...
			return false;
		}
		
		byte sum = getEDC(bytes, length);
		if (sum != bytes[scRespLength - 1]) {
			// check sum error...
			return false;
		}
		
		mRespond = Arrays.copyOf(bytes, length);
		
		int dataLength = mRespond[2] & 0x000000FF;
		Resp_SW1 = mRespond[scRespDataOffset + dataLength - 2];
		Resp_SW2 = mRespond[scRespDataOffset + dataLength + 1 - 2];
		
		return true;
	}
	
	public void DumpRespData() {
		System.out.println("PPR_ReadCardNumber (" + scDescription + ") Respond Data: \n");
		System.out.println("Resp Code = " + String.format("%02X", GetRespCode()) + "\n");
		System.out.println("Resp Desc = " + String.format("%s", GetRespDescription()) + "\n");
		byte[] bytes = GetResp_CardPhysicalID();
		String cardId = "";
		if (bytes != null) {
			cardId = new String(bytes);
		}
		System.out.println("Card Physical ID = " + cardId + "\n");
		System.out.println("Card Physical ID Length = " + String.format("%d", GetResp_CardPhysicalIDLength()) + "\n");
		CardClass cardClass = GetResp_CardClass();
		switch (cardClass) {
		case mifare:
			System.out.println("Card Class = Mifare\n");
			break;
		case gen1:
			System.out.println("Card Class = 一代卡(含一代聯名卡)\n");
			break;
		case gen2:
			System.out.println("Card Class = 二代卡(CPU卡)\n");
			break;
		default:
			System.out.println("Card Class = 未知卡別\n");
			break;
		}
	}
}
