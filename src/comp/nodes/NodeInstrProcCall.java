package comp.nodes;

import comp.Visitor;
import comp.VisitorException;

/**
 * Represents a procedure call instruction.
 * @Author: Benjamin Niedzielski (bniedzie)
 */
public class NodeInstrProcCall extends NodeInstr {
	String _id;
	NodeListProcCallArg _args;

	/**
	 * Creates a NodeInstrProcCall with an id and arguments
	 */
	public NodeInstrProcCall(String id,	NodeListProcCallArg args){
		_id = id;
		_args = args;
	}

	public void visit(Visitor visitor) throws VisitorException {
		visitor.handleNodeInstrProcCall(this);
	}
	
	public String identifier() {
	   return _id;
	}

	public NodeListProcCallArg arguments() {
	   return _args;
	}
}
