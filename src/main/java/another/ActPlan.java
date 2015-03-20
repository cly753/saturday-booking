package another;

import java.util.List;

public class ActPlan {
	public String activity;
	public String venue;
	public int dayInWeek;
	public int dayInMonth;
	public List<Integer> hour;
	public int priority;
	
	public ActPlan() {
		
	}
	
	public int compareTo(ActPlan ap) {
		if (priority > ap.priority)
			return 1;
		if (priority == ap.priority)
			return 0;
		return -1;
	}
}
