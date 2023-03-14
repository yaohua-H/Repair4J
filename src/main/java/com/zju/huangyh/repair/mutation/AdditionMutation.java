package com.zju.huangyh.repair.mutation;

import com.zju.huangyh.repair.DocumentASTRewrite;
import com.zju.huangyh.repair.SourceStatement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEdit;

import java.util.HashMap;

/**
 * @ClassName AdditionMutation
 * @Author huangyaohua
 */
public class AdditionMutation extends Mutation {

    private Block addedBlock;        // The block that has been added containing the faulty and seed statements.

    public AdditionMutation(HashMap<String, DocumentASTRewrite> sourceFileContents, SourceStatement faulty, SourceStatement seed) {
        super(sourceFileContents, faulty, seed);
    }

    /**
     * Adds the seed statement to the AST right before the
     * faulty statement.
     */
    @Override
    public void concreteMutate() throws Exception {
        /* Create a new block to insert in place of the deleted statement. */
        this.addedBlock = (Block) this.rewrite.getAST().createInstance(Block.class);

        /* Make a copy of the seed statement and base it in the faulty statement's AST. */
        ASTNode seedCopy = ASTNode.copySubtree(this.rewrite.getAST(), seed.statement);
        ASTNode faultyCopy = ASTNode.copySubtree(this.rewrite.getAST(), faulty.statement);

        /* Insert the seed and faulty statements into the new Block. */
        ListRewrite lrwt = this.rewrite.getListRewrite(this.addedBlock, Block.STATEMENTS_PROPERTY);
        lrwt.insertFirst(seedCopy, null);
        lrwt.insertLast(faultyCopy, null);

        /* Replace the faulty statement with the new Block. */
        rewrite.replace(faulty.statement, this.addedBlock, null);

        /* Modify the source code file. */
        this.docrwt.resetModifiedDocument(); // Start with the original document to avoid the AST-doesn't-match-doc error.
        TextEdit edits = rewrite.rewriteAST(this.docrwt.modifiedDocument, null);
        edits.apply(this.docrwt.modifiedDocument, TextEdit.NONE);
    }

    /**
     * Removes the statement added in mutate().
     */
    @Override
    public void concreteUndo() throws Exception {
        /* Undo the edit to the AST. */
        this.rewrite.replace(this.addedBlock, this.faulty.statement, null);

        /* We need to write the undo changes back to the source file because of recursion. */
        this.docrwt.resetModifiedDocument(); // Start with the original document to avoid the AST-doesn't-match-doc error.
        TextEdit edits = rewrite.rewriteAST(this.docrwt.modifiedDocument, null);
        edits.apply(this.docrwt.modifiedDocument, TextEdit.NONE);
    }

    @Override
    public String toString() {
        return "Addition " + super.toString();
    }
}
