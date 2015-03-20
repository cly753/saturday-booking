package another;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import booking.Conf;

public class ActRequest {
	private boolean DEBUG = true;
	private String host;
	HttpsURLConnection actCon;
	List<String> cookie;
	
	public ActRequest() {
		CookieHandler.setDefault(new CookieManager());
	}
	
	public boolean sayHi() throws Exception {
		URL actUrl = new URL(Conf.getActUrlLogin());
		host = actUrl.getHost();
		
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);

		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		actCon.setRequestProperty("Cache-Control", "max-age=0");
		actCon.setRequestProperty("Host", actUrl.getHost());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		
		cookie = actCon.getHeaderFields().get("Set-Cookie");
		return true;
	}

	public boolean login(String email, String password) throws Exception {
		URL actUrl = new URL(Conf.getActUrlLogin());
		actCon = (HttpsURLConnection) actUrl.openConnection();

		actCon.setUseCaches(false);
		actCon.setRequestMethod("POST");
		setLoginHeader();

		actCon.setDoOutput(true);
		actCon.setDoInput(true); // default

		DataOutputStream dos = new DataOutputStream(actCon.getOutputStream());
		dos.writeBytes("email=" + email + "&password=" + password); dos.flush(); dos.close();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		cookie = actCon.getHeaderFields().get("Set-Cookie");
		return true;
	}
	private void setLoginHeader() {
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Cache-Control", "max-age=0");
		actCon.setRequestProperty("Connection", "keep-alive");
		actCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", host);
		actCon.setRequestProperty("Origin", "https://" + this.host);
		actCon.setRequestProperty("Referer", "https://" + this.host + "/auth");
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");		
	}

	public String getActivity() throws Exception {
		URL actUrl = new URL(Conf.getActUrlActivity());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setActivityHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();
		if (DEBUG) System.out.println(allLine);
		
		return allLine;
	}
	private void setActivityHeader() {
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}

	public String getVenue(int activity_id) throws Exception {
		URL actUrl = new URL(Conf.getActUrlVenue());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setVenueHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		if (DEBUG) while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		//System.out.println(allLine);
		return allLine;
	}
	private void setVenueHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}

	public String getSlotPre(int activity_id, int venue_id, int dayInWeek, int dayInMonth) throws Exception {
		URL actUrl = new URL(Conf.getActUrlSlotPre() + "?" + getSlotPrePayload(activity_id, venue_id, dayInWeek, dayInMonth));
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setSlotPreHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");
		
		String location = null;
		if (responseCode != HttpsURLConnection.HTTP_OK) {
			if (responseCode == HttpsURLConnection.HTTP_MOVED_TEMP
				|| responseCode == HttpsURLConnection.HTTP_MOVED_PERM
					|| responseCode == HttpsURLConnection.HTTP_SEE_OTHER)
			location = actCon.getHeaderField("Location");
		}
		return location;
	}
	private void setSlotPreHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}
	private String getSlotPrePayload(int activity_id, int venue_id, int dayInWeek, int dayInMonth) {
		return "";
		//		activity_filter:
		//		venue_filter:
		//		day_filter:
		//		date_filter:
		//		search:
	}

	public String getSlot(String location) throws Exception {
		URL actUrl = new URL(location);
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setSlotHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setSlotHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}

	public String hold(String magicUrl, String magicPayload, String referer) throws Exception {
		URL actUrl = new URL(magicUrl);
		actCon = (HttpsURLConnection) actUrl.openConnection();

		actCon.setUseCaches(false);
		actCon.setRequestMethod("POST");
		setHoldHeader(referer);

		actCon.setDoOutput(true);
		actCon.setDoInput(true); // default

		DataOutputStream dos = new DataOutputStream(actCon.getOutputStream());
		dos.writeBytes(magicPayload); dos.flush(); dos.close();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setHoldHeader(String referer) {
		actCon.setRequestProperty("Accept", "*/*");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		actCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", referer);
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}

	public String getCart() throws Exception {
		URL actUrl = new URL(Conf.getActUrlCart());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setCartHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setCartHeader() {
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}

	public String delete(int booking_id, int shopping_cart_item_id) throws Exception {
		URL actUrl = new URL(Conf.getActUrlDelete());
		actCon = (HttpsURLConnection) actUrl.openConnection();

		actCon.setUseCaches(false);
		actCon.setRequestMethod("POST");
		setDeleteHeader();

		actCon.setDoOutput(true);
		actCon.setDoInput(true); // default

		DataOutputStream dos = new DataOutputStream(actCon.getOutputStream());
		dos.writeBytes("booking_id=" + booking_id + "&shopping_cart_item_id=" + shopping_cart_item_id); dos.flush(); dos.close();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setDeleteHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		actCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Origin", "https://" + this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlCart());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}
}
