package ActStateS;

import Act.ActContext;
import Act.ActEngine;
import Act.ActState;
import ActElse.ActUtil;

public class ActDeleteState implements ActState {
	private ActContext context;

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
			
			//TODO change to fast hold // may more state
			context.setState(new ActQueryState());
		} catch (InterruptedException e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			if (context.parent.getAction().equals(ActEngine.ACTION.STOP)) {
				context.setState(new ActStopState());
			}
			else {
				context.setState(new ActStopState()); // unrecoverable / reserved
			}
		} catch (Exception e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState()); // unrecoverable
		}
	}
}
