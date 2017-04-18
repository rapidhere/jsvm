/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.IrInvoke;
import ranttu.rapid.jsvm.codegen.ir.IrLoad;
import ranttu.rapid.jsvm.codegen.ir.IrReturn;
import ranttu.rapid.jsvm.codegen.ir.IrStore;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.jscomp.ast.astnode.AssignmentExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BlockStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ExpressionStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.MemberExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ObjectExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Property;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ReturnStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;
import ranttu.rapid.jsvm.jscomp.comp.CompilePass;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.JsNumberObject;
import ranttu.rapid.jsvm.runtime.JsObjectObject;
import ranttu.rapid.jsvm.runtime.JsStringObject;

import java.util.HashMap;
import java.util.Map;

/**
 * the pass transform the ast-tree to ir-tree
 *
 * @author rapidhere@gmail.com
 * @version $id: IrTransformPass.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrTransformPass extends CompilePass {
    private Map<String, ClassNode> ir;

    @Override
    public void start() {
        context.ir = ir = new HashMap<>();
        visit(context.ast.getRoot());
    }

    private void visit(Node node) {
        if (node.is(Program.class)) {
            visit((Program) node);
        } else if (node.is(VariableDeclaration.class)) {
            visit((VariableDeclaration) node);
        } else if (node.is(FunctionDeclaration.class)) {
            visit((FunctionDeclaration) node);
        } else if (node.is(VariableDeclarator.class)) {
            visit((VariableDeclarator) node);
        } else if (node.is(FunctionExpression.class)) {
            visit((FunctionExpression) node);
        } else if (node.is(Literal.class)) {
            visit((Literal) node);
        } else if (node.is(ObjectExpression.class)) {
            visit((ObjectExpression) node);
        } else if (node.is(ExpressionStatement.class)) {
            visit((ExpressionStatement) node);
        } else if (node.is(AssignmentExpression.class)) {
            visit((AssignmentExpression) node);
        } else if (node.is(MemberExpression.class)) {
            visit((MemberExpression) node);
        } else if (node.is(ReturnStatement.class)) {
            visit((ReturnStatement) node);
        } else if (node.is(BlockStatement.class)) {
            visit((BlockStatement) node);
        }
    }

    private void visit(VariableDeclaration variableDeclaration) {
        variableDeclaration.getDeclarations().forEach(this::visit);
    }

    private void visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        clazz
            .field(varName)
            .acc(Opcodes.ACC_PRIVATE)
            .desc(Object.class);

        if(variableDeclarator.getInitExpression().isPresent()) {
            method.aload(0);
            visit(variableDeclarator.getInitExpression().get());
            method.store(clazz.field(varName));
        }
    }

    private void visit(FunctionExpression functionExpression) {
        visit(functionExpression.getBody());
    }

    private void visit(FunctionDeclaration function) {
        ClassNode funcCls = clazz.inner_class("Function", JsFunctionObject.class, Opcodes.ACC_PRIVATE,
            Opcodes.ACC_SUPER);
        ir.put(funcCls.$.name, funcCls);

        // put the field
        clazz
            .field(function.getId().getName())
            .acc(Opcodes.ACC_PRIVATE)
            .desc(Object.class);

        // build init command
        in(funcCls.method_init()).invoke(() ->
            method
                .aload(0)
                .invoke_init(JsFunctionObject.class)
                .ret()
                // TODO
                .stack(16, 1)
        );

        // build invoke function
        in(funcCls).in(funcCls.method("invoke")).invoke(() -> {
            method
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_VARARGS)
                .desc(Object.class, Object[].class);

            visit(function.getBody());

            // end build invoke function
            method
                // TODO
                .stack(16, 2);
        });

        // load the function
        method
            .aload(0)
            .new_class(funcCls)
            .dup()
            .invoke_init(funcCls)
            .store(clazz.field(function.getId().getName()));
    }

    private void visit(Literal literal) {
        if (literal.isInt()) {
            method
                .new_class(JsNumberObject.class)
                .dup()
                .load_const(literal.getInt())
                .invoke_init(JsNumberObject.class, int.class);
        } else if (literal.isString()) {
            method
                .new_class(JsStringObject.class)
                .dup()
                .load_const(literal.getString())
                .invoke_init(JsStringObject.class, String.class);
        } else if (literal.isBoolean()) {
            method
                .load_const(literal.getBoolean())
                .invoke_static(Boolean.class, "valueOf", boolean.class);
        } else if (literal.isDouble()) {
            method
                .new_class(JsNumberObject.class)
                .dup()
                .load_const(literal.getDouble())
                .invoke_init(JsNumberObject.class, double.class);
        } else {
            $$.notSupport();
        }
    }

    private void visit(ObjectExpression objExp) {
        ClassNode objClass = clazz.inner_class("Object", JsObjectObject.class, Opcodes.ACC_PRIVATE,
            Opcodes.ACC_SUPER);
        ir.put(objClass.$.name, objClass);

        // load init method
        in(objClass).in(objClass.method_init()).invoke(() -> {
            IrLoad.loadThis();
            IrInvoke.invokeInit();

            for (Property prop : objExp.getProperties()) {
                String fieldName = prop.getKeyString();

                clazz
                    .field(fieldName)
                    .acc(Opcodes.ACC_PUBLIC)
                    .desc(Object.class);

                IrLoad.loadThis();
                visit(prop.getValue());
                IrStore.field(fieldName);
            }

            // end init method
            IrReturn.ret();
        });

        // load inner class
        method
            .new_class(objClass)
            .dup()
            .invoke_init(objClass);
    }

    private void visit(ExpressionStatement statement) {
        visit(statement.getExpression());
    }

    private void visit(AssignmentExpression assignExp) {
        // only support normal assign now
        $$.shouldIn(assignExp.getOperator(), AssignmentOperator.ASSIGN);

        // field assignment
        if (assignExp.getLeft().is(Identifier.class)) {
            method.aload(0);
            visit(assignExp.getRight());
            method.store(clazz.field($$.cast(assignExp.getLeft(), Identifier.class).getName()));
        }
        // common assignment
        else {
            visit(assignExp.getLeft());
            visit(assignExp.getRight());
            method
                .indy_jsobj(MethodConst.SET_PROPERTY, void.class, String.class, Object.class);
        }
    }

    private void visit(MemberExpression memExp) {
        $$.should(! memExp.isComputed());

        //~~~ after visit, the parent object is on the stack
        // for identifier, load the field
        if (memExp.getObject().is(Identifier.class)) {
            method
                .aload(0)
                .load(clazz.field($$.cast(memExp.getObject(), Identifier.class).getName()));
        }
        // others, common visit
        else {
            visit(memExp.getObject());

            //~~~ then, invoke get property
            method
                .indy_jsobj(MethodConst.GET_PROPERTY, Object.class, String.class);
        }

        // put field name const on the stack
        method
            // only un-compiled field is supported
            .load_const($$.cast(memExp.getProperty(), Identifier.class).getName());
    }

    private void visit(BlockStatement blockStatement) {
        blockStatement.getBody().forEach(this::visit);
    }

    private void visit(ReturnStatement returnStatement) {
        if (returnStatement.getArgument().isPresent()) {
            visit(returnStatement.getArgument().get());
        }
    }

    private void visit(Program program) {
        ClassNode cls = new ClassNode()
            .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SUPER)
            .name(context.className, JsModule.class)
            .source(context.sourceFileName);

        ir.put(cls.$.name, cls);

        in(cls).invoke(() -> {
            // add MODULE field
            clazz.field(JsModule.FIELD_MODULE_NAME)
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC)
                .desc(clazz)
            .end()

            // init MODULE field
            .method_clinit()
                .new_class(clazz)
                .dup()
                .invoke_init(clazz)
                .store_static(clazz.field(JsModule.FIELD_MODULE_NAME))
                .ret()
                .stack(2, 0)
            .end();

            // generate init method
            in(clazz.method_init()).invoke(() -> {
                method
                    .aload(0)
                    .invoke_init(JsModule.class);

                program.getBody().forEach(this::visit);

                // end init method generate
                method
                    .ret()
                    // TODO
                    .stack(16, 1);
            });
        });
    }
}
