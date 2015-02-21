package booking;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class MyRequest {
	public Map<String, String> data;
	
	public Map<String, String> getData() {
		return data;
	};
	
	public void setAction(String newAction) {
		data.put("action", newAction);
	}
	
	public void setBasketSession(String newBasketSession) {
		data.put("basket_session", newBasketSession);
	};
	
	public void setEventDate(String newEventDate) {
		data.put("event_date", newEventDate);
	};	
	
	public String getEncoded() throws UnsupportedEncodingException {
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
	
	public static Map<String, String> getDefaultData() {
		Map<String, String> temp = new HashMap<String, String>();
		temp.put("event_type", "Activity");
		temp.put("facility_id", "7");
		return temp;
	}
}