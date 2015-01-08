package amobile.java.cmas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class CMASClient extends Thread {
	//private final String scCertFilePath = "d:\\temp\\CMAS-FTP51T.jks";
	private final String scCertFilePath = "CMAS-FTP51T.jks";
	private final String scPassWord = "Cmas@999";
	
	private final int scBufferLen = 65535;
	
	private SSLSocket mSocket = null;
	private String mUrl = null;
	private int mPort = 0; 
	
	private String mReq = "";
    private ICMASResponse mClient;
    
    private byte[] mBuffer = new byte[scBufferLen];
	
	public CMASClient(String url, int port) {
		mUrl = url;
		mPort = port;
	}
	
	private boolean connect() {
		if (mUrl == null) {
			return false;
		}
	
		KeyStore keystore = null;
		try {
			keystore = getKeyStore();
		} catch (KeyStoreException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (NoSuchAlgorithmException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (CertificateException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (IOException e) {
			mClient.onError(e.getMessage());
			return false;
		}
		
		mSocket = null;
		try {
			mSocket = getSSLSocket(keystore);
		} catch (KeyManagementException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (UnrecoverableKeyException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (KeyStoreException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (NoSuchAlgorithmException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (UnknownHostException e) {
			mClient.onError(e.getMessage());
			return false;
		} catch (IOException e) {
			mClient.onError(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private boolean disconnect() {
		if (mSocket == null) {
			return false;
		}
		
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean SendRequest(ICMASResponse client, String req) {
		mClient = client;
		mReq = req;
		
		synchronized (this) {
			this.notifyAll();
		}
		
		return true;
	}
	
	private KeyStore getKeyStore() throws KeyStoreException,
	  									  NoSuchAlgorithmException,
	  									  CertificateException,
	  									  IOException {
		FileInputStream fKeyStore = new FileInputStream(scCertFilePath);

		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(fKeyStore, scPassWord.toCharArray()); 

		fKeyStore.close();

		return keystore;
	}
	
	private SSLSocket getSSLSocket(KeyStore keystore) throws KeyStoreException,
	   														 NoSuchAlgorithmException,
	   														 IOException,
	   														 KeyManagementException,
	   														 UnknownHostException,
	   														 UnrecoverableKeyException,
	   														 IOException {
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SUNX509");
		tmf.init(keystore);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SUNX509");
		kmf.init(keystore, scPassWord.toCharArray()); 

		SSLContext context = SSLContext.getInstance("SSL");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		SocketFactory sf = context.getSocketFactory();
		return (SSLSocket) sf.createSocket(mUrl, mPort);
	}
	
	private void sendRequest() throws IOException {
		if (mSocket == null) {
			throw new IOException(ICMASResponse.scErr_NoConnection);
		}
		if (mReq == null) {
			throw new IOException(ICMASResponse.scErr_InvalidParam);
		}
		OutputStream out = mSocket.getOutputStream();
   	 	InputStream in = mSocket.getInputStream();
   	 	
   	 	out.write(mReq.getBytes("UTF-8"));
   	 	out.flush();

   	 	String resp = null;
   	 	int len = in.read(mBuffer);
   	 	if (len > 0) {
   	 		resp = new String(Arrays.copyOf(mBuffer, len), "UTF-8");
   	 	}

   	 	out.close();
   	 	in.close();
   	 	
   	 	if (mClient != null) {
			mClient.onReady(resp);
		}
	}
	
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				synchronized (this) {
					this.wait();
				}
				if (!connect()) {
					continue;
				}
				sendRequest(); 
				disconnect();
			} catch (InterruptedException e) {
				if (mClient != null) {
					mClient.onError(e.getMessage());
				}
        		break;
			} catch (IOException e) {
				if (mClient != null) {
					mClient.onError(e.getMessage());
				}
			}
		}
	}
}
