package comp;

import java.io.*;

/**
 * This class encapsulates the process of writing MIPS instructions to an output
 * string. Once this string is created, you may output the string to either a
 * file or the java console (stdout). <p>
 * 
 * To write the string to a file, use {@link #writeToFile(String)}. To write it
 * to the console, use {@link #writeToConsole()}. <p>
 * 
 * Each <code>gen...</code> method corresponds to writing exactly one
 * instruction to the output string. The arguments to the methods specify the
 * variable parts of the instructions. (The one-instruction rule is relaxed for
 * the {@link #genOutput()} instruction, which also writes the text "syscall" and then
 * writes three instructions that serve to print a newline after the output
 * integer.)
 *
 * @version Fall 2007
 * @author The TAs
 */
public class MIPSCodeGenerator {
	private StringBuilder _codeString;
	private int _label; //This counter ensures we generate unique labels
    
	/** 
	 * Create an instance of <code>MIPSCodeGenerator</code>.
	 */
	public MIPSCodeGenerator() {
		_label = 0;
		_codeString = new StringBuilder();
	}
	
	/** 
	 * Creates and returns the String for a new label.
	 * Call this to create a label, then pass it to other
	 * gen... methods to make use of it.
	 *
	 * Label will be of the form "label" + int, like label2 or label31
	 */
	public String getNextLabel()
	{
		String newLabel = "label" + _label;
  		_label++;
  		return newLabel;
	}

	/** 
	 * Creates and returns the String for a new procedure label.
	 * Label will be of the form "proc_" + name, like proc_main
	 *
	 * @param: the name of the procedure called
	 */
	public String getProcLabel( String label )
	{
		String newLabel = "proc_" + label;
  		return newLabel;
	}
	
	/** 
	 * Write to the StringBuffer the given label.
	 * 
	 * @param label The label to print.
	 */
	public void genLabel( String label )
	{
		_codeString.append(label + ": \n");
	}
    
	/** 
	 * Write a <code>print-int</code> syscall. You must move the argument into <code>$a0</code>;
	 * this method doesn't do it for you.
	 */
	public void genOutput()
	{
		_codeString.append("\tli\t$v0, 1\n" +
  		"\tsyscall\n" +
  		"\tli\t$v0, 11\n" +
  		"\tli\t$a0, 10\n" +
  		"\tsyscall\n");
	}
	 
	/** 
	 * Write a read-int syscall. Puts the read integer into <code>$v0</code>.
	 */
	public void genInput()
	{
		_codeString.append("\tli\t$v0, 5\n" +
  		"\tsyscall\n");
	}
	 
	/** 
	 * Write <code>.data</code> line.
	 */
	public void genData()
	{
		_codeString.append(".data\n\n");
	}
	 
	/** 
	 * Write <code>.text</code> line.
	 */
	public void genText()
	{
		_codeString.append("\n.text\n\n");
	}
	 
	/** 
	 * Write <code>main:</code> line.
	 * Although one can (and should) create a proc_main
	 * label, this one is needed so we know where to start
	 * the program.
	 */
	public void genMain()
	{
		_codeString.append("main:\n");
	}
	
	/** 
	 * Write code to save registers on the stack.
	 * Saves the return address and s-registers (including
	 * s8, the frame pointer).
	 */
	public void genSaveRegisters()
	{
		_codeString.append("\tsub\t$sp, $sp, 40\n" +
		"\tsw\t$ra, 40($sp)\n" +
		"\tsw\t$s0, 36($sp)\n" +
		"\tsw\t$s1, 32($sp)\n" +
		"\tsw\t$s2, 28($sp)\n" +
		"\tsw\t$s3, 24($sp)\n" +
		"\tsw\t$s4, 20($sp)\n" +
		"\tsw\t$s5, 16($sp)\n" +
		"\tsw\t$s6, 12($sp)\n" +
		"\tsw\t$s7, 8($sp)\n" +
		"\tsw\t$fp, 4($sp)\n\n");
	}
	 
	/** 
	 * Write code to restore the saved registers
	 */
	public void genRestoreRegisters()
	{
		_codeString.append("\tlw\t$fp, 4($sp)\n" +
		"\tlw\t$s7, 8($sp)\n" +
		"\tlw\t$s6, 12($sp)\n" +
		"\tlw\t$s5, 16($sp)\n" +
		"\tlw\t$s4, 20($sp)\n" +
		"\tlw\t$s3, 24($sp)\n" +
		"\tlw\t$s2, 28($sp)\n" +
		"\tlw\t$s1, 32($sp)\n" +
		"\tlw\t$s0, 36($sp)\n" +
		"\tlw\t$ra, 40($sp)\n" +
		"\tadd\t$sp, $sp, 40\n");
	}

