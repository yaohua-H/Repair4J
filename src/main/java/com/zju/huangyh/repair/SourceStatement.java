package com.zju.huangyh.repair;

import org.eclipse.jdt.core.dom.Statement;

/**
 * Holds a Statement object and the source file the statement came from.
 *
 * This is a bit inefficient to do, since we could store AST objects and
 * the location of the source file that was parsed to generate it. We don't
 * actually need to store the ASTs because we can get them from the
 * Statement, so by doing this we save ourselves from building that AST
 * holding data structure.
 *
 * @ClassName SourceStatement
 * @Author huangyaohua
 */
public class SourceStatement {

    public String sourceFile;
    public Statement statement;
    public boolean inUse;

    public SourceStatement(String sourceFile, Statement statement) {
        this.sourceFile = sourceFile;
        this.statement = statement;
        this.inUse = false;
    }
}
