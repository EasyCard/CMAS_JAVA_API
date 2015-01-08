package amobile.java.cmas;

import amobile.java.cmas.ReqTagCreator.CPULastSignOnInfo;
import amobile.java.cmas.ReqTagCreator.CPUSAMInfo;
import amobile.java.cmas.ReqTagCreator.DataXferControl;
import amobile.java.cmas.ReqTagCreator.Issuer;
import amobile.java.cmas.ReqTagCreator.SAMTransactionInfo;
import amobile.java.cmas.ReqTagCreator.TermHostParameters;
import amobile.java.cmas.ReqTagCreator.TermHostParametersResp;
import amobile.java.cmas.ReqTagCreator.TermReaderParameters;
import amobile.java.cmas.ReqTagCreator.VersionInfo;
import amobile.java.cmas.ReqTagCreator.VersionInfo_type;
import amobile.java.cmas.ReqTagCreator.message_type;
import amobile.java.easycard.PPR_Reset;
import amobile.java.easycard.PPR_Reset.CPDReadFlag;
import amobile.java.easycard.PPR_Reset.OneDayQuotaFlagForMicroPayment;
import amobile.java.easycard.PPR_Reset.OneDayQuotaWriteForMicroPayment;
import amobile.java.easycard.PPR_Reset.SAMSignOnControlFlag;
import amobile.java.util.Util;

public class EDCSignOn {
	private ReqDataCreator mReqData = null;
	private CMASResponse mRespObj = null;
	
