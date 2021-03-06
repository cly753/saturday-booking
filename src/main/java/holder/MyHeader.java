package holder;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class MyHeader {
	public Map<String, String> header;

	public MyHeader() {
		header = new HashMap<String, String>();
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
		header.put("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
//		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	}
	
	public Map<String, String> getHeader() {
		return header;
	}

	public static void setHeader(MyHeader header, HttpsURLConnection con) {
		for (Map.Entry<String, String> entry : header.getHeader().entrySet())
			con.setRequestProperty(entry.getKey(), entry.getValue());
	}
}