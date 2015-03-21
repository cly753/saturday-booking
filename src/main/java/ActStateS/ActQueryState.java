package ActStateS;

import java.util.List;

import Act.ActContext;
import Act.ActEngine;
import Act.ActState;
import ActElse.ActSlot;
import ActElse.ActUtil;

public class ActQueryState implements ActState {
	private ActContext context;
	private static final String label = "## ActQueryState ##";

	@Override
	public void doAction(ActContext context) {
		this.context = context;
		
		System.out.println(label);
		
		try {
			String location = context.ar.getSlotPre
					( context.activityList.get(context.ap.activity)
					, context.venueList.get(context.ap.venue)
					, context.ap.dayInWeek
					, context.ap.dayInMonth
					);
			
			String res = context.ar.getSlot(location);
			List<ActSlot> slot = ActUtil.parseSlot(res);
			
			boolean result = ActUtil.checkValid(context.ap, slot);
			if (result) {
				
				//TODO
				// set magic
				// set magic
				// set location
				
				context.setState(new ActHoldState());
			}
			else {
				context.ap = context.planList.poll();
				if (context.ap == null) {
					context.setState(new ActStopState());
				}
				else {
					context.setState(new ActQueryState());
				}
			}
		} catch (InterruptedException e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState());
		} catch (Exception e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState());
		}
	}
}
