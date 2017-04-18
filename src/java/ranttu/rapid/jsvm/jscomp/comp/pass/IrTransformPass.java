/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.nashorn.internal.codegen.types.Type;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.astnode.*;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;
import ranttu.rapid.jsvm.jscomp.comp.CompilePass;
import ranttu.rapid.jsvm.runtime.*;

/**
 * the pass transform the ast-tree to ir-tree
 *
 * @author rapidhere@gmail.com
 * @version $id: IrTransformPass.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrTransformPass extends CompilePass {
    @Override
    public void start() {
        visit(context.ast.getRoot());
    }

    private IrNode visit(Node node) {
        if (node.is(Program.class)) {
            return visit((Program) node);
        } else if (node.is(VariableDeclaration.class)) {
            return visit((VariableDeclaration) node);
        } else if (node.is(FunctionDeclaration.class)) {
            return visit((FunctionDeclaration) node);
        } else if (node.is(VariableDeclarator.class)) {
            return visit((VariableDeclarator) node);
        } else if (node.is(Literal.class)) {
            return visit((Literal) node);
        } else if (node.is(ObjectExpression.class)) {
            return visit((ObjectExpression) node);
        } else if (node.is(ExpressionStatement.class)) {
            return visit((ExpressionStatement) node);
        } else if (node.is(AssignmentExpression.class)) {
            return visit((AssignmentExpression) node);
        } else if (node.is(MemberExpression.class)) {
            return visit((MemberExpression) node);
        } else if (node.is(ReturnStatement.class)) {
            return visit((ReturnStatement) node);
        } else if (node.is(BlockStatement.class)) {
            return visit((BlockStatement) node);
        } else if (node.is(Identifier.class)) {
            return visit((Identifier) node);
        }

        return $$.notSupport();
    }

    private IrNode visit(VariableDeclaration variableDeclaration) {
        IrBlock ret = IrBlock.of();

        variableDeclaration.getDeclarations().forEach((declarator) -> ret.irs.add(visit(declarator)));

        return ret;
    }

    private IrNode visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        clazz
            .field(varName)
            .acc(Opcodes.ACC_PRIVATE)
            .desc(Object.class);

        if(variableDeclarator.getInitExpression().isPresent()) {
            return IrStore.field(
                    IrThis.irthis(),
                    varName,
                    visit(variableDeclarator.getInitExpression().get()));
        } else {
            return IrBlock.of();
        }
    }

    private IrNode visit(FunctionDeclaration function) {
        ClassNode funcCls = clazz.inner_class("Function", JsFunctionObject.class, Opcodes.ACC_PRIVATE,
            Opcodes.ACC_SUPER);

        // put the field
        clazz
            .field(function.getId().getName())
            .acc(Opcodes.ACC_PRIVATE)
            .desc(Object.class);

        // build init command
        in(funcCls.method_init()).invoke(() ->
            method.ir(
                    IrInvoke.invokeInit(JsFunctionObject.class),
                    IrReturn.ret()
            )
        );

        // build invoke function
        in(funcCls).in(funcCls.method("invoke")).invoke(() -> {
            method
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_VARARGS)
                .desc(Object.class, Object[].class);

            method.ir(visit(function.getBody()));
        });

        // load the function
        return IrStore.field(
                IrThis.irthis(),
                function.getId().getName(),
                IrNew.of(funcCls.$.name));
    }

    private IrNode visit(Literal literal) {
        if (literal.isInt()) {
            return IrNew.of(JsNumberObject.class,
                    Type.getMethodDescriptor(void.class, int.class), literal.getInt());
        } else if (literal.isString()) {
            return IrNew.of(JsStringObject.class,
                    Type.getMethodDescriptor(void.class, String.class), literal.getString());
        }  else if (literal.isDouble()) {
            return IrNew.of(JsNumberObject.class,
                    Type.getMethodDescriptor(void.class, double.class), literal.getDouble());
        } else {
            return $$.notSupport();
        }
    }

    private IrNode visit(ObjectExpression objExp) {
        ClassNode objClass = clazz.inner_class("Object", JsObjectObject.class, Opcodes.ACC_PRIVATE,
            Opcodes.ACC_SUPER);

        // load init method
        in(objClass).in(objClass.method_init()).invoke(() -> {
            method.ir(IrInvoke.invokeInit(JsObjectObject.class));

            for (Property prop : objExp.getProperties()) {
                String fieldName = prop.getKeyString();

                method.ir(IrStore.field(
                        IrThis.irthis(),
                        fieldName,
                        visit(prop.getValue())));
            }

            // end init method
            method.ir(IrReturn.ret());
        });

        // load inner class
        return IrNew.of(objClass.$.name);
    }

    private IrNode visit(ExpressionStatement statement) {
        return visit(statement.getExpression());
    }

    private IrNode visit(Identifier identifier) {
        return IrLiteral.of(identifier.getName());
    }

    private IrNode visit(AssignmentExpression assignExp) {
        // only support normal assign now
        $$.shouldIn(assignExp.getOperator(), AssignmentOperator.ASSIGN);

        // field assignment
        if (assignExp.getLeft().is(Identifier.class)) {
            return IrStore.field(IrThis.irthis(), visit(assignExp.getLeft()), visit(assignExp.getRight()));
        }
        // member assignment
        else if(assignExp.getLeft().is(MemberExpression.class)) {
            MemberExpression member = $$.cast(assignExp.getLeft());

            IrNode context = resolveMemberObject(member);
            return IrStore.field(context, visit(member.getProperty()), visit(assignExp.getRight()));
        }
        // otherwise, not support
        else {
            return $$.notSupport();
        }
    }

    private IrNode resolveMemberObject(MemberExpression member) {
        // for identifier, load the field
        if(member.getObject().is(Identifier.class)) {
            return IrLoad.field(IrThis.irthis(), visit(member.getObject()));
        }
        // others, common visit
        else {
            return visit(member.getObject());
        }
    }

    private IrNode visit(MemberExpression memExp) {
        $$.should(! memExp.isComputed());

        IrNode context = resolveMemberObject(memExp);
        // load the field
        return IrLoad.field(context, visit(memExp.getProperty()));
    }

    private IrNode visit(BlockStatement blockStatement) {
        IrBlock ret = IrBlock.of();
        blockStatement.getBody().forEach((statement) -> ret.irs.add(visit(statement)));

        return ret;
    }

    private IrNode visit(ReturnStatement returnStatement) {
        if (returnStatement.getArgument().isPresent()) {
            return IrReturn.ret(visit(returnStatement.getArgument().get()));
        } else {
            return IrReturn.ret();
        }
    }

    private IrNode visit(Program program) {
        ClassNode cls = new ClassNode()
            .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SUPER)
            .name(context.className, JsModule.class)
            .source(context.sourceFileName);

        // store the result
        context.rootClassNode = cls;

        in(cls).invoke(() -> {
            // add MODULE field
            clazz.field(JsModule.FIELD_MODULE_NAME)
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC)
                .desc(clazz)
            .end()

            // init MODULE field
            .method_clinit().ir(
                    IrStore.staticField(clazz.$.name, JsModule.FIELD_MODULE_NAME, IrNew.of(clazz.$.name)),
                    IrReturn.ret()
            );

            // generate init method
            in(clazz.method_init()).invoke(() -> {
                method.ir(IrInvoke.invokeInit(JsModule.class));

                program.getBody().forEach(statement -> method.ir(visit(statement)));

                // end init method generate
                method.ir(IrReturn.ret());
            });
        });

        // not used
        return null;
    }
}
