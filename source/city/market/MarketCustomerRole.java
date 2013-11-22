package city.market;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.market.gui.*;
import city.market.interfaces.MarketCustomer;
import agent.Role;

public class MarketCustomerRole extends Role implements MarketCustomer{
	public MarketCustomerGui gui;
	
	Market market;
	List<Item> order;
	List<Item> orderFulfillment;
	Map<String, Double> price_list;
	
	double money, payment, debt;
	enum CustomerState {wantToBuy, needPay, pickUpItems, payNextTime, none}
	CustomerState state = CustomerState.none;

	private Semaphore atDestination = new Semaphore(0,true);
	
	public MarketCustomerRole(PersonAgent person, Market m){
		super(person);
		this.market = m;
	}
	
	public void msgAnimationFinished() {
		//from animation
		atDestination.release();
		stateChanged();
	}
	
	//command from person
	public void cmdBuyFood(int meals){
		order.add(new Item("Meal", 3));
		state = CustomerState.wantToBuy;
		stateChanged();
	}
	
	public void cmdFinishAndLeave() {
	}
	
	//message
	public void msgHereIsBill(double payment, Map<String, Double> price_list, List<Item> orderFulfillment){
		this.payment = payment;
		this.price_list = price_list;
		this.orderFulfillment = orderFulfillment;
		state = CustomerState.needPay;
		stateChanged();
	}
	
	public void msgHereIsGoodAndChange(List<Item> orderFulfillment, double change){
		this.orderFulfillment = orderFulfillment;
		money += change;
		state = CustomerState.pickUpItems;
		stateChanged();
	}
	
	public void msgHereIsGoodAndDebt(List<Item> orderFulfillment, double debt){
		this.orderFulfillment = orderFulfillment;
		this.debt = debt;
		state = CustomerState.payNextTime;
		stateChanged();
	}

	public boolean pickAndExecuteAnAction(){

		if(state == CustomerState.wantToBuy){
			buyFromMarket();
			state = CustomerState.none;
			return true;
		}
		if (state == CustomerState.needPay){
			payMarket();
			state = CustomerState.none;
			return true;
		}
		if (state == CustomerState.pickUpItems){
			pickUpItems();
			state = CustomerState.none;
			return true;
		}
		if (state == CustomerState.payNextTime && money>0){
			payBackMarket();
			state = CustomerState.none;
			return true;
		}
		return false;
	}
	
	public void payMarket(){
		/*double bill = 0;
		for (Item item : orderFulfillment)
			bill += item.amount * price_list.get(item.name);
		if (Math.abs(bill - payment) > 0.02)
			System.out.println("Incorrect bill calculation by market");
		*/
		DoGoToCashier();
		market.MarketCashier.msgPay(this, money);
		DoGoToWaitingArea();
		money = 0;
	}
	
	public void buyFromMarket(){
		DoGoToWaitingArea();
		market.MarketCashier.msgPlaceOrder(this, order);
	}
	
	public void payBackMarket(){
		DoGoToCashier();
		market.MarketCashier.msgPay(this, money);
		DoGoToWaitingArea();
		money = 0;
	}
	
	public void pickUpItems(){
		DoGoToCashier();
		DoLeaveMarket();
		//Active = false;
	}
	
	public void DoGoToCashier(){
		//gui.GoToCashier();
	}

	public void DoGoToWaitingArea(){
		//gui.GoToWaitingArea();
	}
	
	public void DoLeaveMarket(){
		//gui.LeaveMarket();
	}
}
