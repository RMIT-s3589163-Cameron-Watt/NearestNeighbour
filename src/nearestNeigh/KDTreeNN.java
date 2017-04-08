package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan + StockWell, Patrick + Watt, Cameron
 */
public class KDTreeNN implements NearestNeigh{
	
	private Node root = null;
	
	// Changed to test in the Test file
	public void printTree(Node node, List<Direction> visitedDirections, int level) {
		List<Direction> list = new ArrayList<>();
		list.addAll(visitedDirections);
		if (node == null)
			return;
		System.out.println();
		list.add(Direction.RIGHT);
		for (int x = 0; x < level; ++x) {
			if (list.get(x) == Direction.RIGHT) 
				System.out.print("|   ");
			else
				System.out.print("    ");
		}
		System.out.print("+-- " + node.getPoint().id + ", " + node.getPoint().lat + ", " + node.getPoint().lon);
		printTree(node.getRightChild(), list, level + 1);
		list.add(level + 1, Direction.LEFT);
		printTree(node.getLeftChild(), list, level + 1);
	}
	
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
    
    /**
     * A recursive method that receives a list of nodes
     * selects the middle node along the chosen dimension
     * and calls itself on the sublist to the left, and then the right
     * @param A List<> of Node objects to be added to the tree
     * @param A Node object that will be the parent node of the return value
     * @return Node object chosen to split the collection of nodes
     */
    public Node splitAndAddToTree(List<Node> allNodes, Node parent) {
    	List<Node> nodes = new ArrayList<>();
    	nodes.addAll(allNodes);
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
    	List<NodeAndDistance> closest = findKNodes(root, new Node(searchTerm, true, null, null, null), k, new ArrayList<>());
    	List<Point> kPoints = new ArrayList<>(); 
    	for (NodeAndDistance n : closest.subList(0, k))
    		kPoints.add(n.node.getPoint());
        return kPoints;
    }
    
    private List<NodeAndDistance> findKNodes(Node currentTreeNode, Node searchTerm, int k, List<NodeAndDistance> closest) {
    	boolean sameCategory = searchTerm.getPoint().cat == currentTreeNode.getPoint().cat;
    	double currentDistance = currentTreeNode.getPoint().distTo(searchTerm.getPoint());
    	NodeAndDistance currentNodeAndDistance = new NodeAndDistance(currentTreeNode, currentDistance);
    	List <NodeAndDistance> updatedClosest = closest;
    	if (sameCategory && (closest.size() < k || currentDistance < closest.get(k - 1).distance)) {
    		closest.add(currentNodeAndDistance);
    		updatedClosest = selectionSortByDistance(closest);
    	}
    	Direction d = currentTreeNode.chooseDirection(searchTerm);
    	if (currentTreeNode.getChild(d) != null)
    		updatedClosest = findKNodes(currentTreeNode.getChild(d), searchTerm, k, updatedClosest);
    	//Here we return from the first recursive calls. Check if we need to go the other direction
    	Point darkSide = createStraightLinePoint(currentTreeNode, searchTerm);
    	Double distanceToOtherSide = searchTerm.getPoint().distTo(darkSide);
    	if (currentDistance > distanceToOtherSide && currentTreeNode.getChild(d.getOtherDirection()) != null) {
    		updatedClosest = findKNodes(currentTreeNode.getChild(d.getOtherDirection()), searchTerm, k, updatedClosest);
    	}
    	return updatedClosest;
    }
    
    private Point createStraightLinePoint(Node currentTreeNode, Node searchTerm) {
    	Point darkSide = new Point();
    	if (currentTreeNode.getDirection()) { //if x axis
    		darkSide.lon = searchTerm.getPoint().lon;
    		darkSide.lat = currentTreeNode.getCoordinate();
    	}
    	else { //y axis
    		darkSide.lat = searchTerm.getPoint().lat;
    		darkSide.lon = currentTreeNode.getCoordinate();
    	}
    	return darkSide;
    }
    
