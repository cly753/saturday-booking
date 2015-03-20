package ActStateS;

import Act.ActContext;
import Act.ActEngine;
import Act.ActState;
import ActElse.ActUtil;

public class ActUnauthorizedState implements ActState {
	private ActContext context;

	@Override
	public void doAction(ActContext context) {
		this.context = context;
		
		try {
			login();
			updateList();
			context.setState(new ActQueryState());
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
		
		res = context.ar.getVenue(context.activityList.get(context.ap.activity));
		context.venueList = ActUtil.parseVenue(res);
		
		return true;
	}
}
