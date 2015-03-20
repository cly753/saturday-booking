package Act;

import booking.Conf;

public class ActMain {
	private boolean DEBUG = true;
	
	public static void go() {
		ActEngine ae = new ActEngine(Conf.getActEmail(), Conf.getActPassword(), Conf.getActWantedPlan());
		ae.run();
		
		//TODO accept input
		
		//TODO 401 Unauthorized re-login
	}
}
