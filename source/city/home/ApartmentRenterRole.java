package city.home;

<<<<<<< HEAD
import agent.Role;
import city.PersonAgent;

=======
import java.util.List;

import agent.Role;
import city.Directory;
import city.PersonAgent;

>>>>>>> develop
public class ApartmentRenterRole extends HomeBuyingRole
{
	// ---------------------------------- DATA -------------------------------------
	ApartmentBuilding _apartmentBuilding;
	Apartment  _apartment;
	enum Command { NONE, START_A_RENTAL, CHECK_RATE, MOVE_IN, PAY_RENT }
	Command _command;
	
	
	
	// -------------------------- CONSTRUCTOR & PROPERTIES ----------------------------
	public ApartmentRenterRole(PersonAgent person) { super(person); }
	public boolean haveHome() { if(_apartment != null) { return true; } return false; }
	
	
	
	// ----------------------------- COMMANDS -----------------------------------
	public void cmdStartARental() // from Person
	{
		_command = Command.START_A_RENTAL;
		stateChanged();
	}
	public void cmdFinishAndLeave() { } // nothing to do
	
	// ------------------------------- MESSAGES ---------------------------------
	public void msg() {}
	
	
	
	// ------------------------------- SCHEDULER ---------------------------------
	public boolean pickAndExecuteAnAction() {
		if(_command == Command.START_A_RENTAL) {
			actStartARental();
			return true;
		}
		return false;
	}
	
	
	
	// ---------------------------------- ACTIONS ----------------------------------
	private void actStartARental()
	{
		List<ApartmentBuilding> apartmentbuildings = Directory.apartmentBuildings();
		
		for(ApartmentBuilding ab : apartmentBuildings)
		{
			
		}
		
		landlord = myApartmentBuilding.landlord;
		landlord.msgIWouldLikeToStartRenting(this, myApartmentBuilding);
		command = NONE;
	}

}
