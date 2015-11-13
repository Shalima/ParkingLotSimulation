

public class ExitEvent implements Event{
	public double time = 0;
	public ExitEvent(double timeOfExit) {
		super();
		this.time = timeOfExit;
	}


	/**
	 * Write code to handle the updates.
	 */
	@Override
	public void routine(ParkingSystem system) {
		Car car = system.carsParked.poll();
		//car.timeOfExit = time;
		system.exitRoutine(car);		
	}


	@Override
	public String toString() {
		return "ExitEvent [time=" + time + "]";
	}


	@Override
	public float getTime() {
		return (float) time;
	}
	

}
