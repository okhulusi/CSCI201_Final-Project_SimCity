package city.home;

import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Place;

public class Apartment implements Home
{
	// --------------------------------- DATA ------------------------------------
	private ApartmentBuilding _apartmentBuilding;
	private int _number = -1;
	private Semaphore _occupiedSemaphore = new Semaphore(1, true);
	
	
	
	// ---------------------------- CONSTRUCTOR & PROPERTIES ---------------------------
	public Apartment(ApartmentBuilding apartmentBuilding, int number) {
		_apartmentBuilding = apartmentBuilding;
		_number = number;
	}
	public void setNumber(int number) { _number = number; }
	public int number() { return _number; }
	@Override
	public Place place() { return _apartmentBuilding; }
	public ApartmentBuilding apartmentBuilding() { return _apartmentBuilding; }
	public LandlordRole landlord() { return _apartmentBuilding.landlord(); }
	
	
	
	// --------------------------------- METHODS ---------------------------------------
	public ApartmentOccupantRole tryAcquireHomeOccupantRole(PersonAgent person)
	{
		if(_occupiedSemaphore.tryAcquire())
		{
			// possibly add a function to set the occupant of this House
			return new ApartmentOccupantRole(person, this);
		}
		else return null;
	}
}