	public EDCSignOn(PPR_Reset resetObj, int txnSerialNumber, String devSerialNumber) throws IllegalArgumentException {
		if (resetObj == null) {
			throw new IllegalArgumentException("PPR_Reset object = null");
		}
		
		mReqData = ReqDataCreator.sGetInstance();
		if (mReqData == null) {
			throw new IllegalArgumentException("Request Data = null");
		}
		mReqData.RemoveAllTransUnit();
		mReqData.AddTransUnit();
		
		ReqTagCreator reqTagCreator = new ReqTagCreator();
		
		/* T0100, Message Type ID, M/M/M */
		String s = ReqTagCreator.sGetMessageTypeID(message_type.req);
		addElement(s);
		
		/* T0300, Processing Code, M/M/M */
		s = ReqTagCreator.sGetProcessingCode(ProcessingCode.code.EDCSignOn);
		addElement(s);
		
		/* T1100, TM Serial Number, M/M/M */
		s = ReqTagCreator.sGetTMSerialNumber(txnSerialNumber, true);
		addElement(s);
		
		/* T1101, 收銀機交易序號, O/ /O,  */
		s = ReqTagCreator.sGetCashBoxSerialNumber(txnSerialNumber, true);
		addElement(s);
		
		/* T1200, 本地(EDC)交易時間, M/M/M */
		s = resetObj.GetReq_TMTXNTime();
		s = ReqTagCreator.sGetEDCTime(s);
		addElement(s);
		
		/* T1201, TM TXN Time, O/ / , hhmmss, 收銀機交易時間, 若無收銀機連線, 填入與T1200相同值 */
		s = resetObj.GetReq_TMTXNTime();
		s = ReqTagCreator.sGetTMTXNTime(s);
		addElement(s);
		
		/* T1300, 本地(EDC)交易日期 */
		s = resetObj.GetReq_TMTXNDate();
		s = ReqTagCreator.sGetEDCDate(s, true);
		addElement(s);
		
		/* T1301, TM TXN Date, O/ / , yyyymmdd, 收銀機交易日ahve期, 若無收銀機連線, 填入與T1300相同值 */
		s = resetObj.GetReq_TMTXNDate();
		s = ReqTagCreator.sGetTMTXNDate(s);
		addElement(s);
		
		/* T3700, Retrieval Reference Number, M/M/M, T1300 + T1100 */
		s = ReqTagCreator.sGetRetrievalReferenceNumber(resetObj.GetReq_TMTXNDate(), 
														ReqTagCreator.sGetCashBoxSerialNumber(txnSerialNumber, false));
		addElement(s);
		
		/* T4100, New Device ID, M/M/M */
		byte[] bytes = resetObj.GetResp_NewDeviceID();
		s = ReqTagCreator.sGetNewDeviceID(bytes);
		addElement(s);
		
		/* T4101, Device ID, M/ /  */
		bytes = resetObj.GetResp_DeviceID();
		s = ReqTagCreator.sGetDeviceID(bytes);
		addElement(s);
		
		/* T4102, 端末設備目前IP, M/ / , 要確定計算方式 */
		s = ReqTagCreator.sGetDeviceIP();
		addElement(s);
		
		/* T4103, 端末設備機器序號, M/ / , 可以是終端設備的序號 */
		s = ReqTagCreator.sGetDeviceSerialNumber(devSerialNumber);
		addElement(s);
		
		/* T4104, Reader ID, M/ /  */
		bytes = resetObj.GetResp_ReaderID();
		s = ReqTagCreator.sGetReaderID(bytes);
		addElement(s);
		
		/* T4200, New SP ID, M/M/M , 特約商店代號, New Device ID的byte3, 4(LSB)轉成十進制即為New SP ID */
		s = resetObj.GetReq_NewServiceProviderIDByNewDeviceID();
		s = ReqTagCreator.sGetNewSPID(s);
		addElement(s);
		
		/* T4210, New Location ID, M/M/M, 分公司代號 */
		bytes = resetObj.GetReq_NewLocationID();
		s = ReqTagCreator.sGetNewLocationID5(bytes);
		addElement(s);
		
		/* T4802, Issuer Code, 02: 悠遊卡公司, 04: 基隆 */
		s = ReqTagCreator.sGetIssuerCode(Issuer.easycard);
		
		/* T4820, Spec. Version Number, M/ /  */
		byte b = resetObj.GetResp_SpecVersionNumber();
		s = ReqTagCreator.sGetSpecVersionNumber(b);
		addElement(s);
		
		/* T4823, CPU One Day Quota Write Flag, M/ /  */
		// ???, Reqest有設定但Resp無反應
		OneDayQuotaWriteForMicroPayment e1 = resetObj.GetResp_OneDayQuotaWriteForMicroPayment();
		s = ReqTagCreator.sGetCPUOneDayQuotaWriteFlag(e1);
		addElement(s);
		
		/* T4824, CPU CPD read flag, M/ /  */
		CPDReadFlag e2 = resetObj.GetResp_CPDReadFlag();
		s = ReqTagCreator.sGetCPUCPDReadFlag(e2);
		addElement(s);
		
		/* T4825, CPU credit balance change flag, O/ /M , Sign On後將讀卡機回船隻資料上傳 */
		/*
		boolean bool = resetObj.GetResp_PreviousCreditBalanceChangeFlag();
		s = ReqTagCreator.sGetCPUCreditBalanceChangeFlag(bool);
		addElement(s);
		*/
		
		/* T5301, SAM Key Version, M/ /  */
		b = resetObj.GetResp_SAMKeyVersion();
		s = ReqTagCreator.sGetSAMKVN(b);
		addElement(s);
		
		/* T5304, CPU Host admin KVN, M/ / , ?資料從哪來? */
		// s = ReqTagCreator.sGetCPUHostAdminKVNKVN(b);
		
		/* T5307, RSAM, M/ /  */
		bytes = resetObj.GetResp_RSAM();
		s = ReqTagCreator.sGetRSAM(bytes);
		addElement(s);
		
		/* T5308, RHOST, M/ /  */
		bytes = resetObj.GetResp_RHOST();
		s = ReqTagCreator.sGetRHOST(bytes);
		addElement(s);
		
		/* T5361, SAM ID, M/ /  */
		bytes = resetObj.GetResp_SAMID();
		s = ReqTagCreator.sGetSAMID(bytes);
		addElement(s);
		
		/* T5362, SAM SN, M/ /  */
		bytes = resetObj.GetResp_SAMSN();
		s = ReqTagCreator.sGetSAMSN(bytes);
		addElement(s);
		
		/* T5363, SAM CRN, M/ /  */
		bytes = resetObj.GetResp_SAMCRN();
		s = ReqTagCreator.sGetSAMCRN(bytes);
		addElement(s);
		
		/* T5364, CPU SAM Info, M/ /  */
		CPUSAMInfo info = reqTagCreator.new CPUSAMInfo(); 
		info.SAMVersionNumber = resetObj.GetResp_SAMVersionNumber();
		info.SAMUsageControl = resetObj.GetResp_SAMUsageControl();
		info.SAMAdminKVN = resetObj.GetResp_SAMAdminKVN();
		info.SAMIssuerKVN = resetObj.GetResp_SAMIssuerKVN();
		info.TagListTable = resetObj.GetResp_TagListTable();
		info.SAMIssuerSpecificData = resetObj.GetResp_SAMIssuerSpecificData(); 
		s = ReqTagCreator.sGetCPUSAMInfo(info);
		addElement(s);
		
		/* T5365, SAM Transaction Info, M/ /  */
		SAMTransactionInfo info1 = reqTagCreator.new SAMTransactionInfo(); 
		info1.acl = resetObj.GetResp_ACL();
		info1.acb = resetObj.GetResp_ACB();
		info1.acc = resetObj.GetResp_ACC();
		info1.accc = resetObj.GetResp_ACCC();
		s = ReqTagCreator.sGetSAMTransactionInfo(info1);
		addElement(s);
		
		/* T5366, Single Credit TXN AMT Limit, M/ /  */
		bytes = resetObj.GetResp_SingleCreditTXNAMTLimit();
		if (bytes == null) {
			throw new IllegalArgumentException("PPR_Reset resp failed");
		}
		s = ReqTagCreator.sGetSingleCreditTXNAMTLimit(bytes);
		addElement(s);
		
		/* T5368, STC (SAM TXN Counter), M/ /  */
		bytes = resetObj.GetResp_STC();
		if (bytes == null) {
			throw new IllegalArgumentException("PPR_Reset resp failed");
		}
		s = ReqTagCreator.sGetSTC(bytes);
		addElement(s);
		
		/* T5369, SAM sign on control flag, M/M/  */
		SAMSignOnControlFlag e3 = resetObj.GetResp_SAMSignOnControlFlag();
		s = ReqTagCreator.sGetSAMSignOnControlFlag(e3);
		addElement(s);
		
		/* T5370, CPU Last Sign On Info, M/ /  */
		CPULastSignOnInfo info2 = reqTagCreator.new CPULastSignOnInfo();
		info2.PreviousNewDeviceID = resetObj.GetResp_PreviousNewDeviceID();
		info2.PreviousSTC = resetObj.GetResp_PreviousSTC();
		info2.PreviousTXNDateTime = resetObj.GetResp_PreviousTXNDateTime();
		if (resetObj.GetResp_PreviousCreditBalanceChangeFlag()) {
			info2.PreviousCreditBalanceChangeFlag = 0x01; // 額度有變更
		} else {
			info2.PreviousCreditBalanceChangeFlag = 0x00; // 額度未變更
		}
		info2.PreviousConfirmCode = resetObj.GetResp_PreviousConfirmCode();
		info2.PreviousCACrypto = resetObj.GetResp_PreviousCACrypto();
		s = ReqTagCreator.sGetCPULastSignOnInfo(info2);
		addElement(s);
		
		/* T5371, SID (SAM ID), M/ /  */
		bytes = resetObj.GetResp_SAMIDNew();
		s = ReqTagCreator.sGetSID(bytes);
		addElement(s);
		
		/* T5501, 批次號碼, M/ /M */
		s = resetObj.GetReq_TMTXNDateShort();
		s = ReqTagCreator.sGetBatchNumber(s);
		addElement(s);
		
		/* T5503, TM Location ID, M/ /M */
		s = resetObj.GetReq_TMLocationID();
		s = ReqTagCreator.sGetTMLocationID(s);
		addElement(s);
		
		/* T5504, TM ID, M/ /M */
		s = resetObj.GetReq_TMID();
		s = ReqTagCreator.sGetTMID(s);
		addElement(s);
		
		/* T5510, TM agent number, M/ /M */
		s = resetObj.GetReq_TMAgentNumber();
		s = ReqTagCreator.sGetTMAgentNumber(s);
		addElement(s);
		
		/* T5588, 版本資訊, M/M/ , 內崁端末設備狀態資訊55880x */
		VersionInfo infoSSL = reqTagCreator.new VersionInfo();
		infoSSL.type = VersionInfo_type.ssl;
		infoSSL.VersionInfo_content = "0000";	// ???
		s = reqTagCreator.sGetVersionInfo(infoSSL);
		addElement(s);
		
		VersionInfo infoBlackList = reqTagCreator.new VersionInfo();
		infoBlackList.type = VersionInfo_type.blackList;
		infoBlackList.VersionInfo_subtype = "";
		infoBlackList.VersionInfo_content = "03341";	// ???
		s = reqTagCreator.sGetVersionInfo(infoBlackList);
		addElement(s);
		
		VersionInfo infoProgVer = reqTagCreator.new VersionInfo();
		infoProgVer.type = VersionInfo_type.devProgVer;
		infoProgVer.VersionInfo_subtype = "ECCAPP";
		infoProgVer.VersionInfo_content = "000014";	// ???
		s = reqTagCreator.sGetVersionInfo(infoProgVer);
		addElement(s);
		
		VersionInfo infoParamVer = reqTagCreator.new VersionInfo();
		infoParamVer.type = VersionInfo_type.devParamVer;
		infoParamVer.VersionInfo_content = "000083";	// ???
		s = reqTagCreator.sGetVersionInfo(infoParamVer);
		addElement(s);
		
		/* T5596, 資料傳送控制, C/C/ ??? */
		DataXferControl xferControl = reqTagCreator.new DataXferControl();
		xferControl.total = 0;
		xferControl.xferred = 0;
		xferControl.received = 0;
		xferControl.sn = 0;
		s = reqTagCreator.sGetDataXferControl(xferControl);
		addElement(s);
		
		/* T6000, Reader firmware version, M/ /  */
		bytes = resetObj.GetResp_ReaderFWVersion();
		if (bytes == null) {
			throw new IllegalArgumentException("PPR_Reset resp failed");
		}
		s = ReqTagCreator.sGetReaderFWVersion(bytes);
		addElement(s);
		
		/* T6002, Term Host Parameters, M/C/  */
		TermHostParameters p1 = reqTagCreator.new TermHostParameters();
		p1.OneDayQuotaFlag = resetObj.GetResp_OneDayQuotaFlagForMicroPayment();
		p1.OneDayQuota = resetObj.GetResp_OneDayQuotaForMicroPayment();
		p1.OnceQuotaFlag = resetObj.GetResp_OnceQuotaFlagForMicroPayment();
		p1.OnceQuota = resetObj.GetResp_OnceQuotaForMicroPayment();
		p1.CheckEVFlag = resetObj.GetResp_CheckEVFlagForMifareOnly(); 
		p1.AddQuotaFlag = resetObj.GetResp_AddQuotaFlag();
		p1.AddQuota = resetObj.GetResp_AddQuota();
		p1.CheckDeductFlag = resetObj.GetResp_CheckDebitFlag(); // deduct? debit?
		p1.CheckDeductValue = resetObj.GetResp_CheckDebitValue(); // deduct? debit?
		p1.DeductLimitFlag = false; // ?如何設定?
		p1.APIVersion = new byte[]{0, 0, 0, 0}; // ?如何設定? 
		s = reqTagCreator.sGetTermHostParameters(p1);
		addElement(s);
		
		/* T6003, Term Reader Parameters, M/ /  */
		TermReaderParameters p2 = reqTagCreator.new TermReaderParameters();
		p2.RemainderOfAddQuota = resetObj.GetResp_RemainderofAddQuota();
		p2.deMACParameters = resetObj.GetResp_deMACParameter();
		p2.CancelCreditQuota = resetObj.GetResp_CancelCreditQuota();
		s = reqTagCreator.sGetTermReaderParameters(p2);
		addElement(s);
		
		/* T6004, Blacklist version, M/ /  */
		s = reqTagCreator.sGetBlacklistVersion("03341".getBytes());
		addElement(s);
		
		/* T6400, S-TAC, M/ /  */
		bytes = resetObj.GetResp_S_TAC();
		s = reqTagCreator.sGetS_TAC(bytes);
		addElement(s);
		
		/* T6408, SAToken, M/ /  */
		bytes = resetObj.GetResp_SATOKEN();
		s = reqTagCreator.sGetSAToken(bytes);
		addElement(s);
		
		DumpElementsToFile();
	}
	
