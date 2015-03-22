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
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ActElse.ActPlan;


public class Conf {
	public static final String LABEL = "%%% booking.Configure %%%";

	public static final String configurePath = "./configure.json";
	private static JSONObject conf;
	
	public static SimpleDateFormat startFormat;
	public static SimpleDateFormat responseFormat;
	public static SimpleDateFormat openFormat;
	public static SimpleDateFormat eventFormat;

	public static boolean init() throws IOException {
		System.out.println("Configure File: " + new File(configurePath).getAbsolutePath());
		Stream<String> lines = Files.lines(Paths.get(configurePath));
		String configureRaw = lines
				.reduce("", String::concat);
		conf = new JSONObject(configureRaw);

		lines.close();
		
		startFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		responseFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); // Sat, 21 Feb 2015 14:06:52 GMT
		eventFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		openFormat = new SimpleDateFormat("yyyy-MM-dd 00:00");
		return true;
	}
	
	public static String getUrlEntry() {
		return conf.getString("URL_ENTRY");
	}

	public static String getUrlApi() {
		return conf.getString("URL_API");
	}
	
	public static String getUrlLoad() {
		return conf.getString("URL_LOAD");
	}
	
	public static String getEmailUser() {
		return conf.getString("EMAIL_USER");
	}
	
	public static String getEmailPass() {
		return conf.getString("EMAIL_PASS");
	}
	
	public static String getEmailTo() {
		return conf.getString("EMAIL_TO");
	}
	
	public static String getEmailHost() {
		return conf.getString("EMAIL_HOST");
	}
	
	public static int getEmailPort() {
		return conf.getInt("EMAIL_PORT");
	}
	
	public static ArrayList<Date> getEventDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		ArrayList<Date> temp = new ArrayList<Date>();
		JSONArray arr = conf.getJSONArray("eventDate");
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
		return conf.getString("ACT_EMAIL");
	}
	public static String getActPassword() {
		return conf.getString("ACT_PASSWORD");
	}
	public static String getActUrlLogin() {
		return conf.getString("ACT_URL_LOGIN");
	}
	public static String getActUrlActivity() {
		return conf.getString("ACT_URL_ACTIVITY");
	}
	public static String getActUrlVenue() {
		return conf.getString("ACT_URL_VENUE");
	}
	public static String getActUrlSlotPre() {
		return conf.getString("ACT_URL_SLOT_PRE");
	}
	public static PriorityBlockingQueue<ActPlan> getActPlan() throws JSONException, ParseException {
		PriorityBlockingQueue<ActPlan> pbq = new PriorityBlockingQueue<ActPlan>();
		JSONArray ja = conf.getJSONArray("ACT_PLAN");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			List<Integer> hour = new ArrayList<Integer>();
			JSONArray jahour = jo.getJSONArray("hour");
			for (int j = 0; j < jahour.length(); j++)
				hour.add(jahour.getInt(j));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ActPlan ap = new ActPlan
						( jo.getString("activity")
						, jo.getString("venue")
						, sdf.parse(jo.getString("date"))
						, hour, jo.getInt("priority")
						);
			pbq.put(ap);
		}
		return pbq;
	}
	public static String getActUrlSlot() {
		return conf.getString("ACT_URL_SLOT");
	}
	public static String getActUrlCart() {
		return conf.getString("ACT_URL_CART");
	}
	public static String getActUrlDelete() {
		return conf.getString("ACT_URL_DELETE");
	}
	public static int getActSleepTime() {
		return conf.getInt("ACT_SLEEP_TIME");
	}
	public static String getActHost() {
		return conf.getString("ACT_HOST");
	}
}

