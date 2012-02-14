package comp.nodes;

import comp.SemanticType;
import comp.Visitor;
import comp.VisitorException;
 
 
/**
 * A class representing a procedure declaration argument.
 * 
 * @author Benjamin Niedzielski (bniedzie)
 */
public class NodeProcDeclArg extends Node
{
	private String _id;
	
	/**
	 * Creates a NodeProcDeclArg with semantic type and identifier id.
	 */
	public	NodeProcDeclArg(String id)
	{
		_id = id;
	}
	
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeProcDeclArg(this);
	}
	
	/**
	 * Returns the identifier for this argument.
	 */
	public String identifier()
	{
		return _id;
	}
}
