package city.restaurant.eric;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import city.Place;
import city.interfaces.Person;
import city.market.Market;
import city.restaurant.RestaurantCashierRole;
import city.restaurant.eric.gui.EricCashierGui;
import city.restaurant.eric.interfaces.*;

public class EricCashierRole extends RestaurantCashierRole implements EricCashier
{
	// ----------------------------------------- DATA ----------------------------------------------
	
	// Constants:
	private double MONEY_HIGH_LEVEL = 300;
	private double MONEY_MID_LEVEL = 200;
	private double MONEY_LOW_LEVEL = 100;
	
	// Correspondence:
	private EricHost _host;
	private EricRestaurant _restaurant;
	private EricCashierGui _gui;
	
	// Agent data:
	private double _money = MONEY_MID_LEVEL;
	// Note: public for TEST
	public class Bill
	{
		public Check check;
		public BillState state;
		public EricWaiter waiter = null;
		public EricCustomer customer = null;
		public double paidAmount = 0; // the amount paid, which needs to be processed.
		public double owedAmount = 0;
		public String toString() { return "owedAmount: " + this.owedAmount + "; paidAmount: " + this.paidAmount + "; waiter: " + this.waiter.name() + "; state: " + this.state + "; customer: " + this.customer.name() + "."; }
	}
	// Note: public for TEST
	public enum BillState { REQUESTED, WAITING_FOR_PAYMENT, PAID_NEEDS_CHANGE, OWED, NOTIFY_HOST_OWED, PAY_DEBT_NEEDS_CHANGE }
	private List<Bill> _bills = Collections.synchronizedList(new ArrayList<Bill>());
	private enum MarketBillState { RECEIVED, INVOICE_RECEIVED }
	// Note: public for TEST
	public class MarketBill
	{
		public double amountOwed;
		public Map<String, Double> priceList;
		public Map<String, Integer> foodsReceived;
		public Market market;
		public MarketBillState state;
	}
	private List<MarketBill> _marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	
	// ------------------------------------------ CONSTRUCTOR ----------------------------------------------
	public EricCashierRole(Person person, EricRestaurant restaurant)
	{
		super(person);
		_restaurant = restaurant;
	}

	// ----------------------------------------- PROPERTIES ----------------------------------------------
	public String name() { return _person.name(); }
	public void setHost(EricHost host) { _host = host; }
	public Place place() { return _restaurant; }
	public void setGui(EricCashierGui gui) { _gui = gui; }
	
	// ------------------------------------------- TEST PROPERTIES ------------------------------------------------
	public List<Bill> bills() { return _bills; }
	public double money() { return _money; }
	public void setMoney(double money) { _money = money; }
	public List<MarketBill> marketBills() { return _marketBills; }
	
	
	
	// ----------------------------------------- MESSAGES ----------------------------------------------
	
	public void cmdFinishAndLeave() {
		//TODO design & do FinishAndLeave situation 
	}
	
	public void msgGiveMeCheck(EricWaiter sender, String choice, int table)
	{
		Bill b = new Bill();
		b.check = new Check(choice, table);
		b.state = BillState.REQUESTED;
		b.waiter = sender;
		b.owedAmount = b.check.price();
		
		_bills.add(b);
		
		stateChanged();
	}
	
