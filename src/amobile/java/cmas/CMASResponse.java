package amobile.java.cmas;

public class CMASResponse {
private String mResp;
	
	public static CMASResponse sGetInstance(String resp) {
		return new CMASResponse(resp);
	}
	
	public CMASResponse(String resp) {
		mResp = resp;
	}
	
	public int GetValueCount(String tag) {
		if (mResp == null) {
			return 0;
		}
		
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		int count = 0;
		int start = 0;
		int end = 0;
		while (true) {
			start = mResp.indexOf(startTag, end);
			if (start == -1) {
				return count;
			}
			end = mResp.indexOf(endTag, start);
			if (end == -1) {
				return count;
			}
			count ++;
		}
	}
	
	public String GetValue(String tag) {
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		int start = mResp.indexOf(startTag);
		if (start == -1) {
			return "";
		}
		int end = mResp.indexOf(endTag, start);
		if (end == -1) {
			return "";
		}
		return mResp.substring(start + startTag.length(), end);	
	}
	
	public String GetValue(String tag, String subTag, int index) {
		if (mResp == null) {
			return "";
		}
		
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		String subStartTag = "<" + subTag + ">";
		String subEndTag = "</" + subTag + ">";
		int count = 0;
		int start = 0;
		int end = 0;
		while (true) {
			start = mResp.indexOf(startTag, end);
			if (start == -1) {
				return "";
			}
			end = mResp.indexOf(endTag, start);
			if (end == -1) {
				return "";
			}
			if (count == index) {
				break;
			}
			count ++;
		}
		
		start = mResp.indexOf(subStartTag, start);
		if (start == -1) {
			return "";
		}
		end = mResp.indexOf(subEndTag, start);
		if (end == -1) {
			return "";
		}
		return mResp.substring(start + subStartTag.length(), end);
	}	
}
