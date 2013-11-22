package city.restaurant.yixin;

import java.util.*;

import agent.Role;
import city.PersonAgent;
import city.restaurant.*;

public class YixinRestaurant extends Restaurant{
	public ProducerConsumerMonitor revolving_stand = new ProducerConsumerMonitor();
	int count = -1;
	boolean open;
	public YixinCashierRole Cashier;
	public YixinHostRole Host;
	public YixinCookRole Cook;
	public List<YixinWaiterRole> Waiters = new ArrayList<YixinWaiterRole>();
	
	public YixinRestaurant(){
		super();
		Cashier = new YixinCashierRole(null,this);
		Host = new YixinHostRole(null,this,"Fiona");
		Cook = new YixinCookRole(null,this);
	}
		
	public void updateMarketStatus(){
		if (Cashier == null || Host == null || Cook == null || Waiters.size()==0)
			open = false;
		else
			open = true;
	}

	@Override
	public RestaurantCustomerRole generateCustomerRole(PersonAgent person) {
		//TODO make a new customer that is initialized with a PersonAgent of person
		count++;
		if (count > 10){
			count = 1;
		}
		return (new YixinCustomerRole(person, this, person.name(), count-1));
	}

	@Override
	public Role generateWaiterRole() {
		int i = (new Random()).nextInt(2);
		if (i == 0)
			return (new YixinNormalWaiterRole(null, this, ""));
		else
			return (new YixinSharedDataWaiterRole(null, this, ""));
	}

	@Override
	public Role getHostRole() {
		// TODO Auto-generated method stub
		return Host;
	}
}
