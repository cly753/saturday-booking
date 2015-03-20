package ActStateS;

import Act.ActContext;
import Act.ActState;
import ActElse.ActUtil;

public class ActStopState implements ActState {
	ActContext context;

	@Override
	public void doAction(ActContext context) {		
		this.context = context;
		
		try {
			String res = context.ar.getCart();
			
			int[] bookingIdShoppingCartItemId = ActUtil.parseCart(res);
			for (int i = 0; i < bookingIdShoppingCartItemId.length; i += 2) {
				res = context.ar.delete(bookingIdShoppingCartItemId[i], bookingIdShoppingCartItemId[i + 1]);
				boolean result = ActUtil.successDelete(res);

				if (result) {
					;
				}
				else {
					;
				}
			}
		} catch (InterruptedException e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
		} catch (Exception e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
		}
	}
}
