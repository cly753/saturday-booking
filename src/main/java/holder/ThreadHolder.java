package holder;

import java.text.ParseException;
import java.util.Date;

import booking.Configure;

public class ThreadHolder extends Thread {
	public static int count = 0;

	private String LABEL = "%%% holder.RunnableHolder %%%";
	public int id;

	public Date eventDate;
	public Date openTime;

	private String basketSession;
	private boolean holding;
	private Date lastHoldTime;

	private boolean goon;
	private final long interval = 13 * 60 * 1000;

	@Override
	public void run() {
		try {
			while (true) {
				while (goon && !holding) {
					holding = gank();
					if (holding) {
						System.out.print(LABEL + " " + eventDate + " ok.\n");
						break;
					}

					if (new Date().getTime() - openTime.getTime() > 10 * 1000) {
						System.out.println("%\n" + LABEL + " sorry..... + " + eventDate + "\n%");
						goon = false;
					}
				}
				if (!goon) break;

				farm();

				boolean resultPush = push();
				holding = false;
				openTime = new Date();
			}
		} catch (InterruptedException e) {
			System.out.println(LABEL + " Exception: " + e.getMessage());
		}
		push();
	}

	public ThreadHolder(Date date) {
		id = count++;
		LABEL = LABEL + " id = " + id + " %%%";
		this.setName(LABEL);

		eventDate = date;
		holding = false;
		goon = true;
		lastHoldTime = new Date(0);
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
		boolean ok = MyRequest.requestAdd(this.basketSession, Configure.eventFormat.format(eventDate));
		lastHoldTime = new Date(); // sync
		System.out.println(this.basketSession);
		if (!ok)
			while (MyRequest.error.size() > 0)
				System.out.println("Error: Holder: " + MyRequest.error.poll());
		return ok;
	}

	public void farm() throws InterruptedException {
		while (new Date().getTime() - lastHoldTime.getTime() < interval) {
			System.out.print(id);
			Thread.sleep(90 * 1000);
		}
		System.out.println();
	}

	public boolean push() {
		System.out.println(LABEL + " push() %%%");
		boolean ok = MyRequest.requestRemoveAll(this.basketSession);
		lastHoldTime = new Date(0);
		if (!ok)
			while (MyRequest.error.size() > 0)
				System.out.println("Error: Holder: " + MyRequest.error.poll());
		return ok;
	}

	public void letMe() {
		goon = false;
		this.interrupt();
	}
}
