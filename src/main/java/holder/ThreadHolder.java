package holder;

import java.util.ArrayList;
import java.util.Date;

public class ThreadHolder extends Thread {
	public static int count = 0;
	private String LABEL = "%%% holder.RunnableHolder %%%";

	private Thread t;

	ArrayList<String> eventDateS;

	public boolean goon;

	public ThreadHolder() {
		eventDateS = new ArrayList<String>();
		goon = true;

		LABEL = LABEL + " id = " + (count++) + " %%%";
	}

	public boolean addEvent(String event) {
		eventDateS.add(event);
		return true;
	}

	@Override
	public void start() {
		if (t == null) {
			t = new Thread(this, LABEL + " Thread %%%");
			t.start();
		}
	}
	
	@Override
	public void run() {
		Holder holder = new Holder();
		while (goon) {
			eventDateS.forEach(event -> {
				try {
					if (!holder.requestAdd(event))
						;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(LABEL + " fail to ADD: " + event + " %%%");
				}
			});

			int interval = 14 * 60 * 1000;
			while (goon && new Date().getTime() - holder.lastHoldTime.getTime() < interval) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println(LABEL + " InterruptedException %%%");
				}
			}

			try {
				if (!holder.requestRemoveAll())
					;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(LABEL + " fail to REMOVEALL %%%");
			}

			goon = false;
		}
	}
}
