package nearestNeigh;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{
	
	private List<Point> points;

    @Override
    public void buildIndex(List<Point> points) {
        this.setPoints(points);
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
    	// design point/distance class
    	// for each item in List, get distance and assign to a pointAndDistance object
    	// add object to pointAndDistance array
    	// sort the array by distance
    	// return the first k elements
    	
    	List<PointAndDistance> distances = new ArrayList<PointAndDistance>();
    	List<Point> sortedPoints = new ArrayList<Point>();
    	for (Point point : points) {
    		double distance = point.distTo(searchTerm);
    		PointAndDistance pad = new PointAndDistance(point, distance);
    		distances.add(pad);
    	}
    	distances = selectionSortByDistance(distances);
    	for (int x = 0; x < k; ++x) 
    		sortedPoints.add(distances.get(x).point);
    	return sortedPoints;
    }

    @Override
    public boolean addPoint(Point point) {
    	if ( points.contains(point) ) {
    		// Return false if the point is already in the data structure
    		return false;
    	}
    	points.add(point);
		return true;       
    }

    @Override
    public boolean deletePoint(Point point) {
    	if ( points.remove(point) ) {
    		return true;
    	}
    	// Return false if the point was NOT found in the data structure
		return false;
    }

    @Override
    public boolean isPointIn(Point point) {
   	if ( points.contains(point) ) {
    		return true;
    	}
    	return false;
    }    
    
    private List<PointAndDistance> selectionSortByDistance(List<PointAndDistance> array) {
        for (int j = 0; j < array.size() - 1; ++j) { // visit each item in the list
        	int smallestIndex = j + 1;
        	for (int x = j + 2; x < array.size(); ++x) { // for each 'j', compare it to each array[index > j]
        		if (array.get(x).distance < array.get(smallestIndex).distance) {
        			smallestIndex = x;
        		}
        	}
        	if (array.get(j).distance > array.get(smallestIndex).distance) {
        		PointAndDistance temp = array.get(j);
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

class PointAndDistance {
	
	public Point point;
	public double distance;
	
	public PointAndDistance(Point point, double distance) {
		this.point = point;
		this.distance = distance;
	}
}
