package comp;

import java.util.*;

/**
 * A Procedure represents a procedure.
 * It keeps track of its arguments and
 * calculates frame pointer offsets
 * for all variables in the procedure.
 *
 * To use this class for frames, one must call insert
 * all arguments and local variables in the order that they're
 * declared (during Semantic Analysis).  Once all variables have been
 * added, call finalize to generate the correct offset.
 * Call lookup on a variable during Code Generation to determine
 * its offset from the frame pointer.
 * 
 * @version Sat Mar 06 2011
 * @modifier Benjamin Niedzielski
 */
public class Procedure {
	
	private String _name;
	private List<String> _argList;
	
	//Frame information
	private Map<String, Integer> _varLocList; //contains the offset of each variable from $fp
	private Map<String, Integer> _varSizeList; //contains the size of each variable
	private int _lastSize; //contains the size of the most recently added variable, so we
								  //know where to put the next one
	private int _lastLocation; //contains the offset of the most recently added variable
	
	/**
	 * Make a new Procedure.
	 */
	public Procedure(String name, List<String> argList) {
		_name = name;
		_argList = argList;
		
		_varLocList = new HashMap<String, Integer>();
		_varSizeList = new HashMap<String, Integer>();
		_lastSize = 0;
		_lastLocation = 0;
	}
	
	/**
	 * Call this method when a variable (argument or local variable) is declared
	 * (in Semantic Analysis).
	 * Given the variable name and the amount of space the variable takes, this
	 * method will add the variable to the list used to create the final offsets
	 * from the frame pointer.
	 *
	 * If the variable is a single, size should be 1.  If the variable is an
	 * array, size should be the size of the array (in words).
	 * 
	 * @param name
	 *           Variable being declared
	 * @param size
	 *           The size of the variable
	 * @return True if insert was successful.
	 */
	public boolean insert(String name, int size) {
		// see if the variable name is already used
		if (!_varLocList.containsKey(name)) {
			// if not, we can just add the variable to the end of our list 			
			_varLocList.put(name,_lastLocation + _lastSize); //this gets the next free location
			_varSizeList.put(name, size * 4); //4 bytes per word
			//update where the end of the list is now.
			_lastLocation = _lastLocation + _lastSize;
			_lastSize = size * 4;
			return true;
		} else {
		   //if so, see if we already have enough space associated with this variable.
		   if (_varSizeList.get(name) < size * 4) {
		   	//if not, we need to allocate more space, potentially moving all
		   	//variables after this one in our list.
		     int diff = size * 4 - _varSizeList.get(name);
		     _varSizeList.put(name, size * 4);
		     if (_varLocList.get(name) == _lastLocation) {
		       _lastSize = size * 4;
		     }
		     //move later variables by the amount of space we just added
		     for (String s : _varLocList.keySet()) {
		       if (_varLocList.get(s) > _varLocList.get(name)) {
		         if (_lastLocation == _varLocList.get(s)) {
		           _lastLocation = _varLocList.get(s) + diff;
		         }
		         _varLocList.put(s, _varLocList.get(s) + diff);
		       }
		     }
		   }
		   //if we have enough space, there's nothing to do
		   return true;
		}
	}
	
	/**
	 * Finalizes the list of procedure vars by changing the locations of
	 * the variables to be
	 * relative to the frame pointer.
	 * Call this during Semantic Analysis once you've added all variables
	 * for the procedure with insert.
	 *
	 * Assumes that all $s registers (which includes the frame pointer) and
	 * the return address are pushed
	 * on each procedure call.
	 */
	public void finish() {
	  //40 is the size of the pushed registers.
	  //Ensure the stack goes the correct way and
	  //that offsets are positive numbers and start at $fp.
	  int offset = 40 + this.getSize();
	  for (String s : _varLocList.keySet()) {
	     _varLocList.put(s, -(_varLocList.get(s) - offset + (_varSizeList.get(s) - 4)));
	  }
	}
	
	/**
	 * See if this variable is in the list and where it is
	 * relative to the frame pointer.
	 *
	 * This should be called in Code Generation, and only after
	 * finalize has been called.
	 * 
	 * @param name variable being used in program
	 * @return the offset of the variable from $fp, or -1 if it doesn't exist
	 */
	public int lookup(String name) {
		if (_varLocList.keySet().contains(name)) {
			return _varLocList.get(name);
		}
		return -1;
	}
	
	/**
	 * Gets the amount of space in bytes needed for all variables
	 * used in this procedure.  Although this returns the
	 * space needed for all variables, you will need
	 * to allocate a different amount of memory on the stack,
	 * since the arguments have already been passed on the stack
	 * and so this overcounts.
	 * 
	 */
	public int getSize() {
		//This is the number of bytes needed for the variables in the frame,
		//counting arguments.
		return _lastLocation + _lastSize;
	}
	
	/**
	 * Gets this Procedure's name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Gets this Procedure's list of arguments
	 */
	public List<String> getArgList() {
		return _argList;
	}
	
	/**
	 * Use this to change the argList.  This may
	 * come in handy when you encounter a procedure
	 * for the first time, as you cannot know
	 * its arguments when you have to create a Procedure
	 * object for it.
	 */
	public void setArgList(List<String> argList) {
		_argList = argList;
	}
	
	/**
	 * Checks to see if two Procedures are equal.
	 * Two Procedures are equal when they have the
	 * same name, number of arguments, and name of
	 * arguments
	 */ 
	public boolean equals(Object other) {
		if (!(other instanceof Procedure)) {
			return false;
		}
		Procedure otherProc = (Procedure)other;
		
		if (!this._name.equals(otherProc._name)) {
			return false;
		}		
		
		if (this._argList.size() != otherProc._argList.size()) {
			return false;
		}
		
		//Check the names of the arguments as well.
		//Not strictly necessary.
		for (int i = 0; i < this._argList.size(); i++) {
			if (this._argList.get(i) != otherProc._argList.get(i)) {
				return false;
			}
		}
		
		return true;
	}
}
