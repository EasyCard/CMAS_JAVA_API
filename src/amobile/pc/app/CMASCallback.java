package amobile.pc.app;

import amobile.java.cmas.ICMASResponse;

public class CMASCallback implements ICMASResponse {

	private boolean mSuccess = false;
	
	private String mResp = null;
	
	private String mError = null;
	
	public boolean IsSuccess() {
		return mSuccess;
	}
	
	public String GetResponse() {
		return mResp;
	}
	
	public String GetError() {
		return mError;
	}
	
	@Override
	public void onReady(String resp) {
		mSuccess = true;
		mResp = resp;
		
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public void onError(String err) {
		mSuccess = false;
		mError = err;
		
		synchronized (this) {
			this.notifyAll();
		}
	}	
}
