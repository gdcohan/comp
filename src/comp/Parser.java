package comp;

//WORKS ON STATS

import java.io.FileNotFoundException;
import java.io.IOException;

import comp.nodes.*;

/**
 * Builds a tree of Nodes from a token stream. The syntax tree is rooted at a
 * NodeProgram. Once built, the tree can be used to do semantic analysis and
 * code generation.
 * <p>
 * Parser relies on a tokenizer for Blaise, which is available as class
 * Tokenizer.
 * <p>
 * You will probably want to add a good number of methods to this class to make
 * parsing possible.
 * 
 * @version Fri Nov 7 12:18:59 1997
 * @modified Mark Handy
 */
public class Parser {

    /**
     * Provides input to the Parser.
     */
    private Tokenizer _tokenizer;

    /**
     * The token currently being examined.
     */
    private Token _currentToken;

    /**
     * The next token in the token stream.
     */
    private Token _nextToken;

    /**
     * Opens the file specified and advances the token stream to populate
     * _nextToken.
     * 
     * @param name
     *           Name of file to parse
     */
    public Parser(String name) throws LexicalException, IOException {
        try {
            _tokenizer = new Tokenizer(name);
        } catch (FileNotFoundException e) {
            System.out.println("Error! File " + name + " not found!");
            System.exit(1);
        }
        advanceTokenStream();
    }

    /**
     * Creates a Parser, presumably taking input from stdin.
     */
    public Parser() throws IOException, LexicalException {
        _tokenizer = new Tokenizer();
        advanceTokenStream();
    }

    /**
     * Top-level method, called by client of the Parser to produce the syntax
     * tree.
     * 
     * @return NodeProgram at the root of the syntax tree
     */
    public NodeProgram parse() throws SyntaxException, LexicalException,
            IOException {
		return parseProgram();
    }

    /**
     * Set _currentToken to _nextToken and set _nextToken to the next token from
     * _tokenizer.
     */
    private void advanceTokenStream() throws LexicalException, IOException {
        _currentToken = _nextToken;

        // if we are not at the end of the file, read a token
        if (_currentToken == null || _currentToken.getType() != TokenType.EOF)
            _nextToken = _tokenizer.getToken();
    }

