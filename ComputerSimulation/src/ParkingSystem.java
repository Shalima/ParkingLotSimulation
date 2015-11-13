import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;

import javax.swing.JOptionPane;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;

/**
 * This class represents the parking system simulator. It has an entry and an
 * exit. It has a list of cars to which all the cars that enter the system are
 * added.
 * 
 * @author Shalima, Nico
 *
 */
public class ParkingSystem {
	public float simulationClock = 0;
	public HashMap<Integer, Event> eventList = null;

	public int totalParkingSpots;
	// the exit and entry gate allows 1 car per 10 seconds
	public int EXIT_ENTRY_RATE;

	public int STOP_SIMULATION;
	public int variance;

	// other variables
	public LinkedList<Car> listOfCars = new LinkedList<>();
	public int numberOfCarsExited = 0;
	public Queue<Car> entryQueue = new LinkedList<>();
	public Queue<Car> exitQueue = new LinkedList<>();
	public PriorityQueue<Car> carsParked = new PriorityQueue<>(
			new TimeComparator());
	float averageTimeSpentInSys = 0;
	double averageTimeParked = 0;
	double averageTimeInQueue = 0;

	/**
	 * This function is the main program. It initializes, calls the timing
	 * routine and the event routines till the end of simulation
	 * @throws IOException 
	 */
	public void mainProgram() throws IOException {
		// Initialization Routine
		initialize();

		// Till the end of simulation
		while (!isEndOfSimulation()) {
			Event nextEvent = timingRoutine();
			// go to event routine
			// check the next event type
			// go to appropriate event
			nextEvent.routine(this);
		}
		// generate reports at end of simulation
		generateReports();

		//Display a message box of the statistics at the end of the simulation
		JOptionPane.showMessageDialog(null, "Average time parked: " + averageTimeParked 
				+ "\nAverage time in system: " + averageTimeSpentInSys 
				+ "\nNumber of Cars that entered the system: "
				+ listOfCars.size(),"Simulation Statistics",JOptionPane.PLAIN_MESSAGE);

	}

	/**
	 * Determine the type of the event (retrieve key to identify whether arrival
	 * or departure event. Advance the simulation clock. Return the event type.
	 * Type 0 - Entry Type 1 - Park Type 2 - Exit Type 3 - Departure
	 */
	public Event timingRoutine() {
		Event nextEvent = null;
		int nextEventType = -1;
		float time = 100000000;
		float currTime = 0;
		// if there is only one event in the list, return that event
		if (eventList.size() == 1)
			nextEventType = 0;
		else {
			// else get the next event type to be performed
			for (int eventType : eventList.keySet()) {
				currTime = eventList.get(eventType).getTime();
				if (time > currTime) {
					time = currTime;
					nextEventType = eventType;
				}
			}
		}

		// Get the event
		nextEvent = eventList.get(nextEventType);

		// Advance the simulation clock to the event time.
		if (nextEventType != -1)
			simulationClock = nextEvent.getTime();

		// return the event.
		return nextEvent;

	}

	/**
	 * Initialize the simulation clock and event list Initialize simulation
	 * clock to zero. Initialize the event list (there will be 2 events at a
	 * time type 1(key in map) for arrivalEvent and type 2 for departureEvent
	 * Call parkingSystem's initialize method to initialize the variables there.
	 * @throws IOException 
	 */
	public void initialize() throws IOException {
		// use random nextGaussian to generate eventList
		// initialize the system clock and event list here
		// then go to system.initialize
		eventList = new HashMap<>();
		EntryEvent entryEvent = new EntryEvent(getEntryTime());
		eventList.put(0, entryEvent);		
		getProperties();		
		simulationClock = 0;
	}

