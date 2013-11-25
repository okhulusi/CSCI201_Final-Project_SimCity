package city.home;

import city.PersonAgent;
import city.Place;

public interface Home {
	public Place place();
	public HomeOccupantRole tryAcquireHomeOccupantRole(PersonAgent person);
}
