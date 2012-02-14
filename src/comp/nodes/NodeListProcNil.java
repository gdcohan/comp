
/**
	* This class is the terminator for a list of procedures.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListProcNil extends NodeListProc
{
	/**
	 * Creates a terminator for a linked list of procedures.
	 */
	public NodeListProcNil()
{ }
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListProcNil(this);
	}
}
 
 
 
