package Act;

import ActStateS.ActUnauthorizedState;

public class ActEngine extends Thread {

	public ActEngine() {
		
	}
	
	@Override
	public void run() {
		ActContext context = new ActContext();
		
//		ActUnauthorizedState unauthorized = new ActUnauthorizedState();
//		unauthorized.doAction(context);
		
		
	}
}
