
/**
	* This class is the terminator for a list of global variable declarations.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListVarDeclNil extends NodeListVarDecl
{
	/**
	 * Creates a terminator for a linked list of instructions.
	 */
	public NodeListVarDeclNil()
{ }
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListVarDeclNil(this);
	}
}
 
 
 
