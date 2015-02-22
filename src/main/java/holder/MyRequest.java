package holder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MyRequest {
	public enum Type { ADD, REMOVE, REMOVEALL };

	public static String getEncodedAdd(String basketSession, String eventDate) throws Exception {
		Map<String, String> data = getDefaultData();

		data.put("action", "add");
		//			data.put("price", "30.00");
		//			data.put("basketype", "Peak");
		data.put("basket_session", basketSession);
		data.put("event_date", eventDate);

		return getEncoded(data);
	}

	public static String getEncodedRemove(String basketSession, String eventDate) throws Exception {
		Map<String, String> data = getDefaultData();

		data.put("action", "remove");
		data.put("basket_session", basketSession);
		data.put("event_date", eventDate);

		return getEncoded(data);
	}

	public static String getEncodedRemoveAll(String basketSession) throws Exception {
		Map<String, String> data = getDefaultData();
		
		data.put("action", "removeAll");
		data.put("basket_session", basketSession);
		
		return getEncoded(data);
	}

	private static String getEncoded(Map<String, String> data) throws UnsupportedEncodingException {
		String temp = "";
		boolean first = true;
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (first)
				first = false;
			else
				temp += "&";

			temp += URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
		}
		return temp;
	}

	private static Map<String, String> getDefaultData() {
		Map<String, String> temp = new HashMap<String, String>();
		temp.put("event_type", "Activity");
		temp.put("facility_id", "7");
		return temp;
	}
}