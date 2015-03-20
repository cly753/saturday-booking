package booking;

import holder.MyRequest;
import holder.Space;
import holder.ThreadHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import Act.ActMain;

public class Main {

	public static void main(String[] args) {
		try {
			Conf.init();
			MyRequest.init();
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		
		ActMain am = new ActMain();
		am.go();

//		go();
	}

	public static void go() {
		ArrayList<Date> date = Conf.getEventDate();
		ArrayList<ThreadHolder> hArr = new ArrayList<ThreadHolder>();
		for (Date d : date)
			hArr.add(new ThreadHolder(d));
		
		boolean peek = false;
		if (peek) {
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

		for (ThreadHolder t : hArr)
			t.start();

		Scanner sc = new Scanner(System.in);
		while (true) {
			int iStop = sc.nextInt();
			if (iStop >= hArr.size() || iStop < 0)
				continue;
			
			hArr.get(iStop).terminate();
			
			boolean someLive = false;
			for (ThreadHolder t : hArr)
				someLive |= t.isAlive();
			if (!someLive)
				break;
		}
	}
}
