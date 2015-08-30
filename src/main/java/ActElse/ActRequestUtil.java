package ActElse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActRequestUtil {

	private static final Logger logger = LoggerFactory.getLogger(ActRequestUtil.class);

	public static Map<String, Integer> parseActivity(String raw) {
		Map<String, Integer> hm = new HashMap<>();
		Elements ele = Jsoup.parse(raw).select("script[type=text/javascript");
		for (Element e : ele) {
			if (e.hasAttr("src"))
				continue;

			String text = e.data();
			int ali = text.indexOf("activity_list");
			if (ali == -1)
				continue;

			String al = text.substring(ali);
			int semicolon = al.indexOf(";");
			int square = al.indexOf("[");
			String arr = "{ \"data\" : " + al.substring(square, semicolon) + " }";
			JSONArray ja = new JSONObject(arr).getJSONArray("data");
			for (int j = 0; j < ja.length(); j++) {
				JSONObject each = ja.getJSONObject(j);
				hm.put(each.getString("name"), Integer.valueOf(each.getString("activity_id")));
			}
			break;
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
		Document doc = Jsoup.parse(raw);

		Elements slotEle = doc.select("input[name=timeslot[]");

		logger.info("found " + slotEle.size() + " slots...");
		for (Element e : slotEle)
			logger.debug(e.toString());

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