	/**
	 * Write code to jump to the return address
	 */
	public void genReturnFromProcedure()
	{
		_codeString.append("\tjr $ra\n\n");
	}
	 
	/** 
	 * Write a <code>.word</code> directive that can be used to store a
	 * variable's value. Tacks an underscore onto the end of the identifier so
	 * that variable names like "<code>j</code>" or "<code>bge</code>" won't be
	 * interpreted as instructions.
	 * 
	 * @param id Label for the word declaration.
	 */
	public void genDecl( String id )
	{
		_codeString.append(id + "_" +
  		":\t.word 0\n");
	}
	 
	/** 
	 * Write a .word directive that can be used to store an array of
	 * values. Tacks an underscore onto the end of the identifier so that
	 * variable names like "<code>j</code>" or "<code>bge</code>" won't be
	 * interpreted as instructions.
	 * 
	 * @param size Number of words in the array.
	 * @param id Label for the array.
	 */
	public void genArrayDecl( int size, String id )
	{
		_codeString.append(id + "_" +
  		":\t.word\t0:" +
  		( size ) + "\n");
	}

	/** 
	 * Pushes the contents of the register to the stack.
	 * 
	 * @param reg Register whose value should be pushed.
	 */
	public void genPush(Register reg)
	{
		_codeString.append("\tsub\t$sp, $sp, 4\n"+
			"\tsw\t$" + reg + ", 4($sp)\n");
	}

	/**
	 * Pops the stack into the given register.
	 * 
	 * @param reg Register to pop into.
	 */
	public void genPop(Register reg)
	{
		_codeString.append("\tlw\t$" + reg + ", 4($sp)\n" +
			"\tadd\t$sp, $sp, 4\n");
	}
	 
	/** 
	 * Write an sw instruction in the format:
     * <pre>
     *   sw $t0, label
	 * </pre>
	 * 
	 * @param reg Register whose value should be stored.
	 * @param label Label identifying a variable.
	 */
	public void genStore( Register reg, String label )
	{
		_codeString.append("\tsw\t$" +
  		reg +
  		", " +
  		label + "_\n");
	}
	 
	/** 
	 * Write an sw instruction in the format <code>
	 * sw $t0, label($t1)
	 * </code>
	 * Adds the value of the label to the value in the register
	 * given by index and stores the value in the register given
	 * by reg in the word at that computed address.
	 * @param reg Register whose value should be stored
	 * @param label Label or constant offset for the immediate-register mode
	 * @param index Variable offset or index for the immediate-register mode
	 */
	public void genStoreIndirect( Register reg, String label, Register index )
	{
		_codeString.append("\tsw\t$" +
  		reg +
  		", " +
  		label + "_" +
  		"($" +
  		index +
  		")\n");
	}
	
	/** 
	 * Write an sw instruction in the format <code>
	 * sw $t0, ($t1)
	 * </code>
	 * stores the value in the register given
	 * by reg in the word at the address of index.
	 * @param reg Register whose value should be stored
	 * @param index Variable offset
	 */
	public void genStoreToLocation( Register reg, Register index )
	{
		_codeString.append("\tsw\t$" +
  		reg +
  		", " +
  		"($" +
  		index +
  		")\n");
	}
	 
	/** 
	 * Write an lw instruction in the format<code>
	 * lw $t0, label
	 * </code>
	 * @param reg Register that will be changed
	 * @param label Label identifying a variable
	 */
	public void genLoad(Register reg, String label )
	{
		_codeString.append("\tlw\t$" +
  		reg +
  		", " +
  		label + "_\n");
	}
	
	//this method generates a mips load instruction
	//of the form lw reg1 offset(reg2)
	public void genLoadWithOffset(Register reg1, int offset, Register reg2) {
		_codeString.append("\tlw\t$" + 
		reg1 + ", " + offset + "($" + reg2 + ")\n"); 
	}
	
	//this method generates a mips store instruction 
	//of the form sw reg1 offset(reg2)
	public void genStoreWithOffset(Register reg1, int offset, Register reg2) {
		_codeString.append("\tsw\t$" + 
		reg1 + ", " + offset + "($" + reg2 + ")\n"); 
	}
	
	//this method generates a mips jump instruction
	//of the form jal label
	public void genJumpLabel(String label) {
		_codeString.append("\tjal\t" + label + "\n");
	}
	/** 
	 * Write an lw instruction in the format <code>
	 * lw $t0, label($t1)
	 * </code>
	 * Adds the value of the label to the value in the register
	 * given by index, loads the word at that address and
	 * stores it in the register given by reg.
	 * @param reg Register that will be changed
	 * @param label Label or constant offset for the immediate-register mode
	 * @param index Variable offset or index for the immediate-register mode
	 */
	public void genLoadIndirect(Register reg, String label, Register index )
	{
		_codeString.append("\tlw\t$" +
  		reg +
  		", " +
  		label + "_" +
  		"($" +
  		index +
  		")\n");
	}
	
