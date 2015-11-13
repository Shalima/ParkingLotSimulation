import java.util.Comparator;
/**
 * Use this comparator for priority queue in cars parked
 * @author Shalima
 *
 */

public class TimeComparator implements Comparator<Car> {

	@Override
	public int compare(Car car1, Car car2) {		
		return ((car2.timeOfExit < car1.timeOfExit) ? 1 : -1);
	}
}
