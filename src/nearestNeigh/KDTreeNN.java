package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{
	
	private Node root = null;
	
	/**
	 * Recursively adds 2D points into a tree structure using a Depth first approach
	 * The tree structure is accessible via the root node variable
	 * Calls splitAndAddToTree() method to complete the recursion
	 * @param A java List<> of Point objects
	 * @return void
	 */
    @Override
    public void buildIndex(List<Point> points) {
    	List<Node> unsortedNodes = new ArrayList<>();
    	for (Point point: points)
    		unsortedNodes.add(new Node(point, true, null, null, null));
    	this.root = splitAndAddToTree(unsortedNodes, this.root);
    }
    
    public Node splitAndAddToTree(List<Node> nodes, Node parent) {
    	if (nodes.isEmpty())
    		return null;
    	List<Node> sortedNodes = nodeSelectionSort(nodes); //sort the nodes
    	int middleIndex = sortedNodes.size() / 2; //find the middle node
    	Node middleNode = sortedNodes.remove(middleIndex); //removes from the list AND returns the object
    	middleNode.setParent(parent); //Connect to the tree
    	for (Node node: sortedNodes) //change isVertical in every other node
    		node.changeDimension();
    	//recursive call to left and right sides
    	middleNode.setLeftChild(splitAndAddToTree(sortedNodes.subList(0, middleIndex), middleNode)); //left subtree
    	middleNode.setRightChild(splitAndAddToTree(sortedNodes.subList(middleIndex, sortedNodes.size()), middleNode)); //right subtree
    	return middleNode;
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        return new ArrayList<>();
    }
    
    /**
     * Each Point is wrapped in a Node object
     * It is added to the tree based on the vertical or horizontal split for that level of the tree
     * The left nodes are considered to be the lower values for this implementation
     * @param point is the x and y coordinates to be added
     * @return boolean to test if the addition was successful
     */
    @Override
    public boolean addPoint(Point point) {
    	Node nodeToAdd = new Node(point, true, null, null, null); //Wrap the point in a Node
    	Node nodeFromTree = root; //Pointer node to search the tree for insertion point
    	Node lastFoundNode = root; //Trails the pointer to hold reference to the tree
    	boolean travelledLeft = true; //Remembers the last direction travelled
    	while(nodeFromTree != null) {
			lastFoundNode = nodeFromTree;
    		try {
				if(nodeToAdd.isGreaterThan(nodeFromTree)) { //go right subtree
					nodeFromTree = nodeFromTree.getRightChild();
					travelledLeft = false;
				}
				else { //go left subtree
					nodeFromTree = nodeFromTree.getLeftChild();
					travelledLeft = true;
				}
			} catch (DimensionMismatchException e) {
				e.printStackTrace();
			}
			nodeToAdd.changeDimension();
    	} //end while loop
    	nodeToAdd.setParent(lastFoundNode); //Assign values
    	if (travelledLeft)
    		lastFoundNode.setLeftChild(nodeToAdd);
    	else
    		lastFoundNode.setRightChild(nodeToAdd);
        return true;
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
	
	private List<Node> nodeSelectionSort(List<Node> array) {
		for (int j = 0; j < array.size() - 1; ++j) { // visit each item in the list
			int smallestIndex = j + 1;
			for (int x = j + 2; x < array.size(); ++x) { // for each 'j', compare it to each array[index > j]
				if (array.get(x).getCoordinate() < array.get(smallestIndex).getCoordinate()) {
					smallestIndex = x;
				}
			}
			if (array.get(j).getCoordinate() > array.get(smallestIndex).getCoordinate()) {
				Node temp = array.get(j);
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
	 * The x and y values exist inside the point variable (holds a Point object)
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
	
	public double getCoordinate() {
		if (isVertical)
			return point.lat;
		else
			return point.lon;
	}
	
	public boolean changeDimension() {
		this.isVertical = !this.isVertical;
		return isVertical;
	}
	
	public boolean isGreaterThan(Node nodeFromTree) throws DimensionMismatchException {
		if (nodeFromTree.isVertical != this.isVertical)
			throw new DimensionMismatchException();
		return this.getCoordinate() > nodeFromTree.getCoordinate();
	}
}


