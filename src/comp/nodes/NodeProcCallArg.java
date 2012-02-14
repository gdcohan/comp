package comp.nodes;

import comp.SemanticType;
import comp.Visitor;
import comp.VisitorException;
 
 
/**
 * A class representing a procedure call argument.
 * 
 * @author Benjamin Niedzielski (bniedzie)
 */
public class NodeProcCallArg extends Node
{
	private NodeExpr _expr;
	
	/**
	 * Creates a NodeProcCallArg with semantic type and identifier id.
	 */
	public	NodeProcCallArg(NodeExpr expr)
	{
		_expr = expr;
	}
	
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeProcCallArg(this);
	}
	
	/**
	 * Returns the expression of this argument.
	 */
	public NodeExpr expression()
	{
		return _expr;
	}
}
