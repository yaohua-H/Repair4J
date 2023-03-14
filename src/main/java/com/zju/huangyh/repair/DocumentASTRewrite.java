package com.zju.huangyh.repair;

import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import java.io.File;

/**
 * Stores a Document/ASTRewrite pair so that we can synchronize changes to Documents and ASTs.
 *
 * @ClassName DocumentASTRewrite
 * @Author huangyaohua
 */
public class DocumentASTRewrite {

    public File backingFile;
    public IDocument document;
    public IDocument modifiedDocument;
    public ASTRewrite rewriter;
    private boolean modified;
    private boolean tainted;

    public DocumentASTRewrite(IDocument document, File backingFile, ASTRewrite rewriter){
        this.backingFile = backingFile;
        this.document = document;
        this.rewriter = rewriter;
        this.tainted = false;
        this.modified = false;

        this.resetModifiedDocument();
    }

    public void taintDocument(){
        this.modified = true;
        this.tainted = true;
    }

    public void untaintDocument(){
        this.tainted = false;
    }

    public boolean isDocumentTainted(){
        return this.tainted;
    }

    public void resetModifiedDocument(){
        this.modifiedDocument = new Document(document.get());
    }

    public boolean isDocumentModified(){
        return this.modified;
    }
}
