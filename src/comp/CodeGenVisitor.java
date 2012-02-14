package comp;

import comp.nodes.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Visit a parsed syntax tree from a {@link NodeProgram} and generate MIPS
 * code. It makes use of the {@link MIPSCodeGenerator} for the actual creation
 * of MIPS code.
 *
 * @version Fall 2007
 * @author The TAs
 */
public class CodeGenVisitor implements Visitor {
	
	private MIPSCodeGenerator _codegen;
	private HashMap<String,Integer> _blockSizes; //Represents the global variables
	private HashMap<String, Procedure> _procedures;
	private String _currentProcedure = "main"; //needed for accessing variables in _procedures

    /**
     * Create a new <code>CodeGenVisitor</code>. Uses the given {@link
     * MIPSCodeGenerator} to help generate code.
     *
     * @param codegen The <code>MIPSCodeGenerator</code> to use when outputting
     * code.
     */
	
	//I have modified this method to also generate the data section.
	//It iterates through the list of global variables
	//and declares them in the data section
	public CodeGenVisitor(MIPSCodeGenerator codegen, HashMap<String,Integer> blockSizes,
								 HashMap<String, Procedure> procedures){
		_codegen = codegen;
		_blockSizes = blockSizes;
		_procedures = procedures;
		Set<String> _globalVars = _blockSizes.keySet(); 
		Iterator<String> _iterator = _globalVars.iterator(); //get iterator for global variables
		_codegen.genData();
		while (_iterator.hasNext()) {
			String _nextVar = _iterator.next();
			Integer _sizeNextVar = _blockSizes.get(_nextVar);
			if (_sizeNextVar == 1) {
				_codegen.genDecl(_nextVar); //if size is one, declare single
			} else {
				_codegen.genArrayDecl(_sizeNextVar, _nextVar); //else, declare array 
			}
		}
		_codegen.genText();	
	}
	
	private void checkAndVisit(Node node) throws VisitorException{
        if (node != null)
            node.visit(this);
        else throw new VisitorException("This is not supposed to happen, but a node is missing something");
	}
	
	public void handleNodeVarDeclSingle(NodeVarDeclSingle node) throws VisitorException {
		// Nothing to do
	}

	public void handleNodeVarDeclArray(NodeVarDeclArray node) throws VisitorException {
		// Nothing to do
	}
	
	public void handleNodeInstrDeclArray(NodeInstrDeclArray node) throws VisitorException {
		// Nothing to do
	}

	public void handleNodeInstrDeclSingle(NodeInstrDeclSingle node) throws VisitorException {
		// Nothing to do
	}

	
	//This will generate code for accessing an array in an expression.
	//It first visits the index to determine the offset from the 
	//start of the array. If the array is globally declared, the method
	//just pushes the value that is at the label for the array offset
	//by this index. If the array is not globally declared, the method
	//looks up the array in the procedure and pushes the value at the frame-pointer
	// offset of the offset of the array minus the index of the array.
	public void handleNodeExprArrayVar(NodeExprArrayVar node) throws VisitorException {
		String _id = node.identifier();
		NodeExpr _index = node.index();
		checkAndVisit(_index);
		_codegen.genPop(Register.T0); //get index for array
		_codegen.genLoadI(Register.T1, 4);
		_codegen.genTimes(Register.T0, Register.T1);
		if (_blockSizes.containsKey(_id)) { //was a global variable, so just use label
			_codegen.genLoadIndirect(Register.T1, _id, Register.T0);
			_codegen.genPush(Register.T1);
		} else {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			int offset = _thisProcedure.lookup(_id); //not a global variable, so do
			_codegen.genLoadI(Register.T4, offset);  //some register arithmetic to
			_codegen.genPlus(Register.T4, Register.T0); //get the right value to load from
			_codegen.genPlus(Register.T4, Register.FP);
			_codegen.genLoadFromLocation(Register.T1, Register.T4);
			_codegen.genPush(Register.T1);
		}
	}
	

	//This will generate code for accessing a variable in an expression.
	//If the variable is globally declared, the method
	//just pushes the value that is at the label 
	// If the variable is not globally declared, the method
	//looks up the variable in the procedure and pushes the value at
	//the frame pointer by the offset location of the variable.
	public void handleNodeExprVar(NodeExprVar node) throws VisitorException {
		String _variableName = node.identifier();
		if (_blockSizes.containsKey(_variableName)) { //was global, so use label
			_codegen.genLoad(Register.T0, _variableName);
			_codegen.genPush(Register.T0);
		} else {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			int offset = _thisProcedure.lookup(_variableName); //not global, so use offset
			_codegen.genLoadWithOffset(Register.T0, offset, Register.FP);
			_codegen.genPush(Register.T0);
		}
	}

