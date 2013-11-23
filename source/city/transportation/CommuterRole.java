package city.transportation;

import java.util.Random;

import agent.Role;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.CommuterGui;
import city.transportation.interfaces.Commuter;

/**
 * There is one CommuterRole per person, and the CommuterRole is the one that 
 */
public class CommuterRole extends Role implements Commuter{
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public PersonAgent _person;
	public Place _destination;
	public Place _currentPlace;
	BusStopObject _busStop;
	CarObject _car = new CarObject();
	BusAgent _bus;
	int _fare;
	CommuterGui _gui;
	
	public enum TravelState{choosing, 
		choseCar, driving, 
		choseWalking, walking, 
		choseBus, goingToBusStop, atBusStop, waitingAtBusStop, busIsHere, ridingBus, busIsAtDestination, gettingOffBus,
			atDestination, done, none};
	public TravelState _tState = TravelState.none;
	
	Random _generator = new Random();
	
	//Transportation Hacks
	enum PrefTransport{none, legs, bus, car};
	PrefTransport pTransport = PrefTransport.none;
	
	//Probably won't need -> not 100% sure though
	enum CarState{noCar, hasCar, usingCar};
	CarState _cState = CarState.noCar; 
	
	
	//----------------------------------------------CONSTRUCTOR & PROPERTIES----------------------------------------
	public CommuterRole(PersonAgent person, Place place){
		super(person);
		_person = person;
		_currentPlace = place;
		_car = null;
		// TODO Auto-generated constructor stub
	}
	
	public void setGui(CommuterGui gui) { _gui = gui; }
	
	public void setCar(CarObject car){_car = car;}
	
	public Place destination() { return _destination; }
	public void setDestination(Place place) { _destination = place; }
	
	public Place currentPlace() { return _currentPlace; }

	public Place place() { return currentPlace(); }
	
	public void setBusStop(BusStopObject busStop){_busStop = busStop;}
	
	//----------------------------------------------Command---------------------------------------------
	
	//----------------------------------------------Messages------------------------------------------
	public void msgGoToDestination(Place place){ //Command to go to destination
		_tState = TravelState.choosing;
		_destination = place;
	}
	
	//Bus Transportation messages
	public void msgAtBusStop(BusStopObject busstop){ //GUI message
		_tState = TravelState.atBusStop;
		_currentPlace = busstop;
	}
	public void msgGetOnBus(int fare, BusAgent bus){
		_tState = TravelState.busIsHere;
		_bus = bus;
		_fare = fare;
	}
	public void msgGetOffBus(Place place){
		_tState = TravelState.busIsAtDestination;
		_currentPlace = place;
	}
	
	//Msg At Destination from GUI
	public void msgAtDestination(Place place){
		_tState = TravelState.atDestination;
		_currentPlace = place;
	}
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction() {
		//At Destination
		if(_destination == _currentPlace && _tState == TravelState.atDestination){
			actAtDestination();
			return true;
		}
		if(_destination != _currentPlace && _tState == TravelState.atDestination){
			actChooseTransportation();
			return true;
		}
		
		//Choosing
		if(_tState == TravelState.choosing){
			actChooseTransportation();
			return true;
		}
		
		//Walking
		if(_tState == TravelState.choseWalking){
			actWalking();
			return true;
		}
		
		//Riding Bus
		if(_tState == TravelState.choseBus){
			actGoToBusStop();
			return true;
		}
		if(_tState == TravelState.atBusStop){
			actAtBusStop();
			return true;
		}
		if(_tState == TravelState.busIsHere && _bus != null && _person._money >= _fare){
			actGetOnBus();
			return true;
		}
		if(_tState == TravelState.busIsHere && _bus != null && _person._money <= _fare){
			actChooseNewTransportation();
			return true;
		}
		if(_tState == TravelState.busIsAtDestination){
			actGetOffBus();
			return true;
		}
		
		//Driving
		if(_tState == TravelState.choseCar){
			actDriving();
			return true;
		}

		
		// TODO Auto-generated method stub
		return false;
	}

	//----------------------------------------------Actions----------------------------------------
	//Choosing
	public void actChooseTransportation(){
		int choice = 0;
		
		if(_car == null){
			choice = _generator.nextInt(2);
			if(pTransport == PrefTransport.legs){
				choice = 0;
			}
			else if(pTransport == PrefTransport.bus){
				choice = 1;
			}
		}
		else if(_car != null){
			choice = _generator.nextInt(3);
			if(pTransport == PrefTransport.car){
				choice = 2;
			}
		}
		
		if(choice == 0){
			_tState = TravelState.choseWalking;
		}
		if(choice == 1){
			_tState = TravelState.choseBus;
		}
		if(choice == 2){
			_tState = TravelState.choseCar;
		}

	}
	public void actChooseNewTransportation(){ //Choosing when previous form of transportation doesn't work (Mostly for bus)
		_tState = TravelState.none;
	}
	
	//Walking
	public void actWalking(){
		_tState = TravelState.walking;
		_gui.walkToLocation(_destination);
	}
	
	//Bus
	public void actGoToBusStop(){
		_tState = TravelState.goingToBusStop;
		_busStop = Directory.getNearestBusStop(_currentPlace); //Unit Testing will skip this for now
		_gui.goToBusStop(_busStop);
	}
	public void actAtBusStop(){
		_tState = TravelState.waitingAtBusStop;
		_busStop = Directory.getNearestBusStop(_currentPlace);
		
		_busStop.addPerson(this);
	}
	public void actGetOnBus(){
		_tState = TravelState.ridingBus;
		_person._money -= _fare;
		_gui.getOnBus();
		_bus.msgGettingOnBoard(this, _destination, _fare);
	}
	public void actGetOffBus(){
		_tState = TravelState.gettingOffBus;
		_gui.getOffBus();
		_bus.msgGotOff(this);
		_bus = null;
		actWalking(); //Calls this function here because after you get off of the bus stop you walk to the destination
	}

	//Driving
	public void actDriving(){
		_tState = TravelState.driving;
		_gui.goToCar(_car, _destination);
	}
	
	public void actAtDestination(){
		_tState = TravelState.done;
	}
	
	@Override
	public void cmdFinishAndLeave() {
		// TODO Auto-generated method stub
		//_person.atDestination(_currentPlace);
		active = false;
		
	}
	
	//----------------------------------------------Hacks----------------------------------------
	public void chooseTransportation(int choice){
		if(choice == 0){
			pTransport = PrefTransport.legs;
		}
		else if(choice == 1){
			pTransport = PrefTransport.bus;
		}
		else if(choice == 2){
			pTransport = PrefTransport.car;
		}
		else{
			pTransport = PrefTransport.none;
		}
		
		
	}

}