    private List<NodeAndDistance> selectionSortByDistance(List<NodeAndDistance> array) {
        for (int j = 0; j < array.size() - 1; ++j) { // visit each item in the list
        	int smallestIndex = j + 1;
        	for (int x = j + 2; x < array.size(); ++x) { // for each 'j', compare it to each array[index > j]
        		if (array.get(x).distance < array.get(smallestIndex).distance) {
        			smallestIndex = x;
        		}
        	}
        	if (array.get(j).distance > array.get(smallestIndex).distance) {
        		NodeAndDistance temp = array.get(j);
        		array.set(j, array.get(smallestIndex));
        		array.set(smallestIndex, temp);
        	}
        }
        return array;
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
    	Node lastFoundNode = null; //Trails the pointer to hold reference to the tree
    	boolean travelledLeft = true; //Remembers the last direction travelled
    	while(nodeFromTree != null) {
			lastFoundNode = nodeFromTree;
    		try {
    			// If the current node is already in the data structure
    			if(nodeToAdd.getPoint().equals(nodeFromTree.getPoint())) {
    				return false;
    			}    				
    			else if(nodeToAdd.isGreaterThan(nodeFromTree)) { //go right subtree
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
    	Node nodeToRemove = findNodeNONRec( new Node(point, true, null, null, null), root);
    	// If the node was NOT found
    	if (nodeToRemove == null)
    		return false;
    	
    	// to avoid multiple calls to the getParent()
    	Node parentNode = nodeToRemove.getParent();
    		
    	if (nodeToRemove.hasBothChildren()) { // TODO ########  setting    the isVertical ###
    		// Check if it is the root
    		if ( parentNode == null ) {
    			// ??????????????????????????????????????????????????????????????????
    		}
    		// TODO
    		
    	}
    	else if (nodeToRemove.hasOneChild()) {		// TODO ########  setting    the isVertical ###
    		// Check if it is the root
    		if ( parentNode == null ) {
    			// Find the child node of the root
    			if ( nodeToRemove.getLeftChild() != null ) {
    				root = nodeToRemove.getLeftChild();
    			}
    			else { // If the child node is the right child of the root
    				root = nodeToRemove.getRightChild();
    			}
    			root.changeDimension();
    		}
    		
    		// Check if the nodeToRemove is the left or right child of the parent
    		if ( parentNode.getLeftChild().getPoint().equals( nodeToRemove.getPoint() ) ) {
    			// Find the child node of the nodeToRemove
    			if ( nodeToRemove.getLeftChild() != null ) {
    				parentNode.setLeftChild( nodeToRemove.getLeftChild() );
    				parentNode.getLeftChild().changeDimension();
    			}
    			else { // if the node to keep is the right child of nodeToRemove
    				parentNode.setLeftChild( nodeToRemove.getRightChild() );
    				parentNode.getLeftChild().changeDimension();
    			}
    		}
    		else { // if the nodeToRemove is the right child of the parent
    			// Find the child node of the nodeToRemove
    			if ( nodeToRemove.getLeftChild() != null ) {
    				parentNode.setRightChild( nodeToRemove.getLeftChild() );
    				parentNode.getRightChild().changeDimension();
    			}
    			else { // if the node to keep is the right child of nodeToRemove
    				parentNode.setRightChild( nodeToRemove.getRightChild() );
    				parentNode.getRightChild().changeDimension();
    			}
    		}
    	}
    	else { // If node has no children  
    		// Check if it is the root
    		if ( parentNode == null ) {
    			root = null;
    		}
    		
    		// Check if the nodeToRemove is the left or right child of the parent
    		if ( parentNode.getLeftChild().getPoint().equals( nodeToRemove.getPoint() ) ) {
    			parentNode.setLeftChild(null);
    		}
    		else if ( parentNode.getRightChild().getPoint().equals( nodeToRemove.getPoint() ) ) {
    			parentNode.setRightChild(null);
    		}
    		nodeToRemove.setParent(null);
    	}
        return true;
    }
    
    
    private Node findNodeNONRec( Node nodeToFind, Node nextNode )
    {
		while (nextNode != null) {
			if (  nodeToFind.getPoint().equals( nextNode.getPoint() ) ) {
				return nextNode;
			}
			
			try {
				if ( nodeToFind.isGreaterThan( nextNode ) ) {
					nextNode = nextNode.getRightChild();
				}
				/* This else statement will also catch any points with duplicate dimensions
				 * eg if we have two points with the lat 8 (and different lon values), the 
				 * second point will ALWAYS be placed on the LEFT branch
				 */
				else {
					nextNode = nextNode.getLeftChild();
				}
			} catch (DimensionMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Change the dimension as we are descending further through the tree
			nodeToFind.changeDimension();
		}
        return null;
    }
    
    
    private Node findNode(Node searchTerm, Node treeNode) {
    	Node foundNode = null;
    	if (searchTerm.getPoint().equals(treeNode.getPoint())) { 
    		foundNode = treeNode;
    	}
    	else if (searchTerm.getCoordinate() < treeNode.getCoordinate() && treeNode.getLeftChild() != null) {
    		searchTerm.changeDimension();
    		foundNode = findNode(searchTerm, treeNode.getLeftChild());
    	}
    	else if (searchTerm.getCoordinate() > treeNode.getCoordinate() && treeNode.getRightChild() != null) {
    		searchTerm.changeDimension();
    		foundNode = findNode(searchTerm, treeNode.getRightChild());
    	}
    	else {
    		foundNode = null;
    	}
    	return foundNode;
    }
    
    private void changeAllDimensionsBelow(Node topNode) {
    	if (topNode == null) {
    		return;
    	}
    	topNode.changeDimension();
    	changeAllDimensionsBelow(topNode.getLeftChild());
    	changeAllDimensionsBelow(topNode.getRightChild());
    }

    @Override
    public boolean isPointIn(Point point) {

    	/* BASIC THOUGHT PROCESS 
    	 * nextNode = root
    	 * while (nextNode != null) {
    	 * 	 if point = nextNode.point  ? return true  :  continue  CHECK COMPLETELY
    	 * 	 if point <= nextNode.point ? getLeftChild()  :  getRightChild()
    	 * }
    	 */
    	
    	// Need to create a temporary node Object to allow easy access throughout the tree
    	// NOTE that the parentNode, rightChildNode, leftChildNode references WILL NOT be used
    	Node tempNode = new Node(point, true, null, null, null);
		Node nextNode = root;
		
		while (nextNode != null) {
			if (  tempNode.getPoint().equals( nextNode.getPoint() ) ) {
				return true;
			}
			
			try {
				if ( tempNode.isGreaterThan( nextNode ) ) {
					nextNode = nextNode.getRightChild();
				}
				/* This else statement will also catch any points with duplicate dimensions
				 * eg if we have two points with the lat 8 (and different lon values), the 
				 * second point will ALWAYS be placed on the LEFT branch
				 */
				else {
					nextNode = nextNode.getLeftChild();
				}
			} catch (DimensionMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Change the dimension as we are descending further through the tree
			tempNode.changeDimension();
		}
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

/**
 * A wrapper Class to hold a Node and a distance value 
 * The measurement is the distance to another Node in a search request
 * @author patstockwell
 *
 */
class NodeAndDistance {
	public Node node;
	public double distance;
	
	public NodeAndDistance(Node node, double distance) {
		this.node = node;
		this.distance = distance;
	}
}

/**
 * A Node Object for a 2D tree
 * The x and y values exist inside the point variable (holds a Point object)
 * There are references to 3 other node objects within each Node
 * They belong to the parent, left child, and right child nodes
 * If parent is null, it can be assumed it is the root
 * If either child is null, it can be assumed it is a leaf
 * @author patstockwell
 *
 */
class Node {
	
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
	
	public Point getPoint() {
		return point;
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
	
	public boolean getDirection() {
		return this.isVertical;
	}
	
	public boolean isGreaterThan(Node nodeFromTree) throws DimensionMismatchException {
		if (nodeFromTree.isVertical != this.isVertical)
			throw new DimensionMismatchException();
		return this.getCoordinate() > nodeFromTree.getCoordinate();
	}
	
	public Node getChild(Direction direction) {
		if (direction == Direction.LEFT) {
			return this.leftChild;
		}
		else
			return this.rightChild;
	}
	
	public Direction chooseDirection(Node searchNode) {
		if (searchNode.getCoordinate() < this.getCoordinate()) {
			return Direction.LEFT;
		}
		else
			return Direction.RIGHT;
	}
	
	public boolean hasBothChildren() {
		return (leftChild != null && rightChild != null);
	}
	
	public boolean hasOneChild() {
		return (leftChild != null || rightChild != null);
	}
}



