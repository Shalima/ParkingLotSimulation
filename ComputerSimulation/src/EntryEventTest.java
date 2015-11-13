import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


public class EntryEventTest {
	ParkingSystem system = new ParkingSystem();
	EntryEvent event = new EntryEvent(1);

	@Test
	public void testRoutine() throws IOException {
		system.initialize();
		event.routine(system);
		assertTrue(system.entryQueue.size() == 1);
		assertTrue(system.listOfCars.size() == 1);
		assertTrue(system.eventList.get(0).getTime() != 1);
		assertTrue(system.eventList.get(1) != null);
	}
	
	@Test
	public void testRoutineWith1Cars() throws IOException {
		system.initialize();
		system.entryQueue.add(new Car());
		event.routine(system);
		assertTrue(system.entryQueue.size() == 2);
		assertTrue(system.listOfCars.size() == 1);
		assertTrue(system.eventList.get(0).getTime() != 1);
		assertTrue(system.eventList.get(1) == null);
	}
	
	@Test
	public void testRoutineWith2Cars() throws IOException {
		system.initialize();
		event.routine(system);
		event.routine(system);
		assertTrue(system.entryQueue.size() == 2);
		assertTrue(system.listOfCars.size() == 2);
		assertTrue(system.eventList.get(0).getTime() != 1);
		assertTrue(system.eventList.get(1) != null);
	}

}
