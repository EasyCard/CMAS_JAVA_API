package amobile.java.cmas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReqDataCreator {
	private static final String scTRANSXML_Tag_Start = "<TransXML>";
	private static final String scTRANSXML_Tag_End = "</TransXML>";
	private static final String scTRANS_Tag_Start = "<TRANS>";
	private static final String scTRANS_Tag_End = "</TRANS>";
	
	private static ReqDataCreator sThis = null;
	
	private List<StringBuffer> mTransUnits = new ArrayList<StringBuffer>();
	
	public enum data_attribute {
		a,		// alpha characters, 1 data element = 1 byte
		an, 	// alpha-number characters, 1 data element = 1 byte
		ans, 	// alpha-number and special characters(all characters), 1 data element = 1 byte
		b, 		// binary data, 將給一個byte unpack為00-FF之2bytes ASCII字元, 1 data element = 2 bytes
		n,		// numeric data, each data element represents 4 bits BCD, 1 data element = 1 byte
	};
	
	public static ReqDataCreator sGetInstance() {
		if (sThis == null) {
			sThis = new ReqDataCreator();
		}
		return sThis;
	}
	
	private ReqDataCreator() {
		mTransUnits.clear();
	}
	
	public String GetReqData() {
		String reqData = scTRANSXML_Tag_Start + "\n";
		for (int i = 0; i < mTransUnits.size(); i ++) {
			StringBuffer sb = mTransUnits.get(i);
			reqData += sb.toString();
		}
		reqData += scTRANSXML_Tag_End;
		return reqData;
	}
	
	public int GetTransUnitCount() {
		return mTransUnits.size(); 
	}
	
	public void AddTransUnit() {
		StringBuffer sb = new StringBuffer();
		sb.append(scTRANS_Tag_Start + "\n");
		sb.append(scTRANS_Tag_End + "\n");
		mTransUnits.add(sb);
	}
	
	public void RemoveTransUnit(int index) {
		if (index < mTransUnits.size()) {
			mTransUnits.remove(index);
		}
	}
	
	public void RemoveAllTransUnit() {
		while (0 < mTransUnits.size()) {
			mTransUnits.remove(0);
		}
	}
	
	public boolean AddDataElement(String element, int unitIndex) {
		if (unitIndex < 0 || unitIndex >= mTransUnits.size()) {
			return false;
		}
		StringBuffer sb = mTransUnits.get(unitIndex);
		sb.insert(sb.lastIndexOf(scTRANS_Tag_End), element + "\n");
		return true;
	}
	
	public boolean AddDataElement(String element, String tag, int unitIndex) {
		if (unitIndex < 0 || unitIndex >= mTransUnits.size()) {
			return false;
		}
		tag = "<" + tag + ">";
		StringBuffer sb = mTransUnits.get(unitIndex);
		sb.insert(sb.lastIndexOf(tag), element + "\n");
		return true;
	}
	
	public boolean DumpElementsToFile(String path, String tag) {
		if (path == null) {
			return false;
		}
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		if (path.charAt(path.length() - 1) != '/' || 
				path.charAt(path.length() - 1) != '\\') {
			path += "/";
		}
		String fn = path + tag + sdfDate.format(now) + ".xml";
		
		File fxml = new File(fn);
		BufferedWriter writer = null;
		try {
			fxml.createNewFile();
		    writer = new BufferedWriter(new FileWriter(fxml));
		    writer.write(GetReqData());
		}
		catch (IOException e) {	
			return false;
		}
		finally {
		    try {
		        if (writer != null) {
		        	writer.close( );
		        }
		    }
		    catch (IOException e) {
		    }
		}
		
		return true;
	}	
}
