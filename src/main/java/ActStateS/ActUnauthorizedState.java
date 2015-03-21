package ActStateS;

import java.util.Map.Entry;

import Act.ActContext;
import Act.ActState;
import ActElse.ActUtil;

public class ActUnauthorizedState implements ActState {
	private ActContext context;
	private static final String label = "## ActUnauthorizedState ##";
	
	private static final boolean DEBUG = false;

	@Override
	public void doAction(ActContext context) {
		this.context = context;
		
		System.out.println(label);
		
		try {
//			login();
			updateList();
			
			context.setState(new ActStopState());
//			context.setState(new ActQueryState());
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
	
	private boolean login() throws Exception {
		boolean result;
		
		result = context.ar.sayHi();
		result = context.ar.login(context.email, context.password);
		
		return true;
	}
	
	private boolean updateList() throws Exception {
		String res;
		
		res = context.ar.getActivity();
		context.activityList = ActUtil.parseActivity(res);
		
		if (DEBUG) for (Entry<String, Integer> e : context.activityList.entrySet())
			System.out.println("key: " + e.getKey() + ", value: " + e.getValue());
		if (DEBUG) System.out.println("activity: " + context.ap.activity + ", id: " + context.activityList.get(context.ap.activity));
		
		res = context.ar.getVenue(context.activityList.get(context.ap.activity));
		context.venueList = ActUtil.parseVenue(res);
		
		//TODO check "max_date" in response
		
		if (DEBUG) for (Entry<String, Integer> e : context.venueList.entrySet())
			System.out.println("key: " + e.getKey() + ", value: " + e.getValue());
		
		return true;
	}
}
