package Act;

import java.util.concurrent.PriorityBlockingQueue;

import ActElse.ActPlan;
import ActStateS.ActStopState;
import ActStateS.ActUnauthorizedState;

public class ActEngine extends Thread {
	public enum ACTION { GOON, STOP };
	
	private ActContext context;
	
	public ActEngine(String email, String password, PriorityBlockingQueue<ActPlan> planList) {
		context = new ActContext(email, password, planList, this);
	}
	
	@Override
	public void run() {
		ActUnauthorizedState unauthorized = new ActUnauthorizedState();
		context.setState(unauthorized);
		
		while (true) {
			ActState currentState = context.getState();
			currentState.doAction(context);
			if (currentState instanceof ActStopState) {
				break;
			}
		}
	}
}
