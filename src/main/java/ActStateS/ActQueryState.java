package ActStateS;

import Act.ActContext;
import Act.ActState;
import ActElse.ActRequestUtil;
import ActElse.ActSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActQueryState implements ActState {
	private ActContext context;
	private static final String label = "## ActQueryState ##";
	private static final Logger logger = LoggerFactory.getLogger(ActQueryState.class);

	@Override
	public void doAction(ActContext context) {
		this.context = context;

		logger.info("...");
		
		try {
			String res = context.ar.getSlotPre
					( context.activityList.get(context.ap.activity)
					, context.venueList.get(context.ap.venue)
					, context.ap.date
					);

			List<ActSlot> slot = ActRequestUtil.parseSlot(res);

			boolean result = ActRequestUtil.checkValid(context.ap, slot);
			result = false;
			if (result) {

				//TODO
				// set magic
				// set magic
				// set location

				context.setState(new ActStopState());
//				context.setState(new ActHoldState());
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
			e.printStackTrace();
			context.setState(new ActStopState());
		}
	}
}
