package amobile.java.cmas;

public interface ICMASResponse {
	
	public static final String scErr_NoConnection = "No Socket connection created"; 
	public static final String scErr_InvalidParam = "Invalid Parameters";
	
	public void onReady(String resp);
    public void onError(String err);
}
