package another;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import booking.Configure;

public class ActMain {
	private boolean DEBUG = true;

	private List<String> cookie;
	private HttpsURLConnection actCon;
	private String host;

	private Map<String, Integer> activity_list;
	private Map<String, Integer> venue_list;
	private List<ActPlan> plan_list;

	public ActMain() throws Exception {
		host = new URL(Configure.getActUrlLogin()).getHost();
		plan_list = Configure.getActWantedPlan();
	}

	public void go() throws Exception {
		sayHi();
		login();
		getActivity();
		getVenue(activity_list.get(Configure.getActWantedActivity()));
	}

	private void sayHi() throws Exception {
		URL actUrl = new URL(Configure.getActUrlLogin());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);

		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		actCon.setRequestProperty("Cache-Control", "max-age=0");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		cookie = actCon.getHeaderFields().get("Set-Cookie");

		//		BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
	}

	private void login() throws Exception {
		CookieHandler.setDefault(new CookieManager());

		URL actUrl = new URL(Configure.getActUrlLogin());
		actCon = (HttpsURLConnection) actUrl.openConnection();

		actCon.setUseCaches(false);
		actCon.setRequestMethod("POST");
		setLoginHeader();

		actCon.setDoOutput(true);
		actCon.setDoInput(true); // default

		DataOutputStream dos = new DataOutputStream(actCon.getOutputStream());
		dos.writeBytes(getFormData()); dos.flush(); dos.close();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		cookie = actCon.getHeaderFields().get("Set-Cookie");

		//		String inputline; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		//		if (DEBUG) while ((inputline = br.readLine()) != null) System.out.println("response| " + inputline);
		//		br.close();
	}
	private String getFormData() {
		return "email=" + Configure.getActEmail() + "&password=" + Configure.getActPassword();
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

	private Map<String, Integer> getActivity() throws Exception {
		URL actUrl = new URL(Configure.getActUrlActivity());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setActivityHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		if (DEBUG) while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		//		System.out.println(allLine);
		return parseActivity(allLine);
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
	private Map<String, Integer> parseActivity(String raw) {
		Document doc = Jsoup.parse(raw);
		return new HashMap<String, Integer>();
	}

	private Map<String, Integer> getVenue(int activity_id) throws Exception {
		URL actUrl = new URL(Configure.getActUrlVenue());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setVenueHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		if (DEBUG) while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		//		System.out.println(allLine);
		return parseVenue(allLine);
	}
	private void setVenueHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Configure.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}
	private Map<String, Integer> parseVenue(String raw) {
		return new HashMap<String, Integer>();
	}

	/*
	 * @return time string for query
	 */
	private String getSlotPre(ActPlan ap) throws Exception {
		URL actUrl = new URL(Configure.getActUrlSlotPre() + "?" + getSlotPreData(ap));
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setSlotPreHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		if (DEBUG) while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		System.out.println(allLine);
		return parseSlotPre(allLine);
	}
	private void setSlotPreHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Configure.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}
	/*
	 * @return payload for pre-query
	*/
	private String getSlotPreData(ActPlan ap) {
		return "";
		//		activity_filter:
		//		venue_filter:
		//		day_filter:
		//		date_filter:
		//		search:
	}
	private String parseSlotPre(String raw) {
		return "";
	}

	/*
	 * @return all slot information for activity+venue+date
	 */
	private List<ActSlot> getSlot(ActPlan ap) throws Exception {
		URL actUrl = new URL(Configure.getActUrlSlot() + getSlotUrl(ap) + "?" + getSlotData(ap));
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setSlotHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		if (DEBUG) while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		System.out.println(allLine);
		return parseSlot(allLine);
	}
	private void setSlotHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Configure.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}
	/*
	 * @return url for pre-query
	*/
	private String getSlotUrl(ActPlan ap) {
		return "";
	}
	/*
	 * @return payload for pre-query
	*/
	private String getSlotData(ActPlan ap) {
		return "";
		//		time_from:
	}
	private List<ActSlot> parseSlot(String raw) {
		return null;
	}
	/*
	 * @return whether available
	*/
	private boolean checkValid(ActPlan ap, List<ActSlot> slot) {
		return true;
	}
	/*
	 * @return whether added
	*/
	private boolean addToCart(ActPlan ap, ActSlot as) {
		return true;
	}

	/*
	 * @return slot in hand
	*/
	private List<ActSlot> getCart() throws Exception {
		URL actUrl = new URL(Configure.getActUrlCart());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setCartHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);
		cookie = actCon.getHeaderFields().get("Set-Cookie");

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		if (DEBUG) while ((oneLine = br.readLine()) != null) allLine += "response| " + oneLine; br.close();

		System.out.println(allLine);
		return parseCart(allLine);
	}
	private void setCartHeader() {
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Accept-Encoding", "gzip, deflate, lzma, sdch");
		actCon.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
		actCon.setRequestProperty("Connection", "keep-alive");
		for (String coo : cookie) actCon.addRequestProperty("Cookie", coo.split(";", 1)[0]);
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Configure.getActUrlActivity());
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36 OPR/28.0.1750.40");
	}
	private List<ActSlot> parseCart(String raw) {
		return null;
	}

}