	//This method takes a value of an integer and pushes it to the stack
	public void handleNodeExprInt(NodeExprInt node) throws VisitorException {
		int value = node.value();
		_codegen.genLoadI(Register.S0, value);
		_codegen.genPush(Register.S0);
	}
	
	//This method visits both children of the minus node, pops them from the stack,
	//performs a minus operation on them, and then pushes the result to the stack
	public void handleNodeExprMinus(NodeExprMinus node) throws VisitorException {
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_codegen.genPop(Register.A0);
		_codegen.genPop(Register.A1);
		_codegen.genMinus(Register.A1, Register.A0);
		_codegen.genPush(Register.A1);
	}

	//This method visits both children of the plus node, pops them from the stack,
	//performs a plus operation on them, and then pushes the result to the stack
	public void handleNodeExprPlus(NodeExprPlus node) throws VisitorException {
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_codegen.genPop(Register.A0);
		_codegen.genPop(Register.A1);
		_codegen.genPlus(Register.A1, Register.A0);
		_codegen.genPush(Register.A1);
	}

	//This method visits both children of the times node, pops them from the stack,
	//performs a multiply operation on them, and then pushes the result to the stack
	public void handleNodeExprTimes(NodeExprTimes node) throws VisitorException {
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_codegen.genPop(Register.A0); 
		_codegen.genPop(Register.A1);
		_codegen.genTimes(Register.A0, Register.A1);
		_codegen.genPush(Register.A0);
	}

	//This method visits both children of the divide node, pops them from the stack,
	//performs a division operation on them, and then pushes the result to the stack
	public void handleNodeExprDivide(NodeExprDivide node) throws VisitorException {
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_codegen.genPop(Register.A0); 
		_codegen.genPop(Register.A1);
		_codegen.genDivide(Register.A1, Register.A0);
		_codegen.genPush(Register.A1);
	}

	//This method visits both children of the mod node, pops them from the stack,
	//performs a mod operation on them, and then pushes the result to the stack
	public void handleNodeExprMod(NodeExprMod node) throws VisitorException {
		checkAndVisit(node.leftChild());
		checkAndVisit(node.rightChild());
		_codegen.genPop(Register.A0); 
		_codegen.genPop(Register.A1);
		_codegen.genMod(Register.A1, Register.A0);
		_codegen.genPush(Register.A1);
	}

	

	//This will generate code for assigning a value to an array.
	//It first visits the index to determine the offset from the 
	//start of the array. If the array is globally declared, the method
	//just stores the value of the expression at the array offset 
	//by the index. If the array is not globally declared, the method
	//looks up the array in the procedure and stores the value of the 
	//expression at the frame pointer offset by the array offset minus
	//the index of the array. 
	public void handleNodeInstrAssignArray(NodeInstrAssignArray node) throws VisitorException {
		String _id = node.identifier();
		NodeExpr _index = node.index();
		checkAndVisit(_index); //visit index
		checkAndVisit(node.expression()); //visit expression
		_codegen.genPop(Register.T1); //pop expression
		_codegen.genPop(Register.T0); //pop index
		_codegen.genLoadI(Register.T2, 4);
		_codegen.genTimes(Register.T0, Register.T2);
		if (_blockSizes.containsKey(_id)) { //global variable, so use label
			_codegen.genStoreIndirect(Register.T1, _id, Register.T0);
		} else {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			int offset = _thisProcedure.lookup(_id); //local variable, so use offset from fp
			_codegen.genLoadI(Register.T4, offset);
			_codegen.genPlus(Register.T4, Register.T0);
			_codegen.genPlus(Register.T4, Register.FP);
			_codegen.genStoreToLocation(Register.T1, Register.T4);
		}
	}
	

	//This method handles assigning values to variables. If the variable
	//is globally declared, the method just stores the value of the expression
	//at the label. If the variable isn't globally declared, the method
	//stores the value of the expression at the offset of the variable
	//from the frame-pointer
	public void handleNodeInstrAssignSingle(NodeInstrAssignSingle node) throws VisitorException {
		String _id = node.identifier();
		NodeExpr _expr = node.expression();
		checkAndVisit(_expr);
		_codegen.genPop(Register.S0); //contains value of the expression
		if (_blockSizes.containsKey(_id)) {
			_codegen.genStore(Register.S0, _id); //store value to label
		} else {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			int _offset = _thisProcedure.lookup(_id);
			_codegen.genStoreWithOffset(Register.S0, _offset, Register.FP); //store value to location in frame
		}
	}

