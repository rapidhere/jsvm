/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;

/**
 * the pass that build name scope
 *
 * @author rapidhere@gmail.com
 * @version $id: BuildNameScopePass.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class BuildNameScopePass extends CompilePass<Void> {
    private NamingEnvironment lexicalEnv;
    private NamingEnvironment variableEnv;

    public BuildNameScopePass(Compiler compiler) {
        super(compiler);

        // ~setup
        compiler.lexicalEnvironment = new NamingEnvironment();
        compiler.variableEnvironment = new NamingEnvironment();

        // rename
        this.lexicalEnv = compiler.lexicalEnvironment;
        this.variableEnv = compiler.variableEnvironment;
    }

    @Override
    protected Void visit(Program program) {
        // TODO init global environment
        program.getBody().forEach(this::visit);

        return null;
    }

    @Override
    protected Void visit(VariableDeclaration variableDeclaration) {
        NamingEnvironment currentEnv;
        // decide naming scope
        if (variableDeclaration.getKind() == VariableDeclaration.DeclarationType.VAR) {
            currentEnv = variableEnv;
        } else {
            currentEnv = lexicalEnv;
        }

        variableDeclaration.getDeclarations().forEach(
            variableDeclarator -> {
                Pattern id = variableDeclarator.getId();

                if (id.isIdentifier()) {
                    Name name = new Name(((Identifier) id).getName(), variableDeclarator
                        .getInitExpression());
                    currentEnv.addBinding(variableDeclaration, name);
                }
            });

        return null;
    }

    @Override
    protected Void visit(FunctionDeclaration functionDeclaration) {
        return null;
    }
}
