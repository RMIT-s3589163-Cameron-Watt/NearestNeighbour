package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{
	
	private Node root;
	private List<Point> points;

    @Override
    public void buildIndex(List<Point> points) {
        //sort and add all points to the points array
    	//take the middle point, create a node and assign it to root
    	//iterate through points and use the addPoint method to build the tree
    	//the addPoint method will have to wrap each Point in a node
    	this.points = selectionSortByLatitude(points);
    	int middleIndex = (points.size() / 2);
    	Point rootPoint = points.get(middleIndex);
    	this.root = new Node(rootPoint, true, null, null, null); //First node is vertically split - (x axis)
    	for (Point point: points) {
    		addPoint(point);
    	}
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        return new ArrayList<Point>();
    }

    @Override
    public boolean addPoint(Point point) {
    	//Start by creating a node to wrap the point
    	//set the isVertical to true
    	//use the getCoordinate() to find the comparator (either the x or y based on the isVertical status) 
    	//compare the coordinates
    	//choose a direction and get the next node
    	//if null node then add, else descend
    	//use the changeDimension() function when descending the tree
    	//It will return the new dimension but also allow getCoordinates() to return correctly
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

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
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
}

class Node {
	
	/**
	 * A Node Object for a 2D tree
	 * The x and y values exits inside the point variable (holds a Point object)
	 * There are references to 3 other node objects within each Node
	 * They belong to the parent, left child, and right child nodes
	 * If parent is null, it can be assumed it is the root
	 * If either child is null, it can be assumed it is a leaf
	 */
	
	private Point point;
	private boolean isVertical;
	private Node parent;
	private Node leftChild;
	private Node rightChild;
	
	public Node(Point point, boolean isVertical, Node parent, Node leftChild, Node rightChild) {
		this.point = point;
		this.isVertical = isVertical;
		this.setParent(parent);
		this.setLeftChild(leftChild);
		this.setRightChild(rightChild);
	}
	
	public double getCoordinate() {
		if (isVertical)
			return point.lat;
		else
			return point.lon;
	}
	
	public boolean changeDimension() {
		if (this.isVertical == true)
			this.isVertical = false;
		else
			this.isVertical = true;
		return isVertical;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}
}


