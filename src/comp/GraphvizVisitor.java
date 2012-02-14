package comp;

import comp.nodes.*;
import java.io.*;
import java.util.HashMap;

/**
 * Class that will print out the parsed syntax tree of a NodeProgram.
 * <p>
 * Instantiate it and then have it visit a NodeProgram or any other Node.
 */
public class GraphvizVisitor implements Visitor {

	private BufferedWriter out;
	private int nodeCounter;
	private HashMap<Node, String> nodeIdentifierMap;

	public GraphvizVisitor(Writer o) {
		nodeCounter = 1;
		nodeIdentifierMap = new HashMap<Node, String>();
		try {
			out = new BufferedWriter (o);
			out.write("digraph parse_tree {");
			out.newLine();
			out.write("node [color=lightblue2, style=filled];");
			out.newLine();
		}
		catch (IOException e){
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public GraphvizVisitor (String filepath){
		nodeCounter = 1;
		nodeIdentifierMap = new HashMap<Node, String>();
		try {
			out = new BufferedWriter (new FileWriter (filepath));
			out.write("digraph parse_tree {");
			out.newLine();
			out.write("node [color=lightblue2, style=filled];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Finishes writing the graphviz file and closes the output stream
	 **/
	public void finish (){
		try {
			out.write("}");
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Checks to make sure the node exists, and if so, visits it.
	 */
	private void checkAndVisit(Node node) throws VisitorException{
		if (node != null)
			node.visit(this);
	}
	
	/**
	 * @return The identifier that will be used to identify the given node in the graphviz output file
	 */
	public String getIdentifier (Node node){
		if(nodeIdentifierMap.containsKey(node))
			return nodeIdentifierMap.get(node);
		else{
			String nodeName = "N" + nodeCounter++;
			nodeIdentifierMap.put(node, nodeName);
			return nodeName;
		}
	}
	
	/**
	 * Handles visiting a NodeInstrDeclArray
	 * @throws IOException 
	 */
	public void handleNodeInstrDeclArray(NodeInstrDeclArray node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrDeclArray: " + node.identifier()
					+ " [" + node.size() + "]\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeProc
	 * @throws IOException 
	 */
	public void handleNodeProc(NodeProc node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeProc: " + node.identifier()
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.argumentList()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.instruction()) + ";");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		checkAndVisit(node.argumentList());
		checkAndVisit(node.instruction());
	}
	
	/**
	 * Handles visiting a NodeInstrDeclSingle
	 */
	public void handleNodeInstrDeclSingle(NodeInstrDeclSingle node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrDeclSingle: " + node.identifier()
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeVarDeclArray
	 * @throws IOException 
	 */
	public void handleNodeVarDeclArray(NodeVarDeclArray node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeVarDeclArray: " + node.identifier()
					+ " [" + node.size() + "]\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeVarDeclSingle
	 */
	public void handleNodeVarDeclSingle(NodeVarDeclSingle node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeVarDeclSingle: " + node.identifier()
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Handles visiting a NodeExprArrayVar
	 */
	public void handleNodeExprArrayVar(NodeExprArrayVar node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprArrayVar: " + node.identifier()
					+ "[]\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.index()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.index());
	}

	/**
	 * Handles visiting a NodeExprInt
	 */
	public void handleNodeExprInt(NodeExprInt node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprInt: " + node.value()
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeExprTrue
	 */
	public void handleNodeExprTrue(NodeExprTrue node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprTrue" + "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeExprFalse
	 */
	public void handleNodeExprFalse(NodeExprFalse node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprFalse" + "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Handles visiting a NodeExprMinus
	 */
	public void handleNodeExprMinus(NodeExprMinus node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprMinus"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeExprPlus
	 */
	public void handleNodeExprPlus(NodeExprPlus node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprPlus"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeExprTimes
	 */
	public void handleNodeExprTimes(NodeExprTimes node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprTimes"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeExprDivide
	 */
	public void handleNodeExprDivide(NodeExprDivide node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprDivide"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}
	
	/**
	 * Handles visiting a NodeExprMod
	 */
	public void handleNodeExprMod(NodeExprMod node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprMod"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeExprVar
	 */
	public void handleNodeExprVar(NodeExprVar node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprVar: " + node.identifier()
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeInstrReturnExpr
	 */
	public void handleNodeInstrReturnExpr(NodeInstrReturnExpr node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrReturnExpr" + "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.expression()) + ";");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.expression());
	}
	
	/**
	 * Handles visiting a NodeInstrProcCall
	 */
	public void handleNodeInstrProcCall(NodeInstrProcCall node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrProcCall: " + node.identifier() + "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.arguments()) + ";");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.arguments());
	}	
	
	/**
	 * Handles visiting a NodeExprProcCall
	 */
	public void handleNodeExprProcCall(NodeExprProcCall node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeExprProcCall: " + node.identifier() + "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.arguments()) + ";");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.arguments());
	}
	
	/**
	 * Handles visiting a NodeProcCallArg
	 */
	public void handleNodeProcCallArg(NodeProcCallArg node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeProcCallArg"+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.expression()) + ";");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.expression());
	}
	
	/**
	 * Handles visiting a NodeProcDeclArg
	 */
	public void handleNodeProcDeclArg(NodeProcDeclArg node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeProcDeclArg: "+ node.identifier() + "\"];");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Handles visiting a NodeInstrAssignArray
	 */
	public void handleNodeInstrAssignArray(NodeInstrAssignArray node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrAssignArray: " + node.identifier()
					+ "[]\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.index()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.expression()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		//System.out.println("AssignArray Index:");
		checkAndVisit(node.index());
		//System.out.println("AssignArray Expr:");
		checkAndVisit(node.expression());
	}

	/**
	 * Handles visiting a NodeInstrAssignSingle
	 */
	public void handleNodeInstrAssignSingle(NodeInstrAssignSingle node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrAssignSingle: " + node.identifier()
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.expression()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.expression());
	}

	/**
	 * Handles visiting a NodeInstrBlock
	 */
	public void handleNodeInstrBlock(NodeInstrBlock node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrBlock"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.instructions()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		//System.out.println("Block Body:");
		checkAndVisit(node.instructions());
	}

	/**
	 * Handles visiting a NodeInstrIfthen
	 */
	public void handleNodeInstrIfthen(NodeInstrIfthen node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrIfthen"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.relation()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.thenInstruction()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		//System.out.println("If Relation:");
		checkAndVisit(node.relation());
		//System.out.println("Then Body:");
		checkAndVisit(node.thenInstruction());
	}

	/**
	 * Handles visiting a NodeInstrIfthenelse
	 */
	public void handleNodeInstrIfthenelse(NodeInstrIfthenelse node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrIfthenelse"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.relation()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.thenInstruction()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.elseInstruction()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		//System.out.println("If Relation:");
		checkAndVisit(node.relation());
		//System.out.println("Then Body:");
		checkAndVisit(node.thenInstruction());
		//System.out.println("Else Body:");
		checkAndVisit(node.elseInstruction());
	}

	/**
	 * Handles visiting a NodeInstrOutput
	 */
	public void handleNodeInstrOutput(NodeInstrOutput node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrOutput"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.expression()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.expression());
	}
	
	/**
	 * Handles visiting a NodeInstrInputArray
	 */
	public void handleNodeInstrInputArray(NodeInstrInputArray node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrInputArray: " + node.identifier()
					+ "[]\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.index()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.index());
	}
	
	/**
	 * Handles visiting a NodeInstrInputSingle
	 */
	public void handleNodeInstrInputSingle(NodeInstrInputSingle node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrInputSingle: " + node.identifier()
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Handles visiting a NodeInstrWhile
	 */
	public void handleNodeInstrWhile(NodeInstrWhile node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeInstrWhile"
					+ "\"];");
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.relation()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.instruction()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		//System.out.println("While Relation:");
		checkAndVisit(node.relation());
		//System.out.println("While Body:");
		checkAndVisit(node.instruction());
	}

	/**
	 * Handles visiting a NodeListInstrCons
	 */
	public void handleNodeListInstrCons(NodeListInstrCons node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListInstrCons"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.head()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.tail()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}

	/**
	 * Handles visiting a NodeListInstrNil
	 */
	public void handleNodeListInstrNil(NodeListInstrNil node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListInstrNil "
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeListProcCons
	 */
	public void handleNodeListProcCons(NodeListProcCons node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListProcCons"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.head()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.tail()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	/**
	 * Handles visiting a NodeListProcNil
	 */
	public void handleNodeListProcNil(NodeListProcNil node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListProcNil "
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeListVarDeclCons
	 */
	public void handleNodeListVarDeclCons(NodeListVarDeclCons node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListVarDeclCons"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.head()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.tail()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	/**
	 * Handles visiting a NodeListVarDeclNil
	 */
	public void handleNodeListVarDeclNil(NodeListVarDeclNil node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListVarDeclNil "
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeListProcDeclArgCons
	 */
	public void handleNodeListProcDeclArgCons(NodeListProcDeclArgCons node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListProcDeclArgCons"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.head()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.tail()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	/**
	 * Handles visiting a NodeListProcDeclArgNil
	 */
	public void handleNodeListProcDeclArgNil(NodeListProcDeclArgNil node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListProcDeclArgNil "
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Handles visiting a NodeListProcCallArgCons
	 */
	public void handleNodeListProcCallArgCons(NodeListProcCallArgCons node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListProcCallArgCons"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.head()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.tail()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	/**
	 * Handles visiting a NodeListProcCallArgNil
	 */
	public void handleNodeListProcCallArgNil(NodeListProcCallArgNil node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeListProcCallArgNil "
					+ "\"];");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Handles visiting a NodeProgram
	 */
	public void handleNodeProgram(NodeProgram node) throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeProgram"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.listVarDecl()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.listProc()) + ";");
			out.newLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.listVarDecl());
		checkAndVisit(node.listProc());
	}

	/**
	 * Handles visiting a NodeRelationLessThan
	 */
	public void handleNodeRelationLessThan(NodeRelationLessThan node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationLessThan"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeRelationLessThanEqual
	 */
	public void handleNodeRelationLessThanEqual(NodeRelationLessThanEqual node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationLessThanEqual"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}
	
	/**
	 * Handles visiting a NodeRelationEqual
	 */
	public void handleNodeRelationEqual(NodeRelationEqual node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationEqual"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeRelationGreaterThanEqual
	 */
	public void handleNodeRelationGreaterThanEqual(NodeRelationGreaterThanEqual node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationGreaterThanEqual"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeRelationGreaterThan
	 */
	public void handleNodeRelationGreaterThan(NodeRelationGreaterThan node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationGreaterThan"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeRelationAnd
	 */
	public void handleNodeRelationAnd(NodeRelationAnd node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationAnd"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeRelationOr
	 */
	public void handleNodeRelationOr(NodeRelationOr node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationOr"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.leftChild()) + ";");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.rightChild()) + ";");
			out.newLine();	
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
	}

	/**
	 * Handles visiting a NodeRelationNot
	 */
	public void handleNodeRelationNot(NodeRelationNot node)
	throws VisitorException {
		try {
			out.write(getIdentifier(node) + "[label=\"NodeRelationNot"
					+ "\"];");
			out.newLine();
			
			out.write(getIdentifier(node) + " -> " + getIdentifier(node.relation()) + ";");
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		checkAndVisit(node.relation());
	}
}
