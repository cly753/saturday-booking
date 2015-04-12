package ActStateS;

import Act.ActContext;
import Act.ActState;

public class ActStopState implements ActState {
	ActContext context;
	private static final String label = "## ActStopState ##";

	@Override
	public void doAction(ActContext context) {		
		this.context = context;
		
//		try {
//			
//		} catch (InterruptedException e) {
//			//TODO change to logger
//			System.out.println(e.getClass().getSimpleName());
//		} catch (Exception e) {
//			//TODO change to logger
//			System.out.println(e.getClass().getSimpleName());
//		}
	}
}
