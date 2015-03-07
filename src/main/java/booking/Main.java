package booking;

import holder.MyRequest;
import holder.Space;
import holder.ThreadHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		go();
	}

	public static void go() {
		try {
			Configure.init();
			MyRequest.init();
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		String magic = "1152012055888";
		
		ArrayList<Date> date = Configure.getEventDate();
		ArrayList<ThreadHolder> hArr = new ArrayList<ThreadHolder>();
		for (Date d : date)
			hArr.add(new ThreadHolder(d));
		
		boolean poll = true;
		while (poll && new Date().getTime() - hArr.get(0).openTime.getTime() < 20 * 1000) {
				ArrayList<Space> spaces = MyRequest.requestLoad(magic);
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
		
		for (ThreadHolder t : hArr)
			t.start();

		int running = hArr.size();
		Scanner sc = new Scanner(System.in);
		while (running > 0) {
			int iStop = sc.nextInt();
			if (iStop >= hArr.size() || iStop < 0)
				continue;
			
			if (hArr.get(iStop).isAlive()) {
				hArr.get(iStop).letMe();
				running--;
			}
		}
	}
}
