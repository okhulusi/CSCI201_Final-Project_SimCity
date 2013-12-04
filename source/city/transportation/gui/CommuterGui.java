package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import gui.Gui;
import city.Place;
import city.transportation.*;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;
	
	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	enum Command { none, walk, car}
	Command _transportationMethod = Command.none;
	boolean isPresent = true;
	
	CommuterRole _commuter;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place initialPlace) {
		System.out.println("Created CommuterGui");
		// Note: placeX and placeY can safely receive values of null
		_xPos = placeX(initialPlace);
		_yPos = placeY(initialPlace);
		_commuter = commuter;
	}
	
	public double getManhattanDistanceToDestination(Place destination){
		double x = Math.abs(placeX(destination) - _xPos);
		double y = Math.abs(placeY(destination) - _yPos);
		
		return x+y;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
	public int getX(){
		return _xPos;
	}
	
	public int getY(){
		return _yPos;
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		setPresent(true);
		_transportationMethod = Command.walk;
		_xDestination = placeX(destination);
		_yDestination = placeY(destination);
	}
	
	public void driveToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		setPresent(true);
		_transportationMethod = Command.car;
		_xDestination = placeX(destination);
		_yDestination = placeY(destination);
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationMethod = Command.walk;
		_xDestination = busstop.positionX();
		_yDestination = busstop.positionY();
		setPresent(true);
	}
/*	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		_goingSomewhere = true;
		_xDestination = car.getXPosition();
		_yDestination = car.getYPosition();
		setPresent(true);
	}
	
	public void atCar(){
		setPresent(false);
		_commuter.msgAtCar();
	}
	
*/	
	public void getOnBus(){
		setPresent(false);
	}
	
	public void getOffBus(BusStopObject busstop){
		_xPos = busstop.positionX();
		_yPos = busstop.positionY();
		setPresent(true);
	}
	
	//------------------------------------------Animation---------------------------------------
	@Override
	public void updatePosition() {
		if (_xPos < _xDestination)
			_xPos++;
		else if (_xPos > _xDestination)
			_xPos--;

		if (_yPos < _yDestination)
			_yPos++;
		else if (_yPos > _yDestination)
			_yPos--;
		
		if (_transportationMethod == Command.car){
			if (_xPos < _xDestination)
				_xPos++;
			else if (_xPos > _xDestination)
				_xPos--;

			if (_yPos < _yDestination)
				_yPos++;
			else if (_yPos > _yDestination)
				_yPos--;
		}
		
		if(_xPos == _xDestination && _yPos == _yDestination &&
				(_transportationMethod == Command.car || _transportationMethod == Command.walk)){
			_transportationMethod = Command.none;
			setPresent(false);
			_commuter.msgReachedDestination();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			if(_transportationMethod == Command.car)
				g.setColor(Color.RED);
			else
				g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 5, 5);
		}
	}
	
	// ----------------------------------------- UTILITIES --------------------------------------------
	/** This function returns the x value of the place; it can receive a value of null */
	private int placeX(Place place) {
		if(place != null) {
			return place.positionX();
		}
		else {
			return NULL_POSITION_X;
		}
	}
	/** This function returns the y value of the place; it can receive a value of null */
	private int placeY(Place place) {
		if(place != null) {
			return place.positionY();
		}
		else {
			return NULL_POSITION_Y;
		}
	}
}
