package amobile.java.cmas;

public class ProcessingCode {
	public enum code {
		ParamsDownload, 				/* 參數下載, TMS system參數下載 */ 
		SignOnTMSSystem, 				/* 末端設備對TMS system進行sign on */ 
		StatusReport,					/* 末端設備對TMS system回報端末設備狀態 */
		Settlement,						/* 結帳 */
		InquiryCardTradeDetail6, 		/* 悠遊卡卡片交易明細查詢, 6筆 */
		InquiryDongleTradeDetail1K,		/* 悠遊卡Dongle交易明細查詢, 1000筆 */
		InquiryCardData,				/* 悠遊卡卡片資料查詢 */
		InquiryBalance,					/* 餘額查詢 */
		ExhibitCardLock,				/* 悠遊卡展期鎖卡 */
		CardLock,						/* 悠遊卡鎖卡 */
		EDCSignOn,						/* EDC Sign On */
		CashAddCredit,					/* 現金加值 */
		SocialCareCashAddCredit,		/* 社福卡現金加值 */
		AutoAddCredit,					/* 自動加值 */
		SocialCareAutoAddCredit,		/* 社福卡自動加值 */
		ATMAddCredit,					/* 金融卡加值 */
		SocialCareATMAddCredit,			/* 社福卡金融卡加值 */
		XformRefundAddCredit, 			/* 餘額轉置加值 */
		CancelAddCredit,				/* 取消加值 */
		Merchandise, 					/* 購貨 */
		CancelMerchandise,				/* 取消購貨 */
		SellCard,						/* 售卡 */
		CancelSellCard,					/* 取消售卡 */
		Exhibit,						/* 展期 */
		TXNAuth,						/* 交易合法驗證 */
		ReturnMerchandise,				/* 退貨 */
		ReturnATMCard,					/* 聯名卡退卡 */
		XformRefundReturnCard,			/* 餘額轉置退卡 */
		CoBranderCardAutoCredit,		/* 聯名卡自動加值功能開啟 */
		CPUCashAddCredit,				/* CPU現金加值 */
		CPUSocialCareCashAddCredit,		/* CPU社福卡現金加值 */
		CPUAutoAddCredit,				/* CPU自動加值 */
		CPUSocialCareAutoAddCredit,		/* CPU社福卡自動加值 */
		CPUATMAddCredit,				/* CPU金融卡加值 */
		CPUSocialCareATMAddCredit,		/* CPU社福卡金融卡加值 */
		CPUXformRefundAddCredit,		/* CPU餘額轉置加值 */
		CPUCancelAddCredit,				/* CPU取消加值 */
		CPUMerchandise,					/* CPU購貨 */
		CPUCancelMerchandise,			/* CPU取消購貨 */
		CPUXformRefundMerchandise,		/* CPU餘額轉置購貨 */
		CPUSellCard,					/* CPU售卡 */
		CPUCancelSellCard,				/* CPU取消售卡 */
		CPUExhibit,						/* CPU展期 */
		CPUTXNAuth,						/* CPU交易合法驗證 */
		CPUReturnMerchandise,			/* CPU退貨 */
		CPUReturnCard,					/* CPU退卡 */
		CPUXformRefundReturnCard,		/* CPU餘額轉置退卡 */
		CPUCoBranderCardAutoCredit		/* CPU聯名卡自動加值功能開啟 */ 	
	};
	
	/* 參數下載, TMS system參數下載 */
	public static final String scParamsDownload = "920700";
	
	/* 末端設備對TMS system進行sign on */
	public static final String scSignOnTMSSystem = "940007";
	
	/* 末端設備對TMS system回報端末設備狀態 */ 
	public static final String scStatusReport = "950007";
	
	/* 結帳 */
	public static final String scSettlement = "900000";
	
	/* 悠遊卡卡片交易明細查詢, 6筆 */
	public static final String scInquiryCardTradeDetail6 = "216000";
	
	/* 悠遊卡Dongle交易明細查詢, 1000筆 */
	public static final String scInquiryDongleTradeDetail1K = "214100";
	
