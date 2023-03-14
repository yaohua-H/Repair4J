package com.zju.huangyh.repair.context;

import com.zju.huangyh.repair.DocumentASTRewrite;
import com.zju.huangyh.repair.LineCoverage;
import com.zju.huangyh.repair.MutationASTRequestor;
import com.zju.huangyh.repair.Statements;
import org.eclipse.jdt.core.dom.*;

import java.util.HashMap;
import java.util.HashSet;

/**
 * ParserContext stores the context for and provides parsing operations.
 *
 * @ClassName ParserContext
 * @Author huangyaohua
 */
public class ParserContext {

    private ASTParser parser;
    private HashMap<String, HashSet<String>> scope;
    private HashMap<String, DocumentASTRewrite> sourceFileContents;
    private String[] classpaths;
    private String[] sourcepaths;
    private String[] sourceFilesArray;
    private LineCoverage faultyLineCoverage;
    private LineCoverage seedLineCoverage;
    private Statements faultyStatements;
    private Statements seedStatements;

    public ParserContext(HashMap<String, HashSet<String>> scope,
                         HashMap<String, DocumentASTRewrite> sourceFileContents,
                         String[] classpaths, String[] sourcepaths, String[] sourceFilesArray,
                         LineCoverage faultyLineCoverage, LineCoverage seedLineCoverage,
                         Statements faultyStatements, Statements seedStatements){

        this.parser = ASTParser.newParser(AST.JLS8);

        this.scope = scope;
        this.sourceFileContents = sourceFileContents;
        this.classpaths = classpaths;
        this.sourcepaths = sourcepaths;
        this.sourceFilesArray = sourceFilesArray;
        this.faultyLineCoverage = faultyLineCoverage;
        this.seedLineCoverage = seedLineCoverage;
        this.faultyStatements = faultyStatements;
        this.seedStatements = seedStatements;
    }

    /**
     * Create the ASTParser with the source files to generate ASTs for, and set up the
     * environment using ASTParser.setEnvironment.
     *
     * NOTE: This MUST be called before anything else in Repair4J.
     */
    public void buildASTs() throws Exception{

        parser.setEnvironment(this.classpaths, this.sourcepaths, null, true);
        parser.setResolveBindings(true);

        /* Set up the AST handler. We need to create LineCoverage and Statements classes to store
         * and filter the statements from the ASTs. */
        FileASTRequestor fileASTRequestor = new MutationASTRequestor(sourceFileContents, scope, faultyLineCoverage, seedLineCoverage, faultyStatements, seedStatements);

        parser.createASTs(sourceFilesArray, null, new String[] {}, fileASTRequestor, null);
    }

    /**
     * Checks that the AST produced by the mutation has all variables in-scope.
     *
     * To do this, we create a new AST by parsing the mutated source and compute
     * bindings. We then look to see if there are any bindings missing. If bindings
     * are missing, there is something not in-scope and the program will not compile.
     *
     * @param rewriter The AST rewriter that contains the mutated document.
     */
    public boolean checkScope(DocumentASTRewrite rewriter) {
        String source = rewriter.modifiedDocument.get();

        this.parser.setSource(source.toCharArray());
        this.parser.setEnvironment(this.classpaths, this.sourcepaths, null, true);
        this.parser.setResolveBindings(true);
        this.parser.setUnitName(rewriter.backingFile.getName());

        ASTNode node = this.parser.createAST(null);

        ScopeASTVisitor scopeASTVisitor = new ScopeASTVisitor();
        node.accept(scopeASTVisitor);

        if(!scopeASTVisitor.inScope){
            System.out.println(" - Some variables are out of scope.");
            return false;
        }

        return true;
    }

    /**
     * Checks that each variable, field, method and type has a binding. If
     * one of these does not have a binding, we assume it is out of scope.
     * This is used for checking to make sure mutated ASTs will be compilable.
     */
    private class ScopeASTVisitor extends ASTVisitor {

        public boolean inScope;

        public ScopeASTVisitor(){
            this.inScope = true;
        }

        /**
         * Check that each QualifiedName has a binding. This
         * will ensure that the fully qualified name gets
         * checked instead of it's parts.
         */
        public boolean visit(QualifiedName qn){

            if(!this.inScope) return false; // No point in checking if we're already out of scope

            if(qn.resolveBinding() == null) {
                this.inScope = false;
            }

            return false;
        }

        /**
         * Check that each SimpleName ASTNode has a binding. These
         * will be variables, fields, methods and types.
         */
        public boolean visit(SimpleName s) {

            if(!this.inScope) return false; // No point in checking if we're already out of scope

            if(s.resolveBinding() == null) {
                this.inScope = false;
            }

            return false;
        }
    }


}
