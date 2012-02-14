package comp.nodes;

import comp.Visitor;
import comp.VisitorException;

/**
 * Typing class that represents declaring a single type globally.
 */

public class NodeVarDeclSingle extends NodeVarDecl
{
	private String _id;

	/**
	 * Creates a NodeVarDeclSingle with identitifer id.
	 */
	public NodeVarDeclSingle(String id)
	{
		_id = id;
	}

	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeVarDeclSingle(this);
	}

	/**
	 * Returns the identifier for this single.
	 */
	public String identifier()
	{
		return _id;
	}
}
