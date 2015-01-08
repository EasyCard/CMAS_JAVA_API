package amobile.java.cmas;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import amobile.java.easycard.PPR_Reset.CPDReadFlag;
import amobile.java.easycard.PPR_Reset.OneDayQuotaFlagForMicroPayment;
import amobile.java.easycard.PPR_Reset.OneDayQuotaWriteForMicroPayment;
import amobile.java.easycard.PPR_Reset.SAMSignOnControlFlag;
import amobile.java.util.Util;

public class ReqTagCreator {
	/* n 4, Message Type ID */
	public static final String scMessageTypeID = "T0100";
	public enum message_type {
		req,		// Request
		resp,		// Response
		adv			// Advise
	};
	public static String sGetMessageTypeID(message_type e) {
		String s = "";
		switch(e) {
		case req:
			s = MessageType.scDevControlReq;
			break;
		case resp: 
			s = MessageType.scDevControlReqResp;
			break;
		case adv:
			s = MessageType.scDevControlAdv;
			break;
		}
		s = "<" + scMessageTypeID + ">" + s + "</" + scMessageTypeID + ">";
		return s;
	}
	
	/* n, Card Physical ID, 悠遊卡號內碼, 一代卡4bytes轉成十進位的值, CPU卡7bytes轉成十進位的值, 1~20bytes */
	public static final String scCardPhysicalID = "T0200";
	public static String sGetCardPhysicalID(byte[] bytes, boolean bGen2) {
		if (bytes.length != 7) {
			return "";
		}
		
		byte[] b = new byte[8];
		Arrays.fill(b, (byte) 0);
		if (bGen2) {
			for (int i = 1; i < b.length; i ++) {
				b[i] = bytes[i - 1];
			}
		} else {
			for (int i = 4; i < b.length; i ++) {
				b[i] = bytes[i - 4];
			}
		}
		
		long l = Util.sByteToLong(b, 0, true);
		String s = "<" + scCardPhysicalID + ">" + String.format("%d", l) + "</" + scCardPhysicalID + ">";
		return s;
	}
	
	/* n, Purse ID(PID), 外觀卡號, 16bytes */
	public static final String scPID = "T0211";
	public static String sGetPID(byte[] bytes) {
		if (bytes.length != 8) {
			return "";
		}
		
		long l = Util.sByteToLong(bytes, 0, true);
		String s = "<" + scPID + ">" + String.format("%d", l) + "</" + scPID + ">";
		return s;
	}
	
	/* an, 悠遊卡auto-load flag, 二代卡Purse usage control bit3, 1 byte */
	public static final String scAutoLoadFlag = "T0212";
	public static String sGetAutoFloadFlag(boolean bAutoLoad) {
		String s = "0"; // 未開啟autoload功能
		if (bAutoLoad) {
			return "1"; // 已開啟autoload功能
		} 
		s = "<" + scAutoLoadFlag + ">" + s + "</" + scAutoLoadFlag + ">";
		return s;
	}
	
	/* an, Card type, 2 bytes */
	public static final String scCardType = "T0213";
	public enum card_type {
		normal, 			// 一般普通, 學生, 優待卡
		force_named, 		// 強制記名卡 (敬老, 愛心,及未來校園學生卡)
		normal_named,		// 一般記名卡 (悠遊卡公司發行的普通, 學生卡)
		memorial,			// 悠遊卡公司發行的紀念卡
		replacement, 		// 替代卡 (社會局提供的敬老愛心替代卡)
		special_made,		// 特製卡
		ic,					// IC商品
		test,				// 測試卡
		co_brander,			// 聯名卡
		special_type,		// 特種卡 (觀光護照, 一日票...等)
		digital_student,	// 數位學生證
		token,				// 代幣卡
		bike,				// 腳踏車
		icash,				// iCash卡
	};
	public static String sGetCardType(card_type e) {
		String s = "";
		switch (e) {
		case normal: 			// 一般普通, 學生, 優待卡
			s = "00";
			break;
		case force_named: 		// 強制記名卡 (敬老, 愛心,及未來校園學生卡)
			s = "01";
			break;
		case normal_named:		// 一般記名卡 (悠遊卡公司發行的普通, 學生卡)
			s = "02";
			break;
		case memorial:			// 悠遊卡公司發行的紀念卡
			s = "03";
			break;
		case replacement: 		// 替代卡 (社會局提供的敬老愛心替代卡)
			s = "04";
			break;
		case special_made:		// 特製卡
			s = "05";
			break;
		case ic:				// IC商品
			s = "06";
			break;
		case test:				// 測試卡
			s = "07";
			break;
		case co_brander:		// 聯名卡
			s = "08";
			break;
		case special_type:		// 特種卡 (觀光護照, 一日票...等)
			s = "09";
			break;
		case digital_student:	// 數位學生證
			s = "0A";
			break;
		case token:				// 代幣卡
			s = "0B";
			break;
		case bike:				// 腳踏車
			s = "0C";
			break;
		case icash:				// iCash卡
			s = "0D";
			break;
		}
		s = "<" + scCardType + ">" + s + "</" + scCardType + ">";
		return s; 
	}
	
	/* b, Personal Profile, 2 bytes */
	public static final String scPersonalProfile = "T0214";
	public enum personal_profile {
		normal, 		// 普通 (to DTS : 0x01)
		elder1,			// 敬老一 (to DTS : 0x02)
		elder2, 		// 敬老二 (to DTS : 0x03)
		compassion,		// 愛心 (to DTS : 0x04)
		accompany,		// 愛陪 (to DTS : 0x05)
		student,		// 學生 (to DTS : 0x06)
		soldier,		// 軍人 (to DTS : 0x07)
		police,			// 警察 (to DTS : 0x08)
		children		// 孩童 (to DTS : 0x09)
	}
	public static String sGetPersonalProfile(personal_profile e) {
		String s = "";
		switch (e) {
		case normal: 		// 普通 (to DTS : 0x01)
			s = "00";
			break;
		case elder1:		// 敬老一 (to DTS : 0x02)
			s = "01";
			break;
		case elder2: 		// 敬老二 (to DTS : 0x03)
			s = "02";
			break;
		case compassion:	// 愛心 (to DTS : 0x04)
			s = "03";
			break;
		case accompany:		// 愛陪 (to DTS : 0x05)
			s = "04";
			break;
		case student:		// 學生 (to DTS : 0x06)
			s = "05";
			break;
		case soldier:		// 軍人 (to DTS : 0x07)
			s = "06";
			break;
		case police:		// 警察 (to DTS : 0x08)
			s = "07";
			break;
		case children:		// 孩童 (to DTS : 0x09)
			s = "08";
			break;
		}
		
		s = "<" + scPersonalProfile + ">" + s + "</" + scPersonalProfile + ">";
		return s;
	}
	
