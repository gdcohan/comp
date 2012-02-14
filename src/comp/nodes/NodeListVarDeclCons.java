
/**
	* Typing class that represents a non-null entry in a linked list of global variable declarations
	*
	* @version 1.0
	* @author Benjamin Niedzielski (bniedzie)
	*/
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 
public class NodeListVarDeclCons extends NodeListVarDecl
{
	private NodeVarDecl _head;
	private NodeListVarDecl _tail;
	 
	/**
	 * Creates a NodeListVarDeclCons, which represents an entry in a linked 
	 * list of declarations, with entry head and next-entry-pointer tail.
	 */
	public NodeListVarDeclCons(NodeVarDecl head, NodeListVarDecl tail)
	{
		_head = head;
		_tail = tail;
	}
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeListVarDeclCons(this);
	}
	 
	/**
	 * Returns the NodeVarDecl that represents this entry in the linked-list.
	 */
	public NodeVarDecl head()
	{
		return _head;
	}
	 
	/**
	 * Returns the NodeListVarDecl that represents the rest of the list.
	 */
	public NodeListVarDecl tail()
	{
		return _tail;
	}
}
 
 
