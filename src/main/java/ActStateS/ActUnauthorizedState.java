package ActStateS;

import java.util.Map.Entry;

import Act.ActContext;
import Act.ActState;
import ActElse.ActRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActUnauthorizedState implements ActState {
	private ActContext context;
	private static final String label = "## ActUnauthorizedState ## ";
	private static final Logger logger = LoggerFactory.getLogger(ActUnauthorizedState.class);


	@Override
	public void doAction(ActContext context) {
		this.context = context;

		logger.info("...");
		
		try {
			login();
			updateList();

			context.setState(new ActStopState());
//			context.setState(new ActQueryState());
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
	
	private boolean login() throws Exception {
		boolean result;
		
		result = context.ar.sayHi();
		result = context.ar.login(context.email, context.password);
		
		return true;
	}
	
	private boolean updateList() throws Exception {
		boolean DEBUG = false;
		
		String res = context.ar.getActivity();
		context.activityList = ActRequestUtil.parseActivity(res);
		
		if (DEBUG) for (Entry<String, Integer> e : context.activityList.entrySet())
			System.out.println(label + "key: " + e.getKey() + ", value: " + e.getValue());
		
		res = context.ar.getVenue(context.activityList.get(context.ap.activity));
		context.venueList = ActRequestUtil.parseVenue(res);
		
		//TODO check "max_date" in response
		
		if (DEBUG) for (Entry<String, Integer> e : context.venueList.entrySet())
			System.out.println(label + "key: " + e.getKey() + ", value: " + e.getValue());
		
		return true;
	}
}
