package com.zju.huangyh.repair.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.net.URI;

/**
 * @ClassName Output
 * @Author huangyaohua
 */
public class Output extends SimpleJavaFileObject {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    Output(String name, Kind kind) {
        super(URI.create("memo:///" + name.replace('.', '/') + kind.extension), kind);
    }

    byte[] toByteArray() {
        return this.baos.toByteArray();
    }

    @Override
    public ByteArrayOutputStream openOutputStream() {
        return this.baos;
    }

}
