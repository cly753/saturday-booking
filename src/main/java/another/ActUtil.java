package another;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ActUtil {
	public static Map<String, Integer> parseActivity(String raw) {
		Document doc = Jsoup.parse(raw);
		return new HashMap<String, Integer>();
	}
	
	public static Map<String, Integer> parseVenue(String raw) {
		return new HashMap<String, Integer>();
	}
	
	public static List<ActSlot> parseSlot(String raw) {
		return null;
	}
	
	public static boolean checkValid(ActPlan ap, List<ActSlot> slot) {
		return true;
	}

	public static boolean successHold(String res) {
		return false;
	}	
	public static boolean successDelete(String res) {
		return true;
	}
	
	public static List<ActSlot> parseCart(String raw) {
		return null;
	}
	
	public static boolean parseDelete(String raw) {
		return true;
	}


}
