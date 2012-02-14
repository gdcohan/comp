
/**
	* Typing class that represents a non-null entry in a linked list of procedure
	* call arguments.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListProcCallArgCons extends NodeListProcCallArg
{
	private NodeProcCallArg _head;
	private NodeListProcCallArg _tail;
	 
	/**
	 * Creates a NodeListProcCallArgCons, which represents an entry in a linked 
	 * list of procedure call arguments, with entry head and next-entry-pointer tail.
	 */
	public NodeListProcCallArgCons(NodeProcCallArg head, NodeListProcCallArg tail)
	{
		_head = head;
		_tail = tail;
	}
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListProcCallArgCons(this);
	}
	 
	/**
	 * Returns the NodeProcCallArg that represents this entry in the linked-list.
	 */
	public NodeProcCallArg head()
	{
		return _head;
	}
	 
	/**
	 * Returns the NodeListProcCallArg that represents the rest of the list.
	 */
	public NodeListProcCallArg tail()
	{
		return _tail;
	}
}
 
 
