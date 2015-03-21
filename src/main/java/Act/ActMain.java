package Act;

import booking.Conf;

public class ActMain {	
	public static void go() {
		ActEngine ae = new ActEngine(Conf.getActEmail(), Conf.getActPassword(), Conf.getActPlan());
		ae.run();
		
		//TODO accept input
		
		//TODO 401 Unauthorized re-login
	}
}
