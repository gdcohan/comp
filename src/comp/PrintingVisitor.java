package comp;

import comp.nodes.*;

/**
 * Class that will print out the parsed syntax tree of a NodeProgram.
 * <p>
 * Instantiate it and then have it visit a NodeProgram or any other Node.
 */
public class PrintingVisitor implements Visitor {
	
	private int _tab;
	
	public PrintingVisitor() {
		_tab = 0;
	}
	
	/**
	 * Checks to make sure the node exists, and if so, visits it.
	 */
	private void checkAndVisit(Node node) throws VisitorException{
		if (node != null)
			node.visit(this);
	}
	
	/**
	 * Handles visiting a NodeInstrDeclArray
	 */
	public void handleNodeInstrDeclArray(NodeInstrDeclArray node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrDeclArray: " + node.identifier()
				+ " [" + node.size() + "]\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrDeclSingle
	 */
	public void handleNodeInstrDeclSingle(NodeInstrDeclSingle node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrDeclSingle: " + node.identifier()
				+ "\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeVarDeclArray
	 */
	public void handleNodeVarDeclArray(NodeVarDeclArray node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeVarDeclArray: " + node.identifier()
				+ " [" + node.size() + "]\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeVarDeclSingle
	 */
	public void handleNodeVarDeclSingle(NodeVarDeclSingle node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeVarDeclSingle: " + node.identifier()
				+ "\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprArrayVar
	 */
	public void handleNodeExprArrayVar(NodeExprArrayVar node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprArrayVar: " + node.identifier()
				+ " []");
		checkAndVisit(node.index());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprInt
	 */
	public void handleNodeExprInt(NodeExprInt node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprInt " + node.value() + "\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprMinus
	 */
	public void handleNodeExprMinus(NodeExprMinus node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprMinus");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprProcCall
	 */
	public void handleNodeExprProcCall(NodeExprProcCall node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprProcCall: " + node.identifier());
		System.out.println(tab(_tab) + "Arguments:");
		checkAndVisit(node.arguments());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeProcCallArg
	 */
	public void handleNodeProcCallArg(NodeProcCallArg node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeProcCallArg");
		checkAndVisit(node.expression());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeProcDeclArg
	 */
	public void handleNodeProcDeclArg(NodeProcDeclArg node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeProcDeclArg");
		System.out.println(tab(_tab) + "ID: " + node.identifier());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeProc
	 */
	public void handleNodeProc(NodeProc node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeProc");
		System.out.println(tab(_tab) + "ID: " + node.identifier());
		System.out.println(tab(_tab) + "ArgList:");
		checkAndVisit(node.argumentList());
		System.out.println(tab(_tab) + "Instruction:");
		checkAndVisit(node.instruction());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprTrue
	 */
	public void handleNodeExprTrue(NodeExprTrue node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprTrue");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprFalse
	 */
	public void handleNodeExprFalse(NodeExprFalse node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprFalse");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprPlus
	 */
	public void handleNodeExprPlus(NodeExprPlus node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprPlus");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprTimes
	 */
	public void handleNodeExprTimes(NodeExprTimes node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprTimes");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}

	/**
	 * Handles visiting a NodeExprDivide
	 */
	public void handleNodeExprDivide(NodeExprDivide node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprDivide");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprMod
	 */
	public void handleNodeExprMod(NodeExprMod node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprMod");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeExprVar
	 */
	public void handleNodeExprVar(NodeExprVar node) throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeExprVar: " + node.identifier() + 
		"\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrAssignArray
	 */
	public void handleNodeInstrAssignArray(NodeInstrAssignArray node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrAssignArray: "
				+ node.identifier() + " []");
		System.out.println(tab(_tab) + "AssignArray Index:");
		checkAndVisit(node.index());
		System.out.println(tab(_tab) + "AssignArray Expr:");
		checkAndVisit(node.expression());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrAssignSingle
	 */
	public void handleNodeInstrAssignSingle(NodeInstrAssignSingle node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrAssignSingle: "
				+ node.identifier());
		System.out.println(tab(_tab) + "Assign Expr:");
		checkAndVisit(node.expression());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrBlock
	 */
	public void handleNodeInstrBlock(NodeInstrBlock node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrBlock");
		System.out.println(tab(_tab) + "Block Body:");
		checkAndVisit(node.instructions());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrIfthen
	 */
	public void handleNodeInstrIfthen(NodeInstrIfthen node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrIfthen");
		System.out.println(tab(_tab) + "If Relation:");
		checkAndVisit(node.relation());
		System.out.println(tab(_tab) + "Then Body:");
		checkAndVisit(node.thenInstruction());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrIfthenelse
	 */
	public void handleNodeInstrIfthenelse(NodeInstrIfthenelse node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrIfthenelse");
		System.out.println(tab(_tab) + "If Relation:");
		checkAndVisit(node.relation());
		System.out.println(tab(_tab) + "Then Body:");
		checkAndVisit(node.thenInstruction());
		System.out.println(tab(_tab) + "Else Body:");
		checkAndVisit(node.elseInstruction());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrOutput
	 */
	public void handleNodeInstrOutput(NodeInstrOutput node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrOutput:");
		checkAndVisit(node.expression());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrInputArray
	 */
	public void handleNodeInstrInputArray(NodeInstrInputArray node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrInputArray: " + node.identifier() + "[]");
		System.out.println(tab(_tab) + "NodeInstrInputArray Expr: ");
		checkAndVisit(node.index());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrInputSingle
	 */
	public void handleNodeInstrInputSingle(NodeInstrInputSingle node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrInputSingle: " + node.identifier());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrReturnExpr
	 */
	public void handleNodeInstrReturnExpr(NodeInstrReturnExpr node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrReturnExpr");
		System.out.println(tab(_tab) + "Expression:");
		checkAndVisit(node.expression());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrWhile
	 */
	public void handleNodeInstrWhile(NodeInstrWhile node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrWhile");
		System.out.println(tab(_tab) + "While Relation:");
		checkAndVisit(node.relation());
		System.out.println(tab(_tab) + "While Body:");
		checkAndVisit(node.instruction());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeInstrProcCall
	 */
	public void handleNodeInstrProcCall(NodeInstrProcCall node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeInstrProcCall: " + node.identifier());
		System.out.println(tab(_tab) + "Arguments:");
		checkAndVisit(node.arguments());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListInstrCons
	 */
	public void handleNodeListInstrCons(NodeListInstrCons node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListInstrCons");
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListInstrNil
	 */
	public void handleNodeListInstrNil(NodeListInstrNil node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListInstrNil\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListProcCons
	 */
	public void handleNodeListProcCons(NodeListProcCons node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListProcCons");
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListProcNil
	 */
	public void handleNodeListProcNil(NodeListProcNil node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListProcNil\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListProcDeclArgCons
	 */
	public void handleNodeListProcDeclArgCons(NodeListProcDeclArgCons node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListProcDeclArgCons");
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListProcDeclArgNil
	 */
	public void handleNodeListProcDeclArgNil(NodeListProcDeclArgNil node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListProcDeclArgNil\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListProcCallArgCons
	 */
	public void handleNodeListProcCallArgCons(NodeListProcCallArgCons node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListProcCallArgCons");
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListProcCallArgNil
	 */
	public void handleNodeListProcCallArgNil(NodeListProcCallArgNil node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListProcCallArgNil\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListVarDeclCons
	 */
	public void handleNodeListVarDeclCons(NodeListVarDeclCons node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListVarDeclCons");
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeListVarDeclNil
	 */
	public void handleNodeListVarDeclNil(NodeListVarDeclNil node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeListVarDeclNil\n");
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeProgram
	 */
	public void handleNodeProgram(NodeProgram node) throws VisitorException {
		System.out.println(tab(_tab) + "NodeProgram");
		checkAndVisit(node.listVarDecl());
		checkAndVisit(node.listProc());
	}
	
	/**
	 * Handles visiting a NodeRelationLessThan
	 */
	public void handleNodeRelationLessThan(NodeRelationLessThan node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationLessThan");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
		
	}
	
	/**
	 * Handles visiting a NodeRelationLessThanEqual
	 */
	public void handleNodeRelationLessThanEqual(NodeRelationLessThanEqual node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationLessThanEqual");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeRelationEqual
	 */
	public void handleNodeRelationEqual(NodeRelationEqual node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationEqual");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeRelationGreaterThanEqual
	 */
	public void handleNodeRelationGreaterThanEqual(NodeRelationGreaterThanEqual node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationGreaterThanEqual");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeRelationGreaterThan
	 */
	public void handleNodeRelationGreaterThan(NodeRelationGreaterThan node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationGreaterThan");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeRelationAnd
	 */
	public void handleNodeRelationAnd(NodeRelationAnd node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationAnd");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeRelationOr
	 */
	public void handleNodeRelationOr(NodeRelationOr node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationOr");
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_tab--;
	}
	
	/**
	 * Handles visiting a NodeRelationNot
	 */
	public void handleNodeRelationNot(NodeRelationNot node)
	throws VisitorException {
		_tab++;
		System.out.println(tab(_tab) + "NodeRelationNot");
		checkAndVisit(node.relation());
		_tab--;
	}
	
	public String tab(int amount) {
		String result = "";
		for (int x = 0; x < amount; x++)
			result = result + " ";
		return result;
	}
	
}
