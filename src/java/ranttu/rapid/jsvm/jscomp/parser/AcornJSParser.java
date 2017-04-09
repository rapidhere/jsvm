/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.parser;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.exp.CompileError;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * a js compiler use javascript's acorn lib, require nashorn library
 *
 * @author rapidhere@gmail.com
 * @version $id: AcornJSParser.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class AcornJSParser implements Parser {
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    /**
     * @see Parser#parse(InputStream)
     */
    @Override
    public AbstractSyntaxTree parse(InputStream inputStream) {
        JSONObject esTreeAst = new JSONObject(getESTreeString(inputStream));
        return AbstractSyntaxTree.fromJson(esTreeAst);
    }

    /**
     * get a ast string valid to es tree
     * see <a href="https://github.com/estree/estree">es tree</a>
     */
    private String getESTreeString(InputStream inputStream) {
        try {
            engine.eval(new InputStreamReader(
                    getClass().getResourceAsStream("/acorn/acorn.js")));

            // add a entry point
            engine.eval(
                    "function parse(source) { return JSON.stringify(acorn.parse.call(" +
                            "acorn, source, {locations:true})); }");

            Invocable invoker = $$.cast(engine);

            Object ret = invoker.invokeFunction(
                    "parse", IOUtils.toString(inputStream, "utf-8"));

            return $$.cast(ret);
        } catch (IOException e) {
            throw new CompileError("wrong with acorn compile env", e);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new CompileError(e.toString());
        }
    }
}
