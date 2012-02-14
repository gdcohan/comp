
/**
	* Typing class that represents a non-null entry in a linked list of procedures.
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListProcCons extends NodeListProc
{
	private NodeProc _head;
	private NodeListProc _tail;
	 
	/**
	 * Creates a NodeListProcCons, which represents an entry in a linked 
	 * list of procedures, with entry head and next-entry-pointer tail.
	 */
	public NodeListProcCons(NodeProc head, NodeListProc tail)
	{
		_head = head;
		_tail = tail;
	}
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListProcCons(this);
	}
	 
	/**
	 * Returns the NodeProc that represents this entry in the linked-list.
	 */
	public NodeProc head()
	{
		return _head;
	}
	 
	/**
	 * Returns the NodeListProc that represents the rest of the list.
	 */
	public NodeListProc tail()
	{
		return _tail;
	}
}
 
 
