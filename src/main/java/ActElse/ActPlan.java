package ActElse;

import java.util.List;

public class ActPlan implements Comparable<ActPlan> {
	public String activity;
	public String venue;
	public int dayInWeek;
	public int dayInMonth;
	public int month;
	public List<Integer> hour;
	public int priority;
	
	public ActPlan(String activity, String venue, int dayInWeek, int dayInMonth, int month, List<Integer> hour, int priority) {
		this.activity = activity;
		this.venue = venue;
		this.dayInWeek = dayInWeek;
		this.dayInMonth = dayInMonth;
		this.month = month;
		this.hour = hour;
		this.priority = priority;
	}
	
	@Override
	public int compareTo(ActPlan ap) {
		if (priority > ap.priority)
			return 1;
		if (priority == ap.priority)
			return 0;
		return -1;
	}
}
