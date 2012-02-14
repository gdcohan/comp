/**
	* This class represents a false expression.
	*
	* @version foo
	* @author bniedzie
	*/

package comp.nodes;

import comp.SemanticType;
import comp.Visitor;
import comp.VisitorException;

/**
 * Typing class that represents a false expression.
 */
public class NodeExprFalse extends NodeExpr {
	public SemanticType semanticType() {
		return SemanticType.BOOLEAN;
	}

	public void visit(Visitor visitor) throws VisitorException {
		visitor.handleNodeExprFalse(this);
	}

	public String toString(){
		return "false";
	}
}
