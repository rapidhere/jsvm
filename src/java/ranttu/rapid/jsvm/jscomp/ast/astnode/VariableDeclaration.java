/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Declaration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: VariableDeclaration.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class VariableDeclaration extends BaseAstNode implements Declaration {
    private DeclarationType          kind;

    private List<VariableDeclarator> declarations = new ArrayList<>();

    public void addDeclaration(VariableDeclarator variableDeclarator) {
        declarations.add(variableDeclarator);
    }

    public List<VariableDeclarator> getDeclarations() {
        return declarations;
    }

    public DeclarationType getKind() {
        return kind;
    }

    public void setKind(DeclarationType kind) {
        this.kind = kind;
    }

    public enum DeclarationType {
        VAR, LET

        ;

        public static DeclarationType of(String s) {
            return DeclarationType.valueOf(s.toUpperCase());
        }
    }
}
