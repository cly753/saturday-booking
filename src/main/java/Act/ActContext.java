package Act;

import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import ActElse.ActPlan;
import ActElse.ActRequest;

public class ActContext {
	public ActState state;
	
	public ActEngine parent;
	public PriorityBlockingQueue<ActPlan> planList;
	
	public ActPlan ap;
	public String magicUrl = "";
	public String magicPayload = "";
	public String magicLocation = "";
	
	public String email;
	public String password;
	
	public Map<String, Integer> activityList;
	public Map<String, Integer> venueList;

	public ActRequest ar;
	
	public ActContext(String email, String password, PriorityBlockingQueue<ActPlan> planList, ActEngine parent) {
		this.email = email;
		this.password = password;
		this.planList = planList;
		this.ar = new ActRequest();
		this.parent = parent;
		
		ap = planList.poll();
	}
	
	public ActState getState() {
		return state;
	}
	public void setState(ActState state) {
		this.state = state;
	}
}
