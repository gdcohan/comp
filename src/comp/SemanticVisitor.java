package comp;

import comp.Scope.VarType;
import comp.nodes.*;
import java.util.*;

/**
 * Class that will check the semantics of the parsed syntax tree of a NodeProgram.
 * Semantic check includes:
 *    redefining of variables in the same scope
 *    using a single as an array or an array as a single
 *    using an undefined variable
 *    type analysis
 *    gathering variable usage information
 *    ensuring procedures are used correctly
 * <p>
 * Instantiate it and then have it visit a NodeProgram or any other Node.
 */
public class SemanticVisitor implements Visitor {
	//Your private variables here
	private Stack<Scope> _stackOfScopes; //stack to keep track of scopes
	
	//Other private variables
	private HashMap<String, Procedure> _procedures; //use this to gather information about Procedures
	private HashMap<String, Integer> _blockSize; //use this for global variables
	private String _currentProcedure; //you'll need to know what procedure you're in

	public SemanticVisitor(){
		
		_procedures = new HashMap<String, Procedure>();
		_blockSize = new HashMap<String, Integer>();
		_currentProcedure = "";
		_stackOfScopes = new Stack<Scope>();
	}

	public HashMap<String, Integer> getBlockSizes() {
		return _blockSize;
	}
	
	public HashMap<String, Procedure> getProcedures() {
		return _procedures;
	}

	//this is a helper method that will iterate through all the scopes and
	//check if there is a variable that has already been declared with that
	//identifier. If it finds a variable, it returns it's type, and if it
	//doesn't find a variable, it returns untyped.
	private VarType checkAllScopes(String id) throws VisitorException {
		for (int i = 0; i<_stackOfScopes.size(); i++) { //check each scope for variable with same name
			Scope _toCheck = _stackOfScopes.get(i);
			VarType _varType = _toCheck.lookup(id);
			if (_varType != VarType.UNTYPED) { //found variable, so return its type
				return _varType;
			}
		}
		return VarType.UNTYPED; //not already declared, so return untyped
	}
	
	//this was a helper method used to debug--it just printed the contents
	//of the scopes in the stack
//	private void printScopes() {
//		for (int i= 0; i<_stackOfScopes.size(); i++) {
//			System.out.println(_stackOfScopes.get(i).getVarList());
//		}
//	}

	private void checkAndVisit(Node node) throws VisitorException{
        if (node != null)
            node.visit(this);
        else throw new VisitorException("This is not supposed to happen, but a node is missing something");
	}
	
	private void die(String msg) throws VisitorException {
		throw new SemanticException(msg);
	}

	//this method will visit the listProc and listVarDecl of the NodeProgram. After,
	//it will check to make sure that there was a main procedure and that 
	//the main procedure took no arguments. it also makes the global variables
	//the first scope on the scope stack
	public void handleNodeProgram(NodeProgram node) throws VisitorException {
		Scope _newScope = new Scope(); //make a new scope
		_stackOfScopes.push(_newScope); //push the scope onto the stack
		checkAndVisit(node.listVarDecl());
		checkAndVisit(node.listProc());
		Procedure main = _procedures.get("main");
		if (main != null) {
			if (!main.getArgList().isEmpty()) {
				die("main must take no arguments"); //main had arguments->error
			}
		} else {
			die("must have main procedure");//no main procedure-->empty
		}	
		_stackOfScopes.pop(); //pop stack off the scope
	}
	
