package another;

import java.util.PriorityQueue;

import booking.Conf;

public class ActMain {
	private boolean DEBUG = true;
	
	private PriorityQueue<ActPlan> planList;

	public ActMain() throws Exception {
		planList = Conf.getActWantedPlan();
		
		ActHolder ah = new ActHolder(Conf.getActEmail(), Conf.getActPassword(), planList);
		ah.run();
		
		// if 401 Unauthorized
	}
}
