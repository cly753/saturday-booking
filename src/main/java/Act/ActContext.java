package Act;

public class ActContext {
	private ActState state;
	
	public ActContext() {
		
	}
	
	public ActState getState() {
		return state;
	}
	public void setState(ActState state) {
		this.state = state;
	}
}
