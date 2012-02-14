
/**
 * This class is the root of the parse tree.
 *
 * @version 2.0 (Feb 26, 2011)
 * @author Benjamin Niedzielski (bniedzie)
 */
package comp.nodes;

import comp.Visitor;
import comp.VisitorException;
 

/**
 * Typing class that represents the Program in all its glory.
 */
 
public class NodeProgram extends Node
{
	private NodeListVarDecl _listVarDecl;
	private NodeListProc _listProc;
	 
	/**
	 * Creates a NodeProgram, which represents the Program, with
	 * linked list of variable declatations listVarDecl
	 * and linked list of procedures listProc.
	 */
	public NodeProgram(NodeListVarDecl listVarDecl, NodeListProc listProc)
	{
		_listVarDecl = listVarDecl;
		_listProc = listProc;
	}
	 
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeProgram(this);
	}
	 
	/**
	 * Returns the NodeListVarDecl that represents the list of global variables.
	 */
	public NodeListVarDecl listVarDecl()
	{
		return _listVarDecl;
	}
	
	/**
	 * Returns the NodeListProc that represents the list of procedures.
	 */
	public NodeListProc listProc()
	{
		return _listProc;
	}
}
 
 
