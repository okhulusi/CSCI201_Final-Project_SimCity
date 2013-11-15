package role.market;
import MarketRole.MarketCashierRole.*;
import Container.Market;
import java.util.*;
import Utli.Item;

public class MarketEmployeeRole {
	Market m;
	List<CustomerOrder> pickUpOrders;
	List<RestaurantOrder> deliverOrders;

	public void msgPickOrder(CustomerOrder mc){
		pickUpOrders.add(mc);
	}

	public void msgPickOrder(RestaurantOrder rc){
		deliverOrders.add(rc);
	}

	public boolean pickAndExecuteAnAction(){
		if(pickUpOrders.size()!=0){
			pickUpOrders(pickUpOrders.get(0));
			pickUpOrders.remove(0);
			return true;
		}
		if(deliverOrders.size()!=0){
			deliverFood(deliverOrders.get(0));
			deliverOrders.remove(0);
			return true;
		}
		return false;
	}
	
	public void pickUpOrders(CustomerOrder mc){
		for (Item item : mc.orderFulfillment)
			//DoPickUp(item);
		m.MarketCashier.msgHereAreGoods(mc);
	}
	
	public void deliverFood(RestaurantOrder mc){
		for (Item item : mc.orderFulfillment){
			;
		}
			//DoPickUp(Item);
		//Transportation.Truck.msgDeliverToCook(mc.r, mc.orderFulfillment, mc.bill);
	}
}
