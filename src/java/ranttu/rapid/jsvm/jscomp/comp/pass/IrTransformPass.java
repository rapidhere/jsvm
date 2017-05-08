/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.codegen.CgNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.IrCast;
import ranttu.rapid.jsvm.codegen.ir.IrDup;
import ranttu.rapid.jsvm.codegen.ir.IrInvoke;
import ranttu.rapid.jsvm.codegen.ir.IrLiteral;
import ranttu.rapid.jsvm.codegen.ir.IrLoad;
import ranttu.rapid.jsvm.codegen.ir.IrNew;
import ranttu.rapid.jsvm.codegen.ir.IrNode;
import ranttu.rapid.jsvm.codegen.ir.IrReturn;
import ranttu.rapid.jsvm.codegen.ir.IrStore;
import ranttu.rapid.jsvm.codegen.ir.IrThis;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.astnode.AssignmentExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BinaryExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.CallExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Function;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.MemberExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.NewExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ObjectExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Property;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ReturnStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ThisExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.JsObjectObject;
import ranttu.rapid.jsvm.runtime.JsRuntime;

import java.util.List;

/**
 * the pass transform the ast-tree to ir-tree
 *
 * @author rapidhere@gmail.com
 * @version $id: IrTransformPass.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrTransformPass extends AstBasedCompilePass {
    private void ir(IrNode... irs) {
        method.ir(irs);
    }

    @Override
    protected void visit(NewExpression newExp) {
        super.visit(newExp);
        ir(IrInvoke.construct(newExp.getArguments().size()));
    }

    @Override
    protected void visit(BinaryExpression binExp) {
        switch (binExp.getOperator()) {
            case INSTANCE_OF:
                visit(binExp.getLeft());
                ir(IrCast.cast(JsObjectObject.class));
                visit(binExp.getRight());

                ir(IrInvoke.invokeVirtual(
                    Type.getInternalName(JsObjectObject.class),
                    "instanceOf",
                    Type.getMethodDescriptor(Type.getType(Boolean.class), Type.getType(Object.class))
                ));
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(ThisExpression thisExp) {
        ir(IrLoad.local("$this"));
    }

    @Override
    protected void visit(CallExpression call) {
        // named invoke
        if (call.getCallee().is(MemberExpression.class)) {
            MemberExpression mem = call.getCallee().as();

            // visit invokee
            visit(mem.getObject());

            // put invoke name on the stack
            resolvePropertyName(mem.getProperty());

            // put args on the stack
            call.getArguments().forEach(this::visit);

            // bounded invoke
            ir(IrInvoke.boundedInvoke(call.getArguments().size()));
        }
        // anonymous invoke
        else {
            // visit invokee
            visit(call.getCallee());

            // put context
            ir(IrThis.irthis());

            // put args on the stack
            call.getArguments().forEach(this::visit);

            // unbounded invoke
            ir(IrInvoke.unboundedInvoke(call.getArguments().size()));
        }
    }

    @Override
    protected void visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        clazz.field(varName).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

        if (variableDeclarator.getInitExpression().isPresent()) {
            // store to the context
            ir(IrThis.irthis());
            super.visit(variableDeclarator);
            ir(IrStore.field(clazz.$.name, varName, clazz.field(varName).$.desc));
        }
    }

    @Override
    protected void visit(FunctionExpression function) {
        onFunction(function);
    }

    @Override
    protected void visit(FunctionDeclaration function) {
        clazz.field(function.getId().getName()).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

        // store the function
        ir(IrThis.irthis());
        onFunction(function);
        ir(IrStore.field(
            clazz.$.name,
            function.getId().getName(),
            Type.getDescriptor(Object.class)
        ));
    }

    /**
     * put a invokable function object on the stack
     */
    private void onFunction(Function function) {
        // new function class
        ClassNode funcCls = clazz.inner_class("Function", JsFunctionObject.class,
            Opcodes.ACC_PRIVATE, Opcodes.ACC_SUPER);

        ClassNode outterCls = clazz;
        String constructorDesc = Type.getMethodDescriptor(Type.VOID_TYPE, $$.getType(outterCls));

        in(funcCls).invoke(() -> {
            // add $that field
            clazz.field("$that").acc(Opcodes.ACC_PROTECTED).desc(outterCls);

            // bind class
            context.namingEnv.bindScopeClass(function, clazz);

            // build init command
            in(funcCls.method_init(outterCls)).invoke(() ->
                // $that parameter
                method.par("$that")
                    .ir(
                        // super class init
                        IrThis.irthis(),
                        IrInvoke.invokeInit(
                            Type.getInternalName(JsFunctionObject.class),
                            Type.getMethodDescriptor(Type.VOID_TYPE)),
                        // store field $that
                        IrThis.irthis(),
                        IrLoad.local("$that"),
                        IrStore.field(clazz.$.name, "$that", CgNode.getDescriptor(outterCls)),
                        // ret
                        IrReturn.ret()
                    )

            );

            // build invoke function
            in(funcCls.method("invoke")).invoke(
                () -> {
                    method.acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_VARARGS)
                        .desc(Object.class, Object.class, Object[].class)
                        .par("this")
                        .par("$this")
                        .par("args");

                    // put args into field
                    for (int i = 0; i < function.getParams().size(); i++) {
                        Identifier par = function.getParams().get(i);
                        clazz.field(par.getName()).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

                        // $fieldName = args[i]
                        ir(
                            IrThis.irthis(),
                            IrLoad.local("args"),
                            IrLoad.array(i),
                            IrStore.field(clazz.$.name, par.getName(), Type.getDescriptor(Object.class))
                        );
                    }

                    // compile body
                    visit(function.getBody());
                });
        });

        // create function object and call makeFunction
        ir(
            IrNew.newObject(funcCls.$.name),
            IrDup.dup(),
            IrThis.irthis(),
            IrInvoke.invokeInit(funcCls.$.name, constructorDesc),
            IrDup.dup(),
            IrInvoke.makeFunc()
        );
    }

    @Override
    protected void visit(Literal literal) {
        if (literal.isInt()) {
            ir(IrLiteral.of(literal.getInt()));
        } else if (literal.isString()) {
            ir(IrLiteral.of(literal.getString()));
        } else if (literal.isDouble()) {
            ir(IrLiteral.of(literal.getDouble()));
        } else if (literal.isBoolean()) {
            ir(IrLiteral.of(literal.getBoolean()));
        } else {
            $$.notSupport();
        }
    }

    @Override
    protected void visit(ObjectExpression objExp) {
        // load init method
        ir(
            IrLoad.staticField(JsRuntime.class, "Object", Type.getDescriptor(JsFunctionObject.class)),
            IrInvoke.construct(0)
        );

        // now we get a empty object
        // then put all properties in to object

        for (Property prop : objExp.getProperties()) {
            // object.key = value
            ir(
                IrDup.dup(),
                IrLiteral.of(prop.getKeyString())
            );
            visit(prop.getValue());
            ir(IrStore.property());
        }

        // then the constructed object is leaved on stack
    }

    // resolve property as a string on stack
    private void resolvePropertyName(Expression exp) {
        if(exp.is(Identifier.class)) {
            ir(IrLiteral.of(exp.as(Identifier.class).getName()));
        }
    }

    @Override
    protected void visit(Identifier identifier) {
        visitName(identifier.getName(), identifier);
    }

    private void visitName(String name, Node node) {
        List<Node> nodes = context.namingEnv.resolveJumped(node, name);

        ir(IrThis.irthis());
        // jump to real context
        for (int i = 0; i < nodes.size() - 1; i++) {
            ClassNode cls = context.namingEnv.getScopeClass(nodes.get(i));
            ClassNode nextCls = context.namingEnv.getScopeClass(nodes.get(i + 1));

            // this.$that
            ir(IrLoad.field(cls.$.name, "$that", CgNode.getDescriptor(nextCls)));
        }

        Node last = nodes.get(nodes.size() - 1);
        ClassNode lastCls = context.namingEnv.getScopeClass(last);
        ir(IrLoad.field(lastCls.$.name, name, Type.getDescriptor(Object.class)));
    }

    @Override
    protected void visit(AssignmentExpression assignExp) {
        // only support normal assign now
        $$.shouldIn(assignExp.getOperator(), AssignmentOperator.ASSIGN);

        // field assignment
        if (assignExp.getLeft().is(Identifier.class)) {
            String key = $$.cast(assignExp.getLeft(), Identifier.class).getName();

            ir(IrThis.irthis());
            visit(assignExp.getRight());
            ir(IrStore.field(clazz.$.name, key, clazz.field(key).$.desc));
        }
        // member assignment
        else if (assignExp.getLeft().is(MemberExpression.class)) {
            MemberExpression member = $$.cast(assignExp.getLeft());

            visit(member.getObject());
            resolvePropertyName(member.getProperty());
            visit(assignExp.getRight());
            ir(IrStore.property());
        }
        // otherwise, not support
        else {
            $$.notSupport();
        }
    }

    @Override
    protected void visit(MemberExpression memExp) {
        $$.should(!memExp.isComputed());

        // load the field
        visit(memExp.getObject());
        resolvePropertyName(memExp.getProperty());
        ir(IrLoad.property());
    }

    @Override
    protected void visit(ReturnStatement returnStatement) {
        if (returnStatement.getArgument().isPresent()) {
            visit(returnStatement.getArgument().get());
            ir(IrReturn.retWithValue());
        } else {
            ir(IrReturn.ret());
        }
    }

    @Override
    protected void visit(Program program) {
        ClassNode cls = new ClassNode().acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SUPER)
            .name(context.className, JsModule.class).source(context.sourceFileName);

        // store the result
        context.rootClassNode = cls;
        // add naming binding
        context.namingEnv.bindScopeClass(program, cls);

        in(cls).invoke(() -> {
            // add MODULE field
            clazz.field(JsModule.FIELD_MODULE_NAME)
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC)
                .desc(clazz)
                .end()

                // init MODULE field
                .method_clinit()
                // MODULE = new this();
                .ir(
                    IrNew.newObject(clazz.$.name),
                    IrDup.dup(),
                    IrInvoke.invokeInit(clazz.$.name, Type.getMethodDescriptor(Type.VOID_TYPE)),
                    IrStore.staticField(clazz.$.name, JsModule.FIELD_MODULE_NAME, CgNode.getDescriptor(clazz)),
                    IrReturn.ret()
                );

            // generate init method
            in(clazz.method_init()).invoke(() -> {
                ir(
                    IrThis.irthis(),
                    IrInvoke.invokeInit(
                        Type.getInternalName(JsModule.class),
                        Type.getMethodDescriptor(Type.VOID_TYPE))
                );

                // generate body
                program.getBody().forEach(this::visit);

                // end init method generate
                ir(IrReturn.ret());
            });
        });
    }
}
