package comp.nodes;

import comp.SemanticType;
import comp.Visitor;
import comp.VisitorException;

/**
 * Represents a procedure call expression.
 * @Author: Benjamin Niedzielski (bniedzie)
 */
public class NodeExprProcCall extends NodeExpr {

	private String _id;
	private NodeListProcCallArg _args;

	/**
	 * Creates a NodeExprProcCall with an id and arguments
	 */
	public NodeExprProcCall(String id,	NodeListProcCallArg args){
		_id = id;
		_args = args;
	}
	
	public SemanticType semanticType() {
		return SemanticType.INTEGER;
	}

	@Override
	public void visit(Visitor visitor) throws VisitorException {
		visitor.handleNodeExprProcCall(this);
	}
	
	public String identifier() {
	   return _id;
	}

	public NodeListProcCallArg arguments() {
	   return _args;
	}
}
