/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.parser;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import ranttu.rapid.jsvm.exp.CompileFailed;
import ranttu.rapid.jsvm.exp.CompileInterrupted;
import ranttu.rapid.jsvm.jscomp.ast.AstRoot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * a js compiler use javascript's acorn lib, require Node.js installed
 *
 * @author rapidhere@gmail.com
 * @version $id: AcornJSCompiler.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class AcornJSCompiler implements JSCompiler {
    private static final String ACORN_SCRIPT_FILE_NAME = "_$acorn.js";

    private static final String SOURCE_FILE_NAME       = "_$source.js";

    /**
     * @see JSCompiler#parse(InputStream)
     */
    @Override
    public AstRoot parse(InputStream inputStream) {
        JSONObject jsonAst = new JSONObject(getESTreeString(inputStream));
        return null;
    }

    /**
     * get a ast string valid to es tree
     */
    private String getESTreeString(InputStream inputStream) {
        try {
            cleanUp();
            saveSources(inputStream);

            Process process = new ProcessBuilder("node", "-e", generateCompileScript(), "-p")
                .start();

            // exit failed
            if (process.waitFor() != 0) {
                throw new CompileFailed(IOUtils.toString(process.getErrorStream(), "utf-8").trim());
            }

            return IOUtils.toString(process.getInputStream(), "utf-8");
        } catch (IOException e) {
            throw new CompileFailed("wrong with acorn compile env", e);
        } catch (InterruptedException e) {
            throw new CompileInterrupted(e);
        } finally {
            cleanUp();
        }
    }

    /**
     * generate the compile script
     */
    private String generateCompileScript() {
        return String.format(
            "try { JSON.stringify(require('./%s').parse(require('fs').readFileSync('%s'))) } "
                    + "catch(err) {console.error(err.message);process.exit(1);}",
            ACORN_SCRIPT_FILE_NAME, SOURCE_FILE_NAME);
    }

    /**
     * save the compile needed sources
     */
    private void saveSources(InputStream inputStream) throws IOException {
        Files.copy(getClass().getResourceAsStream("/acorn/acorn.js"), new File(
            ACORN_SCRIPT_FILE_NAME).toPath());
        Files.copy(inputStream, new File(SOURCE_FILE_NAME).toPath());
    }

    /**
     * clean up the working directory
     */
    private void cleanUp() {
        try {
            Files.deleteIfExists(new File(SOURCE_FILE_NAME).toPath());
            Files.deleteIfExists(new File(ACORN_SCRIPT_FILE_NAME).toPath());
        } catch (IOException ignore) {
        }
    }
}