	/* n 6, Processing Code */
	public static final String scProcessingCode = "T0300";
	public static String sGetProcessingCode(ProcessingCode.code e) {
		String s = "";
		switch (e) {
		case ParamsDownload: 				/* 參數下載, TMS system參數下載 */
			s = ProcessingCode.scParamsDownload;
			break;
		case SignOnTMSSystem: 				/* 末端設備對TMS system進行sign on */
			s = ProcessingCode.scSignOnTMSSystem;
			break;
		case StatusReport:					/* 末端設備對TMS system回報端末設備狀態 */
			s = ProcessingCode.scStatusReport;
			break;
		case Settlement:					/* 結帳 */
			s = ProcessingCode.scSettlement;
			break;
		case InquiryCardTradeDetail6: 		/* 悠遊卡卡片交易明細查詢, 6筆 */
			s = ProcessingCode.scInquiryCardTradeDetail6;
			break;
		case InquiryDongleTradeDetail1K:	/* 悠遊卡Dongle交易明細查詢, 1000筆 */
			s = ProcessingCode.scInquiryDongleTradeDetail1K;
			break;
		case InquiryCardData:				/* 悠遊卡卡片資料查詢 */
			s = ProcessingCode.scInquiryCardData;
			break;
		case InquiryBalance:				/* 餘額查詢 */
			s = ProcessingCode.scInquiryBalance;
			break;
		case ExhibitCardLock:				/* 悠遊卡展期鎖卡 */
			s = ProcessingCode.scExhibitCardLock;
			break;
		case CardLock:						/* 悠遊卡鎖卡 */
			s = ProcessingCode.scCardLock;
			break;
		case EDCSignOn:						/* EDC Sign On */
			s = ProcessingCode.scEDCSignOn;
			break;
		case CashAddCredit:					/* 現金加值 */
			s = ProcessingCode.scCashAddCredit;
			break;
		case SocialCareCashAddCredit:		/* 社福卡現金加值 */
			s = ProcessingCode.scSocialCareCashAddCredit;
			break;
		case AutoAddCredit:					/* 自動加值 */
			s = ProcessingCode.scAutoAddCredit;
			break;
		case SocialCareAutoAddCredit:		/* 社福卡自動加值 */
			s = ProcessingCode.scSocialCareAutoAddCredit;
			break;
		case ATMAddCredit:					/* 金融卡加值 */
			s = ProcessingCode.scATMAddCredit;
			break;
		case SocialCareATMAddCredit:		/* 社福卡金融卡加值 */
			s = ProcessingCode.scSocialCareATMAddCredit;
			break;
		case XformRefundAddCredit: 			/* 餘額轉置加值 */
			s = ProcessingCode.scXformRefundAddCredit;
			break;
		case CancelAddCredit:				/* 取消加值 */
			s = ProcessingCode.scCancelAddCredit;
			break;
		case Merchandise: 					/* 購貨 */
			s = ProcessingCode.scMerchandise;
			break;
		case CancelMerchandise:				/* 取消購貨 */
			s = ProcessingCode.scCancelMerchandise;
			break;
		case SellCard:						/* 售卡 */
			s = ProcessingCode.scSellCard;
			break;
		case CancelSellCard:				/* 取消售卡 */
			s = ProcessingCode.scCancelSellCard;
			break;
		case Exhibit:						/* 展期 */
			s = ProcessingCode.scExhibit;
			break;
		case TXNAuth:						/* 交易合法驗證 */
			s = ProcessingCode.scTXNAuth;
			break;
		case ReturnMerchandise:				/* 退貨 */
			s = ProcessingCode.scReturnMerchandise;
			break;
		case ReturnATMCard:					/* 聯名卡退卡 */
			s = ProcessingCode.scReturnATMCard;
			break;
		case XformRefundReturnCard:			/* 餘額轉置退卡 */
			s = ProcessingCode.scXformRefundReturnCard;
			break;
		case CoBranderCardAutoCredit:		/* 聯名卡自動加值功能開啟 */
			s = ProcessingCode.scCoBranderCardAutoCredit;
			break;
		case CPUCashAddCredit:				/* CPU現金加值 */
			s = ProcessingCode.scCPUCashAddCredit;
			break;
		case CPUSocialCareCashAddCredit:	/* CPU社福卡現金加值 */
			s = ProcessingCode.scCPUSocialCareCashAddCredit;
			break;
		case CPUAutoAddCredit:				/* CPU自動加值 */
			s = ProcessingCode.scCPUAutoAddCredit;
			break;
		case CPUSocialCareAutoAddCredit:	/* CPU社福卡自動加值 */
			s = ProcessingCode.scCPUSocialCareAutoAddCredit;
			break;
		case CPUATMAddCredit:				/* CPU金融卡加值 */
			s = ProcessingCode.scCPUATMAddCredit;
			break;
		case CPUSocialCareATMAddCredit:		/* CPU社福卡金融卡加值 */
			s = ProcessingCode.scCPUSocialCareATMAddCredit;
			break;
		case CPUXformRefundAddCredit:		/* CPU餘額轉置加值 */
			s = ProcessingCode.scCPUXformRefundAddCredit;
			break;
		case CPUCancelAddCredit:			/* CPU取消加值 */
			s = ProcessingCode.scCPUCancelAddCredit;
			break;
		case CPUMerchandise:				/* CPU購貨 */
			s = ProcessingCode.scCPUMerchandise;
			break;
		case CPUCancelMerchandise:			/* CPU取消購貨 */
			s = ProcessingCode.scCPUCancelMerchandise;
			break;
		case CPUXformRefundMerchandise:		/* CPU餘額轉置購貨 */
			s = ProcessingCode.scCPUXformRefundMerchandise;
			break;
		case CPUSellCard:					/* CPU售卡 */
			s = ProcessingCode.scCPUSellCard;
			break;
		case CPUCancelSellCard:				/* CPU取消售卡 */
			s = ProcessingCode.scCPUCancelSellCard;
			break;
		case CPUExhibit:					/* CPU展期 */
			s = ProcessingCode.scCPUExhibit;
			break;
		case CPUTXNAuth:					/* CPU交易合法驗證 */
			s = ProcessingCode.scCPUTXNAuth;
			break;
		case CPUReturnMerchandise:			/* CPU退貨 */
			s = ProcessingCode.scCPUReturnMerchandise;
			break;
		case CPUReturnCard:					/* CPU退卡 */
			s = ProcessingCode.scCPUReturnCard;
			break;
		case CPUXformRefundReturnCard:		/* CPU餘額轉置退卡 */
			s = ProcessingCode.scCPUXformRefundReturnCard;
			break;
		case CPUCoBranderCardAutoCredit:	/* CPU聯名卡自動加值功能開啟 */
			s = ProcessingCode.scCPUCoBranderCardAutoCredit;
			break;
		}
		s = "<" + scProcessingCode + ">" + s + "</" + scProcessingCode + ">";
		return s;
	}
	
	/* n 2, 悠遊卡dongle版本編號 */
	public static final String scDongleVer = "T0301";
	public static String sGetDongleVersion(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		for (int i = 0; i < bytes.length; i ++) {
			if (bytes[i] < '0' || bytes[i] > '9') {
				return "";
			}
		}
		String s = new String(bytes);
		s = "<" + scDongleVer + ">" + s + "</" + scDongleVer + ">";
		return s;
	}
	
	/* n <= 12, TXN AMT, 交易金額 */
	public static final String scTXNAMT = "T0400";
	public static String sGetTXNAMT(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scTXNAMT + ">" + s + "</" + scTXNAMT + ">";
		return s;
	}
	
	/* n <= 12, 悠遊卡扣款金額 */
	public static final String scDebitAmount = "T0403";
	public static String sGetDebitAmount(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scDebitAmount + ">" + s + "</" + scDebitAmount + ">";
		return s;
	}
	
	/* n <= 12, 悠遊卡加值或退款金額 */
	public static final String scAddOrReturnAmount = "T0407";
	public static String sGetAddOrReturnAmount(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scAddOrReturnAmount + ">" + s + "</" + scAddOrReturnAmount + ">";
		return s;
	}
	
	/* n <= 12, Purse Balance, 悠遊卡交易後餘額, 3 binary byte to 10進制數字, Signed and LSB First, 正值左靠右補0, 負值左靠右補FF */
	public static final String scPurseBalance = "T0408";
	public static String sGetPurseBalance(byte[] bytes) {
		if (bytes == null || bytes.length != 3) {
			return "";
		}
		byte[] b = new byte[4];
		for (int i = 0; i < 3; i ++) {
			b[i] = bytes[i];
		}
		if (b[2] == 0) {
			b[3] = 0;	// 正值
		} else {
			b[3] = (byte) 0xFF; // 負值
		}
		int i = Util.sByteToInt(b, 0, false);
		String s = String.valueOf(i);
		s = "<" + scPurseBalance + ">" + s + "</" + scPurseBalance + ">";
		return s;
	}
	
	/* n <= 12, Single auto-load transaction amount, 悠遊卡自動加值金額 */
	public static final String scSingleAutoLoadTansacAmount = "T0409";
	public static String sGetSingleAutoLoadTansacAmount(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scSingleAutoLoadTansacAmount + ">" + s + "</" + scSingleAutoLoadTansacAmount + ">";
		return s;
	}
	
	/* n <= 12, Purse Balance Before TXN, 悠遊卡交易前餘額 */
	public static final String scPurseBalanceBeforeTXN = "T0410";
	public static String sGetPurseBalanceBeforeTXN(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scPurseBalanceBeforeTXN + ">" + s + "</" + scPurseBalanceBeforeTXN + ">";
		return s;
	}
	