	/** 
	 * Write an lw instruction in the format <code>
	 * lw $t0, ($t1)
	 * </code>
	 * loads the word at the address of index and
	 * stores it in the register given by reg.
	 * @param reg Register that will be changed
	 * @param index Variable offset
	 */
	public void genLoadFromLocation(Register reg, Register index )
	{
		_codeString.append("\tlw\t$" +
  		reg +
  		", " +
  		"($" +
  		index +
  		")\n");
	}
	 
	/** 
	 * Write an li instruction.
	 * @param reg Register that will be changed
	 * @param val Value to be put in the register
	 */
	public void genLoadI( Register reg, int val )
	{
		_codeString.append("\tli\t$" +
  		reg +
  		", " +
  		val + "\n");
	}
	 
	/** 
	 * Write a move instruction.
	 * @param reg1 Destination register
	 * @param reg2 Source register
	 */
	public void genMove( Register reg1, Register reg2 )
	{
		_codeString.append("\tmove\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	 
	/** 
	 * Write an add instruction in the format<code>
	 * add $t0, $t0, $t1
	 * @param reg1 Source and destination register
	 * @param reg2 Source register
	 */
	public void genPlus( Register reg1, Register reg2 )
	{
		_codeString.append("\tadd\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	
	//this method creates a mips add instruction 
	//of the form add $t0, $t0, 4
	public void genPlusI(Register reg1, int val) {
		_codeString.append("\tadd\t" + "$" + reg1 + ", $" + reg1 + ", " + val + "\n");
	}
	 
	/** 
	 * Write a sub instruction in the format <code>
	 * sub $t0, $t0, $t1
	 * </code>
	 * @param reg1 Minuend and destination register
	 * @param reg2 Subtrahend source register
	 */
	public void genMinus( Register reg1, Register reg2 )
	{
		_codeString.append("\tsub\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	
	//this method creates a mips subtract instruction 
	//of the form sub $t0, $t0, 4
	public void genMinusI(Register reg1, int val) {
		_codeString.append("\tsub\t" + "$" + reg1 + ", $" + reg1 + ", " + val + "\n");
	}
	 
	/** 
	 * Write a mul instruction in the format <code>
	 * mul $t0, $t0, $t1
	 * </code>
	 * @param reg1 Source and destination register
	 * @param reg2 Source register
	 */
	public void genTimes( Register reg1, Register reg2 )
	{
		_codeString.append("\tmul\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	
	/** 
	 * Write a div instruction in the format <code>
	 * div $t0, $t0, $t1
	 * </code>
	 * @param reg1 Source and destination register
	 * @param reg2 Source register
	 */
	public void genDivide( Register reg1, Register reg2 ) 
	{
		_codeString.append("\tdiv\t" +
		"$" +
		reg1 +
		", $" +
		reg1 +
		", $" +
		reg2 + "\n");
	}

	/** 
	 * Write a rem instruction in the format <code>
	 * rem $t0, $t0, $t1
	 * </code>
	 * @param reg1 Source and destination register
	 * @param reg2 Source register
	 */
	public void genMod( Register reg1, Register reg2 )
	{
		_codeString.append("\trem\t" +
		"$" +
		reg1 +
		", $" +
		reg1 +
		", $" +
		reg2 + "\n");
	}
	 
	/**  
	 * Write a j instruction, with the label as in genNextLabel(.).
	 * @param label Unique identifier for the label
	 */
	public void genJump( String label )
	{
		_codeString.append("\tj\t" +
  		label + "\n");
	}
	
	/** 
	 * Write a jal instruction, with the label as in genProcLabel(.).
	 * @param label Unique identifier for the label
	 */
	public void genProcJumpAndLink( String label )
	{
		_codeString.append("\tjal\t" +
  		label + "\n");
	}
	
	/** 
	 * Write a beqz instruction, with the label specified by label.
	 * @param reg Register
	 * @param label Unique identifier for the label
	 */
	public void genJumpEqualToZero( Register reg, String label )
	{
		_codeString.append("\tbeqz\t$" +
		reg +
		", " +
		label + "\n");
	}
	 
	/** 
	 * Write a bnez instruction, with the label specified by label.
	 * @param reg Register
	 * @param label Unique identifier for the label
	 */
	public void genJumpNotEqualToZero( Register reg, String label )
	{
		_codeString.append("\tbnez\t$" +
		reg +
		", " +
		label + "\n");
	}
	 
	/** 
	 * Write a seq instruction, that stores the result in the first register
	 * as in seq $s0, $s0, $s1.
	 * @param reg1 Left-hand source and destination register
	 * @param reg2 Right-hand source register
	 */
	public void genStoreEq( Register reg1, Register reg2)
	{
		_codeString.append("\tseq\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	 
	/** 
	 * Write a sge instruction, that stores the result in the first register
	 * as in sge $s0, $s0, $s1.
	 * @param reg1 Left-hand source and destination register
	 * @param reg2 Right-hand source register
	 */
	public void genStoreGreaterEq( Register reg1, Register reg2)
	{
		_codeString.append("\tsge\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	 
	/** 
	 * Write a sgt instruction, that stores the result in the first register
	 * as in sgt $s0, $s0, $s1.
	 * @param reg1 Left-hand source and destination register
	 * @param reg2 Right-hand source register
	 */
	public void genStoreGreaterThan( Register reg1, Register reg2)
	{
		_codeString.append("\tsgt\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	 
	/** 
	 * Write a slt instruction, that stores the result in the first register
	 * as in slt $s0, $s0, $s1.
	 * @param reg1 Left-hand source and destination register
	 * @param reg2 Right-hand source register
	 */
	public void genStoreLessThan( Register reg1, Register reg2)
	{
		_codeString.append("\tslt\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}
	 
	/** 
	 * Write a sle instruction, that stores the result in the first register
	 * as in sle $s0, $s0, $s1.
	 * @param reg1 Left-hand source and destination register
	 * @param reg2 Right-hand source register
	 */
	public void genStoreLessEq( Register reg1, Register reg2)
	{
		_codeString.append("\tsle\t" +
  		"$" +
  		reg1 +
  		", $" +
  		reg1 +
  		", $" +
  		reg2 + "\n");
	}

	/** 
	 * Write a beq instruction, with the label specified by label.
	 * @param reg1 Left-hand register
	 * @param reg2 Right-hand register
	 * @param label Unique identifier for the label
	 */
	public void genJumpEq( Register reg1, Register reg2, String label )
	{
		_codeString.append("\tbeq\t$" +
		reg1 +
		", $" + 
		reg2 +
		", " +
		label + "\n");
	}
	 
	/** 
	 * Write a bge instruction, with the label specified by label.
	 * @param reg1 Left-hand register
	 * @param reg2 Right-hand register
	 * @param label Unique identifier for the label
	 */
	public void genJumpGreaterEq( Register reg1, Register reg2, String label )
	{
		_codeString.append("\tbge\t$" +
  		reg1 +
  		", $" +
  		reg2 +
  		", " +
  		label + "\n");
	}
	 
	/** 
	 * Write a ble instruction, with the label specified by label.
	 * @param reg1 Left-hand register
	 * @param reg2 Right-hand register
	 * @param label Unique identifier for the label
	 */
	public void genJumpLessEq( Register reg1, Register reg2, String label )
	{
		_codeString.append("\tble\t$" +
  		reg1 +
  		", $" +
  		reg2 +
  		", " +
  		label + "\n");
	}
	 
	/** 
	 * Write a bgt instruction, with the label specified by label.
	 * @param reg1 Left-hand register
	 * @param reg2 Right-hand register
	 * @param label Unique identifier for the label
	 */
	public void genJumpGreaterThan( Register reg1, Register reg2, String label )
	{
		_codeString.append("\tbgt\t$" +
  		reg1 +
  		", $" +
  		reg2 +
  		", " +
  		label + "\n");
	}
	 
	/** 
	 * Write a blt instruction, with the label specified by label.
	 * @param reg1 Left-hand register
	 * @param reg2 Right-hand register
	 * @param label Unique identifier for the label
	 */
	public void genJumpLessThan( Register reg1, Register reg2, String label )
	{
		_codeString.append("\tblt\t$" +
  		reg1 +
  		", $" +
  		reg2 +
  		", " +
  		label + "\n");
	}
	 
	/** 
	 * Write a comment introduced by a # sign.
	 * Comments are nice but not required.
	 * @param comment
	 */
	public void genComment( String comment )
	{
		_codeString.append("# " + comment + "\n");
	}

	/**
	 * Write the _codeString (your program so far) to the console
	 */
	public void writeToConsole()
	{
		System.out.println(_codeString.toString() + "\n");
	}

	/**
	 * Write the _codeString (your program so far) to the file
	 */
	public void writeToFile(String filename)
	{
		try {
			FileWriter out = new FileWriter(filename);
			out.write(_codeString.toString() + "\n");
			out.close();
		}
		catch(java.io.IOException e)
		{
			System.err.println(e.getMessage() + "\n");
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}		

	/**
	 * A demonstration of the use of this class.
	 */
	public static void main(String argv[])
	{
		MIPSCodeGenerator mcg = new MIPSCodeGenerator();
		mcg.genPush(Register.S0);
		mcg.genPop(Register.S0);
		mcg.writeToConsole();
		mcg.writeToFile("test");
	}
}