	//this method visits the instructions of an instruction block
	public void handleNodeInstrBlock(NodeInstrBlock node) throws VisitorException {
		checkAndVisit(node.instructions());
	}

	
	//This method handles an if-then instruction. It places a start label and 
	//then visits the relation. If the relation returns false (indicated by a zero),
	//it branches to the end label. If the relation is true, it visits the then-
	//instructions before generating the end label.
	public void handleNodeInstrIfthen(NodeInstrIfthen node) throws VisitorException {
		String _startLabel = _codegen.getNextLabel();
		String _endLabel = _codegen.getNextLabel();
		_codegen.genLabel(_startLabel);
		checkAndVisit(node.relation()); //check relation
		_codegen.genPop(Register.S0); //if false, jump to end
		_codegen.genJumpEqualToZero(Register.S0, _endLabel);
		checkAndVisit(node.thenInstruction()); //else, evaluate else
		_codegen.genLabel(_endLabel);
	}

	
	//This method handles an if-then-else instruction. It places a start label and 
	//then visits the relation. If the relation returns false (indicated by a zero),
	//it branches to the else label. If the relation is true, it visits the then-
	//instructions before jumping to the end. The else label is then generated,
	//and the else instructions are visited. The end label is then generated.
	public void handleNodeInstrIfthenelse(NodeInstrIfthenelse node) throws VisitorException {
		String _startLabel = _codegen.getNextLabel();
		String _elseLabel = _codegen.getNextLabel();
		String _endLabel = _codegen.getNextLabel();
		_codegen.genLabel(_startLabel);
		checkAndVisit(node.relation()); //check relation
		_codegen.genPop(Register.S0); //if false, jump to else
		_codegen.genJumpEqualToZero(Register.S0, _elseLabel);
		checkAndVisit(node.thenInstruction());//if true, evaluate then-instructions, then jump to end
		_codegen.genJump(_endLabel);
		_codegen.genLabel(_elseLabel); //false, so evaluate else instructions
		checkAndVisit(node.elseInstruction());
		_codegen.genJump(_endLabel); 
		_codegen.genLabel(_endLabel);
	}

	//this method handles the output instruction by visiting the instruction,
	//putting the result in A0, then making a syscall for output
	public void handleNodeInstrOutput(NodeInstrOutput node) throws VisitorException {
		checkAndVisit(node.expression());
		_codegen.genPop(Register.A0);
		_codegen.genOutput();
	}

	
	
	//This method first gets input, which will appear in Register $v0.
	//It then visits the index to obtain the index of the array. If the
	//array has been globally declared, the method stores v0 at the
	//label for the array offset by the index. If the array is not
	//globally declared, the method looks up the array's offset 
	//from the frame-pointer and stores v0 there.
	public void handleNodeInstrInputArray(NodeInstrInputArray node) throws VisitorException {
		String _id = node.identifier();
		_codegen.genInput();
		checkAndVisit(node.index()); //visit index
		_codegen.genPop(Register.T0);
		_codegen.genLoadI(Register.T2, 4);
		_codegen.genTimes(Register.T0, Register.T2);
		if (_blockSizes.containsKey(_id)) { //global variable, so use label
			_codegen.genStoreIndirect(Register.V0, _id, Register.T0); 
		} else {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			int _offset = _thisProcedure.lookup(_id); //local variable, so use offset from fp
			_codegen.genLoadI(Register.T4, _offset);
			_codegen.genPlus(Register.T4, Register.T0);
			_codegen.genPlus(Register.T4, Register.FP);
			_codegen.genStoreToLocation(Register.V0, Register.T4); 
		}
	}
	

	//This method first makes an input syscall, which will put the resulting value
	//in $v0. If it is assigning that value to a global variable, it just stores it
	//at the label of the variable. If the variable is not global, it looks up the
	//offset of the variable in the current frame and stores the value there.
	public void handleNodeInstrInputSingle(NodeInstrInputSingle node) throws VisitorException {
		String _id = node.identifier();
		_codegen.genInput();
		if (_blockSizes.containsKey(_id)) { //global variable, so use label
			_codegen.genStore(Register.V0, _id);
		} else {
			Procedure _thisProcedure = _procedures.get(_currentProcedure);
			int offset = _thisProcedure.lookup(_id); //local variable, so use offset from fp
			_codegen.genStoreWithOffset(Register.V0, offset, Register.FP);
		}
	}
	
