
/**
	* This class holds an equal-to relation.
	*
	* @version foo
	* @author Jon Natkins (jnatkins)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;

/**
 * Typing class that represents an equal-to condition.
 */

public class NodeRelationEqual extends NodeRelation
{
	private NodeExpr _left;
	private NodeExpr _right;

	/**
	 * Creates a NodeRelationEqual, which represents an equal-to
	 * condition with left-hand-side left and right-hand-side right:<br>
	 * left &lt; right
	 */
	public NodeRelationEqual( NodeExpr left,NodeExpr right )
	{
		_left = left;
		_right = right;
	}

	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeRelationEqual(this);
	}

	/**
	 * Returns the NodeExpr that represents the left-hand-side.
	 */
	public NodeExpr leftChild()
	{
		return _left;
	}

	/**
	 * Returns the NodeExpr that represents the right-hand-side.
	 */
	public NodeExpr rightChild()
	{
		return _right;
	}
}
