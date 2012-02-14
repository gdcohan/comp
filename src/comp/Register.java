package comp;

/**
 * Enumeration for all the different registers on a MIPS
 * machine.
 *
 * @author An Awesome TA
 */
public enum Register {
	
	//We associate each register with a String for printing in our final code.
	V0("v0"),
	V1("v1"),

	A0("a0"),
	A1("a1"),
	A2("a2"),
	A3("a3"),

	T0("t0"),
	T1("t1"),
	T2("t2"),
	T3("t3"),
	T4("t4"),
	T5("t5"),
	T6("t6"),
	T7("t7"),
	T8("t8"),
	T9("t9"),

	S0("s0"),
	S1("s1"),
	S2("s2"),
	S3("s3"),
	S4("s4"),
	S5("s5"),
	S6("s6"),
	S7("s7"),
	
	FP("fp"),

	SP("sp"),
	RA("ra");
	
	private final String _name; //String representation of register

	//Create a Register	
	Register(String name) {
		_name = name;
	}
	
	//toString
	public String toString() {
		return _name;
	}
	
	
}
