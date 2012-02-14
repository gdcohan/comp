package comp.nodes;

import comp.Visitor;
import comp.VisitorException;

/**
 * Represents a return expression for a procedure.
 * Has a return value.
 *
 * @Author: bniedzie
 */
public class NodeInstrReturnExpr extends NodeInstr {
	private NodeExpr _expr;
	
	/**
     * Creates a new NodeInstrReturnExpr, given an expression expr.
     */
	public NodeInstrReturnExpr(NodeExpr expr){
		_expr = expr;
	}

	/**
     * Returns the expression this instruction returns
     */
	public NodeExpr expression() {
		return _expr;
	}

	/**
     * Visit this node
     */
	public void visit(Visitor visitor) throws VisitorException {
		visitor.handleNodeInstrReturnExpr(this);
	}
}