	public void msgHereIsMoney(EricCustomer sender, double money, Check c)
	{
		for(Bill b : _bills)
		{
			if(b.check == c) // may want to change this to .equals(c) later, if passing copies around rather than references.
			{
				print(AlertTag.ERIC_RESTAURANT, "Received $" + money + " from " + sender.name());
				b.customer = sender;
				b.paidAmount = money;
				b.state = BillState.PAID_NEEDS_CHANGE;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgDoesCustomerOwe(EricCustomer customer) // from Host
	{
		
		synchronized(_bills) {
			for(Bill b : _bills)
			{
				if(b.customer == customer)
				{
					b.state = BillState.NOTIFY_HOST_OWED;
					stateChanged();
					return; // this is important because it skips 
				}
			}
		}
		
		// If we haven't seen this customer before, make a new check with a balance of zero
		Bill b = new Bill();
		b.customer = customer;
		b.state = BillState.NOTIFY_HOST_OWED;
		b.owedAmount = 0;
		_bills.add(b);
		stateChanged();
	}
	
	public void msgHereIsOwedMoney(EricCustomer sender, double money)
	{
		for(Bill b : _bills)
		{
			if(b.customer == sender)
			{
				print(AlertTag.ERIC_RESTAURANT,"Received " + money + " from " + sender.name());
				b.paidAmount = money;
				b.state = BillState.PAY_DEBT_NEEDS_CHANGE;
				stateChanged();
				return;
			}
		}
	}

	@Override
	public void msgHereIsTheBill(Market m, double bill, Map<String, Double> price_list) {
		MarketBill b = new MarketBill();
		b.market = m;
		b.amountOwed = bill;
		b.priceList = price_list;
		b.state = MarketBillState.RECEIVED;
		_marketBills.add(b);
		stateChanged();
	}

	@Override
	public void msgIReceivedTheseFoods(Market market, Map<String, Integer> foodsReceived) { // from cook
		for(MarketBill b : _marketBills)
		{
			if(b.market == market)
			{
				b.foodsReceived = foodsReceived;
				break;
			}
		}
	}

	@Override
	public void msgHereIsTheChange(Market m, double change) {
		if(change > 0)
		{
			_money += change;
			stateChanged();
		}
	}

	@Override
	public void msgTransactionComplete(double amount, Double balance, Double debt, int newAccountNumber) {
		//TODO Auto-generated method stub
		
	}
	
	
	
	// ----------------------------------------- SCHEDULER ----------------------------------------------
	// Note: public for TEST
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.NOTIFY_HOST_OWED) {
					actNotifyHostCustomerOwes(b);
					return true;
				}
			}
		}
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.REQUESTED) {
					actGiveWaiterCheck(b);
					return true;
				}
			}
		}
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.PAID_NEEDS_CHANGE) {
					actGiveChange(b);
					return true;
				}
			}
		}
		synchronized(_bills) {
			for(Bill b : _bills) {
				if(b.state == BillState.PAY_DEBT_NEEDS_CHANGE) {
					actDebtGiveChange(b);
					return true;
				}
			}
		}
		// TODO add bank interaction
		//	if(_money < MONEY_LOW_LEVEL)
		//	{
		//		
		//	}
		//	else if(_money > MONEY_HIGH_LEVEL)
		//	{
		//		
		//	}
		synchronized(_marketBills) {
			if(_money > 0) {
				for(MarketBill b : _marketBills) {
					if(b.foodsReceived != null) {
						if(actMakeMarketPayment(b)) return true;
						else break;
					}
				}
			}
		}
		return false;
	}
	
	

	// ----------------------------------------- ACTIONS ----------------------------------------------
	
	private void actGiveWaiterCheck(Bill b)
	{
		logThis("actGiveWaiterCheck");
		
		print(AlertTag.ERIC_RESTAURANT,"Giving check to " + b.waiter.name());
		b.state = BillState.WAITING_FOR_PAYMENT;
		b.waiter.msgHereIsCheck(b.check);
	}
	
	private void actGiveChange(Bill b)
	{
		logThis("actGiveChange");
		
		double change = b.paidAmount - b.check.price();
		b.owedAmount = b.check.price() - b.paidAmount;
		if(b.owedAmount < 0) b.owedAmount = 0;
		if(change < 0) change = 0;
		_money += b.paidAmount - change;
		b.paidAmount = 0;
		
		print(AlertTag.ERIC_RESTAURANT,"Giving $" + change + " in change to customer " + b.customer.name());
		b.customer.msgHereIsChange(change);
		
		if(b.owedAmount > 0)
		{
			print(AlertTag.ERIC_RESTAURANT, b.customer.name() + " still owes " + b.owedAmount);
			b.state = BillState.OWED;
			b.waiter = null; // because the customer is leaving and will no longer be assigned to a waiter
			b.check = null; // because the customer is leaving and all we care about now is his debt
		}
		else
		{
			_bills.remove(b);
		}
	}
	
	private void actNotifyHostCustomerOwes(Bill b)
	{
		logThis("actNotifyHostCustomerOwes");
		
		if(b.owedAmount == 0)
		{
			print(AlertTag.ERIC_RESTAURANT, "Notifying " + _host.name() + " that " + b.customer.name() + " does not owe anything.");
			_bills.remove(b);
		}
		else
		{
			print(AlertTag.ERIC_RESTAURANT,"Notifying " + _host.name() + " that " + b.customer.name() + " owes $" + b.owedAmount);
			b.state = BillState.OWED;
		}
		_host.msgCustomerOwes(b.customer, b.owedAmount);
	}
	
	private void actDebtGiveChange(Bill b)
	{
		logThis("actDebtGiveChange");
		
		double change = b.paidAmount - b.owedAmount;
		b.owedAmount = b.owedAmount - b.paidAmount;
		if(b.owedAmount < 0) b.owedAmount = 0;
		if(change < 0) change = 0;
		_money += b.paidAmount - change;
		b.paidAmount = 0;
		
		print(AlertTag.ERIC_RESTAURANT,"Giving $" + change + " in change to customer " + b.customer.name());
		b.customer.msgHereIsChange(change);
		
		if(b.owedAmount > 0)
		{
			b.state = BillState.OWED;
			// don't need to set b.waiter and b.check to null because we already did that
		}
		else
		{
			_bills.remove(b);
		}
	}
	
	private boolean actMakeMarketPayment(MarketBill b)
	{
		logThis("actMakeMarketPayment");
		
		double amountToPay = 0;
		for(Map.Entry<String, Integer> e : b.foodsReceived.entrySet())
		{
			//                  price for this food         number of food items
			amountToPay += b.priceList.get(e.getKey()) * e.getValue();
		}
		
		if(amountToPay > b.amountOwed) amountToPay = b.amountOwed;
		
		if(_money < amountToPay) return false;
		
		print(AlertTag.ERIC_RESTAURANT,"Paying $" + amountToPay + " to " + b.market.name() + ".");

		_marketBills.remove(b);
		b.market.getCashier().msgHereIsPayment(_restaurant, amountToPay);
		
		return true;
	}
}
