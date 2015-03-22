package ActElse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActPlan implements Comparable<ActPlan> {
	public String activity;
	public String venue;
	public Date date;
	public List<Integer> hour;
	public int priority;
	
	public ActPlan(String activity, String venue, Date date, List<Integer> hour, int priority) {
		this.activity = activity;
		this.venue = venue;
		this.date = date;
		this.hour = hour;
		this.priority = priority;
		
		System.out.println("Creating ActPlan... " + this);
	}
	
	@Override
	public int compareTo(ActPlan ap) {
		if (priority > ap.priority)
			return 1;
		if (priority == ap.priority)
			return 0;
		return -1;
	}
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
		String ret = "";
		ret += "activity: " + activity;
		ret += ", venue: " + venue;
		ret += ", date: " + sdf.format(date);
		ret += ", hour: ";
		for (Integer h : hour) ret += h + ":00 ";
		ret += ", priority: " + priority;
		return ret;
	}
}
