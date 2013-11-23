package city.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Place;
import city.market.Market;
import city.market.MarketCashierRole;
import city.restaurant.Restaurant;
import city.restaurant.RestaurantCookRole;
import city.restaurant.yixin.YixinCookRole;
import city.transportation.gui.BusAgentGui;
import city.transportation.gui.TruckAgentGui;
import city.market.Item;

public class TruckAgent {
	List<Package> packages;
	Semaphore isMoving;
	Market _market;
	TruckAgentGui gui;
	Boolean out = false;
	
	enum truckState{parkingLot, docking, drivingtoRestaurant, atRestaurant, drivingtoMarket};
	truckState trState = truckState.parkingLot;
	
	enum packageState{atMarket, inTruck, delivering, unloaded, done};

	class Package{
	    List<Item> _items;
	    Restaurant _restaurant;
	    int orderId;
	    double bill;
	    packageState pState = packageState.atMarket;
	    
	    Package(List<Item> items, Restaurant restaurant){
	    	_items=items;
	    	_restaurant = restaurant;
	    	
	    }
	}
	
	//Constructor
	public TruckAgent(Market market){
		_market = market;
	}
	
	//----------------------------------------------Messages------------------------------------------
	public void msgDeliverToCook(List<Item> items, Restaurant restaurant){
	    packages.add(new Package(items, restaurant));
	}
	
	public void msgAtDestination(){
	    isMoving.release();
	}

	public void msgGoodsUnloaded(int order_id){
	    for(Package aPackage : packages){
	        if(aPackage.orderId == order_id){
	        aPackage.pState = packageState.unloaded;}
	    }
	}

	public void msgAtMarket(){
	    isMoving.release();
	}
	
	//----------------------------------------------Scheduler------------------------------------------
	public boolean pickAndExecuteAnAction(){
		for(Package temp: packages){
			if(temp.pState == packageState.inTruck && trState == truckState.docking){
				DeliverToDestination(temp);
				return true;
			}
			if(temp.pState == packageState.atMarket && trState == truckState.parkingLot){
				PickFromDock(temp);
				return true;
			}
			if(temp.pState == packageState.atMarket && trState == truckState.atRestaurant){
				PickFromDock(temp);
				return true;
			}
		}
		
		if(packages.isEmpty() && out == true){
			GoBackToMarket();
			return true;
		}
        
		return false;
	}
	
	//----------------------------------------------Actions------------------------------------------
	public void PickFromDock(Package aPackage){
		trState = truckState.docking;
		out = true;
		gui.goToDock();
		isMoving.acquire();
		_market.();
		aPackage.pState = packageState.inTruck;
		
	}
	
	public void DeliverToDestination(Package aPackage){
		trState = truckState.drivingtoRestaurant;
		gui.goToDestination(aPackage._restaurant);
		isMoving.acquire();
		aPackage._restaurant.Cook.msgOrderFulfillment(_market, aPackage._items); //Make sure GUI shows that it's dropped off !important!
		trState = truckState.atRestaurant;
		packages.remove(aPackage);
	}

	public void GoBackToMarket(){
	    out = false;
	    gui.goToMarket(_market);
	    isMoving.acquire();
	    _market.MarketCashier.msgBackFromRun(this);
	}
}
