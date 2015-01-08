package amobile.pc.io;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class RS232Port {
	
	SerialPort mPort = null;
	
	public static String[] sGetPortList() {
		return SerialPortList.getPortNames();
	}
	
	public boolean OpenPort(String portName) {
		
		String[] portNames = SerialPortList.getPortNames();
		if (portNames == null) {
			return false;
		}
		int i = 0;
		for (; i < portNames.length; i ++) {
			if (portNames[i].equals(portName)) {
				break;
			}
		}
		if (i >= portNames.length) {
			return false;
		}
		
		mPort = new SerialPort(portName);
		if (mPort == null) {
			return false;
		}
		
		try {
			if (!mPort.openPort()) {
				return false;
			}
			if (!mPort.setParams(SerialPort.BAUDRATE_115200, 
			        		SerialPort.DATABITS_8,
			        		SerialPort.STOPBITS_1,
			        		SerialPort.PARITY_NONE)) {
				ClosePort();
				return false;
			}
		} catch (SerialPortException e) {
			ClosePort();
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean ClosePort() {
		if (mPort == null || !mPort.isOpened()) {
			mPort = null;
			return false;
		}
		
		try {
			mPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		mPort = null;
		return true;
	}
	
	public boolean Write(byte[] bytes) {
		if (mPort == null || !mPort.isOpened() || 
				bytes == null || bytes.length <= 0) {
			return false;
		}
		
		try {
			return mPort.writeBytes(bytes);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public byte[] Read(int byteCount) {
		if (mPort == null || !mPort.isOpened()) {
			return null;
		}
	
		try {
			return mPort.readBytes(byteCount, 1000);
		} catch (SerialPortException | SerialPortTimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}
}
