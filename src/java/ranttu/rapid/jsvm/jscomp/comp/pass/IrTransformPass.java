/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.astnode.*;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Declaration;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;
import ranttu.rapid.jsvm.jscomp.ast.enums.BinaryOperator;
import ranttu.rapid.jsvm.runtime.*;
import ranttu.rapid.jsvm.runtime.async.FuturePromise;

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
    protected void visit(ThrowStatement throwStatement) {
        super.visit(throwStatement);

        ir(
            // convert to Throwable
            IrCast.cast($$.getInternalName(Throwable.class)),
            // then throw
            IrThrow.athrow()
        );
    }

    @Override
    protected void visit(TryStatement tryStatement) {
        // TODO: support finally
        IrLabel start = IrLabel.label(),
            end = IrLabel.label(),
            handler = IrLabel.label(),
            nextBegin = IrLabel.label();

        // create try catch list
        method.try_catch(
            start.label, end.label, handler.label, $$.getInternalName(Throwable.class));

        // block to try catch
        ir(start);
        visit(tryStatement.getBlock());
        ir(end, IrJump.j(nextBegin.label));

        // try catch handler
        ir(handler);
        visit(tryStatement.getHandler());
        ir(nextBegin);
    }

    @Override
    protected void visit(CatchClause catchClause) {
        // TODO: this implementation is still buggy on exception name scopes
        String exceptionName = catchClause.getParam().getName();

        // add new field to closure
        ClassNode closure = clazz.getClosureClass();
        closure.field(exceptionName).acc(Opcodes.ACC_PROTECTED).desc($$.getDescriptor(Object.class));

        // store into closure
        // exception is now on the stack
        ir(
            IrLoad.closure(),
            IrSwap.swap(),
            IrStore.field(closure.$.name, exceptionName, $$.getDescriptor(Object.class))
        );

        // visit catch body
        visit(catchClause.getBody());
    }

    @Override
    protected void visit(ImportDeclaration importDeclaration) {
        ClassNode closure = clazz.getClosureClass();
        String classPath = importDeclaration.getSource().getString();
        String name = importDeclaration.getNamespace().getName();

        // add closure field
        closure.field(name)
            .acc(Opcodes.ACC_PROTECTED, Opcodes.ACC_SYNTHETIC)
            .desc(Object.class);

        // TODO: only support java class import now
        ir(
            IrLoad.closure(),
            // load class
            IrLoad.ldc($$.getType(classPath)),
            IrStore.field(closure.$.name, name, $$.getDescriptor(Object.class))
        );
    }

    @Override
    protected  void visit(AwaitExpression awaitExpression) {
        // TODO: await expression context check
        super.visit(awaitExpression);

        // cast to FuturePromise Object and put it on the stack
        ir(IrCast.cast(FuturePromise.class));

        // current method is over
        ir(IrReturn.ret());
    }

    @Override
    protected void visit(ExportNamedDeclaration exportNamedDeclaration) {
        Declaration declaration = exportNamedDeclaration.getDeclaration();
        visit(declaration);

        // resolve name
        if (declaration.is(FunctionDeclaration.class)) {
            makeDeclarationPublic(
                declaration.as(FunctionDeclaration.class).getId().getName());
        } else if(declaration.is(VariableDeclaration.class)) {
            for (VariableDeclarator declarator: declaration.as(VariableDeclaration.class).getDeclarations()) {
                makeDeclarationPublic(declarator.getId().getName());
            }
        } else {
            $$.notSupport();
        }
    }

    private void makeDeclarationPublic(String name) {
        clazz.getClosureClass().field(name).acc(Opcodes.ACC_PUBLIC);
    }

    @Override
    protected void visit(WhileStatement whileStatement) {
        IrLabel beginLabel = IrLabel.label(), endLabel = IrLabel.label();

        ir(beginLabel);

        // test expression
        visit(whileStatement.getTest());
        // get boolean value
        ir(IrInvoke.invokeStatic(
            $$.getInternalName(JsRuntime.class),
            "castToBooleanValue",
            $$.getMethodDescriptor(int.class, Object.class)
        ));
        ir(IrJump.eq(endLabel.label));

        // body expression
        visit(whileStatement.getBody());
        ir(IrJump.j(beginLabel.label));

        ir(endLabel);
    }


    @Override
    protected void visit(IfStatement ifStatement) {
        visit(ifStatement.getTest());

        // get boolean value
        ir(IrInvoke.invokeStatic(
            $$.getInternalName(JsRuntime.class),
            "castToBooleanValue",
            $$.getMethodDescriptor(int.class, Object.class)
        ));

        // have alternate statement
        if (ifStatement.getAlternate().isPresent()) {
            IrLabel altLabel = IrLabel.label(), endLabel = IrLabel.label();

            ir(IrJump.eq(altLabel.label));
            visit(ifStatement.getConsequent());
            ir(IrJump.j(endLabel.label));

            ir(altLabel);
            visit(ifStatement.getAlternate().get());

            ir(endLabel);
        } else {
            IrLabel endLabel = IrLabel.label();

            ir(IrJump.eq(endLabel.label));
            visit(ifStatement.getConsequent());
            ir(endLabel);
        }
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
                    $$.getInternalName(JsObjectObject.class),
                    "instanceOf",
                    $$.getMethodDescriptor(Boolean.class, Object.class)
                ));
                break;
            case ADD:
            case SUBTRACT:
            case STRONG_EQUAL:
                visit(binExp.getLeft());
                visit(binExp.getRight());
                invokeBinMathOp(binExp.getOperator());
                break;
            default:
                $$.notSupport();
        }
    }

    private void invokeBinMathOp(BinaryOperator op) {
        ir(IrInvoke.invokeStatic($$.getInternalName(JsRuntime.class), op.name(),
            $$.getMethodDescriptor(Object.class, Object.class, Object.class)));
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
            ir(IrLoad.thiz());

            // put args on the stack
            call.getArguments().forEach(this::visit);

            // unbounded invoke
            ir(IrInvoke.unboundedInvoke(call.getArguments().size()));
        }
    }

    @Override
    protected void visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        ClassNode closure = clazz.getClosureClass();

        closure.field(varName).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

        if (variableDeclarator.getInitExpression().isPresent()) {
            // store to the context
            ir(IrLoad.closure());
            super.visit(variableDeclarator);
            ir(IrStore.field(closure.$.name, varName, closure.field(varName).$.desc));
        }
    }

    @Override
    protected void visit(FunctionExpression function) {
        onFunction(function);
    }

    @Override
    protected void visit(FunctionDeclaration function) {
        ClassNode closure = clazz.getClosureClass();
        closure.field(function.getId().getName()).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

        // store the function
        ir(IrLoad.closure());
        onFunction(function);
        ir(IrStore.field(
            closure.$.name,
            function.getId().getName(),
            $$.getDescriptor(Object.class)
        ));
    }

    /**
     * put a invokable function object on the stack
     */
    private void onFunction(Function function) {
        //~~~ new function class
        ClassNode funcCls;
        if(! function.isAsync()) {
            funcCls = clazz.inner_class("Function", JsFunctionObject.class);
        } else {
            funcCls = clazz.inner_class("Function", JsAsyncFunctionObject.class);
        }
        funcCls.acc(Opcodes.ACC_PRIVATE, Opcodes.ACC_SUPER);

        ClassNode outterCls = clazz.getClosureClass();
        String constructorDesc = $$.getMethodDescriptor(void.class, outterCls);

        in(funcCls).invoke(() -> {
            // add closure class
            ClassNode closureClass = clazz.inner_class(
                "Closure", JsClosure.class, Opcodes.ACC_PRIVATE, Opcodes.ACC_SUPER, Opcodes.ACC_SYNTHETIC);

            // add $that field
            clazz.field("$that").acc(Opcodes.ACC_PROTECTED, Opcodes.ACC_SYNTHETIC).desc(outterCls);
            closureClass.field("$that").acc(Opcodes.ACC_PROTECTED, Opcodes.ACC_SYNTHETIC).desc(outterCls);

            // bind class
            context.namingEnv.bindScopeClass(function, closureClass);

            // closure init
            in(closureClass).in(closureClass.method_init()).invoke(() ->
                method.ir(
                    // super class init
                    IrLoad.thiz(),
                    IrInvoke.invokeInit($$.getInternalName(JsClosure.class), $$.getMethodDescriptor(void.class)),
                    // ret
                    IrReturn.ret()
                )
            );

            // build init command
            in(funcCls.method_init(outterCls)).invoke(() ->
                // $that parameter
                method.par("$that")
                    .ir(
                        // super class init
                        IrLoad.thiz(),
                        IrInvoke.invokeInit(
                            $$.getInternalName(JsFunctionObject.class),
                            $$.getMethodDescriptor(void.class)),
                        // store field $that
                        IrLoad.thiz(),
                        IrLoad.local("$that"),
                        IrStore.field(
                            clazz.$.name, "$that",
                            $$.getDescriptor(outterCls)),
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

                    // construct closure
                    method.local("closure")
                        .ir(
                            // new closure
                            IrNew.newObject(closureClass.$.name),
                            IrDup.dup(),
                            IrInvoke.invokeInit(closureClass.$.name, $$.getMethodDescriptor(void.class)),
                            // closure.$that = this.$that
                            IrDup.dup(),
                            IrLoad.thiz(),
                            IrLoad.field(clazz.$.name, "$that", $$.getDescriptor(outterCls)),
                            IrStore.field(closureClass.$.name, "$that", $$.getDescriptor(outterCls)),
                            // store to local
                            IrStore.local("closure")
                        );

                    // put args into field
                    for (int i = 0; i < function.getParams().size(); i++) {
                        Identifier par = function.getParams().get(i);
                        closureClass.field(par.getName()).acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SYNTHETIC).desc(Object.class);

                        // closure.fieldName = args[i]
                        ir(
                            IrLoad.local("closure"),
                            IrLoad.local("args"),
                            IrLoad.array(i),
                            IrStore.field(closureClass.$.name, par.getName(), $$.getDescriptor(Object.class))
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
            IrLoad.closure(),
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
            IrLoad.staticField(JsRuntime.class, "Object", $$.getDescriptor(JsFunctionObject.class)),
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
        ClassNode clazz = visitName(identifier.getName(), identifier);
        ir(IrLoad.field(
            clazz.$.name, identifier.getName(), $$.getDescriptor(Object.class)));
    }

    private ClassNode visitName(String name, Node node) {
        List<Node> nodes = context.namingEnv.resolveJumped(node, name);

        ir(IrLoad.local("closure"));
        // jump to real context
        for (int i = 0; i < nodes.size() - 1; i++) {
            ClassNode cls = context.namingEnv.getScopeClass(nodes.get(i));
            ClassNode nextCls = context.namingEnv.getScopeClass(nodes.get(i + 1));

            // this.$that
            ir(IrLoad.field(cls.$.name, "$that", $$.getDescriptor(nextCls)));
        }

        Node last = nodes.get(nodes.size() - 1);
        return context.namingEnv.getScopeClass(last);
    }

    @Override
    protected void visit(AssignmentExpression assignExp) {
        // only support normal assign now
        $$.shouldIn(assignExp.getOperator(), AssignmentOperator.ASSIGN);

        // field assignment
        if (assignExp.getLeft().is(Identifier.class)) {
            String key = $$.cast(assignExp.getLeft(), Identifier.class).getName();
            ClassNode clazz = visitName(key, assignExp);

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
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC, Opcodes.ACC_SYNTHETIC)
                .desc(clazz)
                .end()

                // init MODULE field
                .method_clinit()
                // MODULE = new this();
                .ir(
                    IrNew.newObject(clazz.$.name),
                    IrDup.dup(),
                    IrInvoke.invokeInit(clazz.$.name, $$.getMethodDescriptor(void.class)),
                    IrStore.staticField(clazz.$.name, JsModule.FIELD_MODULE_NAME, $$.getDescriptor(clazz)),
                    IrReturn.ret()
                );

            // generate init method
            in(clazz.method_init()).invoke(() -> {
                ir(
                    IrLoad.thiz(),
                    IrInvoke.invokeInit(
                        $$.getInternalName(JsModule.class),
                        $$.getMethodDescriptor(void.class)
                    )
                );

                // add closure
                method.local("closure")
                    .ir(
                        IrLoad.thiz(),
                        IrStore.local("closure")
                    );

                // generate body
                program.getBody().forEach(this::visit);

                // end init method generate
                ir(IrReturn.ret());
            });
        });
    }
}