	private void addElement(String s) throws IllegalArgumentException {
		if (s == null || s.length() == 0) {
			throw new IllegalArgumentException("Add element failed");
		}
		mReqData.AddDataElement(s, 0);
	}
	
	public void DumpElementsToFile() {
		mReqData.DumpElementsToFile("d:\\temp\\", "EDCSignOn-Req");
	}
	/*
	public boolean SendRequest(ICMASResponse iResp) {
		return CMASClient.sGetInstance().SendRequest(iResp, mReqData.GetReqData());
	}
	*/
	
	public String GetRequestData() {
		if (mReqData == null) {
			return "";
		}
		return mReqData.GetReqData();
	}
	
	public void SetResponse(String resp) {
		mRespObj = CMASResponse.sGetInstance(resp);
	}
	
	public String GetResp_MessageTypeID() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scMessageTypeID);
	}
	
	public String GetResp_ProcessingCode() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scProcessingCode);
	}
	
	public String GetResp_TMSerialNumber() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scTMSerialNumber);
	}
	
	public String GetResp_EDCTime() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scEDCTime);
	}
	
	public String GetResp_EDCDate() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scEDCDate);
	}
	
	public String GetResp_RetrievalReferenceNumber() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scRetrievalReferenceNumber);
	}
	
	public String GetResp_AuthorTranscNumber() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scAuthorTranscNumber);
	}
	
	public String GetResp_ResponseCode() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scResponseCode);
	}
	
	public String GetResp_NewDeviceID() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scNewDeviceID);
	}
	
	public String GetResp_NewSPID() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scNewSPID);
	}
	
	public String GetResp_NewLocationID5() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scNewLocationID5);
	}
	
	public String GetResp_CPUHashType() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scCPUHashType);
	}
	
	public String GetResp_CPUEDC() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scCPUEDC);
	}
	
	public String GetResp_CPUSAMParameterData() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scCPUSAMParameterSettingData);
	}
	
	public String GetResp_SAMSignOnControlFlag() { 
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scSAMSignOnControlFlag);
	}
	
	public String GetResp_VersionInfoContent(VersionInfo_type type) {
		if (mRespObj == null) {
			return "";
		}
		
		String sType = "";
		switch(type) {
		case ssl:			// SSL版本
			sType = ReqTagCreator.scVersionInfo_type_ssl;
			break;
		case blackList:		// 黑名單版本
			sType = ReqTagCreator.scVersionInfo_type_backList;
			break;
		case devProgVer:	// 端末設備程式版本
			sType = ReqTagCreator.scVersionInfo_type_devProgVer;
			break;
		case devParamVer:	// 端末設備參數版本
			sType = ReqTagCreator.scVersionInfo_type_devParamVer;
			break;
		case dongleProg:		// Dongle程式版本
			sType = ReqTagCreator.scVersionInfo_type_dongleProg;
			break;
		}
		
		int count = mRespObj.GetValueCount(ReqTagCreator.scVersionInfo);
		for (int i = 0; i < count; i ++) {
			String s = mRespObj.GetValue(ReqTagCreator.scVersionInfo, ReqTagCreator.scVersionInfo_type, i);
			if (s.equals(sType)) {
				return mRespObj.GetValue(ReqTagCreator.scVersionInfo, ReqTagCreator.scVersionInfo_content, i);
			}
		}
		
		return "";
	}
	
	public String GetResp_VersionInfoSubtype(VersionInfo_type type) {
		if (mRespObj == null) {
			return "";
		}
		
		String sType = "";
		switch(type) {
		case ssl:			// SSL版本
			sType = ReqTagCreator.scVersionInfo_type_ssl;
			break;
		case blackList:		// 黑名單版本
			sType = ReqTagCreator.scVersionInfo_type_backList;
			break;
		case devProgVer:	// 端末設備程式版本
			sType = ReqTagCreator.scVersionInfo_type_devProgVer;
			break;
		case devParamVer:	// 端末設備參數版本
			sType = ReqTagCreator.scVersionInfo_type_devParamVer;
			break;
		case dongleProg:		// Dongle程式版本
			sType = ReqTagCreator.scVersionInfo_type_dongleProg;
			break;
		}
		
		int count = mRespObj.GetValueCount(ReqTagCreator.scVersionInfo);
		for (int i = 0; i < count; i ++) {
			String s = mRespObj.GetValue(ReqTagCreator.scVersionInfo, ReqTagCreator.scVersionInfo_type, i);
			if (s.equals(sType)) {
				return mRespObj.GetValue(ReqTagCreator.scVersionInfo, ReqTagCreator.scVersionInfo_subtype, i);
			}
		}
		
		return "";
	}
	
	public String GetResp_TXNCounts() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scTXNCounts);
	}
	
	public DataXferControl GetResp_DataXferControl() {
		if (mRespObj == null) {
			return null;
		}
		
		DataXferControl control = new ReqTagCreator().new DataXferControl();
		String s = mRespObj.GetValue(ReqTagCreator.scDataXferControl, ReqTagCreator.scDataXferControl_total, 0);
		control.total = Integer.valueOf(s);
		s = mRespObj.GetValue(ReqTagCreator.scDataXferControl, ReqTagCreator.scDataXferControl_xferred, 0);
		control.xferred = Integer.valueOf(s);
		s = mRespObj.GetValue(ReqTagCreator.scDataXferControl, ReqTagCreator.scDataXferControl_received, 0);
		control.received = Integer.valueOf(s);
		s = mRespObj.GetValue(ReqTagCreator.scDataXferControl, ReqTagCreator.scDataXferControl_sn, 0);
		control.sn = Integer.valueOf(s); 
		return control;
	}
	
	public String GetResp_ReaderFWVersion() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scReaderFWVersion);
	}
	
	public String GetResp_TermHostParametersString() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scTermHostParameters);
	}
	
	public TermHostParametersResp GetResp_TermHostParameters() {
		if (mRespObj == null) {
			return null;
		}
		
		String s = mRespObj.GetValue(ReqTagCreator.scTermHostParameters);
		if (s == null || s.length() != 48) {
			return null;
		}
		TermHostParametersResp params = new ReqTagCreator().new TermHostParametersResp();
		String sub = "";
		
		// 一日限額旗標
		sub = s.substring(0, 2);
		params.OneDayQuotaFlag = OneDayQuotaFlagForMicroPayment.NCNA; 		// 不檢查, 不累計日限額
		if (sub.equals("01")) {
			params.OneDayQuotaFlag = OneDayQuotaFlagForMicroPayment.NCYA;	// 不檢查, 累計日限額
		} else if (sub.equals("10")) {
			params.OneDayQuotaFlag = OneDayQuotaFlagForMicroPayment.YCNA;	// 檢查, 不累計日限額
		} else if (sub.equals("11")) {
			params.OneDayQuotaFlag = OneDayQuotaFlagForMicroPayment.YCYA;	// 檢查, 累計日限額
		}
		
		// 一日限額度
		sub = s.substring(2, 6);
		params.OneDayQuota = Util.sByteToInt(sub.getBytes(), 0, 4, false);
		
		// 次限額旗標
		sub = s.substring(6, 8);
		if (sub.equals("01")) {
			params.OnceQuotaFlag = true; // 限制次限額旗標
		} else {
			params.OnceQuotaFlag = false; // 不限制次限額旗標
		}
		
		// 次限額度
		sub = s.substring(8, 12);
		params.OnceQuota = Util.sByteToInt(sub.getBytes(), 0, 4, false); 
		
		// 檢查餘額旗標
		sub = s.substring(12, 14);
		if (sub.equals("01")) {
			params.CheckEVFlag = false;	// 不檢查餘額
		} else {
			params.CheckEVFlag = true; 	// 檢查餘額 (default)
		}
		
		// 加值額度控管旗標
		sub = s.substring(14, 16);
		if (sub.equals("01")) {
			params.AddQuotaFlag = true;		// 限制加值額度
		} else {
			params.AddQuotaFlag = false; 	// 不限制加值額度
		}
		
		// 加值額度(default = 100,000)
		sub = s.substring(16, 22);
		params.AddQuota = Util.sByteToInt(sub.getBytes(), 0, 6, false);
		
		// 扣值交易合法驗證旗標
		sub = s.substring(22, 24);
		if (sub.equals("01")) {
			params.CheckDeductFlag = true;		// 限制扣值交易合法驗證
		} else {
			params.CheckDeductFlag = false; 	// 不限制扣值交易合法驗證
		}
		
		// 扣值交易合法驗證金額
		sub = s.substring(24, 28);
		params.CheckDeductValue = Util.sByteToInt(sub.getBytes(), 0, 4, false);
		
		// ???
		sub = s.substring(28,30);
		if (sub.equals("01")) {
			params.DeductLimitFlag = false;		// 設備不須檢查卡片是否可執行扣值交易
		} else {
			params.DeductLimitFlag = true; 		// 設備須檢查卡片是否可執行扣值交易
		}
		
		// ???
		params.APIVersion = s.substring(30, 38);
		
		// RFU
		params.RFU = s.substring(38, 48).getBytes();
		
		return params;
	}
	
	public String GetResp_TermReaderParametersString() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scTermReaderParameters);
	}
	
	/*
	public TermReaderParametersResp GetResp_TermReaderParameters() {
		
	}
	*/
	
	public String GetResp_BlacklistVersion() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scBlacklistVersion);
	}
	
	public String GetResp_S_TAC() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scS_TAC);
	}
	
	public String GetResp_SAToken() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scSAToken);
	}
	
	public String GetResp_HAToken() {
		if (mRespObj == null) {
			return "";
		}
		return mRespObj.GetValue(ReqTagCreator.scHAToken);
	}
}
