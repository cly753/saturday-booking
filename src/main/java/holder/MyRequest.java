package holder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import booking.Configure;

public class MyRequest {
	private static final String LABEL = "%%% holder.MyRequest %%%";

	public static ConcurrentLinkedQueue<String> error;

	public static boolean init() {
		error = new ConcurrentLinkedQueue<String>();
		return true;
	}

	public static boolean requestAdd(String basketSession, String eventDate) {
		try {
			boolean ok = true;

			HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
			con.setRequestMethod("POST");
			MyHeader.setHeader(new MyHeader(), con);

			con.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.writeBytes(MyRequest.getEncodedAdd(basketSession, eventDate));
			dos.flush();
			dos.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String responseBody = br.lines()
					.reduce("", String::concat);
			br.close();

			JSONObject tempJson = new JSONObject(responseBody);		
			basketSession = tempJson.getString("basketSessionId");

			Date lastRequestTime = Configure.responseFormat.parse(con.getHeaderField("Date"));
			System.out.println(LABEL + " hold..." + eventDate);
			//		System.out.println("response code: " + con.getResponseCode());
			//		System.out.println("response body: \n" + responseBody);
			System.out.println("last hold time: " + lastRequestTime.toString());
			System.out.println("time now      : " + Configure.responseFormat.format(new Date()));
			System.out.println();

			if (con.getResponseCode() != 200) {
				ok = false;
				error.add("responseCode = " + con.getResponseCode());
			}
			if (!tempJson.isNull("error")) {
				ok = false;
				error.add(eventDate + ": " + tempJson.getString("error"));
			}
			//		con.disconnect();
			return ok;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			error.add("In requestAdd(...): " + e.getClass().getSimpleName());
		} catch (Exception e) {
			error.add("In requestAdd(...): " + e.getClass().getSimpleName());
		}
		return false;
	}
	public static boolean requestRemove(String basketSession, String eventDate) {
		//		System.out.println(LABEL + " remove..." + eventDate);
		try {
			boolean ok = true;

			if (basketSession == null || basketSession == "0") {
				ok = false;
				error.add("No basket in hand");
				return ok;
			}

			HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
			con.setRequestMethod("POST");
			MyHeader.setHeader(new MyHeader(), con);

			con.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.writeBytes(MyRequest.getEncodedRemove(basketSession, eventDate));
			dos.flush();
			dos.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String responseBody = br.lines()
					.reduce("", String::concat);
			br.close();

			JSONObject tempJson = new JSONObject(responseBody);
			//		System.out.println("response code: " + con.getResponseCode());
			//		System.out.println("response body: \n" + responseBody);
			System.out.println();

			if (con.getResponseCode() != 200) {
				ok = false;
				error.add("responseCode = " + con.getResponseCode());
			}
			if (!tempJson.isNull("error")) {
				ok = false;
				error.add(eventDate + ": " + tempJson.getString("error"));
			}
			//		con.disconnect();
			return ok;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			error.add("In requestAdd(...): " + e.getClass().getSimpleName());
		} catch (Exception e) {
			error.add("In requestAdd(...): " + e.getClass().getSimpleName());
		}
		return false;
	}
	public static boolean requestRemoveAll(String basketSession) {
		//		System.out.println(LABEL + " remove all...");
		try {
			boolean ok = true;

			HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
			con.setRequestMethod("POST");
			MyHeader.setHeader(new MyHeader(), con);

			con.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			dos.writeBytes(MyRequest.getEncodedRemoveAll(basketSession));
			dos.flush();
			dos.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String responseBody = br.lines()
					.reduce("", String::concat);
			br.close();

			JSONObject tempJson = new JSONObject(responseBody);
			//		System.out.println("response code: " + con.getResponseCode());
			//		System.out.println("response body: \n" + responseBody);
			//		System.out.println();

			if (con.getResponseCode() != 200) {
				ok = false;
				error.add("responseCode = " + con.getResponseCode());
			}
			if (!tempJson.isNull("error")) {
				ok = false;
				error.add("error response: " + tempJson.getString("error"));
			}
			//		con.disconnect();
			return ok;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			error.add("In requestAdd(...): " + e.getClass().getTypeName());
		} catch (Exception e) {
			error.add("In requestAdd(...): " + e.getClass().getTypeName());
		}
		return false;
	}

	public static ArrayList<Space> requestLoad(String magic) {
		//		System.out.println(LABEL + " load...");
		try {
			boolean ok = true;

			String url = Configure.getUrlLoad() + "?" + MyRequest.getEncodedLoad(magic);
			HttpsURLConnection con = (HttpsURLConnection)new URL(url).openConnection();
			con.setRequestMethod("GET");
			MyHeader.setHeader(new MyHeader(), con);

			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				ok = false;
				error.add("responseCode = " + con.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String responseBody = br.lines()
					.reduce("", String::concat);
			br.close();

			ArrayList<Space> spaces = null;
			if (responseBody.length() >= 2 && responseBody.charAt(0) == '[' && responseBody.charAt(responseBody.length() - 1) == ']') {
				responseBody = "{ \"response\" : " + responseBody + " }";
				JSONObject tempResponse = new JSONObject(responseBody);
				JSONArray spacesJson = tempResponse.getJSONArray("response");
				spaces = new ArrayList<Space>();
				if (spacesJson != null && spacesJson.length() > 0)
					for (int i = 0; i < spacesJson.length(); i++) {
						JSONObject temp = spacesJson.getJSONObject(i);
						if (temp != null && temp.getString("sportId").equals("7"))
							spaces.add(new Space(temp));
					}
			}

			//		System.out.println("response code: " + responseCode);
			//		System.out.println("response body: \n" + responseBody);
			//		System.out.println();

			if (spaces == null) {
				ok = false;
				error.add("responseBody = " + responseBody);
			}
			if (!ok)
				return null;
			return spaces;
		} catch (IOException | ParseException e) {
			error.add("In requestAdd(...): " + e.getClass().getSimpleName());
		} catch (Exception e) {
			error.add("In requestAdd(...): " + e.getClass().getSimpleName());
		}
		return null;
	}

	private static String getEncodedAdd(String basketSession, String eventDate) throws Exception {
		Map<String, String> data = getDefaultData();

		data.put("action", "add");
		//			data.put("price", "30.00");
		//			data.put("basketype", "Peak");
		data.put("basket_session", basketSession);
		data.put("event_date", eventDate);

		return getEncoded(data);
	}
	private static String getEncodedRemove(String basketSession, String eventDate) throws Exception {
		Map<String, String> data = getDefaultData();

		data.put("action", "remove");
		data.put("basket_session", basketSession);
		data.put("event_date", eventDate);

		return getEncoded(data);
	}
	private static String getEncodedRemoveAll(String basketSession) throws Exception {
		Map<String, String> data = getDefaultData();

		data.put("action", "removeAll");
		data.put("basket_session", basketSession);

		return getEncoded(data);
	}

	private static String getEncodedLoad(String magic) throws Exception {
		Map<String, String> data = new HashMap<String, String>();
		data.put("id", magic);
		return getEncoded(data);
	}

	private static String getEncoded(Map<String, String> data) throws UnsupportedEncodingException {
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
	private static Map<String, String> getDefaultData() {
		Map<String, String> temp = new HashMap<String, String>();
		temp.put("event_type", "Activity");
		temp.put("facility_id", "7");
		return temp;
	}
}

// reference: http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
// reference: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/