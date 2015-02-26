package holder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadHolder extends Thread {

	@Override
	public void run() {
		
		try {
			pick();
			
			while (true) {
				while (goon && !holding) {
					holding = gank();
					if (holding) {
						System.out.print(LABEL + " " + eventD + " ok.\n");
						break;
					}

					if (new Date().getTime() - openTime.getTime() > 20 * 1000) {
						System.out.println(LABEL + " sorry......\n" + LABEL + " sorry......\n" + LABEL + " sorry......\n");
						goon = false;
					}
				}

				if (!goon) break;

				farm();

				boolean resultPush = push();
				holding = false;
				openTime = new Date();

				if (!goon) break;
			}
		} catch (Exception e) {
			System.out.println(LABEL + " Exception: " + e.getMessage());
		}

		try {
			push();
		} catch (Exception e) {
			System.out.println(LABEL + " push() %%% holder.requestRemoveAll() Exception");
		}
	}

	public static int count = 0;
	private String LABEL = "%%% holder.RunnableHolder %%%";
	private int id;

	private Date eventD;
	private SimpleDateFormat eventFormat;
	private Date openTime;
	private SimpleDateFormat openFormat;
	
	private Holder holder;
	private boolean holding;

	private boolean goon;
	private final long interval = 14 * 60 * 1000;

	public ThreadHolder(Date date) {
		id = count++;
		LABEL = LABEL + " id = " + id + " %%%";
		this.setName(LABEL);

		eventD = date;
		holding = false;
		goon = true;
		holder = new Holder();
		
		eventFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		openFormat = new SimpleDateFormat("yyyy-MM-dd 00:00");
		
		openTime = new Date(eventD.getTime() - 6 * 24 * 60 * 60 * 1000);
		try {
			openTime = openFormat.parse(openFormat.format(openTime));
		} catch (ParseException e2) {
			System.out.println(LABEL + " ParseException bad event format.");
		}
	}
	
	public void pick() throws InterruptedException {
		System.out.println(LABEL + " event: " + eventFormat.format(eventD) + ", open time: " + eventFormat.format(openTime));
		while (goon && new Date().getTime() - openTime.getTime() < -2 * 1000) {
			System.out.print("w");
			Thread.sleep(500);
		}
		System.out.println();
	}

	public boolean gank() throws Exception {
		boolean ok = holder.requestAdd(eventFormat.format(eventD));
		if (!ok)
			while (holder.error.size() > 0)
				System.out.println("Error: Holder: " + holder.error.poll());
		return ok;
	}

	public void farm() throws InterruptedException {
		while (new Date().getTime() - holder.lastHoldTime.getTime() < interval) {
			System.out.print(id);
			Thread.sleep(5000);
		}
		System.out.println();
	}

	public boolean push() throws Exception {
		System.out.print(LABEL + " push() %%%\n");
		boolean ret = holder.requestRemoveAll();
		if (!ret)
			while (holder.error.size() > 0)
				System.out.println("Error: Holder: " + holder.error.poll());

		return ret;
	}

	public void letMe() {
		goon = false;
		this.interrupt();
	}
}
