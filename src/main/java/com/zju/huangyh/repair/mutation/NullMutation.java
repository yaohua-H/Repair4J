package com.zju.huangyh.repair.mutation;

import com.zju.huangyh.repair.DocumentASTRewrite;
import com.zju.huangyh.repair.SourceStatement;

import java.util.HashMap;

/**
 * @ClassName NullMutation
 * @Author huangyaohua
 */
public class NullMutation extends Mutation{

    public NullMutation(HashMap<String, DocumentASTRewrite> sourceFileContents, SourceStatement faulty, SourceStatement seed){
        super(sourceFileContents, faulty, seed);
    }

    /**
     * Replaces the faulty statement with the seed statement.
     */
    @Override
    public void concreteMutate() throws Exception {/* Don't do anything. */	}

    /**
     * Replace the seed statement with the faulty statement.
     */
    @Override
    public void concreteUndo() throws Exception{ /* Don't do anything. */ }

    @Override
    public String toString(){
        return "Null " + super.toString();
    }
}