    /**
     * A convenience method to save writing the following pattern over and over
     * again: make sure the next token is of the required type and, if so,
     * advance. If the token is not of the required type, do not advance.
     * <p>
     * After eatToken is called, _currentToken is the token that was just eaten
     * (so it has the type specified in the call to eatToken).
     * <p>
     * Examples: <code>
     * if (eatToken(TokenType.ID))
     *   // do something with _currentToken.getName(), which eatToken(.) sets
     * else
     *   // error condition: "Identifier expected"
     * 
     * if (!eatToken(TokenType.LEFTPAREN))
     *   // error condition: "if-condition must begin with left parenthesis"
     * </code>
     * 
     * @param type
     *           Token will be eaten if it has this type.
     * @return True if current token has the specified type and stream was
     *         advanced.
     */
    private boolean eatToken(TokenType type) throws LexicalException,
            IOException {
        if (type == _nextToken.getType()) {
            advanceTokenStream();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Throws a syntax exception with the specified message
     * 
     * @param message
     *           Message of the exception
     * @throws SyntaxException
     */
    private void die(String message) throws SyntaxException {
        throw new SyntaxException(_tokenizer.currentLine(), message);
    }

	/**
	 * Eats the given token, calling die() with an appropriate error message if
	 * it isn't found. Returns the token and advances the input stream.
	 */
	private Token expectToken(TokenType type) throws LexicalException,
			IOException, SyntaxException {
		if (type == _nextToken.getType()) {
			Token t = _nextToken;
			advanceTokenStream();
			return t;
		} else {
			die("expected " + type);
			return null;
		}
	}

    /**
     * Creates a parser that reads from stdin (for testing). You must provide
     * two tokens before it will print the first one.
     */
    public static void main(String args[]) throws IOException, LexicalException {
        Parser p = new Parser();
        while (true) {
            if (p.eatToken(TokenType.INTEGER)) {
                System.out.println("Integer: " + p._currentToken.getValue());
            }
            else if (p.eatToken(TokenType.ID)) {
                System.out.println("Identifier:" + p._currentToken.getName());
            }
            else {
                p.advanceTokenStream();
                System.out.println(p._currentToken.getType());
            }
        }
    }
   
	/**
	 * parses a program
	 */
    //This method will create a new NodeProgram by calling listVar() to process
    //any variable declarations and listProc() to process any procedures.
	private NodeProgram parseProgram() throws IOException, LexicalException,
			SyntaxException {
		NodeProgram program = new NodeProgram(listVar(), listProc());
		return program;
	}

	// This method parses a list of procedures. This catches the error
	// of not starting a procedure correctly.
	// The rule being followed here is:
	// procedure int ID ( ProcArgs ) Instr ListProc
	private NodeListProc listProc() throws LexicalException, IOException, SyntaxException {
		NodeListProc _newListProc;
		if (eatToken(TokenType.PROCEDURE)) {
			expectToken(TokenType.INT);
			expectToken(TokenType.ID); //need all of these tokens exactly
			String id = _currentToken.getName();
			expectToken(TokenType.LEFTPAREN);
			NodeListProcDeclArg arglist = procArgs();
			expectToken(TokenType.RIGHTPAREN);
			NodeInstr instr = instr();
			NodeProc _head = new NodeProc(id, arglist, instr); //not in the empty case, so
			_newListProc = new NodeListProcCons(_head, listProc()); //make a cons
		} else if (eatToken(TokenType.EOF)) { //in the empty case, so make a nil
			_newListProc = new NodeListProcNil();
		} else { //error->throw syntax message
			die("expected procedure");
			_newListProc = null;
		}
		return _newListProc;
	}
	
	// this method processes procedure arguments.
	// it follows the rule:
	// int ID ProcArgsSfx
	// it can also be whitespace
	private NodeListProcDeclArg procArgs() throws LexicalException, IOException, SyntaxException { //works
		NodeListProcDeclArg _newNodeListProcDeclArg;
		if (eatToken(TokenType.INT)) {
			expectToken(TokenType.ID);
			String id = _currentToken.getName();
			NodeProcDeclArg _head = new NodeProcDeclArg(id); //not empty case, so make cons
			_newNodeListProcDeclArg = new NodeListProcDeclArgCons(_head, procArgsSfx());
		} else if (_nextToken.getType() == TokenType.RIGHTPAREN) {
			_newNodeListProcDeclArg = new NodeListProcDeclArgNil(); //empty case, so make nil
		} else { //throw error
			die("expected int");
			_newNodeListProcDeclArg = null;
		}
		return _newNodeListProcDeclArg;
	}

	// this method also helps process procedure arguments.
	// it is called whenever the ProcArgsSfx rule is encountered.
	// it follows the rule:
	// , int ID ProcArgsSfx
	// it can also be whitespace
	private NodeListProcDeclArg procArgsSfx() throws LexicalException, IOException, SyntaxException {
		NodeListProcDeclArg _newNodeListProcDeclArg;
		if (eatToken(TokenType.COMMA)) {
			expectToken(TokenType.INT);
			expectToken(TokenType.ID);
			String id = _currentToken.getName();
			NodeProcDeclArg _head = new NodeProcDeclArg(id); //not empty, so make cons
			_newNodeListProcDeclArg = new NodeListProcDeclArgCons(_head, procArgsSfx());
		} else if (_nextToken.getType() == TokenType.RIGHTPAREN) { //empty, so make nil
			_newNodeListProcDeclArg = new NodeListProcDeclArgNil();
		} else { //throw error
			die("expected comma");
			_newNodeListProcDeclArg = null;
		}
		return _newNodeListProcDeclArg;
	}

	//this method processes instructions.
	//errors will not get caught in this method but rather in the decl() method
	//there are a number of rules for instruction:
	//Instr ::= Decl 
	//Instr ::= ID IDInstrSfx ;
	//Instr ::= if ( Expr ) Instr ElseInstr endif
	//Instr ::= while ( Expr ) Instr
	//Instr ::= output ( Expr ) ;
	//Instr ::= input ( ID IDSfx ) ;
	//Instr ::= { ListInstr }	
	//Instr ::= return Expr ;

	private NodeInstr instr() throws LexicalException, IOException, SyntaxException {
		NodeInstr _newNodeInstr;
		if (eatToken(TokenType.ID)) { //in ID rule
			String _id = _currentToken.getName();
			_newNodeInstr = idInstrSfx(_id); //let idInstrSfx make the node
			expectToken(TokenType.SEMICOLON);
		} else if (eatToken(TokenType.IF)) { //in if rule
			expectToken(TokenType.LEFTPAREN);
			NodeExpr _relation = expr();
			expectToken(TokenType.RIGHTPAREN);
			NodeInstr _then = instr();
			NodeInstr _else = elseInstr();
			if (_else == null) { 
				_newNodeInstr = new NodeInstrIfthen(_relation, _then); //no else, so make Ifthen
			} else { 
				_newNodeInstr = new NodeInstrIfthenelse(_relation, _then, _else); //there was an else,
			}                                                                     //so make Ifthenelse
			expectToken(TokenType.ENDIF);
		} else if (eatToken(TokenType.WHILE)) { //while rule
			expectToken(TokenType.LEFTPAREN);
			NodeExpr _relation = expr();
			expectToken(TokenType.RIGHTPAREN);
			NodeInstr _instr = instr();
			_newNodeInstr = new NodeInstrWhile(_relation, _instr); //make while node
		} else if (eatToken(TokenType.OUTPUT)) { //in output rule
			expectToken(TokenType.LEFTPAREN);
			NodeExpr _expr = expr();
			expectToken(TokenType.RIGHTPAREN);
			expectToken(TokenType.SEMICOLON); 
			_newNodeInstr = new NodeInstrOutput(_expr); //make output node
		} else if (eatToken(TokenType.INPUT)) { //in input rule
			expectToken(TokenType.LEFTPAREN);  
			expectToken(TokenType.ID);
			String _id = _currentToken.getName();
			NodeExpr _index = idSfxInstr();
			expectToken(TokenType.RIGHTPAREN);
			expectToken(TokenType.SEMICOLON);
			if (_index == null) {
				_newNodeInstr = new NodeInstrInputSingle(_id); //no index, so make single input node
			} else {
				_newNodeInstr = new NodeInstrInputArray(_id, _index); //index not null, so make array input node
			}
		} else if (eatToken(TokenType.LEFTBRACE)) { //in list of instructions rule
			NodeListInstr _newNodeListInstr = listInstr();
			_newNodeInstr = new NodeInstrBlock(_newNodeListInstr); //make instruction block node
		} else if (eatToken(TokenType.RETURN)) { //in return rule
			NodeExpr _newNodeExpr = expr();
			_newNodeInstr = new NodeInstrReturnExpr(_newNodeExpr); //make return node
			expectToken(TokenType.SEMICOLON);
		} else {
			NodeInstrDecl _newNodeInstrDecl = decl(); //else, assume in decl rule
			_newNodeInstr = _newNodeInstrDecl;        //throw exceptions in decl
		}
		return _newNodeInstr;
	}
	
	//this method handles idSfx when it is called by instr() (rather than by expr())
	//I had to make a separate method here because this should return a NodeInstr instead
	//of a NodeExpr
	private NodeInstr idInstrSfx(String id) throws LexicalException, IOException, SyntaxException {
		NodeInstr _newNodeInstr;
		if (eatToken(TokenType.LEFTPAREN)) { //in procCallArgs rule
			NodeListProcCallArg _newNodeListProcCallArg = procCallArgs();
			_newNodeInstr = new NodeInstrProcCall(id, _newNodeListProcCallArg); //make procedure call instruction node
			expectToken(TokenType.RIGHTPAREN);
		} else { //else, assume in IDSfx rule
			NodeExpr _expr1 = idSfxInstr(); //idSfxInstr will catch errors
			expectToken(TokenType.ASSIGN);
			NodeExpr _expr2 = expr();
			if (_expr1 == null) {
				_newNodeInstr = new NodeInstrAssignSingle( id , _expr2); //make single assignment 
			} else {
				_newNodeInstr = new NodeInstrAssignArray(id, _expr1, _expr2); //assign array
			}
		}
		return _newNodeInstr;
	}
	
	//this method handles idSfx when it comes from IdInstrSfx or instr(). In this case
	//it does not want to return a NodeExprArrayVar, so instead of making
	//one it just uses the NodeExpr returned by expr()
	//it follows the rule:
	//[ Expr ] 
	//it can also be whitespace, in which case it returns null
	private NodeExpr idSfxInstr() throws LexicalException, IOException, SyntaxException {
		if (eatToken(TokenType.LEFTBRACKET)) { //in array case so make an expression 
			NodeExpr _expr = expr();
			expectToken(TokenType.RIGHTBRACKET);
			return _expr;
		} else { //in single case so return null
			return null;
		}
	}

	//this method handles idSfx when it is called by idFactorSfx().
	//in this case, it does want to possibly return a NodeExprArrayVar.
	//it follows the same rules as the node above, but makes a 
	//NodeExprArrayVar out of expr().
	private NodeExpr idSfx(String id) throws LexicalException, IOException, SyntaxException {
		if (eatToken(TokenType.LEFTBRACKET)) {
			NodeExpr _expr = expr();
			expectToken(TokenType.RIGHTBRACKET);
			return new NodeExprArrayVar(id, _expr); //make NodeExprArrayVar
		} else {
			return null;
		}
	}
	
	
	//this method handles an else instruction.
	//it follows the rule:
	// else Instr
	//if there is no else instruction, it returns no so that the if method can 
	//make an Ifthen instead of an IfThenElse
	private NodeInstr elseInstr() throws LexicalException, IOException, SyntaxException {
		if (eatToken(TokenType.ELSE)) { //else instruction, so make new node
			NodeInstr _newInstr = instr();
			return _newInstr;
		} else if (_nextToken.getType() == TokenType.ENDIF) { //no else instruction, so return null
			return null; 
		} else {
			die("expected endif");
			return null;
		}
	}

	//this method handles a list of instructions (that come after
	//a left brace). The method works by first checking if it is at
	//the end of the list--if it is, it will make a nil node. If it 
	//is not, it will make a new Cons node with instr() as the head
	//and a recursive call as the tail. 
	//it follows the rule:
	//{ ListInstr }
	private NodeListInstr listInstr() throws LexicalException, IOException, SyntaxException {
		NodeListInstr _toReturn;
		if (eatToken(TokenType.RIGHTBRACE)) { //end of list--make nil node
			_toReturn = new NodeListInstrNil();
		} else { //non empty case, so recur and make a cons
			NodeInstr _head = instr();
			NodeListInstr _tail = listInstr();
			_toReturn = new NodeListInstrCons(_head, _tail);
		}
		return _toReturn;
	}

	//this method handles the declaration rule of an instruction.
	//this is what will throw an error if something that is supposed
	//to be an instruction has invalid syntax.
	//it follows the rule:
	//int ID DeclSfx;
	private NodeInstrDecl decl() throws LexicalException, IOException, SyntaxException {
		if (_nextToken.getType() == TokenType.INT) { //so that I can have more informative error message
			expectToken(TokenType.INT); 
			expectToken(TokenType.ID);
			String id = _currentToken.getName();
			NodeInstrDecl _newNodeInstrDecl = instrDeclSfx(id); //valid syntax, so make a instruction declaration node
			expectToken(TokenType.SEMICOLON);
			return _newNodeInstrDecl;
		} else {
			die("expected valid instruction"); //because really the instruction wasn't valid
			return null;
		}
	}

	//this method handles a DeclSfx if it is called by an instruction.
	//it follows the rule:
	//[ expr ]
	//it can also be whitespace which indicates a single (rather than an array).
	private NodeInstrDecl instrDeclSfx(String id) throws LexicalException, IOException, SyntaxException {
		NodeInstrDecl _newNodeInstrDecl;
		if (eatToken(TokenType.LEFTBRACKET)) { //array case
			expectToken(TokenType.INTEGER);
			_newNodeInstrDecl = new NodeInstrDeclArray( id, _currentToken.getValue());
			expectToken(TokenType.RIGHTBRACKET);
		} else { 
			_newNodeInstrDecl = new NodeInstrDeclSingle(id); //single case
		}
		return _newNodeInstrDecl;
	}

	
	//this method handles procCallArgs
	//it follows the rule:
	// Expr ProcCallArgsSfx
	//it can also be whitespace, which indicates that a nil node should be made
	private NodeListProcCallArg procCallArgs() throws LexicalException, IOException, SyntaxException {
		NodeListProcCallArg _toReturn;
		if (_nextToken.getType() == TokenType.RIGHTPAREN) { //empty case, so make nil
			_toReturn = new NodeListProcCallArgNil();
		} else {
			NodeExpr _expr = expr(); //non-empty case, so make cons
			NodeProcCallArg _head = new NodeProcCallArg(_expr);
			_toReturn = new NodeListProcCallArgCons(_head, procCallArgsSfx());
		}
		return _toReturn;
	}
	
	//this method handles procedure call arguments sfx
	//it follows the rule
	// , expr procCallArgsSfx
	//it can also be whitespace, which indicates that a nil node should be made
	private NodeListProcCallArg procCallArgsSfx() throws LexicalException, IOException, SyntaxException {
		NodeListProcCallArg _toReturn;
		if (eatToken(TokenType.COMMA)) { //non-empty case, so make cons and recur
			NodeExpr _expr = expr();
			NodeProcCallArg _head = new NodeProcCallArg(_expr);
			_toReturn = new NodeListProcCallArgCons(_head, procCallArgsSfx());
		} else { //empty case, so make nil
			_toReturn = new NodeListProcCallArgNil();
		}
		return _toReturn;
	}

	//this node handles expressions.
	//the way that the method works is creating an expression
	//that will either be the expression that the method returns or
	//will be the left child of an expression returned by calling 
	//exprSfx on the rest of the expression. In either case, it makes
	//the left child and calls exprSfx(). If that method call returns
	//null, though, then we know we were in the whitespace case of
	//exprSfx and we just want to return the expression that would
	//have been the left child.
	private NodeExpr expr() throws LexicalException, IOException, SyntaxException {
		NodeExpr _left = null;
		if (eatToken(TokenType.TRUE)) {
			_left = new NodeExprTrue();
		} else if (eatToken(TokenType.FALSE)) {
			_left = new NodeExprFalse();
		} else if (eatToken(TokenType.LEFTPAREN)) {
			_left = expr();
			expectToken(TokenType.RIGHTPAREN);
		} else if (eatToken(TokenType.NOT)) {
			expectToken(TokenType.LEFTPAREN);
			_left = new NodeRelationNot(expr());
			expectToken(TokenType.RIGHTPAREN);
		} else if (eatToken(TokenType.INTEGER)) {
			_left = new NodeExprInt(_currentToken.getValue());
		} else if (eatToken(TokenType.ID)) {
			String _id = _currentToken.getName();
			_left = idFactorSfx(_id);
		} else {
			die("expected valid expression");
			return null;
		}
		NodeExpr _fromExprSfx = exprSfx(_left); //try using _left as a child node
		if (_fromExprSfx == null) { //there was no suffix, so just return _left
			return _left;
		} else {
			return _fromExprSfx; //exprSfx() made a node with _left as a child
		}
	}

	//this method handles expression suffixes.
	//there are a number of rules that it could be
	//that I won't enumerate here.
	//if it is not in the whitespace case, it makes a node using the 
	//procedure argument as the left child and the return from another
	//call to expr() as the right child. If it is in the whitespace 
	//case, it just returns null so that expr() knows what case it
	//was in.
	private NodeExpr exprSfx(NodeExpr left) throws LexicalException, IOException, SyntaxException {
		NodeExpr _toReturn;
		if (eatToken(TokenType.PLUS)) { 
			NodeExpr _right = expr();
			_toReturn = new NodeExprPlus(left, _right);
		} else if (eatToken(TokenType.MINUS)) {
			NodeExpr _right = expr();
			_toReturn = new NodeExprMinus(left, _right);
		} else if (eatToken(TokenType.TIMES)) {
			NodeExpr _right = expr();
			_toReturn = new NodeExprTimes(left, _right);
		} else if (eatToken(TokenType.DIVIDE)) {
			NodeExpr _right = expr();
			_toReturn = new NodeExprDivide(left, _right);
		} else if (eatToken(TokenType.MODULUS)) {
			NodeExpr _right = expr();
			_toReturn = new NodeExprMod(left, _right);
		} else if (eatToken(TokenType.LESSTHAN)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationLessThan(left, _right);
		} else if (eatToken(TokenType.LESSEQ)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationLessThanEqual(left, _right);
		} else if (eatToken(TokenType.GREATERTHAN)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationGreaterThan(left, _right);
		} else if (eatToken(TokenType.GREATEREQ)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationGreaterThanEqual(left, _right);
		} else if (eatToken(TokenType.EQUAL)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationEqual(left, _right);
		} else if (eatToken(TokenType.AND)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationAnd(left, _right);
		} else if (eatToken(TokenType.OR)) {
			NodeExpr _right = expr();
			_toReturn = new NodeRelationOr(left, _right);
		} else { //no suffix, so return null
			_toReturn = null;
		}
		return _toReturn;
	}
	
	//this method handles idFactorSfx.
	//it can either be an IDSfx or a list of procedure call arguments
	//in either case, it just makes an appropriate NodeExpr and returns
	//it.
	private NodeExpr idFactorSfx(String id) throws LexicalException, IOException, SyntaxException {
		if (eatToken(TokenType.LEFTPAREN)) { //procCallArgs case
			NodeListProcCallArg _newNodeListProcCallArg = procCallArgs();
			expectToken(TokenType.RIGHTPAREN);
			return new NodeExprProcCall(id, _newNodeListProcCallArg);
		} else { //idSfx case
			NodeExpr _expr = idSfx(id);
			if (_expr == null) { //just make single variable node
				return new NodeExprVar(id);
			} else {
				return _expr; 
			}
		}
	}

	
	//this method handles a list of variable declarations.
	//it follows the rule:
	// var int ID DeclSfx
	//it can also be a whitespace, in which case it makes a nil node
	private NodeListVarDecl listVar() throws LexicalException, SyntaxException, IOException {
		NodeListVarDecl _newListVar;
		if (eatToken(TokenType.VAR)) { //non-empty case, so make cons and recur
			expectToken(TokenType.INT);
			expectToken(TokenType.ID);
			String id = _currentToken.getName();
			NodeVarDecl _newVarDecl = listDeclSfx(id);
			expectToken(TokenType.SEMICOLON);
			_newListVar = new NodeListVarDeclCons(_newVarDecl, listVar());
		} else if (_nextToken.getType() == TokenType.PROCEDURE){ //base case, so make nil
			_newListVar = new NodeListVarDeclNil();
		} else {
			die("expected variable"); //catch errors
			_newListVar = null;
		}
		return _newListVar;
	}

	//this class handles a list declaration suffix
	//it can either be a single declaration or an array declaration.
	//it deals with these cases by either making an array or single.
	private NodeVarDecl listDeclSfx(String identifier) throws LexicalException, IOException, SyntaxException { //see if i can combine this with the other declsfx method...
		NodeVarDecl _newVarDecl;
		if (eatToken(TokenType.LEFTBRACKET)) { //non empty case, so make array
			expectToken(TokenType.INTEGER);
			_newVarDecl = new NodeVarDeclArray( identifier ,_currentToken.getValue());
			expectToken(TokenType.RIGHTBRACKET);
		} else {
			_newVarDecl = new NodeVarDeclSingle(identifier); //empty, so make a single
		}
		return _newVarDecl;
	}
	

	
	
}
