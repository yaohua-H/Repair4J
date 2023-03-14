package com.zju.huangyh.repair.mutation;

import com.zju.huangyh.repair.DocumentASTRewrite;
import com.zju.huangyh.repair.SourceStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.IDocument;

import java.util.HashMap;

/**
 * @ClassName Mutation
 * @Author huangyaohua
 */
public abstract class Mutation {

    protected HashMap<String, DocumentASTRewrite> sourceMap;
    protected DocumentASTRewrite docrwt;
    protected SourceStatement faulty;
    protected SourceStatement seed;
    protected IDocument document;
    protected ASTRewrite rewrite;
    private boolean mutated;

    /**
     * Creates a new Mutation object.
     *
     * @param sourceMap The map of source file paths to source file contents.
     * @param faulty    A potentially faulty statement.
     * @param seed      A seed statement (from somewhere in the program source).
     */
    public Mutation(HashMap<String, DocumentASTRewrite> sourceMap, SourceStatement faulty, SourceStatement seed) {
        this.docrwt = sourceMap.get(faulty.sourceFile);
        this.rewrite = docrwt.rewriter;
        this.document = docrwt.document;
        this.sourceMap = sourceMap;
        this.faulty = faulty;
        this.seed = seed;
        this.mutated = false;
    }

    /**
     * Uses the seed statement to apply a mutation to the faulty statement.
     *
     * @throws Exception
     */
    public void mutate() throws Exception {
        if (mutated)
            throw new Exception("A mutate operation has already been applied. Must call undo() before mutating again.");

        /* The subclass will perform its mutation. */
        this.concreteMutate();

        this.mutated = true;
    }

    /**
     * Each concrete mutator will perform its own mutation to the AST and
     * write the new AST to a document in DocumentASTRewrite.
     *
     * @throws Exception
     */
    protected abstract void concreteMutate() throws Exception;

    /**
     * Undoes the mutation that was applied in mutate().
     * <p>
     * This will undo both the change to the text file (Document) as
     * well as the change to the AST.
     * <p>
     * Best used with memento pattern.
     */
    public void undo() throws Exception {
        if (!mutated)
            throw new Exception("The mutate operation has not been applied. Must call mutate() before undo().");

        /* Undo the operation applied to the AST. */
        this.concreteUndo();

        /* Set the status to not mutated. */
        this.mutated = false;

        /* Relinquish use of the statements. */
        this.faulty.inUse = false;
        if (this.seed != null) this.seed.inUse = false;
    }

    /**
     * Each concrete mutator must also be able to undo its mutation. Because
     * mutation operations are recursive, the concrete mutator must undo its
     * own AST mutation, then re-write the document.
     *
     * @throws Exception
     */
    protected abstract void concreteUndo() throws Exception;

    /**
     * Returns the document modified by the mutation.
     *
     * @return
     */
    public DocumentASTRewrite getRewriter() throws Exception {
        if (!mutated)
            throw new Exception("No mutation operation has been applied. Must call mutate() before getMutatedDocument().");
        return this.docrwt;
    }

    /**
     * Returns true if a mutation operation has been applied.
     */
    public boolean mutated() {
        return this.mutated;
    }

    @Override
    public String toString() {
        String s = "\nFaulty = " + this.faulty.sourceFile + "@" + this.faulty.statement.getStartPosition() + ": " + this.faulty.statement + "\n\n";
        s += "Seed = ";
        if (this.seed == null) s += "null" + ": " + "\n============================\n";
        else
            s += this.seed.sourceFile + "@" + this.seed.statement.getStartPosition() + ": " + this.seed.statement + "\n============================\n";

        return s;
    }
}
