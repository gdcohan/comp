package comp;

import comp.nodes.*;

/**
 * Base interface for classes implementing the visitor pattern. A Visitor
 * implementation must be able to handle every non-abstract Node class.
 */
public interface Visitor {

    /**
     * Handles visiting a NodeInstrDeclArray
     */
    public void handleNodeInstrDeclArray(NodeInstrDeclArray node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeProc
     */        
    public void handleNodeProc(NodeProc node) throws VisitorException;
    
    /**
     * Handles visiting a NodeProcDeclArg
     */        
    public void handleNodeProcDeclArg(NodeProcDeclArg node) throws VisitorException;
    
    /**
     * Handles visiting a NodeProcCallArg
     */        
    public void handleNodeProcCallArg(NodeProcCallArg node) throws VisitorException;

    /**
     * Handles visiting a NodeInstrDeclSingle
     */
    public void handleNodeInstrDeclSingle(NodeInstrDeclSingle node)
            throws VisitorException;

    /**
     * Handles visiting a NodeExprArrayVar
     */
    public void handleNodeExprArrayVar(NodeExprArrayVar node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeExprProcCall
     */
    public void handleNodeExprProcCall(NodeExprProcCall node)
            throws VisitorException;

    /**
     * Handles visiting a NodeExprInt
     */
    public void handleNodeExprInt(NodeExprInt node) 
            throws VisitorException;

	 /**
     * Handles visiting a NodeExprFalse
     */
    public void handleNodeExprFalse(NodeExprFalse node) 
            throws VisitorException;
            
    /**
     * Handles visiting a NodeExprTrue
     */
    public void handleNodeExprTrue(NodeExprTrue node) 
            throws VisitorException;

    /**
     * Handles visiting a NodeExprMinus
     */
    public void handleNodeExprMinus(NodeExprMinus node) 
            throws VisitorException;

    /**
     * Handles visiting a NodeExprPlus
     */
    public void handleNodeExprPlus(NodeExprPlus node) 
            throws VisitorException;

    /**
     * Handles visiting a NodeExprTimes
     */
    public void handleNodeExprTimes(NodeExprTimes node) 
            throws VisitorException;
    
    /**
     * Handles visiting a NodeExprDivide
     */
    public void handleNodeExprDivide(NodeExprDivide node) 
            throws VisitorException;
    
    /**
     * Handles visiting a NodeExprMod
     */
    public void handleNodeExprMod(NodeExprMod node) 
            throws VisitorException;

    /**
     * Handles visiting a NodeExprVar
     */
    public void handleNodeExprVar(NodeExprVar node) 
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrAssignArray
     */
    public void handleNodeInstrAssignArray(NodeInstrAssignArray node)
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrAssignSingle
     */
    public void handleNodeInstrAssignSingle(NodeInstrAssignSingle node)
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrBlock
     */
    public void handleNodeInstrBlock(NodeInstrBlock node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeInstrProcCall
     */
    public void handleNodeInstrProcCall(NodeInstrProcCall node)
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrIfthen
     */
    public void handleNodeInstrIfthen(NodeInstrIfthen node)
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrIfthenelse
     */
    public void handleNodeInstrIfthenelse(NodeInstrIfthenelse node)
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrOutput
     */
    public void handleNodeInstrOutput(NodeInstrOutput node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeInstrInputArray
     */
    public void handleNodeInstrInputArray(NodeInstrInputArray node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeInstrInputSingle
     */
    public void handleNodeInstrInputSingle(NodeInstrInputSingle node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeInstrReturnExpr
     */
    public void handleNodeInstrReturnExpr(NodeInstrReturnExpr node)
            throws VisitorException;

    /**
     * Handles visiting a NodeInstrWhile
     */
    public void handleNodeInstrWhile(NodeInstrWhile node)
            throws VisitorException;

    /**
     * Handles visiting a NodeListInstrCons
     */
    public void handleNodeListInstrCons(NodeListInstrCons node)
            throws VisitorException;

    /**
     * Handles visiting a NodeListInstrNil
     */
    public void handleNodeListInstrNil(NodeListInstrNil node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeListProcCallArgCons
     */
    public void handleNodeListProcCallArgCons(NodeListProcCallArgCons node)
            throws VisitorException;

    /**
     * Handles visiting a NodeListProcCallArgNil
     */
    public void handleNodeListProcCallArgNil(NodeListProcCallArgNil node)
            throws VisitorException;
            
     /**
     * Handles visiting a NodeListProcDeclArgCons
     */
    public void handleNodeListProcDeclArgCons(NodeListProcDeclArgCons node)
            throws VisitorException; 

    /**
     * Handles visiting a NodeListProcDeclArgNil
     */
    public void handleNodeListProcDeclArgNil(NodeListProcDeclArgNil node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeListVarDeclCons
     */
    public void handleNodeListVarDeclCons(NodeListVarDeclCons node)
            throws VisitorException; 

    /**
     * Handles visiting a NodeListVarDeclNil
     */
    public void handleNodeListVarDeclNil(NodeListVarDeclNil node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeVarDeclArray
     */
    public void handleNodeVarDeclArray(NodeVarDeclArray node)
            throws VisitorException; 

    /**
     * Handles visiting a NodeVarDeclSingle
     */
    public void handleNodeVarDeclSingle(NodeVarDeclSingle node)
            throws VisitorException;
            
    /**
     * Handles visiting a NodeListProcCons
     */
    public void handleNodeListProcCons(NodeListProcCons node)
            throws VisitorException;

    /**
     * Handles visiting a NodeListProcNil
     */
    public void handleNodeListProcNil(NodeListProcNil node)
            throws VisitorException;

    /**
     * Handles visiting a NodeProgram
     */
    public void handleNodeProgram(NodeProgram node) 
            throws VisitorException;

    /**
     * Handles visiting a NodeRelationLessThan
     */
    public void handleNodeRelationLessThan(NodeRelationLessThan node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeRelationLessThanEqual
     */
    public void handleNodeRelationLessThanEqual(NodeRelationLessThanEqual node)
            throws VisitorException;

    /**
     * Handles visiting a NodeRelationEqual
     */
    public void handleNodeRelationEqual(NodeRelationEqual node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeRelationGreaterThanEqual
     */
    public void handleNodeRelationGreaterThanEqual(NodeRelationGreaterThanEqual node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeRelationGreaterThan
     */
    public void handleNodeRelationGreaterThan(NodeRelationGreaterThan node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeRelationAnd
     */
    public void handleNodeRelationAnd(NodeRelationAnd node)
            throws VisitorException;

    /**
     * Handles visiting a NodeRelationOr
     */
    public void handleNodeRelationOr(NodeRelationOr node)
            throws VisitorException;
    
    /**
     * Handles visiting a NodeRelationNot
     */
    public void handleNodeRelationNot(NodeRelationNot node)
            throws VisitorException;
}