	/**
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	private void getProperties() throws IOException, NumberFormatException {
		Properties properties = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
		properties.load(input);		
		totalParkingSpots = Integer.valueOf(properties.getProperty("ParkingLots"));
		EXIT_ENTRY_RATE = Integer.valueOf(properties.getProperty("EntryExitRate"));
		STOP_SIMULATION = Integer.valueOf(properties.getProperty("CarsExited"));
		variance = Integer.valueOf(properties.getProperty("Variance"));
	}

	// Get normal distribution value
	public float getEntryTime() {
		Random random = new Random();
		return Math.abs((float) random.nextGaussian() * variance);
	}

	// check if we have to end the simulation.
	private boolean isEndOfSimulation() {
		//return(listOfCars.size() == 50);
		return (numberOfCarsExited == STOP_SIMULATION);
	}

	// Used for generating reports
	private void generateReports() {
		calculateAverageTime();
		System.out.println(this);

	}

	// for generating report
	private void calculateAverageTime() {
		for (Car car : listOfCars) {
			averageTimeSpentInSys = (averageTimeSpentInSys + car.totalTimeSpent);
			averageTimeParked = (int) (averageTimeParked + car.parkedDuration);
			averageTimeInQueue = averageTimeInQueue + car.totalTimeInQueue;
		}		
		averageTimeSpentInSys = averageTimeSpentInSys/listOfCars.size();
		averageTimeParked = averageTimeParked/listOfCars.size();
		averageTimeInQueue = averageTimeInQueue/listOfCars.size();
	}

	// for generating report
	public void arrivalRoutine(Car car) {
		listOfCars.add(car);
		entryQueue.add(car);
		if (entryQueue.size() == 1)
			eventList.put(1, new ParkEvent(simulationClock + EXIT_ENTRY_RATE));
		eventList.put(0, new EntryEvent(simulationClock + getEntryTime()));

	}

	public void parkRoutine(Car car) {

		// reuse the number of available parking spots
		totalParkingSpots--;
		// add a car to cars parked
		carsParked.add(car);
		// of no car at exit queue, generate next exit event for this car
		if (eventList.get(2) == null)
			eventList.put(2, new ExitEvent(car.timeOfExit));
		// if entryqueue is not empty, generate the next park event.
		if (!entryQueue.isEmpty())
			eventList.put(1, new ParkEvent(simulationClock + EXIT_ENTRY_RATE));
		else
			// if no more cars in entry queue to be parked, remove the park
			// event
			eventList.remove(1);
	}

	public void exitRoutine(Car car) {
		// add car to the exit queue
		exitQueue.add(car);
		// increase the number of available parking spots
		totalParkingSpots++;

		// if only 1 car in exitqueue which is this car, egenerate next
		// departure event with this car
		if (exitQueue.size() == 1)
			eventList.put(3, new DepartureEvent(simulationClock
					+ EXIT_ENTRY_RATE));

		// if no cars parked
		if (carsParked.isEmpty())
			// remove the exit event
			eventList.remove(2);
		else
			// else generate the future exit event
			eventList.put(2, new ExitEvent(carsParked.element().timeOfExit));
	}

	public void departureRoutine(Car car) {
		// if no cars in exitqueue , remove the event
		numberOfCarsExited++;
		if (exitQueue.isEmpty())
			eventList.remove(3);
		else
			// else generate the next departure event
			eventList.put(3, new DepartureEvent(simulationClock
					+ EXIT_ENTRY_RATE));
	}

	@Override
	public String toString() {
		return "ParkingSystem [simulationClock=" + simulationClock
				+ ", totalParkingSpots=" + totalParkingSpots
				+ ",  \n Total number of cars that entered the" + "system ="
				+ listOfCars.size() + ",\n entryQueue=" + entryQueue.size()
				+ ", exitQueue=" + exitQueue.size() + ", carsParked="
				+ carsParked.size() + ", averageTimeSpentInSys="
				+ averageTimeSpentInSys + ", averageTimeParked="
				+ averageTimeParked + ", averageTimeInQueue="
				+ averageTimeInQueue + "]";
	}
}
