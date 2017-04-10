package nearestNeigh;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class KDTreeNNTest {

	List<Point> points = new ArrayList<Point>();
	KDTreeNN agent = new KDTreeNN();
	
	@Before
	public void setupPoints() {
		System.out.println(" ");
		System.out.println("Starting to add points");
		
		points.add((new Point("id0", Point.parseCat("restaurant"), 3, 3)));
		points.add((new Point("id1", Point.parseCat("restaurant"), 2.5, 3)));
		points.add((new Point("id2", Point.parseCat("restaurant"), 3.5, 3)));
		points.add((new Point("id3", Point.parseCat("restaurant"), 1, 1)));
		points.add((new Point("id4", Point.parseCat("restaurant"), 1, 5)));
		points.add((new Point("id5", Point.parseCat("restaurant"), 5, 1)));
		points.add((new Point("id6", Point.parseCat("restaurant"), 5, 5)));
		points.add((new Point("id7", Point.parseCat("restaurant"), 0, 0)));
		points.add((new Point("id8", Point.parseCat("restaurant"), 2, 2)));
		points.add((new Point("id9", Point.parseCat("restaurant"), 0, 5)));
		points.add((new Point("id10", Point.parseCat("restaurant"), 2, 4)));
		points.add((new Point("id11", Point.parseCat("restaurant"), 4, 2)));
		points.add((new Point("id12", Point.parseCat("restaurant"), 6, 0)));
		points.add((new Point("id13", Point.parseCat("restaurant"), 4, 4)));
		points.add((new Point("id14", Point.parseCat("restaurant"), 6, 6)));
		
		// Testing tree creation with duplicate x values
		/*points.add((new Point("id0", Point.parseCat("restaurant"), 3, 3)));
		points.add((new Point("id1", Point.parseCat("restaurant"), 3, 3)));
		points.add((new Point("id2", Point.parseCat("restaurant"), 3, 3)));
		points.add((new Point("id3", Point.parseCat("restaurant"), 3, 1)));
		points.add((new Point("id4", Point.parseCat("restaurant"), 3, 5)));
		points.add((new Point("id5", Point.parseCat("restaurant"), 3, 1)));
		points.add((new Point("id6", Point.parseCat("restaurant"), 3, 5)));
		points.add((new Point("id7", Point.parseCat("restaurant"), 0, 0)));
		points.add((new Point("id8", Point.parseCat("restaurant"), 2, 2)));
		points.add((new Point("id9", Point.parseCat("restaurant"), 0, 5)));
		points.add((new Point("id10", Point.parseCat("restaurant"), 2, 4)));
		points.add((new Point("id11", Point.parseCat("restaurant"), 4, 2)));
		points.add((new Point("id12", Point.parseCat("restaurant"), 6, 0)));
		points.add((new Point("id13", Point.parseCat("restaurant"), 4, 4)));
		points.add((new Point("id14", Point.parseCat("restaurant"), 6, 6)));*/
		
		agent.buildIndex(points);
	}
	
	
	
	
	@Test
	public void test2() {	// addPointTest()
		System.out.println("Testing addPoint(point) for duplicate and non-duplicate points");
		Point duplicatePoint = new Point("id8", Point.parseCat("restaurant"), 2, 2);
		Point newPoint = new Point("id15", Point.parseCat("restaurant"), 3, 6);
		assertFalse(agent.addPoint(duplicatePoint));
		assertTrue(agent.addPoint(newPoint));
		
		List<Direction> list = new ArrayList<>();
    	agent.printTree( agent.getRoot(), list, 0);
	}
	
	@Test
	public void test4() {  // isPointIn(Point point)
		System.out.println("Testing isPointIn(point) for duplicate and non-duplicate points");
		Point duplicatePoint = new Point("id8", Point.parseCat("restaurant"), 2, 2);
		Point newPoint = new Point("id15", Point.parseCat("restaurant"), 3, 6);
		assertTrue(agent.isPointIn(duplicatePoint));
		assertFalse(agent.isPointIn(newPoint));
	}
	
	@Test
	public void deleteNodeWithZeroChildrenTest() {
		List<Direction> d = new ArrayList<>();
		d.add(Direction.LEFT);
		Point pointToDelete = new Point("id8", Point.parseCat("restaurant"), 2, 2);
		agent.deletePoint(pointToDelete);
		assertFalse(agent.isPointIn(pointToDelete));
	}
	
	@Test
	public void deleteNodeWithOneChildTest() {
		List<Direction> d = new ArrayList<>();
		d.add(Direction.LEFT);
		Point pointToDelete = new Point("id8", Point.parseCat("restaurant"), 2, 2);
		Point nextPointToDelete = new Point("id3", Point.parseCat("restaurant"), 1, 1);
		agent.deletePoint(pointToDelete);
		agent.deletePoint(nextPointToDelete);
		assertFalse(agent.isPointIn(nextPointToDelete));
	}
	
	@Test
	public void deleteNodeWithTwoChildrenTest() {
		List<Direction> d = new ArrayList<>();
		d.add(Direction.LEFT);
		agent.printTree(agent.getRoot(), d, 0);
		Point pointToDelete = new Point("id0", Point.parseCat("restaurant"), 3, 3);
		agent.deletePoint(pointToDelete);
		agent.printTree(agent.getRoot(), d, 0);
		assertFalse(agent.isPointIn(pointToDelete));
	}
}
