package Act;

import java.text.ParseException;

import org.json.JSONException;

import booking.Conf;

public class ActMain {	
	public static void go() {
		try {
			ActEngine ae = new ActEngine(Conf.getActEmail(), Conf.getActPassword(), Conf.getActPlan());
			ae.run();
		} catch (JSONException | ParseException e) {
			e.printStackTrace();
		}
		
		//TODO accept input
		
		//TODO 401 Unauthorized re-login
	}
}
