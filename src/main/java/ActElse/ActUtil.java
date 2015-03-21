package ActElse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class ActUtil {
	public static Map<String, Integer> parseActivity(String raw) {
		Map<String, Integer> hm = new HashMap<String, Integer>();
		Elements ele = Jsoup.parse(raw).getElementsByTag("script");
		for (int i = 0; i < ele.size(); i++) {
			if (!ele.get(i).hasAttr("src")) {
				String text = ele.get(i).data();
				if (text.contains("activity_list")) {
					int ali = text.indexOf("activity_list");
					String al = text.substring(ali);
					int semicolon = al.indexOf(";");
					int square = al.indexOf("[");
					String arr = "{ \"data\" : " + al.substring(square, semicolon) + " }";
					JSONArray ja = new JSONObject(arr).getJSONArray("data");
					for (int j = 0; j < ja.length(); j++) {
						JSONObject each = ja.getJSONObject(j);
						hm.put(each.getString("name"), Integer.valueOf(each.getString("activity_id")));
					}
				}
			}
		}
		return hm;
	}
	
	public static Map<String, Integer> parseVenue(String raw) {
		Map<String, Integer> hm = new HashMap<String, Integer>();
		JSONArray ja = new JSONObject(raw).getJSONArray("venues");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jov = ja.getJSONObject(i);
			hm.put(jov.getString("name"), Integer.valueOf(jov.getString("venue_id")));
		}
		return 	hm;
	}
	
	public static List<ActSlot> parseSlot(String raw) {
		//TODO
		return null;
	}
	
	public static boolean checkValid(ActPlan ap, List<ActSlot> slot) {
		//TODO
		return true;
	}

	public static boolean successHold(String res) {
		//TODO
		return false;
	}	
	public static boolean successDelete(String res) {
		//TODO
		return true;
	}

	public static boolean parseDelete(String raw) {
		//TODO
		return true;
	}

	public static int[] parseCart(String raw) {
		//TODO
		int length = 0;
		return new int[2 * length]; // booking_id + shopping_cart_item_id
	}
}
