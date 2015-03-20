package booking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import another.ActPlan;


public class Conf {
	public static final String LABEL = "%%% booking.Configure %%%";

	public static final String configurePath = "./configure.json";
	private static JSONObject configure;
	
	public static SimpleDateFormat startFormat;
	public static SimpleDateFormat responseFormat;
	public static SimpleDateFormat openFormat;
	public static SimpleDateFormat eventFormat;

	public static boolean init() throws IOException {
		System.out.println("Configure File: " + new File(configurePath).getAbsolutePath());
		Stream<String> lines = Files.lines(Paths.get(configurePath));
		String configureRaw = lines
				.reduce("", String::concat);
		configure = new JSONObject(configureRaw);

		lines.close();
		
		startFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		responseFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); // Sat, 21 Feb 2015 14:06:52 GMT
		eventFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		openFormat = new SimpleDateFormat("yyyy-MM-dd 00:00");
		return true;
	}
	
	public static String getUrlEntry() {
		return configure.getString("URL_ENTRY");
	}

	public static String getUrlApi() {
		return configure.getString("URL_API");
	}
	
	public static String getUrlLoad() {
		return configure.getString("URL_LOAD");
	}
	
	public static String getEmailUser() {
		return configure.getString("EMAIL_USER");
	}
	
	public static String getEmailPass() {
		return configure.getString("EMAIL_PASS");
	}
	
	public static String getEmailTo() {
		return configure.getString("EMAIL_TO");
	}
	
	public static String getEmailHost() {
		return configure.getString("EMAIL_HOST");
	}
	
	public static int getEmailPort() {
		return configure.getInt("EMAIL_PORT");
	}
	
	public static ArrayList<Date> getEventDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		ArrayList<Date> temp = new ArrayList<Date>();
		JSONArray arr = configure.getJSONArray("eventDate");
		for (int i = 0; i < arr.length(); i++) {
			try {
				temp.add(format.parse(arr.getString(i)));
			} catch (JSONException e) {
				System.out.println(LABEL + " JSONException bad json format.");
			} catch (ParseException e) {
				System.out.println(LABEL + " ParseException bad event format.");
			}
		}
		return temp;
	}

	//	{
	//	    "buttontype": "Non Peak",
	//	    "sportId": "7",
	//	    "price": "15.00",
	//	    "start": "2015-03-06 08:00:00",
	//	    "courts_avail": 2,
	//	    "touristPrice": "22.00"
	//	}
	
	public static String getDateString() {
		Calendar c = Calendar.getInstance();
		
		String year   = "" + (c.get(Calendar.YEAR) - 1900);
		String month  = "" + c.get(Calendar.MONTH);
		String day    = "" + (c.get(Calendar.DAY_OF_WEEK) - 1);
		String hour   = "" + c.get(Calendar.HOUR_OF_DAY);
		String minute = "" + c.get(Calendar.MINUTE);
		String second = "" + c.get(Calendar.SECOND);
		String milli  = "" + c.get(Calendar.MILLISECOND);
		
//		System.out.println(year  );
//		System.out.println(month );
//		System.out.println(day   );
//		System.out.println(hour  );
//		System.out.println(minute);
//		System.out.println(second);
//		System.out.println(milli );
		
		return year + month + day + hour + minute + second + milli;
	}
	
	public static String getActEmail() {
		return configure.getString("ACT_EMAIL");
	}
	public static String getActPassword() {
		return configure.getString("ACT_PASSWORD");
	}
	public static String getActUrlLogin() {
		return configure.getString("ACT_URL_LOGIN");
	}
	public static String getActUrlActivity() {
		return configure.getString("ACT_URL_ACTIVITY");
	}
	public static String getActUrlVenue() {
		return configure.getString("ACT_URL_VENUE");
	}
	public static String getActUrlSlotPre() {
		return configure.getString("ACT_URL_SLOT_PRE");
	}
	public static PriorityQueue<ActPlan> getActWantedPlan() {
		return null;
	}
	public static String getActUrlSlot() {
		return configure.getString("ACT_URL_SLOT");
	}
	
	
	public static String getActUrlCart() {
		return configure.getString("ACT_URL_CART");
	}
	public static String getActUrlDelete() {
		return configure.getString("ACT_URL_DELETE");
	}
}
