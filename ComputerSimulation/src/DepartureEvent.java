/**
 * This class represents the routine to be performed at the time of departure.
 * 
 * @author Shalima, Nico
 *
 */
public class DepartureEvent implements Event {

	public DepartureEvent(float time) {
		this.time = time;
	}

	public float time = 0;

	@Override
	public void routine(ParkingSystem system) {
		// get the car from the exit queue
		Car car = system.exitQueue.poll();
		// set the ca's departure time
		car.timeOfDeparture = time;
		// calculate the values
		car.calculateTime();
		// perform departure routine
		system.departureRoutine(car);
	}

	@Override
	public String toString() {
		return "DepartureEvent [time=" + time + "]";
	}

	@Override
	public float getTime() {
		return time;
	}

}
