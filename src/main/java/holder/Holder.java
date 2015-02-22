package holder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import booking.Configure;

public class Holder {
	// reference: http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
	// reference: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
		
	public static int count = 0;
	
	private String LABEL = "%%% holder.Holder %%%";
	
	public Date lastHoldTime;
	public String basketSession;
	
	public ArrayList<String> error;
	
	public Holder() {
		lastHoldTime = new Date(0);
		basketSession = "0";
		error = new ArrayList<String>();
		
		LABEL = LABEL + " id = " + (count++) + " %%%";
	}
	
	// re-entrant?
	public boolean requestAdd(String eventDate) throws Exception {
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
		
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); // Sat, 21 Feb 2015 14:06:52 GMT
		lastHoldTime = format.parse(con.getHeaderField("Date"));
		
		System.out.println(LABEL + " hold..." + eventDate);
//		System.out.println("response code: " + con.getResponseCode());
//		System.out.println("response body: \n" + responseBody);
		System.out.println("basket session id: " + basketSession);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (con.getResponseCode() != 200) {
			ok = false;
			error.add("responseCode=" + con.getResponseCode());
			System.out.println(error.get(error.size()-1));
		}
		
		if (!tempJson.isNull("error")) {
			ok = false;
			error.add(tempJson.getString("error"));
			System.out.println(error.get(error.size()-1));
		}
		System.out.println();
		// con.disconnect();
		return ok;
	}
	
	public boolean requestRemove(String eventDate) throws Exception {
		boolean ok = true;
		
		if (basketSession == null || basketSession == "0") {
			ok = false;
			error.add("No basket in hand");
			System.out.println(error.get(error.size()-1));
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
		
		lastHoldTime = new Date(0);
		
		System.out.println(LABEL + " remove..." + eventDate);
//		System.out.println("response code: " + con.getResponseCode());
//		System.out.println("response body: \n" + responseBody);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (con.getResponseCode() != 200) {
			ok = false;
			error.add("responseCode=" + con.getResponseCode());
			System.out.println(error.get(error.size()-1));
		}
		
		if (!tempJson.isNull("error")) {
			ok = false;
			error.add(tempJson.getString("error"));
			System.out.println(error.get(error.size()-1));
		}
		System.out.println();
		return ok;
	}
	
	public boolean requestRemoveAll() throws Exception {
		boolean ok = true;
		
		if (basketSession == "0") {
			ok = false;
			error.add("no basket in hand");
			System.out.println(error.get(error.size()-1));
			return ok;
		}
		
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
		
		lastHoldTime = new Date(0);
		
		System.out.println(LABEL + " remove all...");
//		System.out.println("response code: " + con.getResponseCode());
//		System.out.println("response body: \n" + responseBody);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (con.getResponseCode() != 200) {
			ok = false;
			error.add("responseCode=" + con.getResponseCode());
			System.out.println(error.get(error.size()-1));
		}
		
		if (!tempJson.isNull("error")) {
			ok = false;
			error.add(tempJson.getString("error"));
			System.out.println(error.get(error.size()-1));
		}
		System.out.println();
		return ok;
	}
	
	@Deprecated
	public String request(MyRequest.Type type, String eventDate) throws Exception {
		String retError = "";
		
		switch (type) {
		case ADD:
			break;
		case REMOVE:
			if (basketSession == "0") {
				retError += "No basket in hand.";
				System.out.println(retError);
				return retError;
			}
			break;
		case REMOVEALL:
			if (basketSession == "0") {
				retError += "No basket in hand.";
				System.out.println(retError);
				return retError;
			}
			break;
		default:
			break;
		}
		
		HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
		con.setRequestMethod("POST");
		
		MyHeader.setHeader(new MyHeader(), con);
		
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		
		switch (type) {
		case ADD:
			dos.writeBytes(MyRequest.getEncodedAdd(basketSession, eventDate));
			break;
		case REMOVE:
			dos.writeBytes(MyRequest.getEncodedRemove(basketSession, eventDate));
			break;
		case REMOVEALL:
			dos.writeBytes(MyRequest.getEncodedRemoveAll(basketSession));
			break;
		default:
			break;
		}
		
		dos.flush();
		dos.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseBody = br.lines()
				.reduce("", String::concat);
		br.close();
		
		JSONObject tempJson = new JSONObject(responseBody);
		switch (type) {
		case ADD:
			System.out.println(LABEL + " hold..." + eventDate);
			
			basketSession = tempJson.getString("basketSessionId");
			SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); // Sat, 21 Feb 2015 14:06:52 GMT
			lastHoldTime = format.parse(con.getHeaderField("Date"));
			break;
		case REMOVE:
			System.out.println(LABEL + " remove..." + eventDate);
			lastHoldTime = new Date(0);
			break;
		case REMOVEALL:
			System.out.println(LABEL + " remove all...");
			lastHoldTime = new Date(0);
			break;
		default:
			break;
		}
		
		System.out.println("response code: " + con.getResponseCode());
		System.out.println("response body: \n" + responseBody);
		System.out.println("basket session id: " + basketSession);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (!tempJson.isNull("error")) {
			retError += tempJson.getString("error");
			System.out.println("... error: " + retError + " ...");
		}
		System.out.println();
		con.disconnect();
		return retError;
	}
}
