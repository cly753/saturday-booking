package holder;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import booking.ActionListener;
import booking.Conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadHolder extends Thread {
	public static int count = 0;

	private static final Logger logger = LoggerFactory.getLogger(ThreadHolder.class);
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
                Random ran = new Random();
				int tryTimes = 2;
				while (!(goon = gank())) {
					tryTimes--;
					if (tryTimes < 1 && new Date().getTime() - openTime.getTime() > 15 * 1000) {
						logger.warn(Conf.eventFormat.format(eventDate) + " Sorry... Failed to hold " + eventDate);
						selfExitEvent();
						break;
					}
				}

				if (!goon) break;
				else logger.info(Conf.eventFormat.format(eventDate) + " [OK] " + this.basketSession);

				logger.info(Conf.eventFormat.format(eventDate) + " Going to sleep...");
				Thread.sleep(interval);

				push();
				openTime = new Date();
			}
		} catch (InterruptedException e) {
			logger.info(Conf.eventFormat.format(eventDate) + " Exception: " + e.getMessage());
			push();
		}
	}

	public ThreadHolder(Date date) {
		id = count++;
		this.setName(" holder " + id + " ");

		goon = true;
		eventDate = date;
		basketSession = "0";

		openTime = new Date(eventDate.getTime() - 6 * 24 * 60 * 60 * 1000);
		try {
			openTime = Conf.openFormat.parse(Conf.openFormat.format(openTime));
		} catch (ParseException e) {
			logger.info(e.getClass().getSimpleName());
		}
		logger.info("Event: " + Conf.eventFormat.format(eventDate) + ", open time: " + Conf.eventFormat.format(openTime));
	}

	public boolean gank() {
		logger.info(Conf.eventFormat.format(eventDate) + " [Gank]");
		boolean ok = MyRequest.requestAdd(this);
		if (!ok)
			while (MyRequest.error.size() > 0)
				logger.warn(Conf.eventFormat.format(eventDate) + " Holder: " + MyRequest.error.poll());
		return ok;
	}

	public boolean push() {
		logger.info(Conf.eventFormat.format(eventDate) + " Push() %%%");
		boolean ok = MyRequest.requestRemoveAll(this);
		if (!ok)
			while (MyRequest.error.size() > 0)
				logger.warn(Conf.eventFormat.format(eventDate) + " Holder: " + MyRequest.error.poll());
		return ok;
	}

	public void terminate() {
		goon = false;
		this.interrupt();
	}

	private Map<String, ActionListener> listener;
	public void addListener(String event, ActionListener al) {
		if (listener == null)
			listener = new HashMap<>();
		listener.put(event, al);
	}
	public void selfExitEvent() {
		listener.get("selfExitEvent").doAction();
	}
}
