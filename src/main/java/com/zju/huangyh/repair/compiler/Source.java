package com.zju.huangyh.repair.compiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * @ClassName Source
 * @Author huangyaohua
 */
public class Source extends SimpleJavaFileObject {

    private final String content;

    public Source(String name, Kind kind, String content) {
        super(URI.create("memo:///" + name.replace('.', '/') + kind.extension), kind);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignore) {
        return this.content;
    }
}