	//no modifications
	public void handleNodeListVarDeclCons(NodeListVarDeclCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	//handles array global variable. checks to make sure a global variable with 
	//the same name has not already been declared--if it has, it throws an error; if
	//it hasn't it puts the new global variable in _blockSize
	public void handleNodeVarDeclArray(NodeVarDeclArray node) throws VisitorException {
		String id = node.identifier();
		int size = node.size();
		if (_blockSize.containsKey(id)) {
			die("already declared a global variable with identifier: " + id);
		} else {
			_blockSize.put(id, size);
			_stackOfScopes.peek().insert(id, VarType.ARRAY); //also insert into current scope
		}
	}

	//handles single global variable. checks to make sure a a global variable with the 
	//same name has not already been declared--if it has, it throws an error; if it 
	//hasn't, it puts the new global variable in _blockSize
	public void handleNodeVarDeclSingle(NodeVarDeclSingle node) throws VisitorException {
		String id = node.identifier();
		if (_blockSize.containsKey(id)) {
			die("already declared a global variable with identifier: " + id);
		} else {
			_blockSize.put(id, 1); 
			_stackOfScopes.peek().insert(id, VarType.SINGLE); //also insert into current stack
		}
	}
	
	public void handleNodeListVarDeclNil(NodeListVarDeclNil node) throws VisitorException {
		// Nothing to do
	}
	
	//no modifications
	public void handleNodeListProcCons(NodeListProcCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	//this handles a procedure. It first makes a new scope for the procedure and pushes
	//it onto the stack. It also instantiates a new procedure to add to the procedure map
	//first checking to see if that procedure has already been declared. It then visits
	//the list of arguments and list of instructions before calling finish() on the 
	//procedure
	public void handleNodeProc(NodeProc node) throws VisitorException {
		Scope _newScope = new Scope(); //make a new scope
		_stackOfScopes.push(_newScope); //push the scope onto the stack
		NodeListProcDeclArg _argList = node.argumentList(); //get args from node
		NodeInstr _instructions = node.instruction(); //get instruction field
		String id = node.identifier();
		_currentProcedure = id; //set currentProcedure to the name of the procedure so that args can access it by name
		List<String> arglist = new ArrayList<String>(); //instantiate new null list to make a new procedure object
		Procedure _newProcedure = new Procedure(id, arglist); //make new procedure
		if (_procedures.containsKey(id)) {
			die("A procedures with the name " + id + " has already been declared");
		} else {
			_procedures.put(id, _newProcedure); //add procedure to _procedures
		}
		checkAndVisit(_argList); //visit arg list
		checkAndVisit(_instructions); //visit instructions
		_stackOfScopes.pop(); //pop scope off the stack
		Procedure _thisProcedure = _procedures.get(id);
		_thisProcedure.finish(); //call finish on the procedure
	}
	
	public void handleNodeListProcNil(NodeListProcNil node) throws VisitorException {
		// Nothing to do
	}
	
	//no modifications
	public void handleNodeListProcDeclArgCons(NodeListProcDeclArgCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	public void handleNodeListProcDeclArgNil(NodeListProcDeclArgNil node) throws VisitorException {
		// Nothing to do
	}
	
	//no modifications
	public void handleNodeListProcCallArgCons(NodeListProcCallArgCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	public void handleNodeListProcCallArgNil(NodeListProcCallArgNil node) throws VisitorException {
		// Nothing to do
	}
	
	//this method handles a return expression. It checks to make sure that 
	//we are returning an int and then visits that return
	public void handleNodeInstrReturnExpr(NodeInstrReturnExpr node) throws VisitorException {
		NodeExpr _return = node.expression();
		SemanticType _type = _return.semanticType();
		if (_type != SemanticType.INTEGER) {
			die("must return an int");
		}
		checkAndVisit(_return);
	}
	
	//this method handles procedure calls that are instructions in a procedure.
	//It makes sure that 1) procedure has been declared 2) number of args is correct
	//and 3) all args are ints
	public void handleNodeInstrProcCall(NodeInstrProcCall node) throws VisitorException {
		String _id = node.identifier();
		//check that procedure has been declared:
		Procedure _thisProcedure = _procedures.get(_id);
		if (_thisProcedure == null) {
			die(_id + " has not yet been declared");
		}
		List<String> _procedureArgs = _thisProcedure.getArgList();
		NodeListProcCallArg _nodeArgs = node.arguments();
		int counter = 0; //will track how many arguments were passed
		while (_nodeArgs instanceof NodeListProcCallArgCons) { //while we don't have a nil
			if (((NodeListProcCallArgCons) _nodeArgs).head().expression().semanticType() 
					!= SemanticType.INTEGER) { //check that arg is an integer
				die("procedure arguments must be integers");
			} else {
				_nodeArgs = ((NodeListProcCallArgCons) _nodeArgs).tail(); //recur on tail
				counter += 1; //and increment counter
			}
		}
		if (counter != _procedureArgs.size()) {
			die(_id + " requires " + _procedureArgs.size() + " arguments");
		}
		checkAndVisit(node.arguments());
	}
	
	//this method handles a declaration of an array inside an instruction.
	//it first checks to make sure that the array hasn't been declared already--if it 
	//has, it throws an error, and if it hasn't, it inserts the variable in the scope
	//and in the procedure
	public void handleNodeInstrDeclArray(NodeInstrDeclArray node) throws VisitorException {
		String _id = node.identifier();
		int _size = node.size();
		VarType _varType = checkAllScopes(_id);
		//if the variable hasn't been declared, insert it into the current scope and
		//current procedure
		if (_varType == VarType.UNTYPED) {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			_thisProcedure.insert(_id, _size);
			Scope _thisScope = _stackOfScopes.peek();
			_thisScope.insert(_id, VarType.ARRAY);
		} else { 
			die(_id + " has already been declared");
		}	
	}

	//this method handles a declaration of a single inside an instruction.
	//it first checks to make sure that the single hasn't been declared already--if it 
	//has, it throws an error, and if it hasn't, it inserts the variable in the scope
	//and in the procedure
	public void handleNodeInstrDeclSingle(NodeInstrDeclSingle node) throws VisitorException {
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		//variable hasn't already been declared, so insert it into the current scope and
		//current procedure
		if (_varType == VarType.UNTYPED) {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			_thisProcedure.insert(_id, 1);
			Scope _thisScope = _stackOfScopes.peek();
			_thisScope.insert(_id, VarType.SINGLE);
		} else { 
			die(_id + " has already been declared");
		}	
	}
	
	//no modifications
	public void handleNodeProcCallArg(NodeProcCallArg node) throws VisitorException {
		checkAndVisit(node.expression());
	}
	
	
	//This method will handle an argument of a procedure.
	//It will insert the argument into the current procedures
	//list of arguments, giving it size one. It will 
	//also insert into the scope
	public void handleNodeProcDeclArg(NodeProcDeclArg node) throws VisitorException {
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.UNTYPED) {
			Procedure _thisProcedure = _procedures.get(_currentProcedure); 
			_thisProcedure.insert(_id,1); //insert into procedures
			Scope _thisScope = _stackOfScopes.peek();
			_thisScope.insert(_id, VarType.SINGLE); //insert into scope
			_thisProcedure.getArgList().add(_id); //add argument to list of args
		} else {
			die(_id + " has already been declared as a variable"); 
		}
	}


	//this method handles an array used in an express
	//it checks to make sure it is an array and that
	//it has been declared
	public void handleNodeExprArrayVar(NodeExprArrayVar node) throws VisitorException {
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.UNTYPED) {
			die("must declare " + _id + " before referencing it");
		} else if (_varType == VarType.SINGLE) {
			die(_id + "must be an array, not a single");
		}
	}

	public void handleNodeExprInt(NodeExprInt node) throws VisitorException {
		// Nothing to do
	}
	
	public void handleNodeExprTrue(NodeExprTrue node) throws VisitorException {
		// Nothing to do
	}
	
	public void handleNodeExprFalse(NodeExprFalse node) throws VisitorException {
		// Nothing to do
	}
	
	//this method handles a procedure call within another procedure
	//It checks: 1) all args have been declared 2) correct number of args
	//3) all args are ints 
	public void handleNodeExprProcCall(NodeExprProcCall node) throws VisitorException {
		String _id = node.identifier();
		//check that procedure has been declared:
		Procedure _thisProcedure = _procedures.get(_id);
		if (_thisProcedure == null) {
			die(_id + " has not yet been declared");
		}
		List<String> _procedureArgs = _thisProcedure.getArgList();
		NodeListProcCallArg _nodeArgs = node.arguments();
		int counter = 0; //will track how many arguments were passed
		while (_nodeArgs instanceof NodeListProcCallArgCons) { //while we don't have a nil node
			if (((NodeListProcCallArgCons) _nodeArgs).head().expression().semanticType() 
					!= SemanticType.INTEGER) { //check that argument is an int
				die("procedure arguments must be integers");
			} else {
				_nodeArgs = ((NodeListProcCallArgCons) _nodeArgs).tail(); //recur on tail
				counter += 1; //increment counter
			}
		}
		if (counter != _procedureArgs.size()) {
			die(_id + " requires " + _procedureArgs.size() + " arguments");
		}
		checkAndVisit(node.arguments());
	}

	//checks to make sure both arguments are integers
	public void handleNodeExprMinus(NodeExprMinus node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != _right.semanticType() || _left.semanticType() != SemanticType.INTEGER) {
			die("subtraction requires two integer arguments");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//checks to make sure both arguments are integers
	public void handleNodeExprPlus(NodeExprPlus node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != _right.semanticType() || _left.semanticType() != SemanticType.INTEGER) {
			die("addition requires two integer arguments");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//checks to make sure both arguments are integers
	public void handleNodeExprTimes(NodeExprTimes node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != _right.semanticType() || _left.semanticType() != SemanticType.INTEGER) {
			die("multiplication requires two integer arguments");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//checks to make sure both arguments are integers
	public void handleNodeExprDivide(NodeExprDivide node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != _right.semanticType() || _left.semanticType() != SemanticType.INTEGER) {
			die("division requires two integer arguments");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//checks to make sure both arguments are integers
	public void handleNodeExprMod(NodeExprMod node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != _right.semanticType() || _left.semanticType() != SemanticType.INTEGER) {
			die("modulus requires two integer arguments");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//checks to make sure the variable has been declared and that
	//it is a single
	public void handleNodeExprVar(NodeExprVar node) throws VisitorException {
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.ARRAY) {
			die(_id + " must be a single, not array");
		} else if (_varType == VarType.UNTYPED) {
			die(_id + " has not been declared yet");
		}
	}

	//checks that the array has been declared, that it is an array, and 
	//that it is being assigned an integer, and that the index is an integer
	public void handleNodeInstrAssignArray(NodeInstrAssignArray node) throws VisitorException {
		NodeExpr _expr = node.expression();
		NodeExpr _index = node.index();
		if (_expr.semanticType() != SemanticType.INTEGER
				|| _index.semanticType() != SemanticType.INTEGER) {
			die("expected to assign an integer");
		}
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.SINGLE) {
			die("expected assignment to an array");
		} else if (_varType == VarType.UNTYPED) {
			die(_id + " is undeclared");
		}
		checkAndVisit(_expr);
		checkAndVisit(_index);
	}

	//checks that single has been declared, that is is a single,
	//and that it is being assigned an integer
	public void handleNodeInstrAssignSingle(NodeInstrAssignSingle node) throws VisitorException {
		NodeExpr _expr = node.expression();
		if (_expr.semanticType() != SemanticType.INTEGER) {
			die("expected integer assignment");
		}
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.ARRAY) {
			die(_id + " needs to be a single");
		} else if (_varType == VarType.UNTYPED) {
			die(_id + " is undeclared");
		}
		checkAndVisit(_expr);
	}

	//this will check a node instruction block by visiting its list
	//of instructions
	public void handleNodeInstrBlock(NodeInstrBlock node) throws VisitorException {
		Scope _newScope = new Scope();
		_stackOfScopes.push(_newScope); //make new scope
		checkAndVisit(node.instructions());
		_stackOfScopes.pop();	
	}

	//must check that relation is a boolean and then visit the instruction
	public void handleNodeInstrIfthen(NodeInstrIfthen node) throws VisitorException {
		NodeExpr _relation = node.relation();
		if (_relation.semanticType() != SemanticType.BOOLEAN) {
			die("expected boolean relation");
		}
		Scope _newScope = new Scope();
		_stackOfScopes.push(_newScope); //make new scope
		NodeInstr _instruction = node.thenInstruction();
		checkAndVisit(_instruction);
		_stackOfScopes.pop();
	}

	//must check that relation is a boolean and then visit the
	//then instruction and else instruction
	public void handleNodeInstrIfthenelse(NodeInstrIfthenelse node) throws VisitorException {
		NodeExpr _relation = node.relation();
		if (_relation.semanticType() != SemanticType.BOOLEAN) {
			die("expected boolean relation");
		}
		Scope _newScope = new Scope();
		_stackOfScopes.push(_newScope); //make new scope
		NodeInstr _thenInstruction = node.thenInstruction();
		NodeInstr _elseInstruction = node.elseInstruction();
		checkAndVisit(_thenInstruction);
		checkAndVisit(_elseInstruction);
		_stackOfScopes.pop();
	}

	//must check that output is an integer and that it's been declared if
	//if it is a variable
	public void handleNodeInstrOutput(NodeInstrOutput node) throws VisitorException {
		NodeExpr _expr = node.expression();
		if (_expr.semanticType() != SemanticType.INTEGER) {
			die("expected integer output");
		}
		checkAndVisit(_expr);
	}

	//must check that the index is an integer that accesses an index in an
	//array that has been declared
	public void handleNodeInstrInputArray(NodeInstrInputArray node) throws VisitorException {
//		printScopes();
		String _id = node.identifier();
		NodeExpr _index = node.index();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.SINGLE) {
			die(_id + " must be an array");
		} else if (_varType == VarType.UNTYPED) {
			die(_id + " is undeclared");
		}
		if (_index.semanticType() != SemanticType.INTEGER) {
			die("must access integer index in " + _id);
		}
		checkAndVisit(_index);
	}

	//must check that the single has been declared as an integer
	public void handleNodeInstrInputSingle(NodeInstrInputSingle node) throws VisitorException {
		String _id = node.identifier();
		VarType _varType = checkAllScopes(_id);
		if (_varType == VarType.ARRAY) {
			die("must input an integer");
		} else if (_varType == VarType.UNTYPED) {
			die(_id + " is undeclared");
		}
	}

	//must check that relation is a boolean and visit instructions
	public void handleNodeInstrWhile(NodeInstrWhile node) throws VisitorException {
		NodeInstr _instruction = node.instruction();
		NodeExpr _relation = node.relation();
		if (_relation.semanticType() != SemanticType.BOOLEAN) {
			die("relation must be a boolean");
		}
		Scope _newScope = new Scope();
		_stackOfScopes.push(_newScope); //make new scope
		checkAndVisit(_instruction);
		_stackOfScopes.pop();
	}

	//no modifications
	public void handleNodeListInstrCons(NodeListInstrCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}

	public void handleNodeListInstrNil(NodeListInstrNil node) throws VisitorException {
		// Nothing to do
	}

	//must check that both left and right children are integers and then
	//visit them
	public void handleNodeRelationLessThan(NodeRelationLessThan node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.INTEGER
				|| _right.semanticType() != SemanticType.INTEGER) {
			die("must compare integers");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that both left and right children are integers and then
	//visit them
	public void handleNodeRelationLessThanEqual(NodeRelationLessThanEqual node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.INTEGER
				|| _right.semanticType() != SemanticType.INTEGER) {
			die("must compare integers");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that both left and right children are integers and then
	//visit them
	public void handleNodeRelationEqual(NodeRelationEqual node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.INTEGER
				|| _right.semanticType() != SemanticType.INTEGER) {
			die("must compare integers");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that both left and right children are integers and then
	//visit them
	public void handleNodeRelationGreaterThanEqual(NodeRelationGreaterThanEqual node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.INTEGER
				|| _right.semanticType() != SemanticType.INTEGER) {
			die("must compare integers");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that both left and right children are integers and then
	//visit them
	public void handleNodeRelationGreaterThan(NodeRelationGreaterThan node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.INTEGER
				|| _right.semanticType() != SemanticType.INTEGER) {
			die("must compare integers");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that left and right children are booleans and then
	//visit them
	public void handleNodeRelationAnd(NodeRelationAnd node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.BOOLEAN
				|| _right.semanticType() != SemanticType.BOOLEAN) {
			die("AND requires booleans");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that left and right children are booleans and then
	//visit them
	public void handleNodeRelationOr(NodeRelationOr node) throws VisitorException {
		NodeExpr _left = node.leftChild();
		NodeExpr _right = node.rightChild();
		if (_left.semanticType() != SemanticType.BOOLEAN
				|| _right.semanticType() != SemanticType.BOOLEAN) {
			die("OR requires booleans");
		}
		checkAndVisit(_left);
		checkAndVisit(_right);
	}

	//must check that relation is boolean and then visit it
	public void handleNodeRelationNot(NodeRelationNot node) throws VisitorException {
		NodeExpr _relation = node.relation();
		if (_relation.semanticType() != SemanticType.BOOLEAN) {
			die("NOT requires a boolean");
		}
		checkAndVisit(_relation);
	}
}
