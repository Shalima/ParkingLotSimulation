import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;


public class ParkingSystemTest {

	@Test
	public void testGetEntryTime() {
		ParkingSystem simulation = new ParkingSystem();
		float entryTime = simulation.getEntryTime();
		assertNotNull("The value is not null.", entryTime);
	}

	@Test
	public void testTimingRoutineOneEvent(){
		ParkingSystem simulation = new ParkingSystem();
		HashMap<Integer, Event> eventList = new HashMap<>();
		eventList.put(0, new EntryEvent(2));
		simulation.eventList = eventList;
		
		Event event = simulation.timingRoutine();
		assertTrue(event instanceof EntryEvent);
		assertTrue(simulation.simulationClock == event.getTime());
		assertTrue(simulation.simulationClock == 2);
	}
	
	@Test
	public void testTimingRoutineTwoEvents(){
		ParkingSystem simulation = new ParkingSystem();
		HashMap<Integer, Event> eventList = new HashMap<>();
		eventList.put(0, new EntryEvent(2));
		eventList.put(1, new ParkEvent(1));
		simulation.eventList = eventList;
		
		Event event = simulation.timingRoutine();
		assertTrue(event instanceof ParkEvent);
		assertTrue(simulation.simulationClock == event.getTime());
		assertTrue(simulation.simulationClock == 1);
	}
	
	@Test
	public void testTimingRoutineFourEvents(){
		ParkingSystem simulation = new ParkingSystem();
		HashMap<Integer, Event> eventList = new HashMap<>();
		eventList.put(0, new EntryEvent(2));
		eventList.put(1, new ParkEvent(3));
		eventList.put(2, new ExitEvent(4));
		eventList.put(3, new DepartureEvent(1));
		simulation.eventList = eventList;
		
		Event event = simulation.timingRoutine();
		assertTrue(event instanceof DepartureEvent);
		assertTrue(simulation.simulationClock == event.getTime());
		assertTrue(simulation.simulationClock == 1);
	}
	
	@Test
	public void testPriorityQueue(){
		ParkingSystem system = new ParkingSystem();
		Car car = new Car();
		car.timeOfExit = 3;
		system.carsParked.add(car);
		car = new Car();
		car.timeOfExit = 8;
		system.carsParked.add(car);
		car = new Car();
		car.timeOfExit = 5;
		system.carsParked.add(car);
		assertTrue(system.carsParked.size() == 3);
		assertTrue(system.carsParked.poll().timeOfExit == 3);
		assertTrue(system.carsParked.poll().timeOfExit == 5);
		assertTrue(system.carsParked.poll().timeOfExit == 8);
	}
	
}