	//This method first visits the return expression. It then restores the necessary registers
	//before pushing the result to the stack and jumping from the procedure.
	public void handleNodeInstrReturnExpr(NodeInstrReturnExpr node) throws VisitorException {	
		NodeExpr _returnExpression = node.expression();
		checkAndVisit(_returnExpression); //visit return
		_codegen.genPop(Register.T0); //pop result
		_codegen.genRestoreRegisters(); //restore registers
		Procedure _thisProcedure = _procedures.get(_currentProcedure);
		_codegen.genLoadI(Register.T1, _thisProcedure.getSize()); 
		_codegen.genPlus(Register.SP, Register.T1); //make room for result
		_codegen.genMinusI(Register.SP, 4);
		_codegen.genStoreWithOffset(Register.T0, 4, Register.SP); //store return to stack
		_codegen.genReturnFromProcedure();
	}
	

	//this method handles a while instruction. To do so, it visits the relation. If the 
	//relation is false, it jumps to the end. If it is true, it visits the instructions
	//and then does an unconditional jump back to the beginning of the loop.
	public void handleNodeInstrWhile(NodeInstrWhile node) throws VisitorException {
		String _startLabel = _codegen.getNextLabel();
		String _endLabel = _codegen.getNextLabel();
		_codegen.genLabel(_startLabel);
		checkAndVisit(node.relation());
		_codegen.genPop(Register.S0);
		_codegen.genJumpEqualToZero(Register.S0, _endLabel); //if condition not true, skip to end
		checkAndVisit(node.instruction()); //generate instructions
		_codegen.genJump(_startLabel); //jump back to the beginning to check condition again
		_codegen.genLabel(_endLabel);
	}
	
