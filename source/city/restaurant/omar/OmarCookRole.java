package city.restaurant.omar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;
import city.market.Item;
import city.market.Market;
import city.restaurant.RestaurantCookRole;
import city.restaurant.omar.Order.OrderStatus;
import city.restaurant.omar.gui.OmarCookGui;

public class OmarCookRole extends RestaurantCookRole {

	/**
	 * Restaurant Cook Agent
	 */
		//Data
		List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
		List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());
		
		Hashtable<String, Food> cookInventory;
		Timer cookTimer;
		
		String name;
		OmarCashierRole cashier;
		
		OmarCookGui cookGui = null;
		OmarRestaurant restaurant;
		
		public Semaphore cookSem = new Semaphore(0, true);
		
		enum MarketStatus {available, ordering, paying, paid, gone};
		
		public class MyMarket{
			public Market market;
			public MarketStatus marketState;
			public List<Food> currentOrder;
			public double currentOrderCost;
			
			public MyMarket(Market market){
				this.market = market;
				marketState = MarketStatus.available;
				currentOrder = Collections.synchronizedList(new ArrayList<Food>());
				currentOrderCost = 0;
			}
		}

		public OmarCookRole(OmarCashierRole cashier, PersonAgent p, OmarRestaurant r) { //starts out of food
			super(p);
			this.restaurant = r;
			name = "Chef Matt";
			this.cashier = cashier;
			//System.out.println("Chef Matt: About to Restock");
			
			cookInventory = new Hashtable<String, Food>();
			cookInventory.put("Pizza", new Food("Pizza", 1200.0, 0));
			cookInventory.put("Hot Dog", new Food("Hot Dog", 1500.0, 0));
			cookInventory.put("Burger", new Food("Burger", 2000.0, 0));
			cookInventory.put("Filet Mignon", new Food("Filet Mignon", 3500.0, 0));
		}
		
		// Messages
		public void msgHereIsAnOrder(FoodTicket ticket){
			orders.add(new Order(ticket.getW(), ticket.getC().tableNum, this, ticket.getC()));
			stateChanged();
		}
		
		public void msgIHaveNoFood(Market market){
		synchronized(markets){
			System.out.println("Market is out of Food! Ordering from next Market, Paid previous market $2");
			for(MyMarket m: markets){
				if(m.market == market){
					m.marketState = MarketStatus.gone;
					markets.remove(m);
					System.out.println("Ordering from second Market");
					markets.get(0).marketState = MarketStatus.ordering;
					stateChanged();
					return;
				}
			}
		}
		}
		
		public void msgHereIsMyPrice(Market market, List<Food> currentOrder, int currentOrderCost){
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.market == market){
					m.currentOrderCost = currentOrderCost;
					m.currentOrder = currentOrder;
					m.marketState = MarketStatus.paying;
					stateChanged();
					return;
				}
			}
		}
		}
		
		public void msgTakeMyFood(Market market, List<Food> currentOrder){
		synchronized(markets){
			print(market.toString() + " is giving me food");
			for(MyMarket m:markets) {
				if(m.market == market){
					m.currentOrder = currentOrder;
					m.marketState = MarketStatus.paid;
					stateChanged();
					return;
				}
			}
		}
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		public boolean pickAndExecuteAnAction() {
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.gone){
					markets.remove(m);
					return true;
				}
			}
		}
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.ordering){
					purchaseFoodFromMarket(m);
					return true;
				}
			}
		}
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.paying){
					restockFood(m);
				}
			}
		}
		synchronized(markets){
			for(MyMarket m: markets){
				if(m.marketState == MarketStatus.paid){
					tellCashierToPayMarket(m);
				}
			}
		}
			if(!orders.isEmpty()){
				synchronized(orders){
				for(Order o: orders){
					if(o.status == OrderStatus.pending){
						cookOrder(o);
						return true;
					}
				}
				}
				synchronized(orders){
				for(Order o: orders){
					if(o.status == OrderStatus.cooked){
						System.out.println("Order " + o.toString() + " is ready.");
						tellWaiter(o);
						return true;
					}
				}
				}
				return true;
			}
			return false;
		}

		//Actions
		public void addMarket(Market m){
			markets.add(new MyMarket(m));
		}
		
		public void purchaseFoodFromMarket(MyMarket m){
			List<Food> newOrder = new ArrayList<Food>();
			newOrder.add(cookInventory.get("Pizza"));
			newOrder.add(cookInventory.get("Hot Dog"));
			newOrder.add(cookInventory.get("Burger"));
			newOrder.add(cookInventory.get("Filet Mignon"));
			
			m.market.msgINeedFood(this, newOrder);
		}
		
		public void restockFood(MyMarket m){
		synchronized(m.currentOrder){
			for(Food f: m.currentOrder){
				cookInventory.remove(f.type);
				cookInventory.put(f.type, f);
			}
		}
			System.out.println("Restocking from market");
			m.marketState = MarketStatus.paid;
			stateChanged();
		}
		
		public void cookOrder(Order o){
			String choice = o.toString();
			int currentInventory = cookInventory.get(choice).inventoryAmount;
			if(currentInventory == 0){
				o.getWaiter().msgNeedRechoose(o.getCustomer());
				orders.remove(o);
				markets.get(0).marketState = MarketStatus.ordering; 
				stateChanged();
			} else{
				cookInventory.get(choice).decrementInventory();
				cookGui.DoGoToFridge();
				try {
					cookSem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cookGui.DoGoToGrill(o.getTableNumber());
				try {
					cookSem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cookGui.DoGoBackToRest();
				o.status = OrderStatus.cooking;
				System.out.println("Cooking Order " + o.toString());
				o.isCooking();
			}
		}
		
		public void tellWaiter(Order o){
			cookGui.setCurrentStatus(o.toString());
			cookGui.DoGoToGrill(o.getTableNumber());
			try {
				cookSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cookGui.DoMoveFoodToPlatingArea();
			System.out.println(this.name + ": Told Waiter " + o.getWaiter().toString()+ " that " + 
					o.toString() + " is ready");
			o.status = OrderStatus.pickup;
			o.getWaiter().msgOrderIsReady(o.getTableNumber());
			orders.remove(o);
			cookGui.DoGoBackToRest();
		}
		
		public void tellCashierToPayMarket(MyMarket m){ //initialize cashier
			cashier.msgPayTheMarket(m.market, m.currentOrderCost);
			m.marketState = MarketStatus.available;
			System.out.println("Told Cashier To Pay Market");
			stateChanged();
		}

		//utilities
		public String toString(){
			return name;
		}
		
		public void setGui(OmarCookGui g){
			this.cookGui = g;
		}
		
		public void msgArrived(){
			cookSem.release();
		}

		@Override	//TODO INTEGRATION REQUIRED
		public void msgOrderFulfillment(Market m, List<Item> order) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Place place() {
			return restaurant;
		}

		@Override  //TODO INTEGRATION REQUIRED
		public void cmdFinishAndLeave() {
			// TODO Auto-generated method stub
			active = false;
			stateChanged();
		}
}