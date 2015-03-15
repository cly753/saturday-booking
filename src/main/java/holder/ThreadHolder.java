package holder;

import java.text.ParseException;
import java.util.Date;

import booking.Configure;

public class ThreadHolder extends Thread {
	public static int count = 0;

	private String LABEL = "%%% holder.RunnableHolder %%%";
	public int id;

	public Date eventDate;
	public String basketSession;

	public Date openTime;

	private boolean goon;
	private final long interval = 14 * 60 * 1000;

	@Override
	public void run() {
		try {
			while (true) {
				while (!(goon = gank()))
					if (new Date().getTime() - openTime.getTime() > 7 * 1000) {
						System.out.println("%\n" + LABEL + " sorry... " + eventDate + "\n%");
						break;
					}
				
				if (!goon) break;
				else System.out.println(LABEL + " " + eventDate + " ok. " + this.basketSession);

				System.out.println(LABEL + " go to sleep...");
				Thread.sleep(interval);
				
				push();
				openTime = new Date();
			}
		} catch (InterruptedException e) {
			System.out.println(LABEL + " Exception: " + e.getMessage());
			push();
		}
	}

	public ThreadHolder(Date date) {
		id = count++;
		LABEL = LABEL + " id = " + id + " %%%";
		this.setName(" holder " + id + " ");

		goon = true;
		eventDate = date;
		basketSession = "0";

		openTime = new Date(eventDate.getTime() - 6 * 24 * 60 * 60 * 1000);
		try {
			openTime = Configure.openFormat.parse(Configure.openFormat.format(openTime));
		} catch (ParseException e) {
			System.out.println(LABEL + " " + e.getClass().getSimpleName());
		}
		System.out.println(LABEL + " event: " + Configure.eventFormat.format(eventDate) + ", open time: " + Configure.eventFormat.format(openTime));
	}

	public boolean gank() {
		System.out.println(LABEL + " gank: " + Configure.eventFormat.format(eventDate));
		boolean ok = MyRequest.requestAdd(this);
		if (!ok)
			while (MyRequest.error.size() > 0)
				System.out.println("Error: Holder: " + MyRequest.error.poll());
		return ok;
	}

	public boolean push() {
		System.out.println(LABEL + " push() %%%");
		boolean ok = MyRequest.requestRemoveAll(this);
		if (!ok)
			while (MyRequest.error.size() > 0)
				System.out.println("Error: Holder: " + MyRequest.error.poll());
		return ok;
	}

	public void terminate() {
		goon = false;
		this.interrupt();
	}
}
