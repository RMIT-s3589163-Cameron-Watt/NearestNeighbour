package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{
	
	private List<Point> points;

    @Override
    public void buildIndex(List<Point> points) {
        this.setPoints(selectionSortByLatitude(points));
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        return new ArrayList<Point>();
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
        return false;
    }

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
        return false;
    }

    @Override
    public boolean isPointIn(Point point) {
        // To be implemented.
        return false;
    }
    
    private List<Point> selectionSortByLatitude(List<Point> array) {
        for (int j = 0; j < array.size() - 1; ++j) { // visit each item in the list
        	int smallestIndex = j + 1;
        	for (int x = j + 2; x < array.size(); ++x) { // for each 'j', compare it to each array[index > j]
        		if (array.get(x).lat < array.get(smallestIndex).lat) {
        			smallestIndex = x;
        		}
        	}
        	if (array.get(j).lat > array.get(smallestIndex).lat) {
        		Point temp = array.get(j);
        		array.set(j, array.get(smallestIndex));
        		array.set(smallestIndex, temp);
        	}
        }
        return array;
    }

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

}
