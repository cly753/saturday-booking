package holder;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import booking.Conf;

public class Space {	
//	{
//	    "buttontype": "Non Peak",
//	    "sportId": "7",
//	    "price": "15.00",
//	    "start": "2015-03-06 08:00:00",
//	    "courts_avail": 2,
//	    "touristPrice": "22.00"
//	}
	
	public Map<String, String> value;

	public Space(JSONObject raw) {
		value = new HashMap<String, String>();
	    value.put("buttontype",    raw.getString("buttontype"  ));
	    value.put("sportId",       raw.getString("sportId"     ));
	    value.put("price",         raw.getString("price"       ));
	    value.put("start",         raw.getString("start"       ));
	    value.put("courts_avail",  String.valueOf(raw.getInt("courts_avail")));
	    value.put("touristPrice",  raw.getString("touristPrice"));
	}
	
	public boolean sameStart(Date that) {
		Date start;
		try {
			start = Conf.startFormat.parse(value.get("start"));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return start.equals(that);
	}
	
	public boolean sameDay(Date that) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(that);
			int dayOfThat = cal.get(Calendar.DAY_OF_YEAR);
			Date start = Conf.startFormat.parse(value.get("start"));
			cal.setTime(start);
			int day = cal.get(Calendar.DAY_OF_YEAR);
			return dayOfThat == day;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int available() {
		return Integer.parseInt(value.get("courts_avail"));
	}
	
	@Override
	public String toString() {
		return Arrays.toString(value.entrySet().toArray());
	}
}