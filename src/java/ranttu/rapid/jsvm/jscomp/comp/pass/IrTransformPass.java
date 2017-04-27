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
import ranttu.rapid.jsvm.codegen.ir.IrBlock;
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
import ranttu.rapid.jsvm.jscomp.ast.astnode.BlockStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.CallExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Function;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.MemberExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ObjectExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Property;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ReturnStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ThisExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.JsNumberObject;
import ranttu.rapid.jsvm.runtime.JsRuntime;
import ranttu.rapid.jsvm.runtime.JsStringObject;

import java.util.ArrayList;
import java.util.List;

/**
 * the pass transform the ast-tree to ir-tree
 *
 * @author rapidhere@gmail.com
 * @version $id: IrTransformPass.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrTransformPass extends AstBasedCompilePass {
    private IrNode irNode;

    private IrNode visitIr(Node node) {
        visit(node);
        return irNode;
    }

    @Override
    protected void visit(ThisExpression thisExp) {
        irNode = IrLoad.local("$this");
    }

    @Override
    protected void visit(CallExpression call) {
        List<IrNode> args = new ArrayList<>();
        call.getArguments().forEach((arg) -> args.add(visitIr(arg)));

        // named invoke
        if (call.getCallee().is(MemberExpression.class)) {
            MemberExpression mem = call.getCallee().as();
            IrNode context = visitIr(mem.getObject());
            IrNode name = resolveProperty(mem.getProperty());

            irNode = IrInvoke.boundedInvoke(context, name, args);
        }
        // anonymous invoke
        else {
            IrNode callee = visitIr(call.getCallee());

            irNode = IrInvoke.unboundedInvoke(callee, args);
        }
    }

    @Override
    protected void visit(VariableDeclaration variableDeclaration) {
        IrBlock ret = IrBlock.of();

        variableDeclaration.getDeclarations().forEach(
            (declarator) -> ret.irs.add(visitIr(declarator)));

        irNode = ret;
    }

    @Override
    protected void visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        clazz.field(varName).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

        if (variableDeclarator.getInitExpression().isPresent()) {
            irNode = IrStore.field(IrThis.irthis(), varName, visitIr(variableDeclarator
                .getInitExpression().get()), clazz.$.name, clazz.field(varName).$.desc);
        } else {
            irNode = IrBlock.of();
        }
    }

    @Override
    protected void visit(FunctionExpression function) {
        irNode = onFunction(function);
    }

    @Override
    protected void visit(FunctionDeclaration function) {
        clazz.field(function.getId().getName()).acc(Opcodes.ACC_PROTECTED)
            .desc(Object.class);

        IrNode funcIr = onFunction(function);

        // store the function
        irNode = IrStore.field(
            IrThis.irthis(),
            function.getId().getName(),
            funcIr,
            clazz.$.name, Type.getDescriptor(Object.class));
    }

    private IrNode onFunction(Function function) {
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
            in(funcCls.method_init(outterCls)).invoke(
                () -> method.par("$that").ir(
                    IrInvoke.invokeInit(JsFunctionObject.class,
                        Type.getMethodDescriptor(Type.VOID_TYPE)),
                    IrStore.field(IrThis.irthis(), "$that", IrLoad.local("$that"), clazz.$.name,
                        CgNode.getDescriptor(outterCls)), IrReturn.ret()));

            // build invoke function
            in(funcCls.method("invoke")).invoke(
                () -> {
                    method.acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_VARARGS)
                        .desc(Object.class, Object.class, Object[].class).par("this").par("$this")
                        .par("args");

                    // put args into field
                    for (int i = 0; i < function.getParams().size(); i++) {
                        Identifier par = function.getParams().get(i);
                        clazz.field(par.getName()).acc(Opcodes.ACC_PROTECTED).desc(Object.class);

                        // $fieldName = args[i]
                        method.ir(IrStore.field(IrThis.irthis(), par.getName(),
                            IrLoad.array(IrLoad.local("args"), i), clazz.$.name,
                            Type.getDescriptor(Object.class)));
                    }

                    method.ir(visitIr(function.getBody()));
                });
        });

        // load function Object
        return IrInvoke.makeFunc(funcCls.$.name,
            IrDup.dup(IrNew.of(funcCls.$.name, constructorDesc, IrThis.irthis())));
    }

    @Override
    protected void visit(Literal literal) {
        if (literal.isInt()) {
            irNode = IrNew.of(JsNumberObject.class,
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE),
                IrLiteral.of(literal.getInt()));
        } else if (literal.isString()) {
            irNode = IrNew.of(JsStringObject.class,
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)),
                IrLiteral.of(literal.getString()));
        } else if (literal.isDouble()) {
            irNode = IrNew.of(JsNumberObject.class,
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.DOUBLE_TYPE),
                IrLiteral.of(literal.getDouble()));
        } else {
            irNode = $$.notSupport();
        }
    }

    @Override
    protected void visit(ObjectExpression objExp) {
        // load init method
        IrNode ret = IrInvoke.construct(
            Type.getInternalName(JsFunctionObject.class),
            IrLoad.staticField(JsRuntime.class, "Object",
                Type.getDescriptor(JsFunctionObject.class)));

        for (Property prop : objExp.getProperties()) {
            String fieldName = prop.getKeyString();

            ret = IrStore.property(IrDup.dup(ret), fieldName, visitIr(prop.getValue()));
        }

        // load inner class
        irNode = ret;
    }

    @Override
    protected void visit(Identifier identifier) {
        irNode = visitName(identifier.getName(), identifier);
    }

    private IrNode visitName(String name, Node node) {
        List<Node> nodes = context.namingEnv.resolveJumped(node, name);

        IrNode ctx = IrThis.irthis();
        for (int i = 0; i < nodes.size() - 1; i++) {
            ClassNode cls = context.namingEnv.getScopeClass(nodes.get(i));
            ClassNode nextCls = context.namingEnv.getScopeClass(nodes.get(i + 1));

            ctx = IrLoad.field(ctx, "$that", cls.$.name, CgNode.getDescriptor(nextCls));
        }

        Node last = nodes.get(nodes.size() - 1);
        ClassNode lastCls = context.namingEnv.getScopeClass(last);
        return IrLoad.field(ctx, name, lastCls.$.name, Type.getDescriptor(Object.class));
    }

    @Override
    protected void visit(AssignmentExpression assignExp) {
        // only support normal assign now
        $$.shouldIn(assignExp.getOperator(), AssignmentOperator.ASSIGN);

        // field assignment
        if (assignExp.getLeft().is(Identifier.class)) {
            String key = $$.cast(assignExp.getLeft(), Identifier.class).getName();

            irNode = IrStore.field(IrThis.irthis(), key, visitIr(assignExp.getRight()),
                clazz.$.name, clazz.field(key).$.desc);
        }
        // member assignment
        else if (assignExp.getLeft().is(MemberExpression.class)) {
            MemberExpression member = $$.cast(assignExp.getLeft());

            irNode = IrStore.property(visitIr(member.getObject()),
                resolveProperty(member.getProperty()), visitIr(assignExp.getRight()));
        }
        // otherwise, not support
        else {
            irNode = $$.notSupport();
        }
    }

    private IrNode resolveProperty(Node property) {
        // for identifier, load the field
        if (property.is(Identifier.class)) {
            return IrLiteral.of($$.cast(property, Identifier.class).getName());
        }
        // others, common visit
        else {
            return visitIr(property);
        }
    }

    @Override
    protected void visit(MemberExpression memExp) {
        $$.should(!memExp.isComputed());

        // load the field
        irNode = IrLoad
            .property(visitIr(memExp.getObject()), resolveProperty(memExp.getProperty()));
    }

    @Override
    protected void visit(BlockStatement blockStatement) {
        IrBlock ret = IrBlock.of();
        blockStatement.getBody().forEach((statement) -> ret.irs.add(visitIr(statement)));

        irNode = ret;
    }

    @Override
    protected void visit(ReturnStatement returnStatement) {
        if (returnStatement.getArgument().isPresent()) {
            irNode = IrReturn.ret(visitIr(returnStatement.getArgument().get()));
        } else {
            irNode = IrReturn.ret();
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
                .ir(IrStore.staticField(clazz.$.name, JsModule.FIELD_MODULE_NAME,
                    IrNew.of(clazz.$.name)), IrReturn.ret());

            // generate init method
            in(clazz.method_init()).invoke(() -> {
                method.ir(IrInvoke.invokeInit(JsModule.class));

                // generate body
                program.getBody().forEach(statement -> method.ir(visitIr(statement)));

                // end init method generate
                method.ir(IrReturn.ret());
            });
        });
    }
}
