package com.zju.huangyh.repair.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject.Kind;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MemoryFileManager
 * @Author huangyaohua
 */
public class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    public final Map<String, Output> map = new HashMap<String, Output>();

    MemoryFileManager(JavaCompiler compiler) {
        super(compiler.getStandardFileManager(null, null, null));
    }

    @Override
    public Output getJavaFileForOutput(Location location, String name, Kind kind, FileObject source) {
        Output mc = new Output(name, kind);
        this.map.put(name, mc);
        return mc;
    }
}
