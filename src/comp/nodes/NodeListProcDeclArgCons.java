
/**
	* Typing class that represents a non-null entry in a linked list of procedure
	* declaration arguments.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListProcDeclArgCons extends NodeListProcDeclArg
{
	private NodeProcDeclArg _head;
	private NodeListProcDeclArg _tail;
	 
	/**
	 * Creates a NodeListProcDeclArgCons, which represents an entry in a linked 
	 * list of procedure declaration arguments, with entry head and next-entry-pointer tail.
	 */
	public NodeListProcDeclArgCons(NodeProcDeclArg head, NodeListProcDeclArg tail)
	{
		_head = head;
		_tail = tail;
	}
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListProcDeclArgCons(this);
	}
	 
	/**
	 * Returns the NodeProcDeclArg that represents this entry in the linked-list.
	 */
	public NodeProcDeclArg head()
	{
		return _head;
	}
	 
	/**
	 * Returns the NodeListProcDeclArg that represents the rest of the list.
	 */
	public NodeListProcDeclArg tail()
	{
		return _tail;
	}
}
 
 
