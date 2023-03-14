package com.zju.huangyh.repair.context;

import com.zju.huangyh.repair.DocumentASTRewrite;
import com.zju.huangyh.repair.SourceStatement;
import com.zju.huangyh.repair.Statements;
import com.zju.huangyh.repair.mutation.*;

import java.util.HashMap;
import java.util.Random;

/**
 * MutationContext stores the context for mutation operations and
 * provides services to enable mutations.
 * <p>
 * The main operations are:
 * getRandomMutation - Randomly selects and builds a mutation.
 *
 * @ClassName MutationContext
 * @Author huangyaohua
 */
public class MutationContext {

    private HashMap<String, DocumentASTRewrite> sourceFileContents;

    private Statements faultyStatements;
    private Statements seedStatements;

    private Random random;

    /**
     * Create a new MutationContext object.
     * @param sourceFileContents The map of source file names to their DocumentASTRewriter.
     * @param faultyStatements The list of faulty statements to mutate.
     * @param seedStatements The list of seed statements to mutate with.
     * @param random The random number generator (for random statement selection).
     */
    public MutationContext(HashMap<String, DocumentASTRewrite> sourceFileContents,
                           Statements faultyStatements, Statements seedStatements,
                           Random random){
        this.sourceFileContents = sourceFileContents;
        this.faultyStatements = faultyStatements;
        this.seedStatements = seedStatements;
        this.random = random;
    }

    /**
     * Returns a random mutation operation.
     *
     * @return A Mutation memento object for applying one mutation to a faulty statement.
     */
    public Mutation getRandomMutation(MutationType type) throws Exception {
        SourceStatement faultyStatement;
        Mutation mutation;

        switch(type){
            case NULL: // Use for testing (e.g., to make sure the program compiles without mutations).
                System.out.print("Applying null mutation...");
                mutation = new NullMutation(sourceFileContents, faultyStatements.getRandomStatement(), null);
                break;
            case ADDITION:
                System.out.print("Applying addition mutation...");
                faultyStatement = faultyStatements.getRandomStatement();
                mutation = new AdditionMutation(sourceFileContents, faultyStatement, seedStatements.getRandomStatement(faultyStatement));
                break;
            case REPLACEMENT:
                System.out.print("Applying replacement mutation...");
                faultyStatement = faultyStatements.getRandomStatement();
                mutation = new ReplacementMutation(sourceFileContents, faultyStatement, seedStatements.getRandomStatement(faultyStatement));
                break;
            case DELETION:
                System.out.print("Applying deletion mutation...");
                mutation = new DeletionMutation(sourceFileContents, faultyStatements.getRandomStatement(), null);
                break;
            default:
                throw new Exception("MutationContext: MutationType not recognised.");
        }

        return mutation;
    }

    /**
     * Returns a random mutation type.
     *
     * NOTE: Will never return NULL mutation type.
     *
     * @return A MutationType that can be passed to getRandomMutation.
     */
    public MutationType getRandomMutationType() throws Exception {
        return MutationType.values()[(new Double(Math.ceil((this.random.nextDouble() * (MutationType.values().length - 1))))).intValue()];
    }

    public enum MutationType {
        NULL, // Must be manually selected (won't be selected randomly in getRandomMutation)
        ADDITION,
        REPLACEMENT,
        DELETION
    }
}
