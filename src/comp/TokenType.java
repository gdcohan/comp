package comp;

/**
 * Enumeration for the Blaise token types.
 * 
 * @version Sun Feb 20 16:35 2011
 * @author The TAs
 */
public enum TokenType {

    // An identifier literal (variable name)
    ID,
    
    //An integer literal - e.g. 4. Not the same as INT, below
    INTEGER,

    // Operators
    PLUS, MINUS, TIMES, DIVIDE, MODULUS, LESSTHAN, AND, OR, NOT,
    GREATERTHAN, LESSEQ, GREATEREQ, EQUAL,

    // Control structures
    IF, WHILE, ELSE, ENDIF,
    
    // The string ":="
    ASSIGN,

    // The string "int"
    INT,
    
    //The constants true and false 
    TRUE, FALSE,

    // The string "output"
    OUTPUT, INPUT,
    
    //Other data types 
    BOOL, VOID,

	 //Declarations    
    PROCEDURE, VAR,

    // Punctuation
    LEFTPAREN, RIGHTPAREN, SEMICOLON, LEFTBRACE, RIGHTBRACE, LEFTBRACKET, RIGHTBRACKET, COMMA,
    
    //Return
    RETURN,
    
    // End of file
    EOF
    
}
