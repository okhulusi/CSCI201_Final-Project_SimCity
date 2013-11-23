package city.transportation.test;

import city.PersonAgent;
import city.transportation.BusStopObject;
import city.transportation.CommuterRole;
import city.transportation.CommuterRole.TravelState;
import city.transportation.mock.MockBus;
import junit.framework.TestCase;

public class CommuterTest extends TestCase{
	
	PersonAgent person;
	BusStopObject busStop;
	CommuterRole commuter;
	MockBus mockBus;
	TravelState tState;
	
	public void setUp() throws Exception{
		super.setUp();
		
		/*person = new PersonAgent("Person 1"); //WHY PERSONAGENT NO WORK?
		person.changeMoney(100);*/
		commuter = new CommuterRole(null, null);
		
		busStop = new BusStopObject();
		mockBus = new MockBus("MockBus");
		mockBus.setFare(1);
	}
	
	public void testZeroNormalCommuterScenario(){
		//Nothing should happen
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		assertFalse("Scheduler returns false", commuter.pickAndExecuteAnAction());
		
		//Send Message
		commuter.msgGoToDestination(null);
		
		//Check if it works
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		
	}
	
	public void testOneNormalBusCommuterScenario(){
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.none);
		
		//Set Up Message to go to Bus
		commuter.msgGoToDestination(null);
		commuter.chooseTransportation(1);
		
		//Check if it received correctly (Choosing to go to Bus Stop)
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choosing);
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.choseBus);
		
		//Check if it received correctly (Going to Bus Stop) 
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction());
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.goingToBusStop);
		
		//Nothing happens so should be false
		assertFalse("Scheduler returns false", commuter.pickAndExecuteAnAction());
		
		commuter.msgAtBusStop(busStop); //Sent by gui
		
		assertTrue("Scheduler returns true", commuter.pickAndExecuteAnAction()); 
		assertEquals("Travel state should be none, it isn't", commuter._tState, TravelState.goingToBusStop);
		
	}
}
