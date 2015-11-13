/**
 * This class represents each car. It has all the details related to a car from
 * the time it enters the system to the time it departs.
 * 
 * @author Shalima, Nico
 *
 */
public class Car {

	// timeOfEntry - time it enters the entry queue
	public float timeOfEntry = 0;
	// time it leaves the entry queue
	public float timeStartingParking = 0;
	// time it enters the exit queue
	public double timeOfExit = 0;
	// time it departs from the system
	public float timeOfDeparture = 0;

	// the total time spent in the system
	public float totalTimeSpent = 0;
	// total time spent in queue
	public double totalTimeInQueue = 0;
	// total time parked in the system
	public double parkedDuration = 0;

	/**
	 * This function calculates the total time spent in the system and in the
	 * queue when the car departs from the system
	 */
	public void calculateTime() {
		totalTimeSpent = timeOfDeparture - timeOfEntry;
		totalTimeInQueue = (timeStartingParking - timeOfEntry)
				+ (timeOfDeparture - timeOfExit);
		System.out.println(this);
	}

	@Override
	public String toString() {
		return "Car [timeOfEntry=" + timeOfEntry + ", timeStartingParking="
				+ timeStartingParking + ", timeOfExit=" + timeOfExit + ", "
				+ "timeOfDeparture=" + timeOfDeparture + ", totalTimeSpent="
				+ totalTimeSpent + ", totalTimeInQueue=" + totalTimeInQueue
				+ ", parkedDuration=" + parkedDuration + "]";
	}

}
