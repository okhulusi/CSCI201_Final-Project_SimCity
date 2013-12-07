package city.restaurant.eric.interfaces;

import city.restaurant.eric.Check;
import city.restaurant.eric.Menu;
import city.restaurant.eric.gui.EricCustomerGui;

public interface EricCustomer
{
	// Properties
	public String name();
	public EricCustomerGui gui();
	// Messages
	public void cmdGotHungry(); // from CustomerGui
	public void msgRestaurantIsFull(); // from Host
	public void msgGoToCashierAndPayDebt(EricCashier cashier); // from Host
	public void msgWeWontServeYou(); // from Host
	public void msgComeToFrontDesk(); // from Host
	public void msgReachedFrontDesk(); // from CustomerGui
	public void msgFollowMeToTable(EricWaiter sender, Menu menu); // from Waiter
	public void msgReachedTable(); // from CustomerGui
	public void msgWhatDoYouWant(); // from Waiter
	public void msgOutOfChoice(Menu m); // from Waiter
	public void msgHeresYourFood(String choice); // from Waiter
	public void msgFinishedEating(); // from CustomerGui
	public void msgHeresYourCheck(Check check, EricCashier cashier); // from Waiter
	public void msgReachedCashier(); // from CustomerGui
	public void msgHereIsChange(double change); // from Cashier
	public void msgFinishedLeavingRestaurant(); // from CustomerGui
}
