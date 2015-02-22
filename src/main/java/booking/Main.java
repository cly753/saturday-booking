package booking;

import holder.RunnableHolder;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void go() throws Exception {
		Configure.init();
			
		ArrayList<String> date = Configure.getEventDate();
		
		RunnableHolder h = new RunnableHolder();
		h.addEvent(date.get(0));
		h.addEvent(date.get(1));
		h.start();
		
		Scanner sc = new Scanner(System.in);
		sc.nextInt();
		h.goon = false;
		sc.close();
	}
}
