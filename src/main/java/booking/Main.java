package booking;

import Act.ActMain;
import holder.MyRequest;
import holder.Space;
import holder.ThreadHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

	public static void main(String[] args) {		
		try {
			Conf.init();
			MyRequest.init();
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}

		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("Application starting...");

		ActMain am = new ActMain();
		am.go();

//		go();
	}
	
	public static void test() {

	}

	public static void go() {
		ArrayList<Date> date = Conf.getEventDate();
		ArrayList<ThreadHolder> holders = new ArrayList<ThreadHolder>();
		for (Date d : date)
			holders.add(new ThreadHolder(d));
		
		if (false) {
			peekFirst(date, holders);
		}

		AtomicInteger nLive = new AtomicInteger(holders.size());
		for (ThreadHolder t : holders)
			t.addListener("selfExitEvent", new ActionListener() {
				@Override
				public void doAction() {
					nLive.getAndDecrement();
				}
			});
		for (ThreadHolder t : holders)
			t.start();

		Scanner sc = new Scanner(System.in);
		while (nLive.get() > 0) {
			String line = sc.nextLine();
			if (line.isEmpty())
				continue;
			
			int iStop;
			try {
				iStop = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Bad input.");
				continue;
			}
			
			if (iStop >= holders.size() || iStop < 0 || !holders.get(iStop).isAlive())
				continue;
			
			nLive.getAndDecrement();
			holders.get(iStop).terminate();
		}
		System.out.println("End._.");
	}
	
	public static void peekFirst(ArrayList<Date> date, ArrayList<ThreadHolder> hArr) {
		boolean poll = true;
		while (poll && new Date().getTime() - hArr.get(0).openTime.getTime() < 5 * 1000) {
				ArrayList<Space> spaces = MyRequest.requestLoad(Conf.getDateString());
				if (spaces == null) {
					while (MyRequest.error.size() > 0)
						System.out.println(MyRequest.error.poll());
					return ;
				}
				for (Space space : spaces) {
					if (space.sameStart(date.get(0))) {
						poll = false;
						break;
					}
				}
				if (new Date().getTime() % 60 * 1000 == 0)
					System.out.println("%%% still live ");
		}
	}
}