	/* 悠遊卡卡片資料查詢 */
	public static final String scInquiryCardData = "296000";
	
	/* 餘額查詢 */
	public static final String scInquiryBalance = "200000";
	
	/* 悠遊卡展期鎖卡 */
	public static final String scExhibitCardLock = "596000";
	
	/* 悠遊卡鎖卡 */
	public static final String scCardLock = "596100";
	
	/* EDC Sign On */
	public static final String scEDCSignOn = "881999";
	
	/* 現金加值 */
	public static final String scCashAddCredit = "810799";
	
	/* 社福卡現金加值 */
	public static final String scSocialCareCashAddCredit = "840799";
	
	/* 自動加值 */
	public static final String scAutoAddCredit = "824799";
	
	/* 社福卡自動加值 */
	public static final String scSocialCareAutoAddCredit = "844799";
	
	/* 金融卡加值 */
	public static final String scATMAddCredit = "836799";
	
	/* 社福卡金融卡加值 */
	public static final String scSocialCareATMAddCredit = "846799";
	
	/* 餘額轉置加值 */
	public static final String scXformRefundAddCredit = "838799";
	
	/* 取消加值 */
	public static final String scCancelAddCredit = "810899";
	
	/* 購貨 */
	public static final String scMerchandise = "810599";
	
	/* 取消購貨 */
	public static final String scCancelMerchandise = "822899";
	
	/* 售卡 */
	public static final String scSellCard = "850799";
	
	/* 取消售卡 */
	public static final String scCancelSellCard = "850899";
	
	/* 展期 */
	public static final String scExhibit = "810399";
	
	/* 交易合法驗證 */
	public static final String scTXNAuth = "815399";
	
	/* 退貨 */
	public static final String scReturnMerchandise = "850999";
	
	/* 聯名卡退卡 */
	public static final String scReturnATMCard = "860799";
	
	/* 餘額轉置退卡 */
	public static final String scXformRefundReturnCard = "862799";
	
	/* 聯名卡自動加值功能開啟 */
	public static final String scCoBranderCardAutoCredit = "813799";
	
	/* CPU現金加值 */
	public static final String scCPUCashAddCredit = "811799";
	
	/* CPU社福卡現金加值 */
	public static final String scCPUSocialCareCashAddCredit = "841799";
	
	/* CPU自動加值 */
	public static final String scCPUAutoAddCredit = "825799";
	
	/* CPU社福卡自動加值 */
	public static final String scCPUSocialCareAutoAddCredit = "845799";
	
	/* CPU金融卡加值 */
	public static final String scCPUATMAddCredit = "837799";
	
	/* CPU社福卡金融卡加值 */
	public static final String scCPUSocialCareATMAddCredit = "847799";
	
	/* CPU餘額轉置加值 */
	public static final String scCPUXformRefundAddCredit = "839799";
	
	/* CPU取消加值 */
	public static final String scCPUCancelAddCredit = "811899";
	
	/* CPU購貨 */
	public static final String scCPUMerchandise = "811599";
	
	/* CPU取消購貨 */
	public static final String scCPUCancelMerchandise = "823899";
	
	/* CPU餘額轉置購貨 */
	public static final String scCPUXformRefundMerchandise = "813599";
	
	/* CPU售卡 */
	public static final String scCPUSellCard = "851799";
	
	/* CPU取消售卡 */
	public static final String scCPUCancelSellCard = "851899";
	
	/* CPU展期 */
	public static final String scCPUExhibit = "811399";
	
	/* CPU交易合法驗證 */
	public static final String scCPUTXNAuth = "816399";

	/* CPU退貨 */
	public static final String scCPUReturnMerchandise = "851999";
	
	/* CPU退卡 */
	public static final String scCPUReturnCard = "864799";
	
	/* CPU餘額轉置退卡 */
	public static final String scCPUXformRefundReturnCard = "863799";
	
	/* CPU聯名卡自動加值功能開啟 */
	public static final String scCPUCoBranderCardAutoCredit = "814799";
}
