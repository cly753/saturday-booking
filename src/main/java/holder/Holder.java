package holder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import booking.Configure;

public class Holder {
	// reference: http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
	// reference: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
		
	public static int count = 0;
	
	public int id;
	public String LABEL;
	
	public Date lastHoldTime;
	public String basketSession;
		
	public Holder() {
		lastHoldTime = new Date(0);
		basketSession = "0";
		
		id = count++;
		LABEL = "%%% booking.Holder " + id + " %%% ";
	}

	@Deprecated
	public boolean hold(String eventDate) throws Exception {
		HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
		con.setRequestMethod("POST");
		
		MyHeader.setHeader(new MyHeader(), con);
		
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(MyRequest.getEncoded(MyRequest.Type.ADD, basketSession, eventDate));
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
		
		System.out.println(LABEL + "hold...");
		System.out.println("response code: " + con.getResponseCode());
		System.out.println("response body: \n" + responseBody);
		System.out.println("basket session id: " + basketSession);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (!tempJson.isNull("error")) {
			System.out.println("... error: " + tempJson.getString("error") + " ...");
			return false;
		}
		
		// con.disconnect();

		return true;
	}
	
	@Deprecated
	public boolean release(String eventDate) throws Exception {
		if (basketSession == null || basketSession == "0") {
			System.out.println("No basket in hand.");
			return false;
		}
		
		HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
		con.setRequestMethod("POST");
		
		MyHeader.setHeader(new MyHeader(), con);
		
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(MyRequest.getEncoded(MyRequest.Type.REMOVE, basketSession, eventDate));
		dos.flush();
		dos.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseBody = br.lines()
				.reduce("", String::concat);
		br.close();
		
		JSONObject tempJson = new JSONObject(responseBody);
		
		lastHoldTime = new Date(0);
		
		System.out.println(LABEL + "release...");
		System.out.println("response code: " + con.getResponseCode());
		System.out.println("response body: \n" + responseBody);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (!tempJson.isNull("error")) {
			System.out.println("... error: " + tempJson.getString("error") + " ...");
			return false;
		}
		
		return true;
	}
	
	@Deprecated
	public boolean releaseAll() throws Exception {
		if (basketSession == null || basketSession == "0") {
			System.out.println("No basket in hand.");
			return false;
		}
		
		HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.getUrlApi()).openConnection();
		con.setRequestMethod("POST");
		
		MyHeader.setHeader(new MyHeader(), con);
		
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(MyRequest.getEncoded(MyRequest.Type.REMOVEALL, basketSession));
		dos.flush();
		dos.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseBody = br.lines()
				.reduce("", String::concat);
		br.close();
		
		JSONObject tempJson = new JSONObject(responseBody);
				
		lastHoldTime = new Date(0);
		
		System.out.println(LABEL + "release all...");
		System.out.println("response code: " + con.getResponseCode());
		System.out.println("response body: \n" + responseBody);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		if (!tempJson.isNull("error")) {
			System.out.println("... error: " + tempJson.getString("error") + " ...");
			return false;
		}
		
		return true;
	}
	
	public String getBasketSession() {
		return basketSession;
	}
}
