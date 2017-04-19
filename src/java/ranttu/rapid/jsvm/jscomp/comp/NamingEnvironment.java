/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.exp.CompileError;
import ranttu.rapid.jsvm.exp.DuplicateName;
import ranttu.rapid.jsvm.exp.NoSuchName;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Function;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * the naming environment
 *
 * @author rapidhere@gmail.com
 * @version $id: NamingEnvironment.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class NamingEnvironment {
    private Map<Node, Set<String>> scopes         = new HashMap<>();
    private Map<Node, ClassNode>   bindingClasses = new HashMap<>();

    /**
     * bind the scope class to the scope
     */
    public void bindScopeClass(Node node, ClassNode clazz) {
        node = getVarScopeNode(node);
        bindingClasses.put(node, clazz);
    }

    /**
     * get the binding class node
     */
    public ClassNode getScopeClass(Node node) {
        return bindingClasses.get(getVarScopeNode(node));
    }

    /**
     * find the scope node
     */
    public Node getVarScopeNode(Node node) {
        while (true) {
            if (node.is(Function.class) || node.is(Program.class)) {
                if ((scopes.get(node)) != null) {
                    return node;
                }
            }

            if (node.hasParent()) {
                node = node.getParent();
            } else {
                break;
            }
        }

        // no scope exist, this should never happen
        throw new CompileError(node, "cannot find a scope");
    }

    /**
     * get naming scope for `var` kind
     * @param node the node to find scope
     * @return the scope of the node
     */
    public Set<String> getVarScope(Node node) {
        node = getVarScopeNode(node);
        return scopes.get(node);
    }

    /**
     * put a name to scope
     * @param scope the scope
     * @param name  the name
     */
    public void putScope(Node node, Set<String> scope, String name) {
        if (!scope.contains(name)) {
            scope.add(name);
        } else {
            // TODO
            throw new DuplicateName(node, name);
        }
    }

    /**
     * add a binding to the var scope
     */
    public void addVarBinding(Node node, String name) {
        putScope(node, getVarScope(node), name);
    }

    /**
     * find a name, and calc the jumped nodes
     */
    public List<Node> resolveJumped(Node node, String name) {
        Node origNode = node;
        List<Node> nodes = new ArrayList<>();

        while (true) {
            Set<String> scope = scopes.get(node);

            if (scope != null && scope.contains(name)) {
                return nodes;
            } else if (scope != null) {
                nodes.add(node);
            }

            if (node.hasParent()) {
                node = node.getParent();
            } else {
                throw new NoSuchName(origNode, name);
            }
        }
    }

    /**
     * create a new scope
     * @param node the node where scope activate
     * @return the created scope
     */
    public Set<String> newScope(Node node) {
        Set<String> scope = new HashSet<>();
        scopes.put(node, scope);

        return scope;
    }
}
