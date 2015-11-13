/**
 * This class represents the entry event. This class performs the action when a
 * car enters the system. It adds a car to the entry queue, generates the next
 * event list and add the event. It also updates the car's time of entry
 * 
 * @author Shalima, Nico
 *
 */
public class EntryEvent implements Event {
	// time of entry
	public float time = 0;

	public EntryEvent(float time) {
		this.time = time;
	}

	/**
	 * This function updates the car's time of entry, adds a car to the entry
	 * queue and then generates the future event list
	 */
	@Override
	public void routine(ParkingSystem system) {
		// create a new car and enter the time of entry
		Car car = new Car();
		car.timeOfEntry = time;
		// perform the arrival routine
		system.arrivalRoutine(car);
	}

	@Override
	public String toString() {
		return "EntryEvent [time=" + time + "]";
	}

	/**
	 * Returns the time
	 */
	@Override
	public float getTime() {
		return time;
	}

}
