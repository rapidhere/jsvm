/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.parser;

import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Javascript Compiler
 *
 * @author rapidhere@gmail.com
 * @version $id: JSCompiler.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public interface JSCompiler {
    /**
     * parse the file to a js ast root
     *
     * @param f the file to compile
     * @return parsed ast root
     * @throws FileNotFoundException
     */
    default AbstractSyntaxTree parse(File f) throws FileNotFoundException {
        return parse(new FileInputStream(f));
    }

    /**
     * parse the content to a js ast root
     *
     * @param content the content to compile
     * @return parsed ast root
     */
    default AbstractSyntaxTree parse(String content) {
        return parse(new ByteArrayInputStream(content.getBytes()));
    }

    /**
     * parse the input stream to a js ast root
     * compiler will close the stream
     *
     * @param inputStream the input stream
     * @return parsed ast root
     */
    AbstractSyntaxTree parse(InputStream inputStream);
}
