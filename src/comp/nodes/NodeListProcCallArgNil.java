
/**
	* This class is the terminator for a list of procedure call arguments.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListProcCallArgNil extends NodeListProcCallArg
{
	/**
	 * Creates a terminator for a linked list of procedure call arguments.
	 */
	public NodeListProcCallArgNil()
{ }
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListProcCallArgNil(this);
	}
}
 
 
 
