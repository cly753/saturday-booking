package ActStateS;

import Act.ActContext;
import Act.ActState;
import ActElse.ActUtil;

public class ActStopState implements ActState {
	ActContext context;

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
