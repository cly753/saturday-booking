package ActStateS;

import booking.Conf;
import Act.ActContext;
import Act.ActEngine;
import Act.ActState;

public class ActSleepState implements ActState {
	private ActContext context;
	private static final String label = "## ActSleepState ##";

	@Override
	public void doAction(ActContext context) {
		this.context = context;
		
		System.out.println(label);
		
		try {
			ActEngine.sleep(Conf.getActSleepTime());
			context.setState(new ActDeleteState());
		} catch (InterruptedException e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState()); // handover
		} catch (Exception e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState()); // unknow error
		}
	}
}
