package role.transportation;

import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;

public class Bus {
	List<MyPerson> passengers;
	BusState state;
	String currentDestination;
	
	static int fare;
	int register;

	static int capacity;
	int numPeople = 0;
	int expectedPeople = 0;

	enum BusState{moving, atDestination, droppingoff, pickingup, notmoving};
	BusState bState = BusState.notmoving;
	
	enum PassengerState{onBus, offBus};

	class MyPerson{
	    PersonAgent person;
	    String destination;
	    PassengerState pState = PassengerState.onBus;
	    
	    MyPerson(PersonAgent person, String destination){
	    	this.person = person;
	    	this.destination = destination;
	    }
	}
	
	public void msgAtDestination(String destination){
	    currentDestination = destination;
	    state = BusState.atDestination;
	}

	public void msgGotOff(PersonAgent passenger){
	    passengers.remove(passenger); //Fix this
	    numPeople--;
	}

	public void msgGettingOnBoard(PersonAgent person, String destination, int payment){
	    passengers.add(new MyPerson(person, destination));
	    numPeople++; //Fix this
	}
	
	public boolean pickAndExecuteAnAction(){
		if(bState == BusState.atDestination){
			DropOff();
			return true;
		}
		
		if(bState == BusState.droppingoff && expectedPeople == numPeople){
			PickUp();
			return true;
		}
		
		if(bState == BusState.pickingup && expectedPeople == numPeople){
			Leave();
			return true;
		}
		
		return false;
	}
	
	public void DropOff(){
	    int i = 0;
	    bState = BusState.droppingoff;
	    for(MyPerson person: passengers){
	        if(person.destination == currentDestination){
	            person.msgAtBusStop();
	            expectedPeople--;
	        }
	    }
	}

	public void PickUp(){
		bState = BusState.pickingup;
	    while(numPeople <= capacity){
	        for(MyPerson person: busstoplist){
	            person.msgPickUpAtBusStop(fare);
	            expectedPeople++;
	        }
	    }
	}

	public void Leave(){
	    bState = BusState.moving;
	    Gui.movetonextdestination();
	}
	
}
