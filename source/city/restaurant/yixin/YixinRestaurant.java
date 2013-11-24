package city.restaurant.yixin;

import gui.WorldViewBuilding;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.Role;
import city.PersonAgent;
import city.restaurant.*;
import city.restaurant.yixin.gui.YixinAnimationPanel;

public class YixinRestaurant extends Restaurant{
	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	//count stands for the number of waiting list
	int count = -1;
	boolean open;
	public YixinHostRole host;
	private int businessAccountNumber = -1;
	public List<YixinWaiterRole> Waiters = new ArrayList<YixinWaiterRole>();
	private YixinAnimationPanel _animationPanel;
	
	// Semaphores for the host, cashier, and cook
	private Semaphore _hostSemaphore = new Semaphore(1, true);
	
	
	
	// ------------- CONSTRUCTOR & PROPERTIES
	
	public YixinRestaurant(String name, gui.WorldViewBuilding worldViewBuilding, gui.BuildingInteriorAnimationPanel animationPanel){
		super(name, worldViewBuilding);

		this._animationPanel = (YixinAnimationPanel)animationPanel.getBuildingAnimation();

		// The animation object for these will be instantiated when a person enters the building and takes the role.
		cashier = new YixinCashierRole(null,this);
		host = new YixinHostRole(null,this,"Host");
		cook = new YixinCookRole(null,this);
		((YixinCookRole)cook).cashier = (YixinCashierRole)cashier;
	}

	//default constructor for unit testing DO NOT DELETE
	public YixinRestaurant(){
		super("Yixin's Restaurant");    
		cashier = new YixinCashierRole(null,this);
		host = new YixinHostRole(null,this,"Host");
		cook = new YixinCookRole(null,this);
		((YixinCookRole)cook).cashier = (YixinCashierRole)cashier;
	}

	public boolean isOpen(){
		if (cashier.active && host.active && cook.active && Waiters.size()!=0)
			return true;
		else
			return false;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		//TODO make a new customer that is initialized with a PersonAgent of person
		count++;
		if (count > 10){
			count = 1;
		}
		return (new YixinCustomerRole(person, this, person.getName(), count-1));
	}

	@Override
	public Role generateWaiterRole() {
		int i = (new Random()).nextInt(2);
		YixinWaiterRole newWaiter;
		if (i == 0)
			newWaiter = new YixinNormalWaiterRole(null, this, "");
		else
			newWaiter = new YixinSharedDataWaiterRole(null, this, "");
		return newWaiter;
	}

	public void updateAccountNumber(int newAccountNumber){
		this.businessAccountNumber = newAccountNumber;
	}

	public int getAccountNumber(){
		return this.businessAccountNumber;
	}

	@Override
	public Role getHostRole() {
		return host;
	}

	public YixinAnimationPanel getAnimationPanel() {
		return this._animationPanel;
	}

}