	//visits a list of instructions
	public void handleNodeListInstrCons(NodeListInstrCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	//visits a list of variable declarations
	public void handleNodeListVarDeclCons(NodeListVarDeclCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	//visits a list of procedures
	public void handleNodeListProcCons(NodeListProcCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	//visits a list of procedure arguments
	public void handleNodeListProcDeclArgCons(NodeListProcDeclArgCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}
	
	//visits a list of procedure arguments
	public void handleNodeListProcCallArgCons(NodeListProcCallArgCons node) throws VisitorException {
		checkAndVisit(node.head());
		checkAndVisit(node.tail());
	}

	//handles a null node
	public void handleNodeListInstrNil(NodeListInstrNil node) throws VisitorException {
		// Nothing to do
	}
	
	//handles a null node
	public void handleNodeListVarDeclNil(NodeListVarDeclNil node) throws VisitorException {
		// Nothing to do
	}
	
	//handles a null node
	public void handleNodeListProcNil(NodeListProcNil node) throws VisitorException {
		// Nothing to do
	}
	
	//handles a null node
	public void handleNodeListProcDeclArgNil(NodeListProcDeclArgNil node) throws VisitorException {
		// Nothing to do
	}
	
	//handles a null node
	public void handleNodeListProcCallArgNil(NodeListProcCallArgNil node) throws VisitorException {
		// Nothing to do
	}

	//this method handles a program node by visiting both the list of
	//variable declarations and the list of procedures
	public void handleNodeProgram(NodeProgram node) throws VisitorException {
		NodeListProc _listProc = node.listProc();
		NodeListVarDecl _listVarDecl = node.listVarDecl();
		checkAndVisit(_listVarDecl);
		checkAndVisit(_listProc);
	}
	
	//this handles an argument in a procedure call
	//by visiting the expression
	public void handleNodeProcCallArg(NodeProcCallArg node) throws VisitorException {
		checkAndVisit(node.expression());
	}
	
	//This method handles the arguments of a procedure when the procedure is declared.
	//However, this require taking any action, so this method does nothing.
	public void handleNodeProcDeclArg(NodeProcDeclArg node) throws VisitorException {
		// Nothing to do here
//		Procedure _thisProcedure = _procedures.get(_currentProcedure);
//		int offset = _thisProcedure.lookup(node.identifier());
//		_codegen.genLoadWithOffset(Register.T0, offset, Register.FP);
//		_codegen.genPush(Register.T0);
	}
	
	//this method handles a procedure. It first generates a main label if the procedure
	//is the main procedure. It then labels the procedure. Then, it makes room
	//on the stack for all of the variables in the procedure before saving the
	//registers to the stack and moving the frame pointer to align with
	//the stack pointer. It then visits the arguments and the instructions before restoring the
	//registers and popping the frame off the stack. It then returns
	//from the procedure.
	public void handleNodeProc(NodeProc node) throws VisitorException {
		String _procedureName = node.identifier();
		NodeInstr _procedureInstructions = node.instruction();
		_currentProcedure = _procedureName;
		if (_procedureName.equals("main")) { //main procedure--make label
			_codegen.genMain();
		}
		String _procLabel = _codegen.getProcLabel(_procedureName);
		_codegen.genLabel(_procLabel); 
		Procedure _thisProcedure = _procedures.get(_procedureName);
		int _allocateRoom = (_thisProcedure.getSize() - _thisProcedure.getArgList().size() * 4);
		_codegen.genLoadI(Register.T0, _allocateRoom); 
		_codegen.genMinus(Register.SP, Register.T0); //make room for frame on the stack
		_codegen.genSaveRegisters();
		_codegen.genMove(Register.FP, Register.SP); //update frame pointer
		checkAndVisit(node.argumentList()); //visit arguments
		checkAndVisit(_procedureInstructions); //visit instructions
		_codegen.genRestoreRegisters(); //restore registers
		_codegen.genLoadI(Register.T1, _thisProcedure.getSize());  
		_codegen.genPlus(Register.SP, Register.T1);
		_codegen.genLoadI(Register.T0, 0);
		_codegen.genMinusI(Register.SP, 4);
		_codegen.genStoreWithOffset(Register.T0, 4, Register.SP);  //default to returning zero
		_codegen.genReturnFromProcedure();

	}
	//This method handles procedures when they are used in expressions. It pushes 
	//each argument to the stack and then jumps to the procedure.
	public void handleNodeExprProcCall(NodeExprProcCall node) throws VisitorException {
		checkAndVisit(node.arguments());
		String _procName = _codegen.getProcLabel(node.identifier());
		_codegen.genJumpLabel(_procName);
	}
	
	
	//This method handles a procedure call that is an instruction. It does so
	//by visiting each argument so that they can be put on the stack and then jumps
	//to the procedure. After the procedure will have returned, it pops the result.
	public void handleNodeInstrProcCall(NodeInstrProcCall node) throws VisitorException {
		checkAndVisit(node.arguments());
		String _procName = _codegen.getProcLabel(node.identifier());
		_codegen.genJumpLabel(_procName); 
		_codegen.genPop(Register.T0); 
	}
	
	//pushes 1 to the stack to represent true
	public void handleNodeExprTrue(NodeExprTrue node) throws VisitorException {
		_codegen.genLoadI(Register.S0, 1);
		_codegen.genPush(Register.S0);
	}
	
	//pushes 0 to the stack to represent false
	public void handleNodeExprFalse(NodeExprFalse node) throws VisitorException {
		_codegen.genLoadI(Register.S0, 0);
		_codegen.genPush(Register.S0);
	}

	//this method visits a left and right child node. It then pushes the result of making a 
	//less than comparison between the left child and the right child
	public void handleNodeRelationLessThan(NodeRelationLessThan node) throws VisitorException {
		NodeExpr _leftChild = node.leftChild();
		NodeExpr _rightChild = node.rightChild();
		checkAndVisit(_leftChild);
		checkAndVisit(_rightChild);
		_codegen.genPop(Register.S0); //right child
		_codegen.genPop(Register.S1); //left child
		_codegen.genStoreLessThan(Register.S1, Register.S0); 
		_codegen.genPush(Register.S1);//push ( left < right)
		
	}

	//this method visits a left and right child node. It then pushes the result of making a 
	//less-than-or-equal comparison between the left child and the right child
	public void handleNodeRelationLessThanEqual(NodeRelationLessThanEqual node) throws VisitorException {
		NodeExpr _leftChild = node.leftChild();
		NodeExpr _rightChild = node.rightChild();
		checkAndVisit(_leftChild);
		checkAndVisit(_rightChild);
		_codegen.genPop(Register.S0); //right child
		_codegen.genPop(Register.S1); //left child
		_codegen.genStoreLessEq(Register.S1, Register.S0);
		_codegen.genPush(Register.S1); //push (left <= right)
	}

	//this method visits a left and right child node. It then pushes the result of making a 
	//equal comparison between the left child and the right child
	public void handleNodeRelationEqual(NodeRelationEqual node) throws VisitorException {
		NodeExpr _leftChild = node.leftChild();
		NodeExpr _rightChild = node.rightChild();
		checkAndVisit(_leftChild);
		checkAndVisit(_rightChild);
		_codegen.genPop(Register.S0); //right child
		_codegen.genPop(Register.S1); //left child
		_codegen.genStoreEq(Register.S1, Register.S0);
		_codegen.genPush(Register.S1); //push (left == right)
	}

	//handles greater-than-or-equal by reversing it and then doing a less than or equal
	//to comparison
	public void handleNodeRelationGreaterThanEqual(NodeRelationGreaterThanEqual node) throws VisitorException {
		checkAndVisit(new NodeRelationLessThanEqual(node.rightChild(), node.leftChild()));
	}

	//handles greater-than by reversing it and then doing a less then comparison
	public void handleNodeRelationGreaterThan(NodeRelationGreaterThan node) throws VisitorException {
		checkAndVisit(new NodeRelationLessThan(node.rightChild(), node.leftChild()));
	}

	
	//this method handles an And node. It first visits the left child--if it is false,
	//it jumps to the end label. It then resets the value of a register that will ultimately
	//be pushed to the stack back to zero--this way, if the right child is false, we can
	//still push the same register. It then visits the right child and jumps to the end
	//if it is false or changes the register that will be pushed to true before getting
	//to the end.
	public void handleNodeRelationAnd(NodeRelationAnd node) throws VisitorException {
		String _endLabel = _codegen.getNextLabel();
		checkAndVisit(node.leftChild());
		_codegen.genPop(Register.S0);
		_codegen.genJumpEqualToZero(Register.S0, _endLabel); //if left is false, go to end
		_codegen.genLoadI(Register.S0, 0); //reset $s0 to 0 so that it can be returned no matter what
		checkAndVisit(node.rightChild()); 
		_codegen.genPop(Register.S1);
		_codegen.genJumpEqualToZero(Register.S1, _endLabel); //if right is false, go to end
		_codegen.genLoadI(Register.S0, 1); //both were true, so push 1
		_codegen.genLabel(_endLabel);
		_codegen.genPush(Register.S0);
	}

	//this method handles an Or node. It first visits the left child--if it is true,
	//it jumps to the end label. It then resets the value of a register that will ultimately
	//be pushed to the stack back to one--this way, if the right child is true, we can
	//still push the same register. It then visits the right child and jumps to the end
	//if it is true or changes the register that will be pushed to false before getting
	//to the end.
	public void handleNodeRelationOr(NodeRelationOr node) throws VisitorException {
		String _endLabel = _codegen.getNextLabel();
		checkAndVisit(node.leftChild());
		_codegen.genPop(Register.S0);
		_codegen.genJumpNotEqualToZero(Register.S0, _endLabel); //if left is true, go to end
		_codegen.genLoadI(Register.S0, 1); //reset register
		checkAndVisit(node.rightChild());
		_codegen.genPop(Register.S1);
		_codegen.genJumpNotEqualToZero(Register.S1, _endLabel); //if right is true, go to end
		_codegen.genLoadI(Register.S0, 0); //both were false, so push 0
		_codegen.genLabel(_endLabel);
		_codegen.genPush(Register.S0);
	}

	//this method handles an Not node. It first visits the relation--if it is true, it jumps
	//to a label that will turn it to false. If it is false, the register that will be returned
	//gets set to one and then the method jumps to the end label.
	public void handleNodeRelationNot(NodeRelationNot node) throws VisitorException {
		String _turnToZero = _codegen.getNextLabel();
		String _endLabel = _codegen.getNextLabel();
		checkAndVisit(node.relation());
		_codegen.genPop(Register.S0);
		_codegen.genJumpNotEqualToZero(Register.S0, _turnToZero); //was true, so turn $s0 to 0
		_codegen.genLoadI(Register.S0, 1); //was false, so set $s0 to 1
		_codegen.genJump(_endLabel); //jump to end
		_codegen.genLabel(_turnToZero);
		_codegen.genLoadI(Register.S0, 0); //set to false
		_codegen.genLabel(_endLabel);
		_codegen.genPush(Register.S0);
	}
}
