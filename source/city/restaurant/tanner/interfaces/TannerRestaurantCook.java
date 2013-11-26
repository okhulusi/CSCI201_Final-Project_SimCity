package city.restaurant.tanner.interfaces;

import java.awt.Point;

import city.market.interfaces.MarketCashier;


public interface TannerRestaurantCook 
{
	public void msgHereIsANewOrder(int choice, int tableNumber, TannerRestaurantWaiter w);
	
	public void msgThisIsWhatIHave(int[] ItemsGiven, MarketCashier m);
	
	public void msgAllOutOfGoods(MarketCashier m);
	
	public void msgHereIsTheBill(double amount, MarketCashier m);

	public Point getPosition();
}
