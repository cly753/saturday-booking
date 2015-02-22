package booking;

import holder.Holder;

import java.util.ArrayList;

import notification.Notifier;

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
		
		Holder h = new Holder();
		Holder h2 = new Holder();
		
		h.hold(date.get(0));
//		for (int i = 30; i > 0; i--) {
//			Thread.sleep(1000);
//			System.out.println("count down... " + i);
//		}
		h.release(date.get(0));
	}
}
