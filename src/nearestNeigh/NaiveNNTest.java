package nearestNeigh;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NaiveNNTest {
	
	List<Point> points = new ArrayList<Point>();
	NaiveNN agent = new NaiveNN();
	
	@Before
	public void setupPoints() {
		System.out.println(" ");
		System.out.println("Starting to add points");
		points.add((new Point("id0", Point.parseCat("restaurant"), -37.3159209067, 147.812377025)));
		points.add((new Point("id1", Point.parseCat("restaurant"), -33.3159209067, 146.441243728)));
		points.add((new Point("id2", Point.parseCat("restaurant"), -32.3159209067, 142.812377235)));
		points.add((new Point("id3", Point.parseCat("restaurant"), -31.3159209067, 143.512377638)));
		points.add((new Point("id4", Point.parseCat("restaurant"), -35.3159209067, 144.912347025)));
		points.add((new Point("id5", Point.parseCat("restaurant"), -36.3159209067, 137.882372325)));
		points.add((new Point("id6", Point.parseCat("restaurant"), -39.3159209067, 127.812377025)));
		points.add((new Point("id7", Point.parseCat("restaurant"), -50.3159209067, 119.222377025)));
		points.add((new Point("id8", Point.parseCat("restaurant"), -40.3159209067, 148.772377025)));
		agent.buildIndex(points);
	}
	
	@Test
	public void test1() {  // searchTest()
		System.out.println("Starting Test");
		Point searchTerm = new Point("id9", Point.parseCat("restaurant"), -41.3159209067, 140.772377025);
		List<Point> closest = agent.search(searchTerm, 4);
		for (Point point : closest) {
			System.out.println("Distance to point: " + point.distTo(searchTerm));
		}
		assertTrue(closest.get(0).distTo(searchTerm) < closest.get(1).distTo(searchTerm));
	}

	
	@Test
	public void test2() {	// addPointTest()
		System.out.println("Testing addPoint(point) for duplicate and non-duplicate points");
		Point duplicatePoint = new Point("id8", Point.parseCat("restaurant"), -40.3159209067, 148.772377025);
		Point newPoint = new Point("id10", Point.parseCat("restaurant"), -36.4840417904, 144.608559957);
		assertFalse(agent.addPoint(duplicatePoint));
		assertTrue(agent.addPoint(newPoint));
	}
	
	@Test
	public void test3() {	//deletePointTest()
		System.out.println("Testing deletePoint(point) for duplicate and non-duplicate points");
		Point duplicatePoint = new Point("id7", Point.parseCat("restaurant"), -50.3159209067, 119.222377025);
		Point newPoint = new Point("id11", Point.parseCat("restaurant"), -37.7697535692, 143.846771534);
		assertTrue(agent.deletePoint(duplicatePoint));
		assertFalse(agent.deletePoint(newPoint));
		
		// Print out the results to show the updated list
		printOutResults();
	}
	
	@Test
	public void test4() {  // isPointIn(Point point)
		System.out.println("Testing isPointIn(point) for duplicate and non-duplicate points");
		Point duplicatePoint = new Point("id7", Point.parseCat("restaurant"), -50.3159209067, 119.222377025);
		Point newPoint = new Point("id11", Point.parseCat("restaurant"), -37.7697535692, 143.846771534);
		assertTrue(agent.isPointIn(duplicatePoint));
		assertFalse(agent.isPointIn(newPoint));
	}
	
	public void printOutResults() {
		for (Point currentPoint : points ) {
			System.out.println( currentPoint.toString() );
		}
	}
	
}
