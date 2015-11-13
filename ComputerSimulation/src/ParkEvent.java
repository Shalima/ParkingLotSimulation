import java.util.Random;

/**
 * This class represents the Park event and performs park routine
 * 
 * @author Shalima, Nico
 *
 */

public class ParkEvent implements Event {
	public int PARK_RANGE_MAX = 10;
	public int PARK_RANGE_MIN = 2;
	public float time = 0;

	public ParkEvent(float time) {
		this.time = time;
	}

	/**
	 * This function does the park routine
	 */
	@Override
	public void routine(ParkingSystem system) {
		// if there are available parksing spots
		if (system.totalParkingSpots > 0) {
			// get the car from entry queue and set the values
			Car car = system.entryQueue.poll();
			car.timeStartingParking = time;
			car.parkedDuration = getParkDuration();
			car.timeOfExit = time + car.parkedDuration + getTimeFromToGate();
			// perform the park routine
			system.parkRoutine(car);
		}
	}

//	/**
//	 * Library routine that retrieves the park interval
//	 * 
//	 * @return
//	 */
//	public float getParkDuration() {
//		return (24*3*(1-getParkInterval()));
//
//	}
	
	/**
	 * Library routine that retrieves the park interval
	 * 
	 * @return
	 */
	public float getParkDuration() {
		return (72*(1-(getNormalizedParkInterval()/PARK_RANGE_MAX)));

	}

	/**
	 * Generate the random number when car is parked
	 *
	 * @return
	 */
	public float getNormalizedParkInterval() {
		return (new Random().nextInt(PARK_RANGE_MAX - PARK_RANGE_MIN) + PARK_RANGE_MIN);
	}

	/**
	 * The random interval that the car takes from entry gate to parking spot
	 * and parking spot to exit gate
	 * 
	 * @return
	 */
	public float getTimeFromToGate() {
		return (float) (1 + Math.random() * (5));
	}

	@Override
	public String toString() {
		return "ParkEvent [time=" + time + "]";
	}

	@Override
	public float getTime() {

		return time;
	}

}
