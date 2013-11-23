package city.transportation;

import java.util.ArrayList;
import java.util.List;

import city.Place;

public class BusStopObject extends Place {
	List<CommuterRole> _waitList = new ArrayList<CommuterRole>();
	
	public BusStopObject(){
		
	}
	
	public void addPerson(CommuterRole person){
		_waitList.add(person);
	}
	
	public void removePerson(CommuterRole person){
		_waitList.remove(person);
	}
	
	public List getList(){
		return _waitList;
	}
}