	/* n 14, 交易轉送日期時間, yyyymmddhhnnss */
	public static final String scPurseDeliverDateTime = "T0700";
	public static String sGetPurseDeliverDateTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String s = sdfDate.format(now);
		s = "<" + scPurseDeliverDateTime + ">" + s + "</" + scPurseDeliverDateTime + ">";
		return s;
	}
	
	/* n 6, TM Serial Number, 端末交易序號, 左補0至6位 */
	public static final String scTMSerialNumber = "T1100";
	public static String sGetTMSerialNumber(int sn, boolean bTag) {
		if (sn < 0 || sn > 999999) {
			return "";
		}
		String s = String.format("%06d", sn);
		if (bTag) {
			s = "<" + scTMSerialNumber + ">" + s + "</" + scTMSerialNumber + ">";
		}
		return s;
	}
	
	/* n 6, 收銀機交易序號, 左補0至6位 */
	public static final String scCashBoxSerialNumber = "T1101";
	public static String sGetCashBoxSerialNumber(int sn, boolean bTag) {
		if (sn < 0 || sn > 999999) {
			return "";
		}
		String s = String.format("%06d", sn);
		if (bTag) {
			s = "<" + scCashBoxSerialNumber + ">" + s + "</" + scCashBoxSerialNumber + ">";
		}
		return s;
	}
	
	/* n 6, EDC交易時間, hhmmss, bytes came from TM TXN Date Time 14 bytes */
	public static final String scEDCTime = "T1200";
	public static String sGetEDCTime(String time) {
		if (time == null || time.length() != 6) {
			return "";
		}
		String s = "<" + scEDCTime + ">" + time + "</" + scEDCTime + ">";
		return s;
	}
	
	/* n 6, TM TXN Time, hhmmss, 收銀機交易時間, 若無收銀機連線, 填入與T1200相同值 */
	public static final String scTMTXNTime = "T1201";
	public static String sGetTMTXNTime(String time) {
		if (time == null || time.length() != 6) {
			return "";
		}
		String s = "<" + scTMTXNTime + ">" + time + "</" + scTMTXNTime + ">";
		return s;
	}
	
	/* n 8, EDC交易日期, yyyymmdd */
	public static final String scEDCDate = "T1300";
	public static String sGetEDCDate(String date, boolean bTag) {
		if (date == null || date.length() != 8) {
			return "";
		}
		String s = date;
		if (bTag) {
			s = "<" + scEDCDate + ">" + s + "</" + scEDCDate + ">";
		}
		return s;
	}
	
	/* n 8, TM TXN Date, yyyymmdd, 收銀機交易日期, 若無收銀機連線, 填入與T1300相同值 */
	public static final String scTMTXNDate = "T1301";
	public static String sGetTMTXNDate(String date) {
		if (date == null || date.length() != 8) {
			return "";
		}
		String s = "<" + scTMTXNDate + ">" + date + "</" + scTMTXNDate + ">";
		return s;
	}
	
	/* n 4, Card expiry date, YYMM */
	public static final String scCardExpiryDate = "T1400";
	public static String sGetCardExpiryDate(String yymm) {
		if (yymm == null || yymm.length() != 4) {
			return "";
		}
		try {
			Integer.parseInt(yymm);
		} catch (NumberFormatException e) {
			return "";
		}
		String s = "<" + scCardExpiryDate + ">" + yymm + "</" + scCardExpiryDate + ">";
		return s;
	}
	
	/* n 8, Purse Exp. date, 悠遊卡有效期, YYYYMMDD */
	public static final String scPurseExpiryDate = "T1402";
	public static String sGetPurseExpiryDate(String yyyymmdd) {
		if (yyyymmdd == null || yyyymmdd.length() != 8) {
			return "";
		}
		try {
			Integer.parseInt(yyyymmdd);
		} catch (NumberFormatException e) {
			return "";
		}
		String s = "<" + scPurseExpiryDate + ">" + yyyymmdd + "</" + scPurseExpiryDate + ">";
		return s;
	}
	
	/* n 8, New Purse Exp. date, 悠遊卡有效期, YYYYMMDD */
	public static final String scNewPurseExpiryDate = "T1403";
	public static String sGetNewPurseExpiryDate(String yyyymmdd) {
		if (yyyymmdd == null || yyyymmdd.length() != 8) {
			return "";
		}
		try {
			Integer.parseInt(yyyymmdd);
		} catch (NumberFormatException e) {
			return "";
		}
		String s = "<" + scNewPurseExpiryDate + ">" + yyyymmdd + "</" + scNewPurseExpiryDate + ">";
		return s;
	}
	
	/* n 14, Retrieval Reference Number */
	public static final String scRetrievalReferenceNumber = "T3700";
	public static String sGetRetrievalReferenceNumber(String date, String sn) {
		if (date == null || date.length() != 8) {
			return "";
		}
		if (sn == null || sn.length() != 6) {
			return "";
		}
		String s = "<" + scRetrievalReferenceNumber + ">" + date + sn + "</" + scRetrievalReferenceNumber + ">";
		return s;
	}
	
	/* an <= 20, 收銀機交易單號, 收銀機傳送之交易唯一編號, 用於收銀機勾稽收銀機與EDC之交易應對 */
	public static final String scCashBoxNumber = "T3701";
	public static String sGetCashBoxNumber(byte[] bytes) {
		if (bytes == null || bytes.length > 20) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scCashBoxNumber + ">" + s + "</" + scCashBoxNumber + ">";
		return s;
	}
	
	/* an 6, 交易授權碼 */
	public static final String scAuthorTranscNumber = "T3800";
	public static String sGetAuthorTranscNumber(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		} 
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scAuthorTranscNumber + ">" + s + "</" + scAuthorTranscNumber + ">";
		return s;
	}
	
	/* an 2, Response Code */
	public static final String scResponseCode = "T3900";
	public static String sGetResponseCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scResponseCode + ">" + s + "</" + scResponseCode + ">";
		return s;
	}
	
	/* n, API return code */
	public static final String scAPIReturnCode = "T3901";
	public static String sGetAPIReturnCode(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		String s = Util.sGetAttr_n(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scAPIReturnCode + ">" + s + "</" + scAPIReturnCode + ">";
		return s;
	}
	
	/* an 2, EZ host response code */
	public static final String scEZResponseCode = "T3902";
	public static String sGetEZResponseCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scEZResponseCode + ">" + s + "</" + scEZResponseCode + ">";
		return s;
	}
	
	/* an 2, SVCS response code */
	public static final String scSVCSResponseCode = "T3903";
	public static String sGetSVCSResponseCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scSVCSResponseCode + ">" + s + "</" + scSVCSResponseCode + ">";
		return s;
	}
	
	/* n 4, Reader SW */
	public static final String scReaderSW = "T3904";
	public static String sGetReaderSW(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_n(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scReaderSW + ">" + s + "</" + scReaderSW + ">";
		return s;
	}

	/* an 4, 結帳請款差異response code */
	public static final String scPayforBillDiff = "T3911";
	public static String sGetPayforBillDiff(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scPayforBillDiff + ">" + s + "</" + scPayforBillDiff + ">";
		return s;
	}
	
	/* b 12, New device ID, 端末設備代號, 格式為zzyxxx共6個bytes解開為12個bytes, 
	 * xxx為New SP ID(T4200), y為new device type, zz為device ID序號 */
	public static final String scNewDeviceID = "T4100";
	public static String sGetNewDeviceID(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scNewDeviceID + ">" + s + "</" + scNewDeviceID + ">";
		return s;
	}
	
	/* b 8, Device ID, 舊的Device ID, 4個bytes解開為8個bytes, 前2位為舊SP ID, 舊SP ID須轉入CCHS */
	public static final String scDeviceID = "T4101";
	public static String sGetDeviceID(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scDeviceID + ">" + s + "</" + scDeviceID + ">";
		return s;
	}
	
	/* an 15, 端末設備目前IP */
	public static final String scDeviceIP = "T4102";
	public static String sGetDeviceIP() {
		String s = Util.sGetLocalIpAddress();
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scDeviceIP + ">" + s + "</" + scDeviceIP + ">";
		return s;
	}
	
	/* an <= 30, 端末設備機器序號 */
	public static final String scDeviceSerialNumber = "T4103";
	public static String sGetDeviceSerialNumber(String sn) {
		if (sn == null || sn.length() > 30) {
			return "";
		}
		String s = sn;
		s = "<" + scDeviceSerialNumber + ">" + s + "</" + scDeviceSerialNumber + ">";
		return s;
	}
	
	/* b 8, Reader ID, Reader機器序號, 4 bytes解開為8 bytes */
	public static final String scReaderID = "T4104";
	public static String sGetReaderID(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scReaderID + ">" + s + "</" + scReaderID + ">";
		return s;
	}
	
	/* an 8, New SP ID, 特約商店代號, 填入New Service Provider ID, 
	 * Unsigned and LSB first, 3bytes轉十進制8bytes, 左補0至8位 */
	public static final String scNewSPID = "T4200";
	public static String sGetNewSPID(String id) {
		if (id == null || id.length() != 8) {
			return "";
		}
		String s = "<" + scNewSPID + ">" + id + "</" + scNewSPID + ">";
		return s;
	}
	
	/* an <= 30, New Location ID, 分店代號 */
	public static final String scNewLocationID30 = "T4201";
	public static String sGetNewLocationD30(byte[] bytes) {
		if (bytes == null || bytes.length > 30) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scNewLocationID30 + ">" + s + "</" + scNewLocationID30 + ">";
		return s;
	}
	
	/* an <= 30, 連絡電話 */
	public static final String scPhoneNumber = "T4202";
	public static String sGetPhoneNumber(String number) {
		if (number == null || number.length() > 30) {
			return "";
		}
		String s = "<" + scPhoneNumber + ">" + number + "</" + scPhoneNumber + ">";
		return s;
	}
	
	/* an <= 30, 連絡電話 2 */
	public static final String scPhoneNumber2 = "T4203";
	public static String sGetPhoneNumber2(String number) {
		if (number == null || number.length() > 30) {
			return "";
		}
		String s = "<" + scPhoneNumber2 + ">" + number + "</" + scPhoneNumber2 + ">";
		return s;
	}
	
	/* an <= 30, 傳真號碼 */
	public static final String scFaxNumber = "T4204";
	public static String sGetFaxNumber(String number) {
		if (number == null || number.length() > 30) {
			return "";
		}
		String s = "<" + scFaxNumber + ">" + number + "</" + scFaxNumber + ">";
		return s;
	}
	
	/* an <= 200, 營業地址 */
	public static final String scBizAddress = "T4205";
	public static String sGetBizAddress(String addr) {
		if (addr == null || addr.length() > 200) {
			return "";
		}
		String s = "<" + scBizAddress + ">" + addr + "</" + scBizAddress + ">";
		return s;
	}
	
	/* n <= 30, 營業地址區碼, ZIP code, 三碼範例: '105', 五碼範例: '10548' */
	public static final String scZipCode = "T4206";
	public static String sGetZipCode(String code) {
		if (code == null || code.length() > 30) {
			return "";
		}
		String s = "<" + scZipCode + ">" + code + "</" + scZipCode + ">";
		return s;
	}
	
	/* a <= 30, 有效店/無效店註記, 'A': 有效店, 'B': 失效店 */
	public static final String scValidStoreMark = "T4207";
	public static String sGetValidStoreMark(boolean bValid) {
		String s = bValid? "A" : "B";
		s = "<" + scValidStoreMark + ">" + s + "</" + scValidStoreMark + ">";
		return s;
	}
	
	/* n 8, 門市啟用日期, YYYYMMDD */
	public static final String scStoreOpenDate = "T4208";
	public static String sGetStoreOpenDate(String date) {
		if (date == null || date.length() != 6) {
			return "";
		}
		String s = "<" + scStoreOpenDate + ">" + date + "</" + scStoreOpenDate + ">";
		return s;
	}
	
	/* n 8, 門市關閉日期, YYYYMMDD */
	public static final String scStoreCloseDate = "T4209";
	public static String sGetStoreCloseDate(String date) {
		if (date == null || date.length() != 6) {
			return "";
		}
		String s = "<" + scStoreCloseDate + ">" + date + "</" + scStoreCloseDate + ">";
		return s;
	}
	
	/* n <= 5, 填入New Location ID, Unsigned and LSB First, 2bytes轉為10進制的五位數字, 
	 * 左邊兩位(byte 1)為舊Location ID */
	public static final String scNewLocationID5 = "T4210";
	public static String sGetNewLocationID5(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		if (i > 99999) { // 最大5位數
			return "";
		}
		String s = String.format("%d", i);
		s = "<" + scNewLocationID5 + ">" + s + "</" + scNewLocationID5 + ">";
		return s;
	}
	
	/* 
	 * Card 相關欄位 
	 */
	
	/* b 2, Purse Version Number (PVN), 00: Mifare, 01: level 1, 02: level 2 */
	public static final String scPurseVersionNumber = "T4800";
	public enum purse_type {
		mifare,
		level1,
		level2
	};
	public static String sGetPurseVersionNumber(purse_type e) {
		String s = "";
		switch (e) {
		case mifare:
			s = "00";
			break;
		case level1:
			s = "01";
			break;
		case level2:
			s = "02";
			break;
		}
		s = "<" + scPurseVersionNumber + ">" + s + "</" + scPurseVersionNumber + ">";
		return s;
	}
	
	/* b, Last Credit TXN Log (Card AVR data), 卡片最後一筆加值交易紀錄, 
	 * 50 bytes for Gen1 card and 66 bytes for Gen2 card,  */
	public static final String scLastCreditTXNLog = "T4801";
	public class LastCreditTXNLogGen1 { // 25 bytes in total
		byte MSG_type;
		byte TXN_LSB_SNum;		// LSB
		byte[] TXN_date_time = new byte[4];
		byte Subtype;
		byte[] TXN_AMT = new byte[2];
		byte[] EV = new byte[2];				
		byte[] CardPhyID = new byte[4];
		byte IssuerCode;
		byte ServiceProvider;
		byte LocationID;
		byte[] DeviceID = new byte[4];
		byte[] BankCode;
		byte[] LoyaltyCounter = new byte[2];
	};
	public static String sGetLastCreditTXNLogGen1(LastCreditTXNLogGen1 g1) {
		String s = "";
		if (g1 == null) {
			return s;
		}
		
		s += Util.sGetAttr_b(g1.MSG_type);
		s += Util.sGetAttr_b(g1.TXN_LSB_SNum);
		s += Util.sGetAttr_b(g1.TXN_date_time);
		s += Util.sGetAttr_b(g1.Subtype);
		s += Util.sGetAttr_b(g1.TXN_AMT);
		s += Util.sGetAttr_b(g1.EV);
		s += Util.sGetAttr_b(g1.CardPhyID);
		s += Util.sGetAttr_b(g1.IssuerCode);
		s += Util.sGetAttr_b(g1.ServiceProvider);
		s += Util.sGetAttr_b(g1.LocationID);		
		s += Util.sGetAttr_b(g1.DeviceID);
		s += Util.sGetAttr_b(g1.BankCode);
		s += Util.sGetAttr_b(g1.LoyaltyCounter);
		s = "<" + scLastCreditTXNLog + ">" + s + "</" + scLastCreditTXNLog + ">";
		return s;
	}
	public class LastCreditTXNLogGen2 { // 33 bytes in total
		byte PurseVersionNumber; // PVN
		byte[] TSQN = new byte[3];
		byte[] TXN_date_time = new byte[4];
		byte Subtype;
		byte[] TXN_AMT = new byte[3];
		byte[] EV = new byte[3];
		byte[] CPUServiceProvideID = new byte[3];
		byte[] LocationID = new byte[2]; // PVN=2時為新場站代號, PVN<>2時為舊場站代號
		byte[] DeviceID = new byte[6]; // PVN=2時為新場站代號, PVN<>2時為舊場站代號
		byte RFU; // PVN=2時固定為02, PVN<>2時固定為00
		byte[] CPU_EV = new byte[3]; // 交易後餘額(由CPU卡計算), PVN=02時有效, 其他補000000
		byte[] CPU_TSQN = new byte[3]; // 交易序號(由CPU卡計算), PVN=02時有效, 其他補000000
	};
	public static String sGetLastCreditTXNLogGen2(LastCreditTXNLogGen2 g2) {
		String s = "";
		if (g2 == null) {
			return s;
		}
		
		s += Util.sGetAttr_b(g2.PurseVersionNumber);
		s += Util.sGetAttr_b(g2.TSQN);
		s += Util.sGetAttr_b(g2.TXN_date_time);
		s += Util.sGetAttr_b(g2.Subtype);
		s += Util.sGetAttr_b(g2.TXN_AMT);
		s += Util.sGetAttr_b(g2.EV);
		s += Util.sGetAttr_b(g2.CPUServiceProvideID);
		s += Util.sGetAttr_b(g2.LocationID);
		s += Util.sGetAttr_b(g2.DeviceID);
		s += Util.sGetAttr_b(g2.RFU);		
		s += Util.sGetAttr_b(g2.CPU_EV);
		s += Util.sGetAttr_b(g2.CPU_TSQN);
		s = "<" + scLastCreditTXNLog + ">" + s + "</" + scLastCreditTXNLog + ">";
		return s;
	}
	
	/* b 2, Issuer Code, 02: 悠遊卡公司, 04: 基隆 */
	public static final String scIssuerCode = "T4802";
	public enum Issuer {
		easycard,
		keelung,
		unknown
	};
	public static String sGetIssuerCode(Issuer issuer) {
		String s = "";
		switch (issuer) {
		case easycard:
			s = "02";
			break;
		case keelung:
			s = "04";
			break;
		default:
			s = "00";
			break;
		}
		s = "<" + scIssuerCode + ">" + s + "</" + scIssuerCode + ">";
		return s;
	}
	
	/* b 2, Bank Code */
	public static final String scBankCode = "T4803";
	public enum bank_code {
		easycard,		// 00, 遊遊卡公司
		cathaybank,		// 31, 國泰世華
		taishinbank,	// 32, 台新
		ctbcbank,		// 33, 中國信託
		fubon,			// 34, 台北富邦
		megabank,		// 36, 兆豐國際商銀
		firstbank,		// 37, 第一銀
		hsbc,			// 38, 匯豐銀行
		esunbank,		// 39, 玉山銀行
	};
	public static String sGetBankCode(bank_code e) {
		String s = "";
		switch (e) {
		case easycard:		// 00, 遊遊卡公司
			s = "00";
			break;
		case cathaybank:	// 31, 國泰世華
			s = "31";
			break;
		case taishinbank:	// 32, 台新
			s = "32";
			break;
		case ctbcbank:		// 33, 中國信託
			s = "33";
			break;
		case fubon:			// 34, 台北富邦
			s = "34";
			break;
		case megabank:		// 36, 兆豐國際商銀
			s = "36";
			break;
		case firstbank:		// 37, 第一銀
			s = "37";
			break;
		case hsbc:			// 38, 匯豐銀行
			s = "38";
			break;
		case esunbank:		// 39, 玉山銀行
			s = "39";
			break;
		}
		s = "<" + scBankCode + ">" + s + "</" + scBankCode + ">";
		return s;
	}
	
	/* b 2, Area Code */
	public static final String scAreaCode = "T4804";
	public enum area_code {
		taipei,			// 01, 台北市
		newtaipei,		// 02, 新北市
		keelung,		// 03, 基隆市
		taoyuan,		// 04, 桃園縣
		yilan,			// 05, 宜蘭
		matsu,			// 06, 連江縣
		taipeilove,		// 0E, 台北市愛心二(personal profile必須為03)
		newtaipeilove	// 0F, 新台北市愛心二(personal profile必須為03)
	};
	public static String sGetAreaCode(area_code e) {
		String s = "";
		switch (e) {
		case taipei:		// 01, 台北市
			s = "01";
			break;
		case newtaipei:		// 02, 新北市
			s = "02";
			break;
		case keelung:		// 03, 基隆市
			s = "03";
			break;
		case taoyuan:		// 04, 桃園縣
			s = "04";
			break;
		case yilan:			// 05, 宜蘭
			s = "05";
			break;
		case matsu:			// 06, 連江縣
			s = "06";
			break;
		case taipeilove:	// 0E, 台北市愛心二(personal profile必須為03)
			s = "0E";
			break;
		case newtaipeilove:	// 0F, 新台北市愛心二(personal profile必須為03)
			s = "0F";
			break;
		}
		s = "<" + scAreaCode + ">" + s + "</" + scAreaCode + ">";
		return s;
	}

	/* b 4, CPU Sub Area Code, Unsigned and LSB First */
	public static final String scCPUSubAreaCode = "T4805";
	public static String sGetCPUSubAreaCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s  = Util.sGetAttr_b(bytes);
		s = "<" + scCPUSubAreaCode + ">" + s + "</" + scCPUSubAreaCode + ">";
		return s;
	}
	
	/* b 8, ProfileExp. Date, 身分到期日, UnixDateTime, Unsigned and LSB First */
	public static final String scProfileExpDate = "T4806";
	public static String sGetProfileExpDate(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s  = Util.sGetAttr_b(bytes);
		s = "<" + scProfileExpDate + ">" + s + "</" + scProfileExpDate + ">";
		return s;
	}

	/* b 8, New ProfileExp. Date, 新身分到期日, UnixDateTime, Unsigned and LSB First */
	public static final String scNewProfileExpDate = "T4807";
	public static String sGetNewProfileExpDate(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s  = Util.sGetAttr_b(bytes);
		s = "<" + scNewProfileExpDate + ">" + s + "</" + scNewProfileExpDate + ">";
		return s;
	}

	/* n 6, TSQN, 票卡交易序號, Unsigned and LSB, Mifare: 填入2bytes轉為十進制的值, CPU: 填入3bytes轉為十進制的值 */
	public static final String scTSQN = "T4808";
	public static String sGetTSQN(byte[] bytes) {
		if (bytes == null || bytes.length != 3) {
			return "";
		}
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		String s = String.format("%06d", i);
		s = "<" + scTSQN + ">" + s + "</" + scTSQN + ">";
		return s;
	}
	
	/* b 2, TM, 交易模式種類 (TXN Mode, 由設備產生) */
	public static final String scTM = "T4809";
	public static String sGetTM(byte b) {
		String s = Util.sGetAttr_b(b);
		s = "<" + scTM + ">" + s + "</" + scTM + ">";
		return s;
	}
	
	/* b 2, TQ, 交易屬性設定值 (TXN Qualifier, 由設備產生) */
	public static final String scTQ = "T4810";
	public static String sGetTQ(byte b) {
		String s = Util.sGetAttr_b(b);
		s = "<" + scTQ + ">" + s + "</" + scTQ + ">";
		return s;
	}
	
	/* an 6, TXN SN. before TXN, 交易前的交易序號 */
	public static final String scTXNSNBeforeTXN = "T4811";
	public static String sGetTXNSNBeforeTXN(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		s = "<" + scTXNSNBeforeTXN + ">" + s + "</" + scTXNSNBeforeTXN + ">";
		return s;
	}
	
	/* b 6, CTC, 二代卡 */
	public static final String scCTC = "T4812";
	public static String sGetCTC(byte[] bytes) {
		if (bytes == null || bytes.length != 3) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scCTC + ">" + s + "</" + scCTC + ">";
		return s;
	}
	
	/* n <= 12, Loyalty counter, 依票卡原始資料傳送 */
	public static final String scLoyaltyCounter = "T4813";
	public static String sGetLoyaltyCounter(long counter) {
		if (counter < 0) {
			return "";
		}
		String s = String.valueOf(counter);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scLoyaltyCounter + ">" + s + "</" + scLoyaltyCounter + ">";
		return s;
	}
	
	/* n <= 12, Deposit, 押金 */
	public static final String scDeposit = "T4814";
	public static String sGetDeposit(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scDeposit + ">" + s + "</" + scDeposit + ">";
		return s;
	}
	
	/* n <= 5, New Refund Fee, 退卡手續費, 2 bytes (Unsigned and LSB)轉為10進制數字 */
	public static final String scNewRefundFee = "T4815";
	public static String sGetNewRefundFee(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}	
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		String s = String.format("%d", i);
		if (s.length() > 5) {
			return "";
		}
		s = "<" + scNewRefundFee + ">" + s + "</" + scNewRefundFee + ">";
		return s;
	}
	
	/* an <= 5, Broken Fee, 票卡損毀費, 2 bytes (Unsigned and LSB)轉為10進制數字 */
	public static final String scBrokenFee = "T4816";
	public static String sGetBrokenFee(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}	
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		String s = String.format("%d", i);
		if (s.length() > 5) {
			return "";
		}
		s = "<" + scBrokenFee + ">" + s + "</" + scBrokenFee + ">";
		return s;
	}
	
	/* an <= 5, Customer Fee, 退卡申訴費, 2 bytes (Unsigned and LSB)轉為10進制數字 */
	public static final String scCustomerFee = "T4817";
	public static String sGetCustomerFee(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 2) {
			return s;
		}	
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		s = String.format("%d", i);
		if (s.length() > 5) {
			return "";
		}
		s = "<" + scCustomerFee + ">" + s + "</" + scCustomerFee + ">";
		return s;
	}
	
	/* n <= 12, Another EV, 依票卡原始資料傳送 */
	public static final String scAnotherEV = "T4818";
	public static String sGetAnotherEV(long value) {
		String s = "";
		if (value < 0) {
			return s;
		}
		s = String.valueOf(value);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scAnotherEV + ">" + s + "</" + scAnotherEV + ">";
		return s;
	}
	
	/* an 2, 鎖卡原因 */
	public static final String scCardLockReson = "T4819";
	public static String sGetCardLockReson(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 2) {
			return s;
		}
		s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return s;
		}
		s = "<" + scCardLockReson + ">" + s + "</" + scCardLockReson + ">";
		return s;
	}
	
	/* b 2, Spec. version number, Host識別版號 */
	public static final String scSpecVersionNumber = "T4820";
	public static String sGetSpecVersionNumber(byte b) {
		String s = Util.sGetAttr_b(b);
		if (s.length() == 0) {
			return s;
		}
		s = "<" + scSpecVersionNumber + ">" + s + "</" + scSpecVersionNumber + ">";
		return s;
	}
	
	/* b 8, Card Parameters, 票卡參數 */
	public static final String scCardParameters = "T4821";
	public static String sGetCardParameters(byte[] cardOneDayQuota, byte[] cardOneDayQuotaDate) {
		if (cardOneDayQuota == null || cardOneDayQuota.length != 2) {
			return "";
		}
		if (cardOneDayQuotaDate == null || cardOneDayQuotaDate.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_b(cardOneDayQuotaDate);
		if (s.length() == 0) {
			return "";
		}
		String s1 = Util.sGetAttr_b(cardOneDayQuotaDate);
		if (s1.length() == 0) {
			return "";
		}
		s = "<" + scCardParameters + ">" + s + s1 + "</" + scCardParameters + ">";
		return s;
	}
	
	/* 2 bytes, 小額消費日限額寫入位置 */
	public static final String scCPUOneDayQuotaWriteFlag = "T4823";
	public static String sGetCPUOneDayQuotaWriteFlag(OneDayQuotaWriteForMicroPayment e) {
		String s = "";
		switch (e) {
		case NMNC: 		// T=Mifare與T-CPU都不寫入
			s = "00";
			break;
		case WMNC: 		// T=Mifare寫入, T-CPU不寫入
			s = "01";
			break;
		case NMWC: 		// T=Mifare不寫入, T-CPU寫入
			s = "10";
			break;
		case WNWC:  	// T=Mifare與T-CPU都寫入
			s = "11";
			break;
		}
		s = "<" + scCPUOneDayQuotaWriteFlag + ">" + s + "</" + scCPUOneDayQuotaWriteFlag + ">";
		return s;
	}
	
	/* n 2, CPU CPD read flag */
	public static final String scCPUCPDReadFlag = "T4824";
	public static String sGetCPUCPDReadFlag(CPDReadFlag flag) {
		String s = "";
		switch (flag) {
		case YRHostNCReader: // 讀回Host且Reader不驗證
			s = "00"; // 不讀取CPD
			break;
		case NRHostYCReader: // 不讀回Host且Reader要驗證
			s = "10"; // 讀取CPD
			break;
		case YRHostYCReader: // 讀回Host且Reader要驗證
			s = "10"; // 讀取CPD
			break;
		default: // 不讀回Host且Reader不驗證
			s = "00"; // 不讀取CPD
			break;
		}
		s = "<" + scCPUCPDReadFlag + ">" + s + "</" + scCPUCPDReadFlag + ">";
		return s;
	}
	
	/* n 2, CPU credit balance change flag */
	public static final String scCPUCreditBalanceChangeFlag = "T4825";
	public static String sGetCPUCreditBalanceChangeFlag(boolean bChange) {
		String s = "";
		if (bChange) {
			s = "01"; // ACB已變更
		} else {
			s = "00"; // ACB未變更
		}
		s = "<" + scCPUCreditBalanceChangeFlag + ">" + s + "</" + scCPUCreditBalanceChangeFlag + ">";
		return s;
	}
	
	/* n 1, Card physical ID length, 4 or 7 */
	public static final String scCardPhysicalIDLength = "T4826";
	public static String sGetCardPhysicalIDLength(int length) {
		if (length != 4 || length != 7) {
			return "";
		}
		
		String s = String.valueOf(length);		
		s = "<" + scCardPhysicalIDLength + ">" + s + "</" + scCardPhysicalIDLength + ">";
		return s;
	}
	
	/* b 10, CPU Card Parameters, 
	 * 1. Card One Day Quota, b 6, 交易前卡片累計日限額額度,
	 * 2. Card One Day Quota Date, b 4, 交易前卡片累計日限額額度日期 */
	public static final String scCPUCardParameters = "T4827";
	public static String sGetCPUCardParameters(byte[] CardOneDayQuota, byte[] CardOneDayQuotaDate) {
		if (CardOneDayQuota == null || CardOneDayQuota.length != 3) {
			return "";
		}
		if (CardOneDayQuotaDate == null || CardOneDayQuotaDate.length != 2) {
			return "";
		}
		String s = "";
		s = Util.sGetAttr_b(CardOneDayQuota);
		s += Util.sGetAttr_b(CardOneDayQuotaDate);
		s = "<" + scCPUCardParameters + ">" + s + "</" + scCPUCardParameters + ">";
		return s;
	}
	
	/* b 20, Mifare setting data, 
	 * 1. Personal profile authorization, b 2, Mifare S1B1
	 * 2. RFU, b 18, 保留 */
	public static final String scMifareSettingData = "T4828";
	public static String sGetMifareSettingData(byte[] PersonalProfileAuthor, byte[] RFU) {
		if (PersonalProfileAuthor == null || PersonalProfileAuthor.length != 1) {
			return "";
		}
		if (RFU == null || RFU.length != 9) {
			return "";
		}
		String s = "";
		s = Util.sGetAttr_b(PersonalProfileAuthor);
		s += Util.sGetAttr_b(RFU);
		s = "<" + scMifareSettingData + ">" + s + "</" + scMifareSettingData + ">";
		return s;
	}
	
	/* b 32, CPU Card Setting Parameter, 
	 * 1. Micro Payment Setting, b 2, CPU SFI1 CID
	 * 2. RFU, b 30, 保留 */
	public static final String scCPUCardSettingParameter = "T4829";
	public static String sGetCPUCardSettingParameter(byte[] MicroPaymentSetting, byte[] RFU) {
		if (MicroPaymentSetting == null || MicroPaymentSetting.length != 1) {
			return "";
		}
		if (RFU == null || RFU.length != 15) {
			return "";
		}
		String s = "";
		s = Util.sGetAttr_b(MicroPaymentSetting);
		s += Util.sGetAttr_b(RFU);
		s = "<" + scCPUCardSettingParameter + ">" + s + "</" + scCPUCardSettingParameter + ">";
		return s;
	}
	
	/* b 1, Read Purse Flag, 0=不檢查(不須同一票卡), 1=檢查(需同一票卡) */
	public static final String scReadPurseFlag = "T4830";
	public static String sGetReadPurseFlag(boolean bCheck) {
		String s = "0";
		if (bCheck) {
			s = "1";
		}
		s = "<" + scReadPurseFlag + ">" + s + "</" + scReadPurseFlag + ">";
		return s;
	}
	
	/* b 2, SAM Key Version Numner (KVN), Key set + Key version
	 * b7: RFU 
	 * b6: Confirm key set, 0: normal, 1: backup
	 * b5: Host key set, 0: normal, 1: backup
	 * b4: SAM key set, 0: normal, 1: backup
	 * b0~3: Key version (ex. 0001b)
	 * 對應一代MAC (T6403)
	 */
	public static final String scSAMKVN = "T5301";
	public static String sGetSAMKVN(byte b) {
		String s = "";
		s = Util.sGetAttr_b(b);
		s = "<" + scSAMKVN + ">" + s + "</" + scSAMKVN + ">";
		return s;
	}
	
	/* b 6, CPU Card Key Info */
	public static final String scCPUCardKeyInfo = "T5302";
	public class CPUCardKeyInfo {
		byte adminKeyKVN;
		byte creditKeyKVN;
		byte debitKeyKVN;
	};
	public static String sGetCPUCardKeyInfo(CPUCardKeyInfo info) {
		String s = "";
		if (info == null) {
			return s;
		}
		s = Util.sGetAttr_b(info.adminKeyKVN);
		s += Util.sGetAttr_b(info.creditKeyKVN);
		s += Util.sGetAttr_b(info.debitKeyKVN);
		s = "<" + scCPUCardKeyInfo + ">" + s + "</" + scCPUCardKeyInfo + ">";
		return s;
	}
	
	/* b 2, CPU Hash Type */
	public static final String scCPUHashType = "T5303";
	public static String sGetCPUHashType(byte b) {
		String s = "";
		s = Util.sGetAttr_b(b);
		s = "<" + scCPUHashType + ">" + s + "</" + scCPUHashType + ">";
		return s;
	}
	
	/* b 2, CPU host admin KVN */
	public static final String scCPUHostAdminKVNKVN = "T5304";
	public static String sGetCPUHostAdminKVNKVN(byte b) {
		String s = "";
		s = Util.sGetAttr_b(b);
		s = "<" + scCPUHostAdminKVNKVN + ">" + s + "</" + scCPUHostAdminKVNKVN + ">";
		return s;
	}
	
	/* ??????????????????? */
	public static final String scCPUEDC = "T5306";
	/* ??????????????????? */
	
	/* b 16, RSAM */
	public static final String scRSAM = "T5307";
	public static String sGetRSAM(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scRSAM + ">" + s + "</" + scRSAM + ">";
		return s;
	}
	
	/* T5308, RHOST, M/ /  */
	public static final String scRHOST = "T5308";
	public static String sGetRHOST(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scRHOST + ">" + s + "</" + scRHOST + ">";
		return s;
	}
	
	/* b 16, SAM ID, 8 bytes Unsigned and MSB First unpack to 16 bytes */
	public static final String scSAMID = "T5361";
	public static String sGetSAMID(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSAMID + ">" + s + "</" + scSAMID + ">";
		return s;
	}
	
	/* b 8, SAM SN, 4 bytes Unsigned and MSB First unpack to 8 bytes */
	public static final String scSAMSN = "T5362";
	public static String sGetSAMSN(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 4) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSAMSN + ">" + s + "</" + scSAMSN + ">";
		return s;
	}
	
	/* b 16, SAM CRN, 8 bytes Unsigned and MSB First unpack to 16 bytes */
	public static final String scSAMCRN = "T5363";
	public static String sGetSAMCRN(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSAMCRN + ">" + s + "</" + scSAMCRN + ">";
		return s;
	}
	
	/* b 156, CPU SAM Info, 78 bytes unpack to 156 bytes */
	public static final String scCPUSAMInfo = "T5364";
	public class CPUSAMInfo {
		public byte SAMVersionNumber;			// SAM Version Number
		public byte[] SAMUsageControl;			// 3 bytes, SAM Usage Control 
		public byte SAMAdminKVN;				// SAM Admin KVN
		public byte SAMIssuerKVN;				// SAM Issuer KVN
		public byte[] TagListTable;				// 40 bytes, Tag List Table
		public byte[] SAMIssuerSpecificData;	// 32 bytes, SAM Issuer Specific Data 
	};
	public static String sGetCPUSAMInfo(CPUSAMInfo info) {
		String s = "";
		if (info == null || info.SAMUsageControl == null || info.SAMUsageControl.length != 3 ||  
				info.TagListTable == null || info.TagListTable.length != 40 || 
				info.SAMIssuerSpecificData == null || info.SAMIssuerSpecificData.length != 32) {
			return s;
		}
		s = Util.sGetAttr_b(info.SAMVersionNumber);
		s += Util.sGetAttr_b(info.SAMUsageControl);
		s += Util.sGetAttr_b(info.SAMAdminKVN);
		s += Util.sGetAttr_b(info.SAMIssuerKVN);
		s += Util.sGetAttr_b(info.TagListTable);
		s += Util.sGetAttr_b(info.SAMIssuerSpecificData);
		s = "<" + scCPUSAMInfo + ">" + s + "</" + scCPUSAMInfo + ">";
		return s;
	}
	
	/* b 24, SAM Transaction Info, 12 bytes unpack to 24 bytes */
	public static final String scSAMTransactionInfo = "T5365";
	public class SAMTransactionInfo {
		public byte[] acl;	// 3 bytes, Authodrized Credit Limit
		public byte[] acb;	// 3 bytes, Authodrized Credit Balance
		public byte[] acc;	// 3 bytes, Authodrized Credit Cumulative
		public byte[] accc;	// 3 bytes, Authodrized Cancel Credit Cumulative
	}
	public static String sGetSAMTransactionInfo(SAMTransactionInfo info) {
		String s = "";
		if (info == null || info.acl == null || info.acl.length != 3 ||  
				info.acb == null || info.acb.length != 3 || 
				info.acc == null || info.acc.length != 3 ||
				info.accc == null || info.accc.length != 3) {
			return s;
		}
		s = Util.sGetAttr_b(info.acl);
		s += Util.sGetAttr_b(info.acb);
		s += Util.sGetAttr_b(info.acc);
		s += Util.sGetAttr_b(info.accc);
		s = "<" + scSAMTransactionInfo + ">" + s + "</" + scSAMTransactionInfo + ">";
		return s;
	}
	
	/* b 6, Single Credit TXN AMT Limit, 3 bytes unpack to 6 bytes */
	public static final String scSingleCreditTXNAMTLimit = "T5366";
	public static String sGetSingleCreditTXNAMTLimit(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 3) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSingleCreditTXNAMTLimit + ">" + s + "</" + scSingleCreditTXNAMTLimit + ">";
		return s;
	}
	
	/* b 114, CPU SAM Parameter Setting Data */
	public static final String scCPUSAMParameterSettingData = "T5367";
	public class CPUSAMParameterSettingData {
		byte SAMUpdateOption;		// SAM Update Option 
		byte[] NewSAMValue;			// 40 bytes, New SAM Value 
		byte[] UpdateSAMValueMAC;	// 16 bytes, Update SAM Value MAC 
	};
	public static String sGetCPUSAMParameterSettingData(CPUSAMParameterSettingData data) {
		String s = "";
		if (data == null || data.NewSAMValue == null || data.NewSAMValue.length != 40 ||
				data.UpdateSAMValueMAC == null || data.UpdateSAMValueMAC.length != 16) {
			return s;
		}
		s = Util.sGetAttr_b(data.SAMUpdateOption);
		s += Util.sGetAttr_b(data.NewSAMValue);
		s += Util.sGetAttr_b(data.UpdateSAMValueMAC);
		s = "<" + scCPUSAMParameterSettingData + ">" + s + "</" + scCPUSAMParameterSettingData + ">";
		return s;
	}
	
	/* b 8, STC (SAM TXN Counter), 4 bytes unpack to 8 bytes */
	public static final String scSTC = "T5368";
	public static String sGetSTC(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 4) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSTC + ">" + s + "</" + scSTC + ">";
		return s;
	}
	
	/* b 2, SAM sign on control flag */
	public static final String scSAMSignOnControlFlag = "T5369";
	public static String sGetSAMSignOnControlFlag(SAMSignOnControlFlag e) {
		String s = "";
		switch (e) {
		case NoSignOnForBoth: 	// 兩張SAM卡都不須SignOn(離線)
			s = "00";
			break;
		case SignOnForNewOnly: 	// 只SignOn新SAM卡(淘汰Mifare時)
			s = "01";
			break;
		case SignOnForOldOnly: 	// 只SignOn舊SAM卡
			s = "10";
			break;
		case SignOnForBoth: 	// 兩張SAM卡都要SignOn
			s = "11";
			break;
		}
		s = "<" + scSAMSignOnControlFlag + ">" + s + "</" + scSAMSignOnControlFlag + ">";
		return s;
	}
	
	/* b 66, CPU Last Sign On Info, 33 bytes unpack to 66 bytes */
	public static final String scCPULastSignOnInfo = "T5370";
	public class CPULastSignOnInfo {
		byte[] PreviousNewDeviceID;		// 6 bytes, Unsigned and LSB First
		byte[] PreviousSTC;				// 4 bytes, SAM TXN Counter, Unsigned and MSB First
		byte[] PreviousTXNDateTime;		// 4 bytes, Unix Time
		byte PreviousCreditBalanceChangeFlag;	// 1 byte, 加值授權額度(ACB)變更旗標
		byte[] PreviousConfirmCode;		// 2 bytes, Status code 1 + Status code 2
		byte[] PreviousCACrypto;		// 16 bytes, Credit Authorization Cryptogram
	};
	public static String sGetCPULastSignOnInfo(CPULastSignOnInfo info) {
		String s = "";
		if (info == null || info.PreviousNewDeviceID == null || info.PreviousNewDeviceID.length != 6 ||  
				info.PreviousSTC == null || info.PreviousSTC.length != 4 || 
				info.PreviousTXNDateTime == null || info.PreviousTXNDateTime.length != 4 ||
				info.PreviousConfirmCode == null || info.PreviousConfirmCode.length != 2 ||
				info.PreviousCACrypto == null || info.PreviousCACrypto.length != 16) {
			return s;
		}
		s = Util.sGetAttr_b(info.PreviousNewDeviceID);
		s += Util.sGetAttr_b(info.PreviousSTC);
		s += Util.sGetAttr_b(info.PreviousTXNDateTime);
		s += Util.sGetAttr_b(info.PreviousCreditBalanceChangeFlag);
		s += Util.sGetAttr_b(info.PreviousConfirmCode);
		s += Util.sGetAttr_b(info.PreviousCACrypto);
		s = "<" + scCPULastSignOnInfo + ">" + s + "</" + scCPULastSignOnInfo + ">";
		return s;
	}
	
	/* b 16, SID (SAM ID) */
	public static final String scSID = "T5371";
	public static String sGetSID(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSID + ">" + s + "</" + scSID + ">";
		return s;
	}
	
	/* b 8, 批次號碼, M/ /M */
	public static final String scBatchNumber = "T5501";
	private static int sSerialNumber = 0;
	public static String sGetBatchNumber(String date) {
		if (date == null || date.length() != 6) {
			return "";
		}
		String s = date;
		s += String.format("%02d", sSerialNumber ++);
		if (sSerialNumber >= 100) {
			sSerialNumber = 0;
		}
		s = "<" + scBatchNumber + ">" + s + "</" + scBatchNumber + ">";
		return s;
	}
	
	/* an 10, TM Location ID, 特約商店分店號M/ /M */
	public static final String scTMLocationID = "T5503";
	public static String sGetTMLocationID(String id) {
		if (id == null || id.length() != 10) {
			return "";
		}
		String s = "<" + scTMLocationID + ">" + id + "</" + scTMLocationID + ">";
		return s;
	}
	
	/* an 2, TM ID, 收銀機機號, M/ /M */
	public static final String scTMID = "T5504";
	public static String sGetTMID(String id) {
		if (id == null || id.length() != 2) {
			return "";
		}
		String s = "<" + scTMID + ">" + id + "</" + scTMID + ">";
		return s;
	}
	
	/* ?b 4?, TM agent number, 收銀員代號, M/ /M */
	public static final String scTMAgentNumber = "T5510";
	public static String sGetTMAgentNumber(String number) {
		if (number == null || number.length() != 4) {
			return "";
		}
		String s = "<" + scTMAgentNumber + ">" + number + "</" + scTMAgentNumber + ">";
		return s;
	}
	
	/* var, 版本資訊, M/M/, 內崁端末設備狀態資訊55880x */
	public static final String scVersionInfo = "T5588";
	public enum VersionInfo_type {
		ssl,			// SSL版本
		blackList,		// 黑名單版本
		devProgVer,		// 端末設備程式版本
		devParamVer,	// 端末設備參數版本
		dongleProg		// Dongle程式版本
	};
		/* an 2, 狀態資訊類型, 01: SSL版本, 02: 黑名單版本, 03: 端末設備程式版本, 04: 端末設備參數版本, 05: Dongle程式版本 */
		public static final String scVersionInfo_type = "T558801";
		public static final String scVersionInfo_type_ssl = "01";
		public static final String scVersionInfo_type_backList = "02";
		public static final String scVersionInfo_type_devProgVer = "03";
		public static final String scVersionInfo_type_devParamVer = "04";
		public static final String scVersionInfo_type_dongleProg = "05";
		/* 0~30bytes, 狀態資訊子類型, Key的分類不填入, 黑名單檔案類型, 端末設備程式代號program ID, 端末設備參數不填入*/
		public static final String scVersionInfo_subtype = "T558802";
		/* an 0~30, 狀態資訊類型內容, SSL版本編號, 黑名單版本編號, 端末設備程式版本編號, Dongle程式版本編號, 其他版本編號 */
		public static final String scVersionInfo_content = "T558803";
	public class VersionInfo {
		VersionInfo_type type;
		String VersionInfo_type;
		String VersionInfo_subtype;
		String VersionInfo_content;
	};
	public String sGetVersionInfo(VersionInfo info) {
		String s = "";
		if (info == null) {
			return s;
		}
		
		switch (info.type) {
		case ssl:			// SSL版本
			s += s = "<" + scVersionInfo_type + ">" + "01" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case blackList:	// 黑名單版本
			s += s = "<" + scVersionInfo_type + ">" + "02" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_subtype == null) {
				s += s = "<" + scVersionInfo_subtype + ">" + "</" + scVersionInfo_subtype + ">";
			} else {
				s += s = "<" + scVersionInfo_subtype + ">" + info.VersionInfo_subtype + "</" + scVersionInfo_subtype + ">";
			}
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case devProgVer:	// 端末設備程式版本
			s += s = "<" + scVersionInfo_type + ">" + "03" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_subtype == null) {
				s += s = "<" + scVersionInfo_subtype + ">" + "</" + scVersionInfo_subtype + ">";
			} else {
				s += s = "<" + scVersionInfo_subtype + ">" + info.VersionInfo_subtype + "</" + scVersionInfo_subtype + ">";
			}
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case devParamVer:	// 端末設備參數版本
			s += s = "<" + scVersionInfo_type + ">" + "04" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case dongleProg:
			s += s = "<" + scVersionInfo_type + ">" + "05" + "</" + scVersionInfo_type + ">";
			s += s = "<" + scVersionInfo_content + ">" + "???" + "</" + scVersionInfo_content + ">";
			break;
		}
		s = "<" + scVersionInfo + ">" + s + "</" + scVersionInfo + ">";
		return s;
	}
	
	/* n 6, 交易總比數, 所有message type=0200的加總, 以batchno為計算基礎 */
	public static final String scTXNCounts = "T5591";
	
	/* var, 資料傳送控制,  */
	public static final String scDataXferControl = "T5596";
		/* n 8, 總筆數 */
		public static final String scDataXferControl_total = "T559601";
		/* n 8, 已傳送筆數 */
		public static final String scDataXferControl_xferred = "T559602";
		/* n 8, 已接收筆數 */
		public static final String scDataXferControl_received = "T559603";
		/* n 8, 傳送序號 */
		public static final String scDataXferControl_sn = "T559604";
	public class DataXferControl{
		int total; 			/* n 8, 總筆數 */
		int xferred;		/* n 8, 已傳送筆數 */
		int received;		/* n 8, 已接收筆數 */		
		int sn;				/* n 8, 傳送序號 */
	};
	public String sGetDataXferControl(DataXferControl control) {
		String s = "";
		if (control == null) {
			return "";
		}
		String total = String.format("%08d", control.total);
		s = "<" + scDataXferControl_total + ">" + total + "</" + scDataXferControl_total + ">";
		String xferred = String.format("%08d", control.xferred);
		s += "<" + scDataXferControl_xferred + ">" + xferred + "</" + scDataXferControl_xferred + ">";
		String received = String.format("%08d", control.received);
		s += "<" + scDataXferControl_received + ">" + received + "</" + scDataXferControl_received + ">";
		String sn = String.format("%08d", control.sn);
		s += "<" + scDataXferControl_sn + ">" + sn + "</" + scDataXferControl_sn + ">";
		s = "<" + scDataXferControl + ">" + s + "</" + scDataXferControl + ">";
		return s;
	}
	
	/* b 12, Reader firmware version, M/ /  */
	public static final String scReaderFWVersion = "T6000";
	public static String sGetReaderFWVersion(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scReaderFWVersion + ">" + s + "</" + scReaderFWVersion + ">";
		return s;
	}
	
	/* b 48, Term Host Parameters, M/C/  */
	public static final String scTermHostParameters = "T6002";
	public class TermHostParameters {
		// b 2, 日限額旗標, 1 bytes unpack to 2bytes
		OneDayQuotaFlagForMicroPayment OneDayQuotaFlag;
		// b 4, 日限額度 (default = 3000), 2 bytes unpack to 4 bytes
		byte[] OneDayQuota;			
		// b 2,	次限額旗標, 1 bytes unpack to 2bytes, 00: 不限制, 01: 限制
		boolean OnceQuotaFlag;		
		// b 4, 次限額度 (default = 1000), 2 bytes unpack to 4 bytes
		byte[] OnceQuota;			
		// b 2, 檢查餘額旗標, 1 bytes unpack to 2bytes, 00: 檢查餘額 (default), 01: 不檢查餘額
		boolean CheckEVFlag;			
		// b 2, 加值額度控管旗標, 1 bytes unpack to 2bytes, 00: 不限制制加值額度, 01: 限制加值額度
		boolean AddQuotaFlag;		
		// b 6, 加值額度(default = 100,000), 3bytes unpack to 6bytes
		byte[] AddQuota;			
		// b 2, 扣值交易合法驗證旗標, 1 bytes unpack to 2bytes, 00: 不限制, 01: 限制
		boolean CheckDeductFlag;
		// b 4, 扣值交易合法驗證金額 (default = 500), 2 bytes unpack to 4 bytes
		byte[] CheckDeductValue;	
		// b 2, ???, 1bytes unpack to 2bytes
		boolean DeductLimitFlag;
		// b 8, ???, 4bytes unpack to 8bytes
		byte[] APIVersion;			
		// b 10, 5bytes unpack to 10bytes
		byte[] RFU;					
	};
	
	public class TermHostParametersResp {
		// 日限額旗標
		OneDayQuotaFlagForMicroPayment OneDayQuotaFlag;
		// 日限額度 (default = 3000)
		int OneDayQuota;			
		// 次限額旗標, 00: 不限制, 01: 限制
		boolean OnceQuotaFlag;		
		// 次限額度 (default = 1000)
		int OnceQuota;			
		// 檢查餘額旗標, 00: 檢查餘額 (default), 01: 不檢查餘額
		boolean CheckEVFlag;			
		// 加值額度控管旗標, 00: 不限制制加值額度, 01: 限制加值額度
		boolean AddQuotaFlag;		
		// 加值額度(default = 100,000)
		int AddQuota;			
		// 扣值交易合法驗證旗標, 00: 不限制, 01: 限制
		boolean CheckDeductFlag;
		// 扣值交易合法驗證金額 (default = 500)
		int CheckDeductValue;	
		// 扣值交易合法驗證旗標
		boolean DeductLimitFlag;
		// 
		String APIVersion;			
		// 保留
		byte[] RFU;					
	};
	
	public String sGetTermHostParameters(TermHostParameters params) {
		String s = "";
		if (params == null || params.OneDayQuota == null || params.OneDayQuota.length != 2 ||
				params.OnceQuota == null || params.OnceQuota.length != 2 || 
				params.AddQuota == null || params.AddQuota.length != 3 ||
				params.CheckDeductValue == null || params.CheckDeductValue.length != 2 || 
				params.APIVersion == null || params.APIVersion.length != 4) {
			return s;
		}
		switch (params.OneDayQuotaFlag) {
		case NCNA: // 不檢查, 不累計日限額
			s += "00";
			break;
		case NCYA: // 不檢查, 累計日限額
			s += "01";
			break;
		case YCNA: // 檢查, 不累計日限額
			s += "10";
			break;
		case YCYA: // 檢查, 累計日限額
			s += "11";
			break;
		}
		s += Util.sGetAttr_b(params.OneDayQuota);
		if (params.OnceQuotaFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.OnceQuota);			
		if (params.CheckEVFlag) {
			s += "00";
		} else {
			s += "01";
		}
		if (params.AddQuotaFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.AddQuota);			
		if (params.CheckDeductFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.CheckDeductValue);	
		if (params.DeductLimitFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.APIVersion);			
		s += "0000000000";
		s = "<" + scTermHostParameters + ">" + s + "</" + scTermHostParameters + ">";
		return s;
	}
	
	/* b 64, Term Reader Parameters, M/ /  */
	public static final String scTermReaderParameters = "T6003";
	public class TermReaderParameters {
		byte[] RemainderOfAddQuota;			// b 6
		byte[] deMACParameters;				// b 16
		byte[] CancelCreditQuota;			// b 6
		byte[] RFU;							// b 36
	};
	public String sGetTermReaderParameters(TermReaderParameters params) {
		String s = "";
		if (params == null || 
				params.RemainderOfAddQuota == null || params.RemainderOfAddQuota.length != 3 ||
				params.deMACParameters == null || params.deMACParameters.length != 8 || 
				params.CancelCreditQuota == null || params.CancelCreditQuota.length != 3) {
			return s;
		}
		s += Util.sGetAttr_b(params.RemainderOfAddQuota);
		s += Util.sGetAttr_b(params.deMACParameters);	
		s += Util.sGetAttr_b(params.CancelCreditQuota);
		s += "000000000000000000000000000000000000";
		s = "<" + scTermReaderParameters + ">" + s + "</" + scTermReaderParameters + ">";
		return s;
	}
	
	/* n 5, Blacklist version, M/ /  */
	public static final String scBlacklistVersion = "T6004";
	public String sGetBlacklistVersion(byte[] bytes) {
		if (bytes == null || bytes.length != 5) {
			return "";
		}
		String s = Util.sGetAttr_n(bytes);
		s = "<" + scBlacklistVersion + ">" + s + "</" + scBlacklistVersion + ">";
		return s;
	}
	
	/* b 16, S-TAC, Request悠遊卡ADATA亂碼後S-TAC, M/ /  */
	public static final String scS_TAC = "T6400";
	public String sGetS_TAC(byte[] bytes) {
		if (bytes == null || bytes.length != 8) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scS_TAC + ">" + s + "</" + scS_TAC + ">";
		return s;
	}
	
	/* b 32, SAToken, M/ /  */
	public static final String scSAToken = "T6408";
	public String sGetSAToken(byte[] bytes) {
		if (bytes == null || bytes.length != 16) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scSAToken + ">" + s + "</" + scSAToken + ">";
		return s;
	}
	
	/* b 32, HAToken, M/ /  */
	public static final String scHAToken = "T6409";
}
