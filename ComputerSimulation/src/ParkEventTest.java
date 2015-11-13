import static org.junit.Assert.*;

import org.junit.Test;


public class ParkEventTest {
	ParkingSystem system = new ParkingSystem();
	
	
	@Test
	public void testGetParkInterval() {
		ParkEvent event = new ParkEvent(2);
		double interval = event.getNormalizedParkInterval();
//		assertTrue(interval<=15);
//		assertTrue(interval>=5);
	}

}
