package city.home;

import java.util.ArrayList;
import java.util.List;

import city.Directory;
import city.PersonAgent;
import city.Place;
import agent.Role;

public class LandlordRole extends Role {
	
	// ------------------------------------ DATA -------------------------------------
	public enum MyApartmentState { VACANT, REQUESTED, ASKED_TENANT, READY_TO_LEASE, OCCUPIED }
	class MyApartment {
		Apartment apartment;
		ApartmentBuilding apartmentBuiding;
		ApartmentRenterRole tenant;
		double weeklyRate = 40; // hard-coded for now
		double owedAmount = 0;
		MyApartmentState state;
		boolean justPaid;
	}
	List<MyApartment> _myApartments = new ArrayList<MyApartment>();
	
	public enum MyHouseState { VACANT, REQUESTED, ASKED_OWNER, CHECK_OWED_AMOUNT, OWNED }
	class MyHouse
	{
		House house;
		HouseOwnerRole owner;
		double price;
		double owedAmount;
		MyHouseState state;
	}
	List<MyHouse> _myHouses;
	
	// ------------------------------ CONSTRUCTOR & PROPERTIES ---------------------------
	public LandlordRole(PersonAgent person)
	{
		super(person);
		List<ApartmentBuilding> apartmentBuildings = Directory.apartmentBuildings();
	}
	@Override
	public Place place() { return null; }
	
	
	
	// ------------------------------------ COMMANDS ------------------------------------
	@Override
	public void cmdFinishAndLeave() { } // do nothing
	
	// ------------------------------------- MESSAGES ---------------------------------
	public void msgIWouldLikeToStartRenting(ApartmentRenterRole sender, ApartmentBuilding a)
	{
		MyApartment m = myApartments.findByBuilding(a);
		if(m.state is VACANT)
		{
			m.state = REQUESTED;
			m.tenant = sender;
		}
		stateChanged();
	}
	public void msgIAcceptRate(ApartmentRenter sender)
	{
		m.state = READY_TO_LEASE;
		stateChanged();
	}
	


	// ------------------------------------ SCHEDULER --------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
