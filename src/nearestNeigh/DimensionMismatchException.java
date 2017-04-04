package nearestNeigh;

public class DimensionMismatchException extends Exception{
	
	/**
	 * In a 2DTree, each level of the tree is split on an alternating dimension
	 * Commonly the two dimensions are an x/y tuple from the Cartesian plane
	 * When comparing two points, they must both use the same dimension (axis)
	 * Measuring an x against a y will lead to irregularities in the tree structure 
	 */
	private static final long serialVersionUID = 1L;

	public DimensionMismatchException()
	{
		super("Trying to Compare an x and a y value. Must use the same dimension to compare points.");
	}
	
	public DimensionMismatchException( String message)
	{
		super(message);
	}
}
