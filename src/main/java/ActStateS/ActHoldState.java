package ActStateS;

import Act.ActContext;
import Act.ActEngine;
import Act.ActState;
import ActElse.ActUtil;

public class ActHoldState implements ActState {
	ActContext context;
	private static final String label = "## ActHoldState ##";

	@Override
	public void doAction(ActContext context) {
		this.context = context;
		
		System.out.println(label);
		
		try {
			String res = context.ar.hold(context.magicUrl, context.magicPayload, context.magicLocation);
			boolean result = ActUtil.successHold(res);
			if (result) {
				context.setState(new ActSleepState());
			}
			else {
				context.setState(new ActDeleteState()); // try another plan
			}
		} catch (InterruptedException e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState()); // unrecoverable / reserved
		} catch (Exception e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState()); // unrecoverable
		}
	}
}
