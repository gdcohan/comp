
/**
	* This class represents a true expression.
	*
	* @version foo
	* @author bniedzie
	*/
package comp.nodes;

import comp.SemanticType;
import comp.Visitor;
import comp.VisitorException;

/**
 * Typing class that represents a true expression.
 */

public class NodeExprTrue extends NodeExpr {

	public SemanticType semanticType() {
		return SemanticType.BOOLEAN;
	}

	public void visit(Visitor visitor) throws VisitorException {
		visitor.handleNodeExprTrue(this);
	}

	public String toString(){
		return "true";
	}
}
