package comp.nodes;

import comp.SemanticType;
import comp.Visitor;
import comp.VisitorException;
 
 
/**
 * A class representing a procedure.
 * 
 * @author Benjamin Niedzielski (bniedzie)
 */
public class NodeProc extends Node
{
	private String _id;
	private NodeListProcDeclArg _argList;
	private NodeInstr _instr;
	
	/**
	 * Creates a NodeProc with semantic type, identifier id,
	 * list of arguments, and the procedure's instruction.
	 */
	public	NodeProc(String id, NodeListProcDeclArg argList,
									 NodeInstr instr)
	{
		_id = id;
		_argList = argList;
		_instr = instr;
	}
	
	public void visit( Visitor visitor ) throws VisitorException
	{
		visitor.handleNodeProc(this);
	}
	
	/**
	 * Returns the identifier for this argument.
	 */
	public String identifier()
	{
		return _id;
	}
	
	/**
	 * Returns the argument list for this procedure.
	 */
	public NodeListProcDeclArg argumentList()
	{
		return _argList;
	}
	
	/**
	 * Returns the instruction for this argument.
	 */
	public NodeInstr instruction()
	{
		return _instr;
	}
}
