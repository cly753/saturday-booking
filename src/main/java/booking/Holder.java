package booking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class Holder {
	// reference: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
	
	public static final String LABEL = "%%% booking.Holder %%% ";
	
	public Date lastHoldTime;
	public String basketSession;
	
	public Holder() {
		lastHoldTime = new Date(0);
		basketSession = "0";
	}

	public boolean hold(String eventDate) throws IOException, ParseException {
		HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.configure.getString("URL_API")).openConnection();
		con.setRequestMethod("POST");
		
		MyHeader.setHeader(new MyHeader(), con);
		
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(new AddRequest(basketSession, eventDate).getEncoded());
		dos.flush();
		dos.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseBody = br.lines()
				.reduce("", String::concat);
		br.close();
		
		basketSession = new JSONObject(responseBody).getString("basketSessionId");
		
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); // Sat, 21 Feb 2015 14:06:52 GMT
		lastHoldTime = format.parse(con.getHeaderField("Date"));
		
		System.out.println(LABEL + "hold...");
		System.out.println("response code: " + con.getResponseCode());
		System.out.println("response body: \n" + responseBody);
		System.out.println("basket session id: " + basketSession);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		// con.disconnect();

		return true;
	}
	
	public boolean release(String eventDate) throws MalformedURLException, IOException, ParseException {
		if (basketSession == null || basketSession == "0") {
			System.out.println("No basket in hand.");
			return false;
		}
		
		HttpsURLConnection con = (HttpsURLConnection)new URL(Configure.configure.getString("URL_API")).openConnection();
		con.setRequestMethod("POST");
		
		MyHeader.setHeader(new MyHeader(), con);
		
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(new RemoveRequest(basketSession, eventDate).getEncoded());
		dos.flush();
		dos.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String responseBody = br.lines()
				.reduce("", String::concat);
		br.close();
				
		lastHoldTime = new Date(0);
		
		System.out.println(LABEL + "hold...");
		System.out.println("response code: " + con.getResponseCode());
		System.out.println("response body: \n" + responseBody);
		System.out.println("last hold time: " + lastHoldTime.toString());
		
		return true;
	}
	
	public String getBasketSession() {
		return basketSession;
	}

	class AddRequest extends MyRequest {
		public AddRequest(String basketSession, String eventDate) {
			data = MyRequest.getDefaultData();
			
			data.put("action", "add");
			data.put("price", "15.00");
			data.put("basketype", "Non Peak");

			setAction("add");
			setBasketSession(basketSession);
			setEventDate(eventDate);
		}
	}
	
	class RemoveRequest extends MyRequest {
		public RemoveRequest(String basketSession, String eventDate) {
			data = MyRequest.getDefaultData();
						
			setAction("remove");
			setBasketSession(basketSession);
			setEventDate(eventDate);
		}
	}

	class RemoveAllRequest extends MyRequest {
		public RemoveAllRequest(String basketSession) {
			data = new HashMap<String, String>();
			
			setAction("removeAll");
			setBasketSession(basketSession);
		}
	}
}
