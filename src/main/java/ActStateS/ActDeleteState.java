package ActStateS;

import Act.ActContext;
import Act.ActState;
import ActElse.ActRequestUtil;

public class ActDeleteState implements ActState {
	private ActContext context;
	private static final String label = "## ActDeleteState ##";

	@Override
	public void doAction(ActContext context) {
		this.context = context;
		
		System.out.println(label);

		try {
			String res = context.ar.getCart();
			
			int[] bookingIdShoppingCartItemId = ActRequestUtil.parseCart(res);
			for (int i = 0; i < bookingIdShoppingCartItemId.length; i += 2) {
				res = context.ar.delete(bookingIdShoppingCartItemId[i], bookingIdShoppingCartItemId[i + 1]);
				boolean result = ActRequestUtil.successDelete(res);

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
			context.setState(new ActStopState());
		} catch (Exception e) {
			//TODO change to logger
			System.out.println(e.getClass().getSimpleName());
			context.setState(new ActStopState()); // unknow error
		}
	}
}
