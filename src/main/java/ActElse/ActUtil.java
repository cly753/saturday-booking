package ActElse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ActUtil {
	public static Map<String, Integer> parseActivity(String raw) {
		//TODO
		Document doc = Jsoup.parse(raw);
		return new HashMap<String, Integer>();
	}
	
	public static Map<String, Integer> parseVenue(String raw) {
		//TODO
		return new HashMap<String, Integer>();
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
