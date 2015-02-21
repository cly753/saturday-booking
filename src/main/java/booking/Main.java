package booking;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;

public class Main {

	public static void main(String[] args) {
		try {
			go();
		} catch (IOException | JSONException | ParseException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void go() throws IOException, JSONException, ParseException, InterruptedException {
		Configure.init();
		
		JSONArray arr = Configure.configure.getJSONArray("eventDate");
		for (int i = 0; i < arr.length(); i++)
			System.out.println(arr.getString(i));
		
		Holder h = new Holder();
		h.hold(arr.getString(0));
		
		for (int i = 30; i > 0; i--) {
			Thread.sleep(1000);
			System.out.println("count down... " + i);
		}
		h.release(arr.getString(0));
	}
}
