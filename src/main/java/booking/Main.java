package booking;

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
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		
		ArrayList<Date> date = Configure.getEventDate();
		
		ArrayList<ThreadHolder> hArr = new ArrayList<ThreadHolder>();
		for (int i = 0; i < 2; i++)
			hArr.add(new ThreadHolder(date.get(i)));
		
		for (int i = 0; i < 2; i++)
			hArr.get(i).start();

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
