package ActElse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import booking.Conf;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActRequest {
	private static final String label = "## ActRequest ## ";
	private static final Logger logger = LoggerFactory.getLogger(ActRequest.class);

	private String host;

	private String publicKey;
	private String _csrf;

	CookieManager cookieManager;
	HttpsURLConnection actCon;

	public ActRequest() {
		host = Conf.getActHost();

		cookieManager = new CookieManager( null, CookiePolicy.ACCEPT_ALL );
		CookieHandler.setDefault( cookieManager );
		HttpsURLConnection.setFollowRedirects(true);
	}

	private void setCommonHeader() {
		// chrome
		actCon.setRequestProperty("Accept-Encoding", "deflate, sdch"); // causing problem?
		actCon.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		actCon.setRequestProperty("Connection", "keep-alive");
		actCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
	}
	private void retrievePublicKey_csrf(String html) {
		Document doc = Jsoup.parse(html);
		Element publicKeyNode = doc.select("input[name=rsapublickey]").first();
		if (publicKeyNode != null)
			publicKey = publicKeyNode.attr("value");

		Element _csrfNode = doc.select("input[name=_csrf]").first();
		if (_csrfNode != null)
			_csrf = _csrfNode.attr("value");

		logger.debug("publicKey: " + publicKey);
		logger.debug("_csrf: " + _csrf);
	}

	public boolean sayHi() throws Exception {
		String lbl = "## sayHi ## ";
		URL actUrl = new URL(Conf.getActUrlLogin());

		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);

		setCommonHeader();
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Cache-Control", "max-age=0");
		actCon.setRequestProperty("Host", actUrl.getHost());

		int responseCode = actCon.getResponseCode();
		logger.debug("response code: " + responseCode);
//		for (Entry<String, List<String>> e : actCon.getHeaderFields().entrySet()) logger.debug("key: " + e.getKey() + ", value: " + e.getValue());

		String oneLine, allLine = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream(), "UTF-8"));
		while ((oneLine = br.readLine()) != null) allLine += oneLine;  br.close();
		logger.debug("allLine: " + allLine);

		retrievePublicKey_csrf(allLine);

		for (HttpCookie cook : cookieManager.getCookieStore().getCookies())
			logger.debug("cookie: " + cook.getValue());


		return true;
	}

	public boolean login(String email, String password) throws Exception {
		HttpsURLConnection.setFollowRedirects(false);

		boolean DEBUG = true;
		String lbl = "## login ## ";

		URL actUrl = new URL(Conf.getActUrlLogin() + "/signin");
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setUseCaches(false);
		setLoginHeader();

		actCon.setDoOutput(true); // including .setRequestMethod("POST");
		actCon.setDoInput(true); // default

		Map<String, String> payload = new HashMap<String, String>();
		payload.put("email", Conf.getActEmail());
		payload.put("ecpassword", ActUtil.encrypt(publicKey, Conf.getActPassword()));
		payload.put("_csrf", _csrf);
		String encoded = ActRequestCommonUtil.getDataEncoded(payload);

		DataOutputStream dos = new DataOutputStream(actCon.getOutputStream());
		dos.writeBytes(encoded);
		dos.flush(); dos.close();

		int responseCode = actCon.getResponseCode();

		for (Map.Entry<String, List<String>> e : actCon.getRequestProperties().entrySet())
			for (String val : e.getValue())
				logger.debug("request  header: " + e.getKey() + ": " + val);

		logger.debug("response code  : " + responseCode);
		logger.debug("current url    : " + actCon.getURL());
		logger.debug("response msg   : " + actCon.getResponseMessage());
		for (Map.Entry<String, List<String>> e : actCon.getHeaderFields().entrySet())
			for (String val : e.getValue())
				logger.debug("response header: " + e.getKey() + ": " + val);

		String oneLine, allLine = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream(), "UTF-8"));
		while ((oneLine = br.readLine()) != null) allLine += oneLine;  br.close();

		logger.debug("response content: " + allLine);
		return true;
	}
	private void setLoginHeader() {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Cache-Control", "max-age=0");
		actCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		actCon.setRequestProperty("Host", host);
		actCon.setRequestProperty("Origin", "https://" + this.host);
		actCon.setRequestProperty("Referer", "https://" + this.host + "/auth");
	}

	public String getActivity() throws Exception {
		boolean DEBUG = false;
		String lbl = "## getActivity ## ";

		URL actUrl = new URL(Conf.getActUrlActivity());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setActivityHeader();

		int responseCode = actCon.getResponseCode();
		logger.debug(lbl, "response code: " + responseCode);

		String oneLine, allLine = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream(), "UTF-8"));
		while ((oneLine = br.readLine()) != null) allLine += oneLine;  br.close();
		if (DEBUG) System.out.println(label + lbl + allLine);

		if (DEBUG) System.out.println(label + lbl + actCon.getHeaderFields().get("Set-Cookie"));
		if (DEBUG) for (Entry<String, List<String>> e : actCon.getHeaderFields().entrySet()) System.out.println(label + lbl + "key: " + e.getKey() + ", value: " + e.getValue());
		return allLine;
	}
	private void setActivityHeader() {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Host", this.host);
	}

	public String getVenue(int activity_id) throws Exception {
		boolean DEBUG = false;
		String lbl = "## getVenue ## ";

		URL actUrl = new URL(Conf.getActUrlVenue() + "/" + activity_id);
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setVenueHeader();

		int responseCode = actCon.getResponseCode();
		logger.debug(lbl, "response code: " + responseCode);
		if (DEBUG) System.out.println("response code = " + responseCode);

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setVenueHeader() {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}

	public String getSlotPre(int activity_id, int venue_id, Date date) throws Exception {
		boolean DEBUG = true;
		String lbl = "## getSlotPre ## ";

		URL actUrl = new URL(Conf.getActUrlSlotPre() + "?" + getSlotPrePayload(activity_id, venue_id, date));

		logger.debug(lbl + actUrl.toString());

		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setSlotPreHeader();

		int responseCode = actCon.getResponseCode();
		logger.debug(lbl + "response code: " + responseCode);

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += oneLine; br.close();

		logger.debug(lbl + "allLine: " + allLine);
		return allLine;
	}
	private void setSlotPreHeader() {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
	}
	private String getSlotPrePayload(int activity_id, int venue_id, Date date) throws UnsupportedEncodingException {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		String ret = "";
		ret += "activity_filter=" + URLEncoder.encode(String.valueOf(activity_id), "UTF-8");
		ret += "&venue_filter=" + URLEncoder.encode(String.valueOf(venue_id), "UTF-8");
		ret += "&date_filter=" + URLEncoder.encode(String.valueOf((c.get(Calendar.DAY_OF_WEEK) - 1 - 1 + 7) % 7 + 1), "UTF-8");
		ret += "&date_filter=" + URLEncoder.encode(sdf.format(date), "UTF-8");
		ret += "&search=" + URLEncoder.encode("Search", "UTF-8");
		return ret;
		//		activity_filter:
		//		venue_filter:
		//		day_filter:
		//		date_filter:
		//		search:
	}

	@Deprecated
	public String getSlot(String location) throws Exception {
		boolean DEBUG = false;
		String lbl = "## getSlot ## ";

		URL actUrl = new URL(location);
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setSlotHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setSlotHeader() {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
	}


	public String hold(String magicUrl, String magicPayload, String referer) throws Exception {
		boolean DEBUG = false;
		String lbl = "## hold ## ";

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

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setHoldHeader(String referer) {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "*/*");
		actCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", referer);
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}


	public String getCart() throws Exception {
		boolean DEBUG = false;
		String lbl = "## getCart ## ";

		URL actUrl = new URL(Conf.getActUrlCart());
		actCon = (HttpsURLConnection) actUrl.openConnection();
		actCon.setRequestMethod("GET");
		actCon.setUseCaches(false);
		setCartHeader();

		int responseCode = actCon.getResponseCode();
		if (DEBUG) System.out.println("response code = " + responseCode);

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setCartHeader() {
		setCommonHeader();
		actCon.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlActivity());
	}


	public String delete(int booking_id, int shopping_cart_item_id) throws Exception {
		boolean DEBUG = false;
		String lbl = "## delete ## ";

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

		String oneLine, allLine = ""; BufferedReader br = new BufferedReader(new InputStreamReader(actCon.getInputStream()));
		while ((oneLine = br.readLine()) != null) allLine += oneLine; br.close();

		if (DEBUG) System.out.println(allLine);
		return allLine;
	}
	private void setDeleteHeader() {
		actCon.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		actCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		actCon.setRequestProperty("Host", this.host);
		actCon.setRequestProperty("Origin", "https://" + this.host);
		actCon.setRequestProperty("Referer", Conf.getActUrlCart());
		actCon.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	}
}
