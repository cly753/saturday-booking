package booking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Configure {
	public static final String LABEL = "%%% booking.Configure %%%";

	public static final String configurePath = "./configure.json";
	private static JSONObject configure;

	public static boolean init() throws IOException {
		System.out.println("Configure File: " + new File(configurePath).getAbsolutePath());
		Stream<String> lines = Files.lines(Paths.get(configurePath));
		String configureRaw = lines
				.reduce("", String::concat);
		configure = new JSONObject(configureRaw);

		lines.close();
		return true;
	}
	
	public static String getUrlEntry() {
		return configure.getString("URL_ENTRY");
	}

	public static String getUrlApi() {
		return configure.getString("URL_API");
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
}

