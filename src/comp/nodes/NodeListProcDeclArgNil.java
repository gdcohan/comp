
/**
	* This class is the terminator for a list of procedure declaration arguments.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListProcDeclArgNil extends NodeListProcDeclArg
{
	/**
	 * Creates a terminator for a linked list of procedure declaration arguments.
	 */
	public NodeListProcDeclArgNil()
{ }
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListProcDeclArgNil(this);
	}
}
 
 
 
