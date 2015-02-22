package holder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MyRequest {
	public enum Type { ADD, REMOVE, REMOVEALL };

	public static String getEncoded(Type type, String basketSession, String eventDate) throws Exception {
		Map<String, String> data = getDefaultData();

		switch (type) {
		case ADD:
			data.put("action", "add");
			data.put("price", "15.00");
			data.put("basketype", "Non Peak");

			data.put("basket_session", basketSession);
			data.put("event_date", eventDate);
			break;
		case REMOVE:
			data.put("action", "remove");

			data.put("basket_session", basketSession);
			data.put("event_date", eventDate);
			break;
		default:
			throw new Exception(" MyRequest.getEncoded ");
		}

		return getEncoded(data);
	}

	public static String getEncoded(Type type, String basketSession) throws Exception {
		Map<String, String> data = getDefaultData();
		
		switch (type) {
		case REMOVEALL:
			data.put("action", "removeAll");
			data.put("basket_session", basketSession);
			break;
		default:
			throw new Exception(" MyRequest.getEncoded ");
		}
		
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