package another;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ActHolder extends Thread {
	private PriorityQueue<ActPlan> planList;
	private ActPlan ap;
	
	private String email;
	private String password;
	
	private Map<String, Integer> activityList;
	private Map<String, Integer> venueList;
	
	private ActRequest ar;
	
	public ActHolder(String email, String password, PriorityQueue<ActPlan> planList) {
		this.email = email;
		this.password = password;
		this.planList = planList;
		this.ar = new ActRequest();
	}
	
	@Override
	public void run() {
		ap = planList.poll();
		boolean result;
		String res;
		boolean unauthorized = true;
		try {
			
			if (unauthorized) {
				result = login();
				result = updateList();
				unauthorized = true;
			}

			String location = ar.getSlotPre(activityList.get(ap.activity), venueList.get(ap.venue), ap.dayInWeek, ap.dayInMonth);
			res = ar.getSlot(location);
			List<ActSlot> slot = ActUtil.parseSlot(res);
			
			result = ActUtil.checkValid(ap, slot);
			if (result) { // available
				String magicUrl = "";
				String magicPayload = "";
				res = ar.hold(magicUrl, magicPayload, location);
				result = ActUtil.successHold(res);
				
				if (result) { // hold ok
					// wait
					// delete
					// hold
				}
				else {
					res = ar.getCart();
					// 
					
					int[] booking_id = new int[0];
					int[] shopping_cart_item_id = new int[0];
					for (int i = 0; i < booking_id.length; i++) {
						res = ar.delete(booking_id[i], shopping_cart_item_id[i]);
						result = ActUtil.successDelete(res);
						
						if (result) {
							
						}
						else {
							
						}
					}
					
					ap = planList.poll();
				}
			}
			else {
				ap = planList.poll();
			}
				
			
		} catch (Exception e) {
			System.out.println(e.getClass().getSimpleName());
			// exit
		}
	}
	
	private boolean login() throws Exception {
		boolean result;
		
		result = ar.sayHi();
		result = ar.login(email, password);
		
		return true;
	}
	
	private boolean updateList() throws Exception {
		String res;
		
		res = ar.getActivity();
		activityList = ActUtil.parseActivity(res);
		
		res = ar.getVenue(activityList.get(ap.activity));
		venueList = ActUtil.parseVenue(res);
		
		return true;
	}
}
